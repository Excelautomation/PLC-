package dk.aau.sw402F15.CodeGenerator;

import dk.aau.sw402F15.Symboltable.Scope;
import dk.aau.sw402F15.Symboltable.ScopeDepthFirstAdapter;
import dk.aau.sw402F15.Symboltable.Symbol;
import dk.aau.sw402F15.Symboltable.SymbolArray;
import dk.aau.sw402F15.Symboltable.SymbolFunction;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.geom.FlatteningPathIterator;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EmptyStackException;

/**
 * Created by Claus & Jimmi on 24-04-2015.
 */
public class CodeGenerator extends ScopeDepthFirstAdapter {
    private int jumpLabel = 0;
    private int returnlabel;
    private int nextDAddress = -4;
    private int nextWAddress = 0;
    private int nextHAddress = 0;

    private ArrayList<String> functions;

    PrintWriter instructionWriter;
    PrintWriter symbolWriter;

    public String getNextDAddress(boolean increment) {
        if (nextDAddress > 32763)
            throw new OutOfMemoryError();

        if (increment)
            return "D" + (nextDAddress += 4);
        else
            return "D" + nextDAddress;
    }

    public String getPreviousDAddress()
    {
        int tmp = nextDAddress;

        return "D" + (nextDAddress - 4);
    }

    public String getNextWAddress(boolean increment) {
        if (nextWAddress > 508)
            throw new OutOfMemoryError();

        if (increment)
            return "W" + ((nextWAddress += 1) * 100);
        else
            return "W" + nextWAddress * 100;
    }

    public String stackPointer(boolean increment) {
        if (nextHAddress > 4091)
            throw new OutOfMemoryError();

        int currentAddress = nextHAddress;
        nextHAddress += 4;

        if (increment)
            return "H" + (currentAddress);
        else
            return "H" + currentAddress;
    }

    public < T > void push(T value)
    {
        if (value.getClass() == Integer.class)
            Emit("MOV(021) &" + value + " " + stackPointer(true), true);
        else if (value.getClass() == Float.class || value.getClass() == Double.class)
            Emit("+F(454) +0,0 +" + value.toString().replace(".", ",") + " " + stackPointer(true) + "", true);
        else if (value.getClass() == String.class)
            Emit("MOV(021) " + value + " " + stackPointer(true), true);
        //else
            //throw new ClassFormatError();
    }

    public String pop()
    {
        if (nextHAddress < 0)
            throw new EmptyStackException();
        return "H" + (nextHAddress -= 4);
    }

    public String peek()
    {
        return "H" + nextHAddress;
    }

    public CodeGenerator(Scope scope, ArrayList functions) {
        super(scope, scope);

        this.functions = functions;

        try {
            // create writer instances
            instructionWriter = new PrintWriter("InstructionList.txt", "UTF-8");
            symbolWriter = new PrintWriter("SymbolList.txt", "UTF-8");

            // here we call the init method
            Emit("LD P_First_Cycle", true);

            // reset all addresses
            Emit("SSET(630) " + getNextDAddress(true) + " &32767", true);
            Emit("SSET(630) " + stackPointer(true) + " &1535", true);
            Emit("SBS(091) 0", true);

            // here we call the run Method
            Emit("LD P_On", true);
            Emit("SBS(091) 1", true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void outStart(Start node){
        Emit("END(001)", true);

        instructionWriter.close();
        symbolWriter.close();
    }

    @Override
    public void caseAAssignmentExpr(AAssignmentExpr node) {
        inAAssignmentExpr(node);
        if(node.getRight() != null)
        {
            node.getRight().apply(this);
        }
        if(node.getLeft() != null) {
            node.getLeft().apply(this);
        }
        outAAssignmentExpr(node);
    }

    @Override
    public void outAAssignmentExpr(AAssignmentExpr node) {
        super.outAAssignmentExpr(node);

        if (!(node.getLeft() instanceof APortOutputExpr))
            Emit("MOV(021) " + pop() + " " + node.getLeft(), true);



    }

    @Override
    public void caseAArrayDefinition(AArrayDefinition node){
        int size = Integer.parseInt(node.getNumber().getText());
        // Reserver memory for array
    }

    @Override
    public void caseAArrayExpr(AArrayExpr node){
        // TODO: currently only gets the values
        node.getExpr().apply(this);
        SymbolArray symbol = (SymbolArray) currentScope.getSymbolOrThrow(node.getName().getText(), node.getName());
        int location = 0; // Get location in memory
        int size = 1;
        if (symbol.getContainedType().getType() == SymbolType.Type.Int || symbol.getContainedType().getType() == SymbolType.Type.Decimal) {
            size = 2;
        }
        node.getExpr().apply(this);
        int offset = size; // * Value of the expression
        location += offset;
        Emit("*(420) " + getNextDAddress(false) + " &" + size + " " + getNextDAddress(false), false);
        Emit("+(400) " + getNextDAddress(false) + " &" + location + " " + getNextDAddress(false), false);
        Emit("+(400) " + getNextDAddress(false) + " &" + node.getName() + " " + getNextDAddress(false), false);
    }

    @Override
    public void caseADeclaration(ADeclaration node){
        super.caseADeclaration(node);
    }

    @Override
    public void outABreakStatement(ABreakStatement node){
        super.outABreakStatement(node);

        Emit("BREAK(514)", true);
    }

    @Override
    public void outACaseStatement(ACaseStatement node){
        //throw new NotImplementedException();
    }

    @Override
    public void outAIncrementExpr(AIncrementExpr node) {
        super.outAIncrementExpr(node);

        Emit("++(590) " + node.getName(), true);
    }

    @Override
    public void outADecrementExpr(ADecrementExpr node) {
        super.outADecrementExpr(node);

        Emit("--(592) " + node.getName(), true);
    }

    @Override
    public void outACompareAndExpr(ACompareAndExpr node){
        super.outACompareAndExpr(node);

        Emit("ANDW(034) D" + (nextDAddress - 4) + " " + getNextDAddress(false) + " " + getNextDAddress(true), true);
    }
    
    @Override
    public void outACompareOrExpr(ACompareOrExpr node){
        super.outACompareOrExpr(node);

        Emit("ORW(035) D" + (nextDAddress - 4) + " " + getNextDAddress(false) + " " + getNextDAddress(true), true);
    }

    @Override
    public void outACompareEqualExpr(ACompareEqualExpr node){
        super.outACompareEqualExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        Emit("AND=(300)" + " " + arg2 + " " + arg1, true);
        Emit("SET " + getNextWAddress(true), true);
    }

    @Override
    public void outACompareGreaterExpr(ACompareGreaterExpr node){
        super.outACompareGreaterExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        Emit("AND>(320)" + " " + arg2 + " " + arg1, true);
        Emit("SET " + getNextWAddress(true), true);

    }

    @Override
    public void outACompareGreaterOrEqualExpr(ACompareGreaterOrEqualExpr node){
        super.outACompareGreaterOrEqualExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        Emit("AND>=(325)" + " " + arg2 + " " + arg1, true);
        Emit("SET " + getNextWAddress(true), true);
    }

    @Override
    public void outACompareLessExpr(ACompareLessExpr node){
        super.outACompareLessExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        if (node.parent().getClass() == AWhileStatement.class)
        {
            Emit("AND<(310)" + " " + arg1 + " " + arg2, true);
            Emit("SET " + getNextWAddress(false), true);
            Emit("AND<(310)" + " " + arg2 + " " + arg1, true);
            Emit("RSET " + getNextWAddress(false), true);
        }

        else
        {
            Emit("AND<(310)" + " " + arg2 + " " + arg1, true);
            Emit("SET " + getNextWAddress(false), true);
        }
    }

    @Override
    public void outACompareLessOrEqualExpr(ACompareLessOrEqualExpr node) {
        super.outACompareLessOrEqualExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        Emit("AND<=(315)" + " " + arg2 + " " + arg1, true);
        Emit("SET " + getNextWAddress(true), true);
    }

    @Override
    public void outACompareNotEqualExpr(ACompareNotEqualExpr node) {
        super.outACompareNotEqualExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        Emit("AND<>(305)" + " " + arg2 + " " + arg1, true);
        Emit("SET " + getNextWAddress(true), true);
    }

    @Override
    public void outAContinueStatement(AContinueStatement node){
        //throw new NotImplementedException();
    }

    @Override
    public void outADeclaration(ADeclaration node) {
        Symbol symbol = currentScope.getSymbolOrThrow(node.getName().getText(), node);

        if (symbol.getType().equals(SymbolType.Boolean())) {
            declareBool(node.getName().getText(), false);

        } else if (symbol.getType().equals(SymbolType.Int())) {
            declareInt(node.getName().getText(), 0);

        } else if (symbol.getType().equals(SymbolType.Char())) {
            throw new NotImplementedException();

        } else if (symbol.getType().equals(SymbolType.Decimal())) {
            Emit(node.getName().getText() + "\tREAL\tD" + node.getName() + "\t\t0\t", false);

        } else if (symbol.getType().equals(SymbolType.Timer())) {
            Emit(node.getName().getText() + "\tTIMER\tD" + node.getName() + "\t\t0\t", false);

        } else if (symbol.getType().equals(SymbolType.Array())) {
            throw new NotImplementedException();

        } else if (symbol.getType().equals(SymbolType.Void())){ // Method is a void function
            throw new NotImplementedException();

        } else if (symbol.getType().equals(SymbolType.Type.Function)) {
            throw new NotImplementedException();

        } else if (symbol.getType().equals(SymbolType.Type.Struct)) {
            throw new NotImplementedException();

        } else {
            // throw new RuntimeException(); // TODO Need new Exception. Pretty unknown error though
        }
    }

    private void declareInt(String name, int value){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tINT\t" + address + "\t\t0\t", false);
        // Assign
        Emit("MOV(021) &" + value + " " + name, true);
    }

    private void declareBool(String name, boolean value){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tBOOL\t" + address + ".00\t\t0\t", false);
        // Assign
        if (value)
            Emit("SET " + name, true);
        else
            Emit("RSET " + name, true);
    }

    private void declareDecimal(String name, int value){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tBOOL\t" + address + ".00\t\t0\t", false);
        // Assign
        Emit("MOV(021) &" + value + " " + address, true);
    }

    private void declareTimer(String name, int value){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tBOOL\t" + address + ".00\t\t0\t", false);
        // Assign
        Emit("MOV(021) &" + value + " " + address, true);
    }

    @Override
    public void outADefaultStatement(ADefaultStatement node){
        //throw new NotImplementedException();
    }

    @Override
    public void inAFunctionCallExpr(AFunctionCallExpr node){

        SymbolFunction function = (SymbolFunction) currentScope.getSymbolOrThrow(node.getName().getText(), node);
        if (function.getReturnType().equals(SymbolType.Type.Void)) {
            Emit("LD P_On", true);
            Emit("SBS(091) " + functions.indexOf(node.getName().getText()), true);
        } else {
            Emit("MCRO(099) " + functions.indexOf(node.getName().getText()) + " " + getNextDAddress(true) + " " + pop(), true);
        }

        //Emit("SBS(091) " + getFunctionNumber(true), true);
    }

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node){
        super.inAFunctionRootDeclaration(node);

        Emit("SBN(092) " + functions.indexOf(node.getName().getText()), true);

        if (!node.getStatements().isEmpty())
            Emit("LD P_First_Cycle", true);

        //returnlabel = getNextJump();
    }

    @Override
    public void outAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.outAFunctionRootDeclaration(node);
        //Emit("JME(005) #" + returnlabel, true);
        Emit("RET(093)", true);
        //Emit("END(001)", true);
    }

    @Override
    public void outAIdentifierExpr(AIdentifierExpr node){
        push(node.getName().getText());
    }

    @Override
    public void outAMemberExpr(AMemberExpr node){
        //throw new NotImplementedException();
    }

    @Override
    public void outANegationExpr(ANegationExpr node){
        super.outANegationExpr(node);

        Emit("NOT " + getNextDAddress(false), true);
    }

    @Override
    public void outAPortAnalogInputExpr(APortAnalogInputExpr node){
        //throw new NotImplementedException();
    }

    @Override
    public void outAPortAnalogOutputExpr(APortAnalogOutputExpr node){
        //throw new NotImplementedException();
    }

    @Override
    public void outAPortInputExpr(APortInputExpr node){
        //throw new NotImplementedException();
    }

    @Override
    public void outAPortOutputExpr(APortOutputExpr node){
        super.outAPortOutputExpr(node);

        Emit("LD " + getNextWAddress(false), true);
        Emit("OUT " + node.getExpr(), true);
    }

    @Override
    public void outAReturnStatement(AReturnStatement node){
        super.outAReturnStatement(node);
        //Emit("JMP(004) #" + returnlabel, true);
        //Emit("RET(093)", true);
    }

    @Override
    public void outASwitchStatement(ASwitchStatement node){
        //throw new NotImplementedException();
    }

    @Override
    public void outATrueExpr(ATrueExpr node){
        super.outATrueExpr(node);

        Emit("MOV(021) #1 " + getNextDAddress(true), true);
        Emit("SET " + getNextWAddress(true), true);
    }

    @Override
    public void outAFalseExpr(AFalseExpr node) {
        super.outAFalseExpr(node);

        Emit("MOV(021) #0 " + getNextDAddress(true), true);
        Emit("RSET " + getNextWAddress(true), true);
    }

    @Override
    public void outATypeCastExpr(ATypeCastExpr node){
        //throw new NotImplementedException();
    }

    @Override
    public void caseABranchStatement(ABranchStatement node) {
        //Do not call super as this function handles calls of the child classes

        if (node.getRight() != null) {
            // If - else statement
            int ifLabel = getNextJump();
            int elseLabel = getNextJump();

            node.getCondition().apply(this);
            Emit("CJP(510) #" + ifLabel, true);
            node.getRight().apply(this);
            Emit("JMP(004) #" + elseLabel, true);
            Emit("JME(005) #" + ifLabel, true);
            node.getLeft().apply(this);
            Emit("JME(005) #" + elseLabel, true);
        }
        else {
            // If statement
            int label = getNextJump();

            node.getCondition().apply(this);
            Emit("CJPN(511) #" + label, true);
            node.getLeft().apply(this);
            Emit("JME(005) #" + label, true);
        }
    }

    @Override
    public void caseAForStatement(AForStatement node){
        // Not needed since we convert For-loops til While-loops
    }

    @Override
    public void caseASwitchStatement(ASwitchStatement node){
        //throw new NotImplementedException();
    }

    @Override
    public void caseAWhileStatement(AWhileStatement node){
        int jumpLabel = getNextJump();
        int loopLabel = getNextJump();

        //node.getCondition().apply(this);
        Emit("LDNOT " + getNextWAddress(true), true);
        Emit("JMP(004) #" + jumpLabel, true);
        node.getStatement().apply(this);
        node.getCondition().apply(this);
        Emit("JME(005) #" + jumpLabel, true);
        Emit("LD P_On", true);

    }

    @Override
    public void outAIntegerExpr(AIntegerExpr node) {
        super.outAIntegerExpr(node);
        push(Integer.parseInt(node.getIntegerLiteral().getText())); // TODO Ouch, a hack...
    }

    @Override
    public void outADecimalExpr(ADecimalExpr node) {
        super.outADecimalExpr(node);
        push(node.getDecimalLiteral());
    }

    @Override
    public void outAAddExpr(AAddExpr node) {
        super.outAAddExpr(node);

        // TODO Different if float
        Emit("+(400) " + pop() + " " + pop() + " " + stackPointer(true), true);
    }

    @Override
    public void outADivExpr(ADivExpr node) {
        super.outADivExpr(node);

        // TODO Different if float
        Emit("/(430) " + pop() + " " + pop() + " " + stackPointer(true), true);
    }

    @Override
    public void outAMultiExpr(AMultiExpr node) {
        super.outAMultiExpr(node);

        // TODO Different if float
        Emit("*(420) " + pop() + " " + pop() + " " + stackPointer(true), true);
    }

    @Override
    public void outASubExpr(ASubExpr node) {
        super.outASubExpr(node);

        // TODO Different if float
        Emit("-(410) " + pop() + " " + pop() + " " + stackPointer(true), true);
    }

    private int getNextJump(){
        jumpLabel = jumpLabel + 1;
        if(jumpLabel > 255)
            throw new IndexOutOfBoundsException();
        return jumpLabel;
    }

    protected void Emit(String s, boolean instruction){
        if (instruction == true) { // Write to InstructionList, if instruction
            instructionWriter.println(s);
        } else { // Otherwise it's a symbol, then write to SymbolList
            symbolWriter.println(s);
        }
    }
}
