package newt.lang;

import java.util.HashMap;
import java.util.Map;

public class NewtSymbol extends NewtObject {

    private static Map<String, NewtSymbol> symbols = new HashMap<>();
    private String value;

    public static NewtSymbol intern(String value) {
        NewtSymbol symbol = symbols.get(value);
        if (symbol == null) {
            symbol = new NewtSymbol(value);
            symbols.put(value, symbol);
        }
        return symbol;
    }

    private NewtSymbol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(java.lang.Object that) {
        return this == that;
    }

    public String toString() {
        return value;
    }
}
