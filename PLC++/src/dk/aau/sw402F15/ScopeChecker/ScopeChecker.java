package dk.aau.sw402F15.ScopeChecker;
import dk.aau.sw402F15.TypeChecker.Exceptions.*;
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
    public void caseAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        inAFunctionRootDeclaration(node);

        if (node.getReturnType() != null) {
            node.getReturnType().apply(this);
        }
        if (node.getName() != null) {
            node.getName().apply(this);
        }
        currentScope = currentScope.addSubScope(node);
        List<PDeclaration> params = new ArrayList<PDeclaration>(node.getParams());
        for (PDeclaration parameter : params) {
            parameter.apply(this);
        }

        List<PStatement> statements = new ArrayList<PStatement>(node.getStatements());
        for (PStatement statement : statements) {
            statement.apply(this);
        }

        currentScope = currentScope.getParentScope();

        outAFunctionRootDeclaration(node);
    }

    @Override
    public void outStart(Start node){
        // Check success of functions
        for (AFunctionCallExpr n : functions) {
            super.caseAFunctionCallExpr(n);
        }
        // Check success of structs
        for (AIdentifierTypeSpecifier n : structs) {
            super.caseAIdentifierTypeSpecifier(n);
        }
    }

    // Declaration
    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        super.outAFunctionRootDeclaration(node);

        // convert parameters to SymbolType's
        ArrayList<SymbolType> symbolTypeList = new ArrayList<SymbolType>();
        for (Node parameter : node.getParams()) {
            if (parameter.getClass() == ADeclaration.class) {
                ADeclaration simpleDeclaration = (ADeclaration) parameter;
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
    public void inAScopeStatement(AScopeStatement node){
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
        //TODO We dont call super here ? is that correct?
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void inAAssignmentDeclaration(AAssignmentDeclaration node){
        //Get children
        TIdentifier id = node.getName();
        boolean isArray = node.getArray() != null;
        SymbolType type = getSymbolType(node.getType());

        //Check for errors
        if(id == null){
            throw new NullPointerException();
        }

        //Add the symbol
        if(isArray)
            currentScope.addSymbol(new SymbolArray(type, id.getText(), node, currentScope));
        else
            currentScope.addSymbol(new SymbolVariable(type, id.getText(), node, currentScope, false));
    }

    @Override
    public void inADeclaration(ADeclaration node){
        //Get children
        TIdentifier id = node.getName();
        SymbolType type = getSymbolType(node.getType());
        boolean isArray = node.getArray() != null;

        //Check for errors
        if(id == null){
            throw new NullPointerException();
        }
        //Add the symbol
        if(isArray)
            currentScope.addSymbol(new SymbolArray(type, id.getText(), node, currentScope));
        else
            currentScope.addSymbol(new SymbolVariable(type, id.getText(), node, currentScope, false));
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

        //Check parameters
        for(PExpr expr : node.getArgs()){
            expr.apply(this);
        }
    }

    @Override
    public void caseAStructRootDeclaration(AStructRootDeclaration node){
        inAStructRootDeclaration(node);
        currentScope = currentScope.addSubScope(node);
        node.getProgram().apply(this);
        currentScope = currentScope.getParentScope();
        outAStructRootDeclaration(node);
    }

    @Override
    public void caseAIdentifierTypeSpecifier(AIdentifierTypeSpecifier node)
    {
        //Assume success of struct types
        structs.add(node);
    }

    @Override
    public void inAIdentifierExpr(AIdentifierExpr node) {
        super.inAIdentifierExpr(node);
        currentScope.getSymbolOrThrow(node.getName().getText());
    }

    @Override
    public void inAMemberExpr(AMemberExpr node) {
        super.inAMemberExpr(node);

        if (node.getLeft().getClass() == AIdentifierExpr.class){
                //cast left node
                AIdentifierExpr expr = (AIdentifierExpr)node.getLeft();
                // check if symbol is in table
                Symbol symbol = currentScope.getSymbolOrThrow(expr.getName().getText());

                //check if returned symbol is a struct
                if ( symbol instanceof SymbolStruct){
                    // go to scope of left node


                    // check right node for type. Declaration or func
                    if (node.getRight().getClass() == AIdentifierExpr.class){
                        // TODO Go to inner scope of struct

                        AIdentifierExpr var = (AIdentifierExpr)node.getRight();
                        currentScope.getSymbolOrThrow(var.getName().getText());

                    }else if (node.getRight().getClass() == AFunctionCallExpr.class){
                        AFunctionCallExpr var = (AFunctionCallExpr)node.getRight();
                        currentScope.getSymbolOrThrow(var.getName().getText());

                    } else {
                        // right node is neigther var or func!!!
                        throw new IllegalArgumentException();
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } else if ((node.getLeft().getClass() == AFunctionCallExpr.class)) {
                //cast left node
                AFunctionCallExpr expr = (AFunctionCallExpr) node.getLeft();
                // check if symbol is in table
                Object symbol = currentScope.getSymbolOrThrow(expr.getName().getText());

                // check if it returns a struct that have right node as field
                //check if returned symbol is a function
                if (symbol instanceof SymbolFunction) {
                    SymbolFunction symbolFunction = (SymbolFunction)symbol;


                    // check right node for type. Declaration or func
                    if (node.getRight().getClass() == AIdentifierExpr.class) {
                        AIdentifierExpr var = (AIdentifierExpr) node.getRight();
                        currentScope.getSymbolOrThrow(var.getName().getText());

                    } else if (node.getRight().getClass() == AFunctionCallExpr.class) {
                        AFunctionCallExpr var = (AFunctionCallExpr) node.getRight();
                        currentScope.getSymbolOrThrow(var.getName().getText());

                    } else {
                        // right node is neigther var or func!!!
                        throw new IllegalArgumentException();
                    }
                } else {
                    throw new IllegalArgumentException();
                }
        }
    }
    private void checkRightNode(Object symbol){

    }

    public Scope getSymbolTable() {
        return rootScope;
    }

    private SymbolType getSymbolType(PTypeSpecifier type){

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