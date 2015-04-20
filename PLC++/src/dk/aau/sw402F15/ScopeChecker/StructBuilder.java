package dk.aau.sw402F15.ScopeChecker;

import dk.aau.sw402F15.TypeChecker.Symboltable.*;
import dk.aau.sw402F15.parser.node.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Claus on 16-04-2015.
 */
public class StructBuilder extends DepthFirstAdapter {
    private Scope rootScope;
    private Scope returnScope;

    public StructBuilder(Scope rootScope) {
        this.rootScope = rootScope;
    }

    @Override
    public void inAStructRootDeclaration(AStructRootDeclaration node) {
        super.inAStructRootDeclaration(node);

        this.returnScope = this.rootScope.addSubScope(node);
    }

    @Override
    public void outAStructRootDeclaration(AStructRootDeclaration node) {
        super.outAStructRootDeclaration(node);

        this.rootScope.addSymbol(new SymbolStruct(node.getName().getText(), returnScope.toList(), node, this.rootScope));
    }

    @Override
    public void inAAssignmentDeclaration(AAssignmentDeclaration node){
        returnScope.addSymbol(new SymbolVariable(getSymbolType(node.getType()), node.getName().getText(), node, returnScope, false));
    }

    @Override
    public void inADeclaration(ADeclaration node){
        returnScope.addSymbol(new SymbolVariable(getSymbolType(node.getType()), node.getName().getText(), node, returnScope, false));
    }

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node){
        LinkedList<PDeclaration> params = node.getParams();
        List<SymbolType> paramTypes = new ArrayList<SymbolType>(params.size());

        for(PDeclaration p : params){
            ADeclaration param = (ADeclaration) p;
            paramTypes.add(getSymbolType(param.getType()));
        }

        returnScope.addSymbol(new SymbolFunction(getSymbolType(node.getReturnType()), paramTypes, node.getName().getText(), node, returnScope));
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
