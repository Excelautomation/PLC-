package dk.aau.sw402F15;

import dk.aau.sw402F15.Symboltable.Type.SymbolType;
import dk.aau.sw402F15.parser.node.*;

/**
 * Created by sahb on 23/04/15.
 */
public class Helper {
    public static SymbolType getSymbolTypeFromTypeSpecifier(PTypeSpecifier type) {

        //Check for errors
        if (type == null) {
            throw new NullPointerException();
        }

        //Find the symbol type
        SymbolType sType = null;
        if (type instanceof ABoolTypeSpecifier) {
            sType = SymbolType.Boolean();
        } else if (type instanceof ACharTypeSpecifier) {
            sType = SymbolType.Char();
        } else if (type instanceof ADoubleTypeSpecifier || type instanceof AFloatTypeSpecifier) {
            sType = SymbolType.Decimal();
        } else if (type instanceof AIntTypeSpecifier || type instanceof ALongTypeSpecifier) {
            sType = SymbolType.Int();
        } else if (type instanceof AInputTypeSpecifier) {
            sType = SymbolType.PortInput();
        } else if (type instanceof AOutputTypeSpecifier) {
            sType = SymbolType.PortOuput();
        } else if (type instanceof ATimerTypeSpecifier) {
            sType = SymbolType.Timer();
        } else if (type instanceof AStructTypeSpecifier) {
            AStructTypeSpecifier struct = (AStructTypeSpecifier) type;
            sType = SymbolType.Struct(struct.getIdentifier().getText());
        } else if (type instanceof AEnumTypeSpecifier) {
            sType = SymbolType.Enum();
        } else if (type instanceof AVoidTypeSpecifier) {
            sType = SymbolType.Void();
        }

        return sType;
    }
}
