package dk.aau.sw402F15.Symboltable.Type;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by sahb on 28/04/15.
 */
public class SymbolType {
    private final Type type;

    public SymbolType(Type type) {
        this.type = type;
    }

    private Type getType() {
        return type;
    }

    public boolean hasName() {
        return false;
    }

    public String getName() {
        throw new RuntimeException();
    }

    public boolean equals(SymbolType symbolType) {
        if (this.getType() != symbolType.getType())
            return false;

        if (this.hasName() && symbolType.hasName())
            return this.getName().equals(symbolType.getName());
        else if (!(this.hasName() && symbolType.hasName()))
            return true;

        return false;
    }

    public boolean equals(SymbolType.Type type) {
        return this.getType() == type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SymbolType)
            return equals((SymbolType) obj);
        else if (obj instanceof SymbolType.Type)
            return equals((SymbolType.Type) obj);

        return super.equals(obj);
    }

    @Override
    public String toString() {
        return type.name() + (hasName() ? (" " + getName()) : "");
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

    public static SymbolType Void() {
        return new SymbolType(Type.Void);
    }

    public enum Type {
        Int, Decimal, Boolean, Struct, Function, Timer, Port, Char, Array, Enum, Void
    }
}

