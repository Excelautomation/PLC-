package dk.aau.sw402F15.ScopeChecker;

import dk.aau.sw402F15.TypeChecker.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mads on 08/04/15.
 */

public class ScopeChecker extends DepthFirstAdapter {
    private Scope rootScope = new Scope(null, null);
    private Scope currentScope;

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
    public void inAWhileStatement(AWhileStatement node){
        currentScope = currentScope.addSubScope(node);
    }

    @Override
    public void outAWhileStatement(AWhileStatement node){
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void caseAProgram(AProgram node){
        List<AEnumRootDeclaration> enums = new ArrayList<AEnumRootDeclaration>();
        List<AFunctionRootDeclaration> functions = new ArrayList<AFunctionRootDeclaration>();
        List<AStructRootDeclaration> structs = new ArrayList<AStructRootDeclaration>();
        List<ADeclarationRootDeclaration> variables = new ArrayList<ADeclarationRootDeclaration>();

        for(PRootDeclaration d : node.getRootDeclaration()){
            if(d.getClass() == AEnumRootDeclaration.class){
                enums.add((AEnumRootDeclaration)d);
            }
            else if(d.getClass() == AFunctionRootDeclaration.class){
                functions.add((AFunctionRootDeclaration)d);
            }
            else if(d.getClass() == AStructRootDeclaration.class){
                structs.add((AStructRootDeclaration)d);
            }
            else if(d.getClass() == ADeclarationRootDeclaration.class){
                variables.add((ADeclarationRootDeclaration) d);
            }
        }

        for (AEnumRootDeclaration e : enums){
            DeclareEnum(e);
        }
        for (AStructRootDeclaration s : structs){
            StructBuilder builder = new StructBuilder();
            currentScope.addSymbol(builder.BuildSymbol(s, currentScope));
        }
        for (ADeclarationRootDeclaration v : variables){
            DeclareVariable(v.getDeclaration());
        }
        for (AFunctionRootDeclaration f : functions){
            DeclareFunction(f);
        }
        super.caseAProgram(node);
    }

    private void DeclareVariable(PDeclaration node){
        if(node.getClass() == ADeclaration.class){
            ADeclaration n = (ADeclaration)node;
            boolean isArray = n.getArray() != null;
            boolean isConst = n.getQuailifer() != null;
            if(isArray)
                DeclareArray(n.getName().getText(), getSymbolType(n.getType()), n);
            else
                DeclareVariable(n.getName().getText(), getSymbolType(n.getType()), n, isConst);
        }
        else if (node.getClass() == AAssignmentDeclaration.class){
            AAssignmentDeclaration n = (AAssignmentDeclaration)node;
            boolean isArray = n.getArray() != null;
            boolean isConst = n.getQuailifer() != null;
            if(isArray)
                DeclareArray(n.getName().getText(), getSymbolType(n.getType()), n);
            else
                DeclareVariable(n.getName().getText(), getSymbolType(n.getType()), n, isConst);
        }
    }

    private void DeclareVariable(String name, SymbolType type, Node node, boolean isConst){
        currentScope.addSymbol(new SymbolVariable(type, name, node, currentScope, isConst));
    }

    private void DeclareArray(String name, SymbolType type, Node node){
        currentScope.addSymbol(new SymbolArray(type, name, node, currentScope));
    }

    private void DeclareEnum(AEnumRootDeclaration node){
        currentScope.addSymbol(new SymbolEnum(node.getName().getText(), node, currentScope));
    }

    private void DeclareFunction(AFunctionRootDeclaration node){
        LinkedList<PDeclaration> params = node.getParams();
        List<SymbolType> paramTypes = new ArrayList<SymbolType>(params.size());

        for(PDeclaration p : params){
            ADeclaration param = (ADeclaration) p;
            paramTypes.add(getSymbolType(param.getType()));
        }

        currentScope.addSymbol(new SymbolFunction(getSymbolType(node.getReturnType()), paramTypes, node.getName().getText(), node, currentScope));
    }

    @Override
    public void inAScopeStatement(AScopeStatement node)
    {
        currentScope = currentScope.addSubScope(node);
    }

    @Override
    public void outAScopeStatement(AScopeStatement node){
        //TODO We dont call super here ? is that correct?
        currentScope = currentScope.getParentScope();
    }

    @Override
    public void inAAssignmentDeclaration(AAssignmentDeclaration node){
        DeclareVariable(node);
    }

    @Override
    public void inADeclaration(ADeclaration node){
        DeclareVariable(node);
    }

    @Override
    public void caseTIdentifier(TIdentifier node)
    {
        currentScope.getSymbolOrThrow(node.getText());
    }

    @Override
    public void caseAStructRootDeclaration(AStructRootDeclaration node){
        inAStructRootDeclaration(node);
        currentScope = currentScope.getSubScopeByNode(node);
        node.getProgram().apply(this);
        currentScope = currentScope.getParentScope();
        outAStructRootDeclaration(node);
    }

    @Override
    public void caseADeclarationRootDeclaration(ADeclarationRootDeclaration node){
        inADeclarationRootDeclaration(node);
        //Do nothing the variable is already declared
        outADeclarationRootDeclaration(node);
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
        else if(type instanceof AStructTypeSpecifier){
            sType = SymbolType.Struct;
        }
        else if (type instanceof AEnumTypeSpecifier){
            sType = SymbolType.Enum;
        }

        return sType;
    }
}