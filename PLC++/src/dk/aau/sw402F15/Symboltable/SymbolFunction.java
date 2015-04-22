package dk.aau.sw402F15.Symboltable;

import dk.aau.sw402F15.parser.node.Node;

import java.util.List;

public class SymbolFunction extends Symbol {
    private SymbolType returnType;
    private List<SymbolType> formalParameters;

    public SymbolFunction(SymbolType returnType, List<SymbolType> formalParameters, String name, Node node, Scope scope) {
        super(SymbolType.Function, name, node, scope);

        this.returnType = returnType;
        this.formalParameters = formalParameters;
    }

    public List<SymbolType> getFormalParameters() {
        return formalParameters;
    }

    public SymbolType getReturnType() {
        return returnType;
    }
}

