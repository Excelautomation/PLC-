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
            if(isArray)
                DeclareArray(n.getName().getText(), getSymbolType(n.getType()), n);
            else
                DeclareVariable(n.getName().getText(), getSymbolType(n.getType()), n);
        }
        else if (node.getClass() == AAssignmentDeclaration.class){
            AAssignmentDeclaration n = (AAssignmentDeclaration)node;
            boolean isArray = n.getArray() != null;
            if(isArray)
                DeclareArray(n.getName().getText(), getSymbolType(n.getType()), n);
            else
                DeclareVariable(n.getName().getText(), getSymbolType(n.getType()), n);
        }
    }

    private void DeclareVariable(String name, SymbolType type, Node node){
        currentScope.addSymbol(new SymbolVariable(type, name, node, currentScope, false));
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

        DeclareVariable(node);
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
        }
        else if (type instanceof AEnumTypeSpecifier){
            sType = SymbolType.Enum;
        }

        return sType;
    }
}