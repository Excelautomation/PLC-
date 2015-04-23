package dk.aau.sw402F15.Symboltable.Table.Type;

import dk.aau.sw402F15.parser.node.PTypeSpecifier;

/**
 * Created by sahb on 23/04/15.
 */
public abstract class SymbolType {
    private PTypeSpecifier typeSpecifier;

    protected SymbolType(PTypeSpecifier typeSpecifier) {
        this.typeSpecifier = typeSpecifier;
    }
}
