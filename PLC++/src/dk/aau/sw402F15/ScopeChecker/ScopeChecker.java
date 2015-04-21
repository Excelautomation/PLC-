package dk.aau.sw402F15.ScopeChecker;

import com.sun.org.apache.xpath.internal.operations.Bool;
import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolFoundWrongTypeException;
import dk.aau.sw402F15.TypeChecker.Exceptions.SymbolNotFoundException;
import dk.aau.sw402F15.TypeChecker.Symboltable.*;
import dk.aau.sw402F15.parser.analysis.DepthFirstAdapter;
import dk.aau.sw402F15.parser.node.*;
import sun.org.mozilla.javascript.internal.ObjToIntMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mads on 08/04/15.
 *
 */

public class ScopeChecker extends DepthFirstAdapter {
    private Scope rootScope = new Scope(null, null);
    private Scope currentScope;

    public ScopeChecker() {
        currentScope = rootScope;
    }

    // When a functions is entered this makes sure that the scope is changed as well
    @Override
    public void caseAFunctionRootDeclaration(AFunctionRootDeclaration node) {
        inAFunctionRootDeclaration(node);

        if (node.getReturnType() != null) {
            node.getReturnType().apply(this);
        }
        if (node.getName() != null) {
            node.getName().apply(this);
        }
        // Open subscope before visiting the inner statements
        currentScope = currentScope.addSubScope(node);
        // Import parameters so that they are accessible from within the function.
        List<PDeclaration> params = new ArrayList<PDeclaration>(node.getParams());
        for (PDeclaration parameter : params) {
            parameter.apply(this);
        }
        // Visit the inner statements
        List<PStatement> statements = new ArrayList<PStatement>(node.getStatements());
        for (PStatement statement : statements) {
            statement.apply(this);
        }
        // Leave the subscope
        currentScope = currentScope.getParentScope();

        outAFunctionRootDeclaration(node);
    }

    // Runs a small preprocessor which makes sure that global variables, structs, enum and functions are declared
    // before continuing on with the compiling of the program
    @Override
    public void caseAProgram(AProgram node){
        List<AEnumRootDeclaration> enums = new ArrayList<AEnumRootDeclaration>();
        List<AFunctionRootDeclaration> functions = new ArrayList<AFunctionRootDeclaration>();
        List<AStructRootDeclaration> structs = new ArrayList<AStructRootDeclaration>();
        List<ADeclarationRootDeclaration> variables = new ArrayList<ADeclarationRootDeclaration>();

        // Separate the different root decelerations into different lists.
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
        // Declare the different elements in the order in which they are likely to need each other
        for (AEnumRootDeclaration e : enums){
            DeclareEnum(e);
        }
        for (AStructRootDeclaration s : structs){
            StructBuilder builder = new StructBuilder(rootScope);
            s.apply(builder);
        }
        for (ADeclarationRootDeclaration v : variables){
            DeclareVariable(v.getDeclaration());
        }
        for (AFunctionRootDeclaration f : functions){
            DeclareFunction(f);
        }
        // Runs the normal processor
        super.caseAProgram(node);
    }

    private void DeclareVariable(PDeclaration node){
        boolean isArray = false;
        boolean isConst = false;
        String name = null;
        SymbolType type = null;

        if(node.getClass() == ADeclaration.class){
            ADeclaration n = (ADeclaration)node;
            isArray = n.getArray() != null;
            isConst = n.getQuailifer() != null;
            name = n.getName().getText();
            type = getSymbolType(n.getType());
        }
        else if (node.getClass() == AAssignmentDeclaration.class){
            AAssignmentDeclaration n = (AAssignmentDeclaration)node;
            isArray = n.getArray() != null;
            isConst = n.getQuailifer() != null;
            name = n.getName().getText();
            type = getSymbolType(n.getType());
        }
        if(isArray)
            DeclareArray(name, type, node);
        else
            DeclareVariable(name, type, node, isConst);
    }

    private void DeclareVariable(String name, SymbolType type, Node node, boolean isConst){
        currentScope.addSymbol(new SymbolVariable(type, name, node, currentScope, isConst));
    }

    private void DeclareArray(String name, SymbolType type, Node node){
        currentScope.addSymbol(new SymbolArray(type, name, node, currentScope));
    }

    private void DeclareEnum(AEnumRootDeclaration node){
        currentScope = currentScope.addSubScope(node);
        List<PEnumFlag> list = node.getProgram();
        for(PEnumFlag flag : list){
            flag.apply(this);
        }
        List<Symbol> symbols = currentScope.toList();
        currentScope = currentScope.getParentScope();
        List<PEnumFlag> flags = new ArrayList<PEnumFlag>(symbols.size());
        for (Symbol symbol : symbols){
            if(symbol.getNode().getClass() == PEnumFlag.class){
                PEnumFlag flag = (PEnumFlag)symbol.getNode();
                flags.add(flag);
            }
        }
        currentScope.addSymbol(new SymbolEnum(node.getName().getText(), flags, node, currentScope));
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
        currentScope = currentScope.getSubScopeByNodeOrThrow(node);
        // Already applied in struct builder
        //node.getProgram().apply(this);
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
    public void inAMemberExpr(AMemberExpr node) {     
        super.inAMemberExpr(node);

        // check if Left node is an identifierExpr
        if (node.getLeft().getClass() == AIdentifierExpr.class){
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
        }
    }

    private void checkRightNode(Object symbol){

    }

    // Returns the outermost scope as the symbol table.
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