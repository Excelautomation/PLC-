package dk.aau.sw402F15.Symboltable.Type;

import dk.aau.sw402F15.Symboltable.SymbolArray;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by sahb on 28/04/15.
 */
public class SymbolType {
    private final Type type;

    public SymbolType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean hasName() {
        return false;
    }

    public String getName() {
        throw new RuntimeException();
    }

    public static SymbolType Int() {
        return new SymbolType(Type.Int);
    }

    public static SymbolType Decimal() {
        return new SymbolType(Type.Decimal);
    }

    public static SymbolType Boolean() {
        return new SymbolType(Type.Boolean);
    }

    public static SymbolType Char() {
        return new SymbolType(Type.Char);
    }

    public static SymbolType Struct(String name) {
        return new SymbolTypeWithName(Type.Struct, name);
    }

    public static SymbolType Function(String name) {
        return new SymbolTypeWithName(Type.Function, name);
    }

    public static SymbolType Timer() {
        return new SymbolType(Type.Timer);
    }

    public static SymbolType Port() {
        return new SymbolType(Type.Port);
    }

    public static SymbolType Array() {
        return new SymbolType(Type.Array);
    }

    public static SymbolType Enum() {
        throw new NotImplementedException();
    }

    public static SymbolType Method() {
        return new SymbolType(Type.Method);
    }

    public enum Type {
        Int, Decimal, Boolean, Struct, Function, Timer, Port, Char, Array, Enum, Method
    }
}

