package dk.aau.sw402F15;

import dk.aau.sw402F15.Symboltable.SymbolType;
import dk.aau.sw402F15.parser.node.*;

/**
 * Created by sahb on 23/04/15.
 */
public class Helper {
    public static SymbolType getSymbolTypeFromTypeSpecifier(PTypeSpecifier type){

        //Check for errors
        if(type == null){
            throw new NullPointerException();
        }

        //Find the symbol type
        SymbolType sType = null;
        if(type.getClass() == ABoolTypeSpecifier.class)
        {
            sType = SymbolType.Boolean;
        }
        else if(type.getClass() == ACharTypeSpecifier.class)
        {
            sType = SymbolType.Char;
        }
        else if(type.getClass() == ADoubleTypeSpecifier.class || type.getClass() == AFloatTypeSpecifier.class)
        {
            sType = SymbolType.Decimal;
        }
        else if(type.getClass() == AIntTypeSpecifier.class || type.getClass() == ALongTypeSpecifier.class)
        {
            sType = SymbolType.Int;
        }
        else if(type.getClass() == APortTypeSpecifier.class)
        {
            sType = SymbolType.Port;
        }
        else if(type.getClass() == ATimerTypeSpecifier.class)
        {
            sType = SymbolType.Timer;
        }
        else if(type.getClass() == AStructTypeSpecifier.class){
            sType = SymbolType.Struct;
        }
        else if (type.getClass() == AEnumTypeSpecifier.class){
            sType = SymbolType.Enum;
        }

        return sType;
    }
}
