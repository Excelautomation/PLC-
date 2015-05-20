package dk.aau.sw402F15.CodeGenerator;

import dk.aau.sw402F15.Symboltable.Symbol;
import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.Node;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Claus on 20-05-2015.
 */
public class Stack{
    public final int FieldSize = 2;           // defines the size of each object in the stack as nr of bytes.
    private int stackPointerStart = 400;            // start address of the stack area in H memory
    private int stackPointer = stackPointerStart;   // stackPointer
    private CodeGenerator generator;

    public Stack(CodeGenerator generator) {
        this.generator = generator;
    }

    public < T > void push(T value)
    {
        if(stackPointer <= 510){
            stackPointerIncrement();

            if (value.getClass() == Integer.class)
                generator.Emit("MOVL(498) &" + value + " " + stackPointer(), true);

            else if (value.getClass() == Float.class || value.getClass() == Double.class)
                generator.Emit("+F(454) +0,0 +" + value.toString().replace(".", ",") + " " + stackPointer() + "", true);

            else if (value.getClass() == String.class)
                generator.Emit("MOVL(498) " + value + " " + stackPointer(), true);

            else if (value.getClass() == Boolean.class){

                if ((Boolean)value)
                    generator.Emit("SET " + stackPointer() + ".00", true);
                else
                    generator.Emit("RSET " + stackPointer() + ".00", true);
            }
            else
                throw new NotImplementedException();
        } else
            throw new OutOfMemoryError();
    }

    public < T > void push(T value, Node node)
    {
        Symbol symbol = generator.currentScope.getSymbolOrThrow(value.toString(), node);

        if (symbol.getType().getType() == SymbolType.Type.Boolean) {
            String pointer = getPointerAndIncrement();
            generator.Emit("LD " + value, true);
            generator.Emit("SET " + pointer + ".00", true);
            generator.Emit("LDNOT " + value, true);
            generator.Emit("RSET " + pointer + ".00", true);
        }

        else
            push(value);
    }

    public String pop()
    {
        String stPtr = stackPointer();
        stackPointerDecrement();
        return stPtr;
    }

    public String peek(){
        return stackPointer();
    }

    // returns the address of the memory the stack points to.
    private String stackPointer(){
        return "W" + stackPointer;
    }

    public String getPointerAndIncrement(){
        stackPointerIncrement();
        return stackPointer();
    }

    // increments stack pointer with stack size
    private void stackPointerIncrement() {
        if (stackPointer > 510)
            throw new OutOfMemoryError();
        stackPointer += FieldSize;
    }

    // increments stack pointer x times
    private void stackPointerIncrement(int x) {
        for (int i = 0; i < x; i++)
            stackPointerIncrement();
    }

    // decrements stack pointer with stack size
    private void stackPointerDecrement() {
        if (stackPointer < 400)
            throw new OutOfMemoryError();
        stackPointer -= FieldSize;
    }

    // decrement stack pointer x times
    private void stackPointerDecrement(int x) {
        for (int i = 0; i < x; i++)
            stackPointerDecrement();
    }
}
