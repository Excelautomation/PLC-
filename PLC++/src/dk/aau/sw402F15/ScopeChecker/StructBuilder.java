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
    Scope scope;

    public Symbol BuildSymbol(AStructRootDeclaration struct, Scope scope){
        this.scope = new Scope(null, struct);
        struct.getProgram().apply(this);
        return new SymbolStruct(struct.getName().getText(), scope.toList(), struct, scope);
    }

    @Override
    public void inAAssignmentDeclaration(AAssignmentDeclaration node){
        scope.addSymbol(new Symbol(getSymbolType(node.getType()), node.getName().getText(), node, scope));
    }

    @Override
    public void inADeclaration(ADeclaration node){
        scope.addSymbol(new Symbol(getSymbolType(node.getType()), node.getName().getText(), node, scope));
    }

    @Override
    public void inAFunctionRootDeclaration(AFunctionRootDeclaration node){
        LinkedList<PDeclaration> params = node.getParams();
        List<SymbolType> paramTypes = new ArrayList<SymbolType>(params.size());

        for(PDeclaration p : params){
            ADeclaration param = (ADeclaration) p;
            paramTypes.add(getSymbolType(param.getType()));
        }

        scope.addSymbol(new SymbolFunction(getSymbolType(node.getReturnType()), paramTypes, node.getName().getText(), node, scope));
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
