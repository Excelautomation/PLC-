package dk.aau.sw402F15.ScopeChecker;
import dk.aau.sw402F15.TypeChecker.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mads on 08/04/15.
 */

public class ScopeChecker extends DepthFirstAdapter {
    private Scope rootScope = new Scope(null, null);
    private Scope currentScope;
    private List<PTypeSpecifier> typeList = new ArrayList<PTypeSpecifier>();
    private List<AIdentifierTypeSpecifier> structs = new ArrayList<AIdentifierTypeSpecifier>();
    private List<AFunctionCallExpr> functions = new ArrayList<AFunctionCallExpr>();

    public ScopeChecker() {
        currentScope = rootScope;
    }

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.outAFunctionRootDeclaration(node);

        // convert parameters to SymbolType's
        ArrayList<SymbolType> symbolTypeList = new ArrayList<SymbolType>();
        for (Node parameter : node.getParams()) {
            if (parameter.getClass() == ASimpleDeclaration.class) {
                ASimpleDeclaration simpleDeclaration = (ASimpleDeclaration) parameter;
                //add to list
                symbolTypeList.add(this.getSymbolType(simpleDeclaration.getType()));
            }
            else
                throw new RuntimeException();
        }

        currentScope.addSymbol(new SymbolFunction(this.getSymbolType(node.getReturnType()), symbolTypeList, node.getName().getText(), node, currentScope));
    }

    @Override
    public void inAStructRootDeclaration(AStructRootDeclaration node){
        currentScope = currentScope.addSubScope(node);
        List<Symbol> list = currentScope.toList();
        node.getProgram().apply(this);
        currentScope = currentScope.getParentScope();
        currentScope.addSymbol(new SymbolStruct(node.getName().getText(), list, node, currentScope));
    }

    @Override
    public void outStart(Start node)
    {
        // Check success of functions
        for (AFunctionCallExpr n : functions) {
            super.caseAFunctionCallExpr(n);
        }
        // Check success of structs
        for (AIdentifierTypeSpecifier n : structs) {
            super.caseAIdentifierTypeSpecifier(n);
        }
    }

    // Types
    @Override
    public void outABoolTypeSpecifier(ABoolTypeSpecifier node) {
        super.outABoolTypeSpecifier(node);
    }

    @Override
    public void outACharTypeSpecifier(ACharTypeSpecifier node) {
        super.outACharTypeSpecifier(node);
    }

    @Override
    public void outAIntTypeSpecifier(AIntTypeSpecifier node) {
        super.outAIntTypeSpecifier(node);
    }

    @Override
    public void outALongTypeSpecifier(ALongTypeSpecifier node) {
        super.outALongTypeSpecifier(node);
    }

    @Override
    public void outAFloatTypeSpecifier(AFloatTypeSpecifier node) {
        super.outAFloatTypeSpecifier(node);

    }

    @Override
    public void outADoubleTypeSpecifier(ADoubleTypeSpecifier node) {
        super.outADoubleTypeSpecifier(node);
        typeList.add(node);
    }

    @Override
    public void outATimerTypeSpecifier(ATimerTypeSpecifier node) {
        super.outATimerTypeSpecifier(node);
    }

    @Override
    public void outAPortTypeSpecifier(APortTypeSpecifier node) {
        super.outAPortTypeSpecifier(node);
        typeList.add(node);
    }

    @Override
    public void outAIdentifierTypeSpecifier(AIdentifierTypeSpecifier node) {
        super.outAIdentifierTypeSpecifier(node);
    }

    @Override
    public void inAScopeStatement(AScopeStatement node)
    {
        currentScope = currentScope.addSubScope(node);

        /* TODO: Make sure the program functions without this code segment
        // Import formal parameters if any
        if(node.parent() instanceof AVoidFunctionFunctionDeclaration)
        {
            ((AVoidFunctionFunctionDeclaration) node.parent()).getFormalParameters().apply(this);
        }
        if(node.parent() instanceof AFunctionRootDeclaration)
        {
            ((AFunctionRootDeclaration) node.parent()).getParams().apply(this);
        }
        */
    }

    @Override
    public void outAScopeStatement(AScopeStatement node){
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void inAAssignmentDeclaration(AAssignmentDeclaration node){
        //Get children
        TIdentifier id = node.getName();
        PTypeSpecifier type = node.getType();

        //Check for errors
        if(id == null || type == null){
            throw new NullPointerException();
        }

        //Find the symbol type
        SymbolType sType = null;
        if(type instanceof ABoolTypeSpecifier)
        {
            sType = SymbolType.Boolean;
        }
        else if(type instanceof ACharTypeSpecifier)
        {
            sType = SymbolType.Char;
        }
        else if(type instanceof ADoubleTypeSpecifier || type instanceof AFloatTypeSpecifier)
        {
            sType = SymbolType.Decimal;
        }
        else if(type instanceof AIntTypeSpecifier || type instanceof ALongTypeSpecifier)
        {
            sType = SymbolType.Int;
        }
        else if(type instanceof APortTypeSpecifier)
        {
            sType = SymbolType.Port;
        }
        else if(type instanceof ATimerTypeSpecifier)
        {
            sType = SymbolType.Timer;
        }
        else if(type instanceof AIdentifierTypeSpecifier){
            sType = SymbolType.Struct;
            structs.add((AIdentifierTypeSpecifier) type);
        }

        //Add the symbol
        currentScope.addSymbol(new Symbol(sType, id.getText(), node, currentScope));
    }

    @Override
    public void inASimpleDeclaration(ASimpleDeclaration node){
        //Get children
        TIdentifier id = node.getName();
        PTypeSpecifier type = node.getType();

        //Check for errors
        if(id == null || type == null){
            throw new NullPointerException();
        }

        //Add the symbol
        currentScope.addSymbol(new Symbol(this.getSymbolType(type), id.getText(), node, currentScope));
    }

    @Override
    public void caseTIdentifier(TIdentifier node)
    {
        currentScope.getSymbolOrThrow(node.getText());
    }

    @Override
    public void caseAFunctionCallExpr(AFunctionCallExpr node)
    {
        //Assume success of functions
        functions.add(node);
    }

    @Override
    public void caseAIdentifierTypeSpecifier(AIdentifierTypeSpecifier node)
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
        if(type instanceof ABoolTypeSpecifier)
        {
            sType = SymbolType.Boolean;
        }
        else if(type instanceof ACharTypeSpecifier)
        {
            sType = SymbolType.Char;
        }
        else if(type instanceof ADoubleTypeSpecifier || type instanceof AFloatTypeSpecifier)
        {
            sType = SymbolType.Decimal;
        }
        else if(type instanceof AIntTypeSpecifier || type instanceof ALongTypeSpecifier)
        {
            sType = SymbolType.Int;
        }
        else if(type instanceof APortTypeSpecifier)
        {
            sType = SymbolType.Port;
        }
        else if(type instanceof ATimerTypeSpecifier)
        {
            sType = SymbolType.Timer;
        }
        else if(type instanceof AIdentifierTypeSpecifier){
            sType = SymbolType.Struct;
            structs.add((AIdentifierTypeSpecifier) type);
        }

        return sType;
    }
}