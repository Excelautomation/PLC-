package dk.aau.sw402F15.ScopeChecker;

import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolFoundWrongTypeException;
import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolNotFoundException;
import dk.aau.sw402F15.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mads on 08/04/15.
 *
 */

public class ScopeChecker extends ScopeDepthFirstAdapter {
    public ScopeChecker(Scope rootScope) {
        this(rootScope, rootScope);
    }

    public ScopeChecker(Scope rootScope, Scope currentScope) {
        super(rootScope, currentScope);
    }

    @Override
    public void caseTIdentifier(TIdentifier node)
    {
        currentScope.getSymbolOrThrow(node.getText());
    }

    @Override
    public void inAMemberExpr(AMemberExpr node) {     
        super.inAMemberExpr(node);

        return;

        // check if Left node is an identifierExpr
        /*if (node.getLeft().getClass() == AIdentifierExpr.class){
            //cast left node
            AIdentifierExpr IdentifierExpr = (AIdentifierExpr)node.getLeft();
            // Get indentifierExpr from symbolTable
            Symbol symbol = currentScope.getSymbolOrThrow(IdentifierExpr.getName().getText());

            //check if returned symbol is a symbolStruct
            if ( symbol.getClass() == SymbolStruct.class){
                SymbolStruct struct = (SymbolStruct)symbol;
                Scope structScope = currentScope.getSubScopeByNode(struct.getNode());

                // Check if right node is an identifier-----------------------------------------------------
                if (node.getRight().getClass() == AIdentifierExpr.class){
                    AIdentifierExpr rightNodeExpr = (AIdentifierExpr)node.getRight();
                    Symbol rightNodeSymbol = structScope.getSymbolOrThrow(rightNodeExpr.getName().getText());

                    // check if the rightNode is in struct's scope
                    List<Symbol> symbolList = struct.getSymbolList();
                    boolean okBit = false;
                    for(Symbol sym : symbolList) {
                        if (sym.getName().equals(rightNodeSymbol.getName())) {
                            okBit = true;
                        }
                    }
                    if (!okBit) {
                        throw new SymbolNotFoundException();
                    }

                // Check if right node is a functionCall---------------------------------------------------
                }else if (node.getRight().getClass() == AFunctionCallExpr.class){
                    AFunctionCallExpr rightNodeExpr = (AFunctionCallExpr)node.getRight();
                    Symbol rightNodeSymbol = structScope.getSymbolOrThrow(rightNodeExpr.getName().getText());

                    // / check if the rightNode is in struct's scope
                    List<Symbol> symbolList = struct.getSymbolList();
                    boolean okBit = false;
                    for(Symbol sym : symbolList) {
                        if (sym.getName().equals(rightNodeSymbol.getName())) {
                            okBit = true;
                        }
                    }
                    if (!okBit) {
                        throw new SymbolNotFoundException();
                    }
                } else {
                    throw new SymbolFoundWrongTypeException();
                }
            } else {
                throw new SymbolFoundWrongTypeException();
            }
        // check if Left node is a functionCallExpr
        } else if ((node.getLeft().getClass() == AFunctionCallExpr.class)) {
            //cast left node
            AFunctionCallExpr functionCallExpr = (AFunctionCallExpr)node.getLeft();
            // Get FunctionCallExpr from symboltable
            Symbol symbol = currentScope.getSymbolOrThrow(functionCallExpr.getName().getText());

            //check if returned symbol is a SymbolFunction
            if (symbol.getClass() == SymbolFunction.class) {
                SymbolFunction symbolFunction = (SymbolFunction)symbol;

                // Check if symbolfunction has correct return type and..
                // Check if symbolfunction has correct node
                if (symbolFunction.getReturnType() == SymbolType.Struct && symbolFunction.getNode().getClass() == AFunctionRootDeclaration.class) {
                    AFunctionRootDeclaration function = (AFunctionRootDeclaration) symbolFunction.getNode();

                    // Check if returntype is AStructTypeSpecifier
                    if (function.getReturnType().getClass() == AStructTypeSpecifier.class) {
                        AStructTypeSpecifier struct = (AStructTypeSpecifier) function.getReturnType();

                        // get structTypeSpecifier from symboltable
                        Symbol structTypeSymbol = currentScope.getSymbolOrThrow(struct.getIdentifier().getText());


                        if (structTypeSymbol.getClass() == SymbolStruct.class){
                            SymbolStruct symbolStruct = (SymbolStruct)structTypeSymbol;

                            Scope structScope = currentScope.getSubScopeByNode(symbolStruct.getNode());

                            // Check if right node is an identifier-----------------------------------------------------
                            if (node.getRight().getClass() == AIdentifierExpr.class){
                                AIdentifierExpr rightNodeExpr = (AIdentifierExpr)node.getRight();
                                Symbol rightNodeSymbol = structScope.getSymbolOrThrow(rightNodeExpr.getName().getText());

                                // check if the rightNode is in struct's scope
                                List<Symbol> symbolList = symbolStruct.getSymbolList();
                                boolean okBit = false;
                                for(Symbol sym : symbolList) {
                                    if (sym.getName().equals(rightNodeSymbol.getName())) {
                                        okBit = true;
                                    }
                                }
                                if (!okBit) {
                                    throw new SymbolNotFoundException();
                                }
                                // Check if right node is a functionCall---------------------------------------------------
                            }else if (node.getRight().getClass() == AFunctionCallExpr.class){
                                AFunctionCallExpr rightNodeExpr = (AFunctionCallExpr)node.getRight();
                                Symbol rightNodeSymbol = structScope.getSymbolOrThrow(rightNodeExpr.getName().getText());

                                // / check if the rightNode is in struct's scope
                                List<Symbol> symbolList = symbolStruct.getSymbolList();
                                boolean okBit = false;
                                for(Symbol sym : symbolList) {
                                    if (sym.getName().equals(rightNodeSymbol.getName())) {
                                        okBit = true;
                                    }
                                }
                                if (!okBit) {
                                    throw new SymbolNotFoundException();
                                }
                            // Right node is neigther variable or functionCall ----------------------------------------
                            } else {
                                throw new SymbolFoundWrongTypeException();
                            }
                        } else {
                            throw new SymbolFoundWrongTypeException();
                        }
                    } else {
                        throw new SymbolFoundWrongTypeException();
                    }
                } else {
                    throw new SymbolFoundWrongTypeException();
                }
            } else {
                throw new SymbolFoundWrongTypeException();
            }
        } else {
            throw new SymbolFoundWrongTypeException();
        }*/
    }

    // Returns the outermost scope as the symbol table.
    public Scope getSymbolTable() {
        return rootScope;
    }
}