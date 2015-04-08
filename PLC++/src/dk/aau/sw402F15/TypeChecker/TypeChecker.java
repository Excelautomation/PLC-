package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolNotFoundException;
import dk.aau.sw402F15.TypeChecker.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;

/**
 * Created by sahb on 19/03/15.
 */


public class TypeChecker extends DepthFirstAdapter {
    private Scope rootScope = new Scope(null);
    private Scope currentScope;

    public TypeChecker() {
        currentScope = rootScope;
    }

    @Override
    public void inAScope(AScope node) {
        super.inAScope(node);

        currentScope = currentScope.addSubScope();
    }

    @Override
    public void outAScope(AScope node) {
        super.outAScope(node);

        currentScope = currentScope.getParentScope();
    }

    @Override
    public void outAStruct(AStruct node) {
        super.outAStruct(node);

        currentScope.addSymbol(new SymbolStruct(node.getIdentifier().getText(), node, currentScope));
    }

    @Override
    public void outAFunctionFunctionDeclaration(AFunctionFunctionDeclaration node) {
        super.outAFunctionFunctionDeclaration(node);

        // TODO: Should be updated
        ArrayList<SymbolType> formalParameters = new ArrayList<SymbolType>();
        SymbolType returnType = SymbolType.Function;

        currentScope.addSymbol(new SymbolFunction(returnType, formalParameters, node.getIdentifier().getText() , node, currentScope));
    }

    @Override
    public void outAVoidFunctionFunctionDeclaration(AVoidFunctionFunctionDeclaration node) {
        super.outAVoidFunctionFunctionDeclaration(node);

        currentScope.addSymbol(new Symbol(SymbolType.Method, node.getIdentifier().getText(), node, currentScope));
    }

    @Override
    public void outADeclarationDeclarationStatement(ADeclarationDeclarationStatement node) {
        super.outADeclarationDeclarationStatement(node);

        // TODO: Update symbol type
        SymbolType type = SymbolType.Boolean;


        currentScope.addSymbol(new Symbol(type, node.getIdentifier().getText(), node, currentScope));
    }

    @Override
    public void outADeclarationAssignmentDeclarationStatement(ADeclarationAssignmentDeclarationStatement node) {
        super.outADeclarationAssignmentDeclarationStatement(node);

        SymbolType type = getTypeFromNode(node.getType());

        // TODO: Update
        //currentScope.addSymbol(node.getIdentifier().getText(), type);

        // Check assignment

    }

    @Override
    public void outAAssignmentAssignmentStatement(AAssignmentAssignmentStatement node) {
        super.outAAssignmentAssignmentStatement(node);

        // TODO:
        /*SymbolType type = currentScope.getTypeOrThrow(node.getIdentifier().getText());

        // Check additional identifer
        if (type == SymbolType.Struct) {
            if (node.getAdditionalIdentifier() == null) {
                // Check assignment is struct

            } else {
                // Check additionalIdentifier

            }
        } else if (node.getAdditionalIdentifier() != null) {
            throw new RuntimeException();
        }*/

        // Check assignment

    }

    // TODO: Remove function
    private SymbolType getTypeFromNode(PType type) {
        if (type.getClass() == ABoolType.class) {
            return SymbolType.Boolean;
        } else if (type.getClass() == AIntType.class) {
            return SymbolType.Int;
        } else if (type.getClass() == ADoubleType.class) {
            return SymbolType.Decimal;
        } else if (type.getClass() == AFloatType.class) {
            return SymbolType.Decimal;
        } else if (type.getClass() == ALongType.class) {
            return SymbolType.Int;
        } else if (type.getClass() == ATimerType.class) {
            return SymbolType.Timer;
        } else if (type.getClass() == APortType.class) {
            return SymbolType.Port;
        } else if (type.getClass() == AIdentifierType.class) {
            // Find type from structs
            AIdentifierType identifier = (AIdentifierType)type;
           // SymbolType t = currentScope.getType(identifier.getIdentifier().getText());
           // if (t == SymbolType.Struct)
             //   return t;

            throw new SymbolNotFoundException();
        }

        throw new SymbolNotFoundException();
    }
}
