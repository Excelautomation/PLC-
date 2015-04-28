package dk.aau.sw402F15.Symboltable.Type;

public class SymbolTypeWithName extends SymbolType {

    private String name;

    public SymbolTypeWithName(Type type, String name) {
        super(type);

        this.name = name;
    }

    @Override
    public boolean hasName() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }
}
