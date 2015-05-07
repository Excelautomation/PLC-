package dk.aau.sw402F15.Exception.TypeChecker;

import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sahb on 07/05/15.
 */
public class IncompaitbleTypesException extends TypeCheckerException {
    private List<SymbolType> types;

    private IncompaitbleTypesException(Node node, List<SymbolType> types) {
        super("Cannot assign " + getTypeString(types), node);

        this.types = types;
    }

    private IncompaitbleTypesException(Node node, SymbolType type1) {
        this(node, createArrayList(type1));
    }

    public IncompaitbleTypesException(Node node, SymbolType type1, SymbolType type2) {
        this(node, createArrayList(type1, type2));
    }

    public IncompaitbleTypesException(Node node, SymbolType type1, SymbolType type2, SymbolType type3) {
        this(node, createArrayList(type1, type2, type3));
    }

    private static <T> ArrayList<T> createArrayList(T type) {
        ArrayList<T> arrayList = new ArrayList<T>();
        arrayList.add(type);
        return arrayList;
    }

    private static <T> ArrayList<T> createArrayList(T type1, T type2) {
        ArrayList<T> arrayList = createArrayList(type1);
        arrayList.add(type2);
        return arrayList;
    }

    private static <T> ArrayList<T> createArrayList(T type1, T type2, T type3) {
        ArrayList<T> arrayList = createArrayList(type1, type2);
        arrayList.add(type3);
        return arrayList;
    }

    private static String getTypeString(List<SymbolType> symbolTypes) {
        String out = "";
        for (SymbolType type : symbolTypes) {
            if (!out.equals("")) {
                out += " to ";
            }
            out += type.getType().name();

            if (type.hasName()) {
                out += " " + type.getName();
            }
        }

        return out;
    }
}