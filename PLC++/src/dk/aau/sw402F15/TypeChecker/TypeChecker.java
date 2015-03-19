package dk.aau.sw402F15.TypeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolNotFoundException;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

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

        currentScope.addSymbol(node.getIdentifier().getText(), Type.Struct);
    }

    @Override
    public void outAFunctionFunctionDeclaration(AFunctionFunctionDeclaration node) {
        super.outAFunctionFunctionDeclaration(node);

        currentScope.addSymbol(node.getIdentifier().getText(), Type.Function);
    }

    @Override
    public void outAVoidFunctionFunctionDeclaration(AVoidFunctionFunctionDeclaration node) {
        super.outAVoidFunctionFunctionDeclaration(node);

        currentScope.addSymbol(node.getIdentifier().getText(), Type.Method);
    }

    @Override
    public void outADeclarationDeclarationStatement(ADeclarationDeclarationStatement node) {
        super.outADeclarationDeclarationStatement(node);

        currentScope.addSymbol(node.getIdentifier().getText(), getTypeFromNode(node.getType()));
    }

    @Override
    public void outADeclarationAssignmentDeclarationStatement(ADeclarationAssignmentDeclarationStatement node) {
        super.outADeclarationAssignmentDeclarationStatement(node);

        Type type = getTypeFromNode(node.getType());

        currentScope.addSymbol(node.getIdentifier().getText(), type);

        // Check assignment

    }

    @Override
    public void outAAssignmentAssignmentStatement(AAssignmentAssignmentStatement node) {
        super.outAAssignmentAssignmentStatement(node);

        Type type = currentScope.getTypeOrThrow(node.getIdentifier().getText());

        // Check additional identifer
        if (type == Type.Struct) {
            if (node.getAdditionalIdentifier() == null) {
                // Check assignment is struct

            } else {
                // Check additionalIdentifier

            }
        } else if (node.getAdditionalIdentifier() != null) {
            throw new RuntimeException();
        }

        // Check assignment

    }

    private Type getTypeFromNode(PType type) {
        if (type.getClass() == ABoolType.class) {
            return Type.Boolean;
        } else if (type.getClass() == AIntType.class) {
            return Type.Numeric;
        } else if (type.getClass() == ADoubleType.class) {
            return Type.Numeric;
        } else if (type.getClass() == AFloatType.class) {
            return Type.Numeric;
        } else if (type.getClass() == ALongType.class) {
            return Type.Numeric;
        } else if (type.getClass() == ATimerType.class) {
            return Type.Timer;
        } else if (type.getClass() == APortType.class) {
            return Type.Port;
        } else if (type.getClass() == AIdentifierType.class) {
            // Find type from structs
            AIdentifierType identifier = (AIdentifierType)type;

            Type t = currentScope.getType(identifier.getIdentifier().getText());
            if (t == Type.Struct)
                return t;

            throw new SymbolNotFoundException();
        }

        throw new SymbolNotFoundException();
    }
}
