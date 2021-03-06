package dk.aau.sw402F15.CodeGenerator;

import dk.aau.sw402F15.Symboltable.*;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
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
    private int nextDAddress = -2;
    private Stack _stack = new Stack(this);
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

    public String getPreviousDAddress() {
        int tmp = nextDAddress;

        return "D" + (nextDAddress - 2);
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

        String arg1 = _stack.pop();

        if (node.getRight() instanceof AFunctionCallExpr)
        {
            String functionName = ((AFunctionCallExpr)node.getRight()).getName().toString().replaceAll("\\s+","");
            Emit("MOVL(498) " + functionName + "_return " + node.getLeft().toString(), true);
            return;
        }

        if ((node.getLeft() instanceof APortOutputExpr)) {

        } else if (node.getLeft() instanceof APortInputExpr) {

        } else if (node.getRight().getClass() == ACompareAndExpr.class ||
            node.getRight().getClass() == ACompareOrExpr.class ||
            node.getRight().getClass() == ACompareEqualExpr.class ||
            node.getRight().getClass() == ACompareNotEqualExpr.class ||
            node.getRight().getClass() == ACompareGreaterExpr.class ||
            node.getRight().getClass() == ACompareGreaterOrEqualExpr.class ||
            node.getRight().getClass() == ACompareLessExpr.class ||
            node.getRight().getClass() == ACompareLessOrEqualExpr.class)
        {
        if (node.getLeft() instanceof AIdentifierExpr){
            AIdentifierExpr expr = (AIdentifierExpr)node.getLeft();
                assignBool(expr.getName().getText(), _stack.pop());
            }
        } else if(node.getLeft().getClass() ==  AArrayExpr.class){
            Emit("MOVL(498) " + arg1 + " @" + getNextDAddress(false), true);
        }
        else {
            Emit("MOVL(498) " + _stack.pop() + " " + node.getLeft(), true);
        }
    }

    @Override
    public void caseAArrayExpr(AArrayExpr node){
        String arg1, arg2;

        _stack.push(node.getName().getText());
        node.getExpr().apply(this);
        arg1 = _stack.pop();
        Emit("*(420) " + arg1 + " &" + _stack.FieldSize + " " + _stack.getPointerAndIncrement(), true);
        arg1 = _stack.pop();
        arg2 = _stack.pop();
        Emit("+(400) " + arg1 + " " + arg2 + " " + _stack.getPointerAndIncrement(), true);  // offset + start = location
        arg1 = _stack.pop();
        Emit("MOVL(498) " + arg1 + " " + getNextDAddress(false), true);
        Node parent = node.parent();
        if (parent.getClass() != AAssignmentExpr.class || ((AAssignmentExpr)parent).getLeft() != node) {
            Emit("MOVL(498) @" + getNextDAddress(false) + " " + _stack.getPointerAndIncrement(), true);
        }
    }

    @Override
    public void outABreakStatement(ABreakStatement node){
        super.outABreakStatement(node);

        Emit("BREAK(514)", true);
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

        Emit("LD P_First_Cycle", true);
        String code = "ANDW(034) " + _stack.pop() + " " + _stack.pop() + " ";
        code = code + _stack.getPointerAndIncrement();
        Emit(code, true);
    }

    @Override
    public void outACompareOrExpr(ACompareOrExpr node){
        super.outACompareOrExpr(node);

        Emit("LD P_First_Cycle", true);
        String code = "ORW(035) " + _stack.pop() + " " + _stack.pop() + " ";
        code = code + _stack.getPointerAndIncrement();
        Emit(code, true);
    }

    @Override
    public void outACompareEqualExpr(ACompareEqualExpr node) {
        super.outACompareEqualExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        Emit("RSET " + _stack.peek() + ".00", true);
        Emit("AND=(300)" + " " + arg1 + " " + arg2, true);
        Emit("SET " + _stack.peek() + ".00", true);
    }

    @Override
    public void outACompareGreaterExpr(ACompareGreaterExpr node){
        super.outACompareGreaterExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        Emit("RSET " + _stack.peek() + ".00", true);
        Emit("AND>(320)" + " " + arg1 + " " + arg2, true);
        Emit("SET " + _stack.peek() + ".00", true);
    }

    @Override
    public void outACompareGreaterOrEqualExpr(ACompareGreaterOrEqualExpr node){
        super.outACompareGreaterOrEqualExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        Emit("RSET " + _stack.peek() + ".00", true);
        Emit("AND>=(325)" + " " + arg1 + " " + arg2, true);
        Emit("SET " + _stack.peek() + ".00", true);
    }

    @Override
    public void outACompareLessExpr(ACompareLessExpr node){
        super.outACompareLessExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        Emit("RSET " + _stack.peek() + ".00", true);
        Emit("AND<(310)" + " " + arg1 + " " + arg2, true);
        Emit("SET " + _stack.peek() + ".00", true);
    }

    @Override
    public void outACompareLessOrEqualExpr(ACompareLessOrEqualExpr node) {
        super.outACompareLessOrEqualExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        Emit("RSET " + _stack.peek() + ".00", true);
        Emit("AND<=(315)" + " " + arg1 + " " + arg2, true);
        Emit("SET " + _stack.peek() + ".00", true);
    }

    @Override
    public void outACompareNotEqualExpr(ACompareNotEqualExpr node) {
        super.outACompareNotEqualExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        Emit("RSET " + _stack.peek() + ".00", true);
        Emit("AND<>(305)" + " " + arg1 + " " + arg2, true);
        Emit("SET " + _stack.peek() + ".00", true);
    }

    @Override
    public void outADeclaration(ADeclaration node) {
        Symbol symbol = currentScope.getSymbolOrThrow(node.getName().getText(), node);

        if (symbol.getType().equals(SymbolType.Boolean())) {
            if (node.getExpr() != null){
                declareAndAssignBool(node.getName().getText(), _stack.pop());
            } else {
                declareBool(node.getName().getText());
            }

        } else if (symbol.getType().equals(SymbolType.Int())) {
            if (node.getExpr() != null){
                declareAndAssignInt(node.getName().getText(), _stack.pop());
            } else {
                declareInt(node.getName().getText());
            }

        } else if (symbol.getType().equals(SymbolType.Char())) {
            throw new NotImplementedException();

        } else if (symbol.getType().equals(SymbolType.Decimal())) {
            if (node.getExpr() != null){
                declareAndAssignDecimal(node.getName().getText(), _stack.pop());
            } else {
                declareDecimal(node.getName().getText());
            }

        } else if (symbol.getType().equals(SymbolType.Timer())) {
            if (node.getExpr() != null){
                declareAndAssignTimer(node.getName().getText(), _stack.pop());
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
            throw new RuntimeException();
        }
    }

    private void declareArray(SymbolArray symbol){
        ADeclaration node = (ADeclaration) symbol.getNode();
        AArrayDefinition array = (AArrayDefinition) node.getArray();
        int size = Integer.parseInt(array.getNumber().getText());
        int address = nextDAddress;
        getNextDAddress(true);
        for (int i = 0; i < size; i++){
            getNextDAddress(true);
        }
        String name = symbol.getName();
        Emit(name + "\tINT\t D" + address + "\t\t0\t", false);
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
        String address = _stack.getPointerAndIncrement();
        String boolAddress = address + ".00";
        // Declare
        Emit(name + "\tBOOL\t" + address + ".00" + "\t\t0\t", false);
    }

    private void assignBool(String name, String value){
        String boolAddress = value + ".00";
        // assign
        /*Emit("LD P_On",false);
        Emit("OUT TR0",false);
        Emit("AND " + boolAddress,false);
        Emit("SET " + name,false);
        Emit("LD TR0",false);
        Emit("ANDNOT " + value,false);
        Emit("RSET " + name,false);*/
    }

    private void declareAndAssignBool(String name, String value){
        // get next free address in symbolList
        String address = _stack.getPointerAndIncrement();
        String boolAddress = address + ".00";

        // Declare
        Emit(name + "\tBOOL\t" + boolAddress + "\t\t0\t", false);

        // assign
        /*Emit("LD P_On",false);
        Emit("OUT TR0",false);
        Emit("AND " + boolAddress,false);
        Emit("SET " + name,false);
        Emit("LD TR0",false);
        Emit("ANDNOT " + value,false);
        Emit("RSET " + name,false);*/
    }

    private void declareDecimal(String name){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tREAL\t" + address + "0.00" + "\t\t0\t", false);
    }

    private void declareAndAssignDecimal(String name, String value){
        // get next free address in symbolList
        String address = getNextDAddress(true);

        // Declare
        Emit(name + "\tREAL\t" + address + "\t\t0\t", false);
        // Assign
        Emit("MOVL(498) " + value + " " + name, true);
    }

    @Override
    public void inAFunctionCallExpr(AFunctionCallExpr node){

        for(PExpr expr : node.getArgs())
        {
            _stack.push(expr.toString());
        }

        Emit("SBS(091) " + functions.indexOf(node.getName().getText()), true);
    }

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node){
        super.inAFunctionRootDeclaration(node);

        String lulz = node.getName().getText();

        if(node.getName().getText().contentEquals("init")) // If we've passed declaration of global variables
        {
            // Calling init and run
            Emit("SBS(091) 0", true);
            Emit("LD P_On", true);
            Emit("SBS(091) 1", true);
        }

        Emit("SBN(092) " + functions.indexOf(node.getName().getText()), true);

        if (!node.getStatements().isEmpty())
            Emit("LD P_On", true);

        for (PDeclaration decl : node.getParams())
            Emit("MOVL(498) " + _stack.pop() + " " + decl.toString(), true);
    }

    @Override
    public void outAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.outAFunctionRootDeclaration(node);

        Emit(node.getName().toString().replaceAll("\\s+", "") + "_return\tBYTE\t" + getNextDAddress(true) + "\t\t0\t", false);

        Emit("RET(093)", true);
    }

    @Override
    public void outAReturnStatement(AReturnStatement node) {
        super.outAReturnStatement(node);

        Emit("MOVL(498) " + _stack.peek() + " " + ((AFunctionRootDeclaration) node.parent()).getName().toString().replaceAll("\\s+","") + "_return", true);
    }

    @Override
    public void outAIdentifierExpr(AIdentifierExpr node) {
        _stack.push(node.getName().getText(), node);
    }

    @Override
    public void outANegationExpr(ANegationExpr node){
        super.outANegationExpr(node);

        Emit("NOT " + getNextDAddress(false), true);
    }

    @Override
    public void outAPortInputExpr(APortInputExpr node){

        if (node.parent() instanceof ADeclaration)
        {
            ADeclaration parentNode = (ADeclaration)node.parent();

            Emit("LD " + node.getExpr().toString(), true);
            Emit("SET " + parentNode.getName().getText().toString(), true);
            Emit("SET " + _stack.peek() + ".00", true);

            Emit("LDNOT " + node.getExpr().toString(), true);
            Emit("RSET " + parentNode.getName().getText().toString(), true);
            Emit("RSET " + _stack.peek() + ".00", true);
        }
        else if (node.parent() instanceof AAssignmentExpr)
        {
            AAssignmentExpr parentNode = (AAssignmentExpr)node.parent();

            Emit("LD " + node.getExpr().toString(), true);
            Emit("SET " + parentNode.getLeft().toString(), true);
            Emit("SET " + _stack.peek() + ".00", true);

            Emit("LDNOT " + node.getExpr().toString(), true);
            Emit("RSET " + parentNode.getLeft().toString(), true);
            Emit("RSET " + _stack.peek() + ".00", true);
        }
        else
            throw new RuntimeException();


    }

    @Override
    public void outAPortOutputExpr(APortOutputExpr node) {
        super.outAPortOutputExpr(node);

        Emit("LD " + _stack.peek() + ".00", true);
        Emit("OUT " + node.getExpr(), true);
    }

    @Override
    public void outATrueExpr(ATrueExpr node) {
        super.outATrueExpr(node);

        Emit("MOVL(498) #1 " + getNextDAddress(true), true);
        Emit("SET " + _stack.peek() + ".00", true);
    }

    @Override
    public void outAFalseExpr(AFalseExpr node) {
        super.outAFalseExpr(node);

        Emit("MOVL(498) #0 " + getNextDAddress(true), true);
        Emit("RSET " + _stack.peek() + ".00", true);
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
            Emit("LD " + _stack.peek() + ".00", true);
            node.getRight().apply(this);
            Emit("JMP(004) #" + elseLabel, true);
            Emit("JME(005) #" + ifLabel, true);
            Emit("LD P_First_Cycle", true);
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
    public void caseAWhileStatement(AWhileStatement node){
        int jumpLabel = getNextJump();
        int loopLabel = getNextJump();

        //node.getCondition().apply(this);
        Emit("LDNOT " + _stack.getPointerAndIncrement() + ".00", true);
        Emit("JMP(004) #" + jumpLabel, true);
        node.getStatement().apply(this);
        node.getCondition().apply(this);
        Emit("JME(005) #" + jumpLabel, true);
        Emit("LD P_On", true);

    }

    @Override
    public void outAIntegerExpr(AIntegerExpr node) {
        super.outAIntegerExpr(node);
        _stack.push(Integer.parseInt(node.getIntegerLiteral().getText()));
    }

    @Override
    public void outADecimalExpr(ADecimalExpr node) {
        super.outADecimalExpr(node);
        if (!(node.parent() instanceof APortOutputExpr)) { }
        else if (!(node.parent() instanceof APortInputExpr)) { }
        else
            _stack.push(Float.parseFloat(node.getDecimalLiteral().getText()));
    }

    @Override
    public void outAAddExpr(AAddExpr node) {
        super.outAAddExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        if (node.getRight() instanceof ADecimalExpr)
            Emit("+F(454) " + arg1 + " " + arg2 + " " + _stack.getPointerAndIncrement(), true);
        else
            Emit("+(400) " + arg1 + " " + arg2 + " " + _stack.getPointerAndIncrement(), true);
    }

    @Override
    public void outADivExpr(ADivExpr node) {
        super.outADivExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        if (node.getRight() instanceof ADecimalExpr)
            Emit("/F(457) " + arg1 + " " + arg2 + " " + _stack.getPointerAndIncrement(), true);
        else
            Emit("/(430) " + arg1 + " " + arg2 + " " + _stack.getPointerAndIncrement(), true);
    }

    @Override
    public void outAMultiExpr(AMultiExpr node) {
        super.outAMultiExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        if (node.getRight() instanceof ADecimalExpr)
            Emit("*F(456) " + arg1 + " " + arg2 + " " + _stack.getPointerAndIncrement(), true);
        else
            Emit("*(420) " + arg1 + " " + arg2 + " " + _stack.getPointerAndIncrement(), true);
    }

    @Override
    public void outASubExpr(ASubExpr node) {
        super.outASubExpr(node);

        String arg1 = _stack.pop();
        String arg2 = _stack.pop();

        if (node.getRight() instanceof ADecimalExpr)
            Emit("-F(455) " + arg1 + " " + arg2 + " " + _stack.getPointerAndIncrement(), true);
        else
            Emit("-(410) " + arg1 + " " + arg2 + " " + _stack.getPointerAndIncrement(), true);
    }

    private int getNextJump(){
        jumpLabel = jumpLabel + 1;
        if(jumpLabel > 255)
            throw new IndexOutOfBoundsException();
        return jumpLabel;
    }

    public void Emit(String s, boolean instruction){
        if (instruction == true) { // Write to InstructionList, if instruction
            instructionWriter.println(s);
        } else { // Otherwise it's a symbol, then write to SymbolList
            symbolWriter.println(s);
        }
    }

    @Override
    public void outACaseStatement(ACaseStatement node){
        throw new NotImplementedException();
    }

    @Override
    public void outAContinueStatement(AContinueStatement node){
        throw new NotImplementedException();
    }


    private void declareTimer(String name){
        throw new NotImplementedException();
    }

    private void declareAndAssignTimer(String name, String value){
        throw new NotImplementedException();
    }

    @Override
    public void outADefaultStatement(ADefaultStatement node){
        throw new NotImplementedException();
    }

    @Override
    public void outAPortAnalogInputExpr(APortAnalogInputExpr node){
        throw new NotImplementedException();
    }

    @Override
    public void outAPortAnalogOutputExpr(APortAnalogOutputExpr node){
        throw new NotImplementedException();
    }


    @Override
    public void outASwitchStatement(ASwitchStatement node){
        //throw new NotImplementedException();
    }

    @Override
    public void outATypeCastExpr(ATypeCastExpr node){
        throw new NotImplementedException();
    }

    @Override
    public void caseASwitchStatement(ASwitchStatement node){
        throw new NotImplementedException();
    }

    @Override
    public void outAMemberExpr(AMemberExpr node){
        throw new NotImplementedException();
    }
}
