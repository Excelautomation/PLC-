package dk.aau.sw402F15.ScopeChecker;
import dk.aau.sw402F15.TypeChecker.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;
import sun.org.mozilla.javascript.internal.ast.FunctionCall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mads on 08/04/15.
 */

public class ScopeChecker extends DepthFirstAdapter {
    private Scope rootScope = new Scope(null, null);
    private Scope currentScope;
    private List<PType> typeList = new ArrayList<PType>();
    private List<AIdentifierType> structs = new ArrayList<AIdentifierType>();
    private List<AFunctionCall> functions = new ArrayList<AFunctionCall>();

    public ScopeChecker() {
        currentScope = rootScope;
    }

    @Override
    public void inAFunctionFunctionDeclaration(AFunctionFunctionDeclaration node) {
        super.outAFunctionFunctionDeclaration(node);

        // Clear list for loading returntypes and input parameters.
        typeList.clear();
    }

    @Override
    public void outAFunctionFunctionDeclaration(AFunctionFunctionDeclaration node) {
        super.outAFunctionFunctionDeclaration(node);
        ArrayList<SymbolType> symbolTypeList = new ArrayList<SymbolType>();

        // convert parameter nodes to symboltype.
        for(PType type: typeList.subList(1, typeList.size())){
            symbolTypeList.add(this.getSymbolType(type));
        }
        // add to symbolTable
        currentScope.addSymbol(new SymbolFunction(this.getSymbolType(typeList.get(0)), symbolTypeList, node.getIdentifier().getText(), node, currentScope));
    }

    @Override
    public void caseAStruct(AStruct node){
        currentScope = currentScope.addSubScope(node);
        List<Symbol> list = currentScope.toList();
        node.getStructBody().apply(this);
        currentScope = currentScope.getParentScope();
        currentScope.addSymbol(new SymbolStruct(node.getIdentifier().getText(), list, node, currentScope));
    }

    @Override
    public void outStart(Start node)
    {
        // Check success of functions
        for (AFunctionCall n : functions) {
            super.caseAFunctionCall(n);
        }
        // Check success of structs
        for (AIdentifierType n : structs) {
            super.caseAIdentifierType(n);
        }
    }

    // Types
    @Override
    public void outABoolType(ABoolType node) {
        super.outABoolType(node);
        typeList.add(node);
    }

    @Override
    public void outACharType(ACharType node) {
        super.outACharType(node);
        typeList.add(node);
    }

    @Override
    public void outAIntType(AIntType node) {
        super.outAIntType(node);
        typeList.add(node);
    }

    @Override
    public void outALongType(ALongType node) {
        super.outALongType(node);
        typeList.add(node);
    }

    @Override
    public void outAFloatType(AFloatType node) {
        super.outAFloatType(node);
        typeList.add(node);

    }

    @Override
    public void outADoubleType(ADoubleType node) {
        super.outADoubleType(node);
        typeList.add(node);
    }

    @Override
    public void outATimerType(ATimerType node) {
        super.outATimerType(node);
        typeList.add(node);
    }

    @Override
    public void outAPortType(APortType node) {
        super.outAPortType(node);
        typeList.add(node);
    }

    @Override
    public void outAIdentifierType(AIdentifierType node) {
        super.outAIdentifierType(node);
        typeList.add(node);
    }

    @Override
    public void caseAScope(AScope node)
    {
        currentScope = currentScope.addSubScope(node);
        // Import formal parameters if any
        if(node.parent() instanceof AVoidFunctionFunctionDeclaration)
        {
            ((AVoidFunctionFunctionDeclaration) node.parent()).getFormalParameters().apply(this);
        }
        if(node.parent() instanceof AFunctionFunctionDeclaration)
        {
            ((AFunctionFunctionDeclaration) node.parent()).getFormalParameters().apply(this);
        }

        if(node.getStatements() != null)
        {
            super.caseAScope(node);
        }
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void caseADeclarationAssignmentDeclarationStatement(ADeclarationAssignmentDeclarationStatement node){
        //Get children
        TIdentifier id = node.getIdentifier();
        PType type = node.getType();

        //Check for errors
        if(id == null || type == null){
            throw new NullPointerException();
        }

        //Find the symbol type
        SymbolType sType = null;
        if(type instanceof ABoolType)
        {
            sType = SymbolType.Boolean;
        }
        else if(type instanceof ACharType)
        {
            sType = SymbolType.Char;
        }
        else if(type instanceof ADoubleType || type instanceof AFloatType)
        {
            sType = SymbolType.Decimal;
        }
        else if(type instanceof AIntType || type instanceof ALongType)
        {
            sType = SymbolType.Int;
        }
        else if(type instanceof APortType)
        {
            sType = SymbolType.Port;
        }
        else if(type instanceof ATimerType)
        {
            sType = SymbolType.Timer;
        }
        else if(type instanceof AIdentifierType){
            sType = SymbolType.Struct;
            structs.add((AIdentifierType) type);
        }

        //Add the symbol
        currentScope.addSymbol(new Symbol(sType, id.getText(), node, currentScope));
    }

    @Override
    public void caseADeclarationDeclarationStatement(ADeclarationDeclarationStatement node){
        //Get children
        TIdentifier id = node.getIdentifier();
        PType type = node.getType();

        //Check for errors
        if(id == null || type == null){
            throw new NullPointerException();
        }

        //Find the symbol type
        SymbolType sType = null;
        if(type instanceof ABoolType)
        {
            sType = SymbolType.Boolean;
        }
        else if(type instanceof ACharType)
        {
            sType = SymbolType.Char;
        }
        else if(type instanceof ADoubleType || type instanceof AFloatType)
        {
            sType = SymbolType.Decimal;
        }
        else if(type instanceof AIntType || type instanceof ALongType)
        {
            sType = SymbolType.Int;
        }
        else if(type instanceof APortType)
        {
            sType = SymbolType.Port;
        }
        else if(type instanceof ATimerType)
        {
            sType = SymbolType.Timer;
        }
        else if(type instanceof AIdentifierType){
            sType = SymbolType.Struct;
            structs.add((AIdentifierType) type);
        }

        //Add the symbol
        currentScope.addSymbol(new Symbol(sType, id.getText(), node, currentScope));
    }

    @Override
    public void caseAFormalParameter(AFormalParameter node)
    {
        //Get children
        TIdentifier id = node.getIdentifier();
        PType type = node.getType();

        //Check for errors
        if(id == null || type == null){
            throw new NullPointerException();
        }

        //Find the symbol type
        SymbolType sType = null;
        if(type instanceof ABoolType)
        {
            sType = SymbolType.Boolean;
        }
        else if(type instanceof ACharType)
        {
            sType = SymbolType.Char;
        }
        else if(type instanceof ADoubleType || type instanceof AFloatType)
        {
            sType = SymbolType.Decimal;
        }
        else if(type instanceof AIntType || type instanceof ALongType)
        {
            sType = SymbolType.Int;
        }
        else if(type instanceof APortType)
        {
            sType = SymbolType.Port;
        }
        else if(type instanceof ATimerType)
        {
            sType = SymbolType.Timer;
        }
        else if(type instanceof AIdentifierType){
            sType = SymbolType.Struct;
            structs.add((AIdentifierType) type);
        }

        //Add the symbol
        currentScope.addSymbol(new Symbol(sType, id.getText(), node, currentScope));
    }

    @Override
    public void caseTIdentifier(TIdentifier node)
    {
        currentScope.getSymbolOrThrow(node.getText());
    }

    @Override
    public void caseAFunctionCall(AFunctionCall node)
    {
        //Assume success of functions
        functions.add(node);
    }

    @Override
    public void caseAIdentifierType(AIdentifierType node)
    {
        //Assume success of struct types
        structs.add(node);
    }

    public Scope getSymbolTable() {
        return rootScope;
    }

    private SymbolType getSymbolType(Object type){

        //Check for errors
        if(type == null){
            throw new NullPointerException();
        }

        //Find the symbol type
        SymbolType sType = null;
        if(type instanceof ABoolType)
        {
            sType = SymbolType.Boolean;
        }
        else if(type instanceof ACharType)
        {
            sType = SymbolType.Char;
        }
        else if(type instanceof ADoubleType || type instanceof AFloatType)
        {
            sType = SymbolType.Decimal;
        }
        else if(type instanceof AIntType || type instanceof ALongType)
        {
            sType = SymbolType.Int;
        }
        else if(type instanceof APortType)
        {
            sType = SymbolType.Port;
        }
        else if(type instanceof ATimerType)
        {
            sType = SymbolType.Timer;
        }
        else if(type instanceof AIdentifierType){
            sType = SymbolType.Struct;
            structs.add((AIdentifierType) type);
        }

        return sType;
    }
}