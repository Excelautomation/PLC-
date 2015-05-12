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
    private int nextDAddress = 0;
    private int nextWAddress = 0;
    private int nextHAddress = 0;

    private ArrayList<String> functions;

    PrintWriter instructionWriter;
    PrintWriter symbolWriter;

    public String getNextDAddress(boolean increment) {
        if (nextDAddress > 32763)
            throw new OutOfMemoryError();

        if (increment)
            return "D" + (nextDAddress += 2);
        else
            return "D" + nextDAddress;
    }

    public String getPreviousDAddress()
    {
        int tmp = nextDAddress;

        return "D" + (nextDAddress - 2);
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

    public <T> void push(T value)
    {
        if (value.getClass() == Integer.class)
            Emit("MOVL(498) &" + value + " " + stackPointer(true), true);
        else if (value.getClass() == Float.class || value.getClass() == Double.class)
            Emit("+F(454) +0,0 +" + value.toString().replace(".", ",") + " " + stackPointer(true) + "", true);
        else if (value.getClass() == String.class)
            Emit("MOVL(498) " + value + " " + stackPointer(true), true);
        else
            throw new ClassFormatError();
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
        if(node.getLeft().getClass() ==  AArrayExpr.class){
            String address = pop();
            Emit("MOVL(498) " + pop() + " @" + address, true);
        }
        else if (!(node.getLeft() instanceof APortOutputExpr)){
            Emit("MOVL(498) " + pop() + " " + node.getLeft(), true);
        }
    }

    @Override
    public void outAArrayExpr(AArrayExpr node){
        SymbolArray symbol = (SymbolArray) currentScope.getSymbolOrThrow(node.getName().getText(), node.getName());
        push(node.getName().getText());
        int size = 1;
        if (symbol.getContainedType().getType() == SymbolType.Type.Decimal) {
            size = 2;
            Emit("*(420) " + pop() + " &2 " + stackPointer(true), true);
        }                    // index * size = offset
        Emit("+(400) " + pop() + " " + pop() + " " + stackPointer(true), true);  // offset + start = location
        Node parent = node.parent();
        if (parent.getClass() != AAssignmentExpr.class || ((AAssignmentExpr)parent).getLeft() != node) {
            String address = pop();
            Emit("MOVL(498) @" + address + " " + stackPointer(true), true);                           // push value to stack
            if(size == 2) {
                Emit("+(400) " + address + " 1 " + stackPointer(true), true);
                Emit("MOVL(498) @" + pop() + " " + stackPointer(true), true);
            }
        }
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
            if (node.getExpr() != null){
                declareAndAssignBool(node.getName().getText(), pop());
            } else {
                declareBool(node.getName().getText());
            }

        } else if (symbol.getType().equals(SymbolType.Int())) {
            if (node.getExpr() != null){
                declareAndAssignInt(node.getName().getText(), pop());
            } else {
                declareInt(node.getName().getText());
            }

        } else if (symbol.getType().equals(SymbolType.Char())) {
            throw new NotImplementedException();

        } else if (symbol.getType().equals(SymbolType.Decimal())) {
            if (node.getExpr() != null){
                declareAndAssignDecimal(node.getName().getText(), pop());
            } else {
                declareDecimal(node.getName().getText());
            }

        } else if (symbol.getType().equals(SymbolType.Timer())) {
            if (node.getExpr() != null){
                declareAndAssignTimer(node.getName().getText(), pop());
            } else {
                declareTimer(node.getName().getText());
            }

        } else if (symbol.getType().equals(SymbolType.Timer())) {
            throw new NotImplementedException();

        } else if (symbol.getType().equals(SymbolType.Array())) {
            declareArray((SymbolArray)symbol);

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

    private void declareArray(SymbolArray symbol){
        ADeclaration node = (ADeclaration) symbol.getNode();
        AArrayDefinition array = (AArrayDefinition) node.getArray();
        int size = Integer.parseInt(array.getNumber().getText());
        int address = nextDAddress;
        for (int i = 0; i < size; i++){
            getNextDAddress(true);
        }
        String name = symbol.getName();
        Emit(name + "\tINT\t &"+ address + "\t\t0\t", false);
        Emit("MOVL(498) &" + (address + 2) + " " + name, true);
    }

    private void declareInt(String name){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tINT\t" + address + "\t\t0\t", false);
    }

    private void declareAndAssignInt(String name, String value){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tINT\t" + address + "\t\t0\t", false);
        // Assign
        Emit("MOVL(498) " + value + " " + name, true);
    }

    private void declareBool(String name){
        // get next free address in symbolList
        String address = getNextWAddress(true);

        // Declare
        Emit(name + "\tBOOL\t" + address + "\t\t0\t", false);
    }

    private void declareAndAssignBool(String name, String value){
        // get next free address in symbolList
        String address = getNextWAddress(true);

        // Declare
        Emit(name + "\tBOOL\t" + address + "\t\t0\t", false);

        // assign
        Emit("LD P_On",false);
        Emit("OUT TR0",false);
        Emit("AND " + value,false);
        Emit("SET " + name,false);
        Emit("LD TR0",false);
        Emit("ANDNOT " + value,false);
        Emit("RSET " + name,false);
    }

    private void declareDecimal(String name){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tREAL\t" + address + "\t\t0\t", false);
    }

    private void declareAndAssignDecimal(String name, String value){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tREAL\t" + address + "\t\t0\t", false);
        // Assign
        Emit("MOVL(498) " + value + " " + name, true);
    }

    private void declareTimer(String name){
        throw new NotImplementedException();
        // get next free address in symbolList
        //String address = getNextDAddress(true);
        //Emit(node.getName().getText() + "\tTIMER\tD" + getNextDAddress(true) + "\t\t0\t", false);

        // Declare
        //Emit(name + "\tBOOL\t" + address + ".00\t\t0\t", false);
        // Assign
        //Emit("MOVL(498) &" + value + " " + address, true);
    }

    private void declareAndAssignTimer(String name, String value){
        throw new NotImplementedException();
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

        Emit("MOVL(498) #1 " + getNextDAddress(true), true);
        Emit("SET " + getNextWAddress(true), true);
    }

    @Override
    public void outAFalseExpr(AFalseExpr node) {
        super.outAFalseExpr(node);

        Emit("MOVL(498) #0 " + getNextDAddress(true), true);
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
        if (!(node.parent() instanceof APortOutputExpr))
            push(Float.parseFloat(node.getDecimalLiteral().getText()));
    }

    @Override
    public void outAAddExpr(AAddExpr node) {
        super.outAAddExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        if (node.getRight() instanceof ADecimalExpr)
            Emit("+F(454) " + arg1 + " " + arg2 + " " + stackPointer(true), true);
        else
            Emit("+(400) " + arg1 + " " + arg2 + " " + stackPointer(true), true);
    }

    @Override
    public void outADivExpr(ADivExpr node) {
        super.outADivExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        if (node.getRight() instanceof ADecimalExpr)
            Emit("/F(457) " + arg1 + " " + arg2 + " " + stackPointer(true), true);
        else
            Emit("/(430) " + pop() + " " + pop() + " " + stackPointer(true), true);
    }

    @Override
    public void outAMultiExpr(AMultiExpr node) {
        super.outAMultiExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        if (node.getRight() instanceof ADecimalExpr)
            Emit("*F(456) " + arg1 + " " + arg2 + " " + stackPointer(true), true);
        else
            Emit("*(420) " + pop() + " " + pop() + " " + stackPointer(true), true);
    }

    @Override
    public void outASubExpr(ASubExpr node) {
        super.outASubExpr(node);

        String arg1 = pop();
        String arg2 = pop();

        if (node.getRight() instanceof ADecimalExpr)
            Emit("-F(455) " + arg1 + " " + arg2 + " " + stackPointer(true), true);
        else
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
