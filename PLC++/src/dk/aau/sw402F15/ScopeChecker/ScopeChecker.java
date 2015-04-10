package dk.aau.sw402F15.ScopeChecker;
import dk.aau.sw402F15.TypeChecker.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Mads on 08/04/15.
 */

public class ScopeChecker extends DepthFirstAdapter {
    private List<AFunctionCall> functions = new ArrayList<AFunctionCall>();
    private List<AIdentifierType> structs = new ArrayList<AIdentifierType>();
    private Scope rootScope = new Scope(null, null);
    private Scope currentScope;

    Stack<Object> stack = new Stack<Object>();

    public ScopeChecker() {
        currentScope = rootScope;
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

    @Override
    public void outAFunctionFunctionDeclaration(AFunctionFunctionDeclaration node) {
        super.outAFunctionFunctionDeclaration(node);

        //currentScope.addSymbol(new SymbolFunction(node));
    }

    @Override
    public void outAVoidFunctionFunctionDeclaration(AVoidFunctionFunctionDeclaration node) {
        super.outAVoidFunctionFunctionDeclaration(node);

    }

    @Override
    public void outABoolType(ABoolType node) {
        super.outABoolType(node);
        stack.push(node);
        // use list
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
}