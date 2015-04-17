package dk.aau.sw402F15.TypeChecker.Symboltable;

import dk.aau.sw402F15.parser.node.Node;
import dk.aau.sw402F15.parser.node.PEnumFlag;

import java.util.List;

/**
 * Created by Claus on 16-04-2015.
 */
public class SymbolEnum extends Symbol{
    private List<PEnumFlag> enumFlags;

    public SymbolEnum(String name, List<PEnumFlag> flags, Node node, Scope scope) {
        super(SymbolType.Enum, name, node, scope);
        enumFlags = flags;
    }

    public List<PEnumFlag> getFlags(){ return enumFlags; }
}
