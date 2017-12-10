package newt.lang;

//import static newt.lang.NewtRuntime.*;

import static newt.lang.NewtRuntime.OBJ_NIL;

public abstract class NewtObject {

    public boolean isNumber() {
        return this instanceof NewtNumber;
    }

    public boolean isPair() {
        return this instanceof NewtPair;
    }

    public boolean isString() {
        return this instanceof NewtString;
    }

    public boolean isCharacter() {
        return this instanceof NewtCharacter;
    }

    public boolean isSymbol() {
        return this instanceof NewtSymbol;
    }

    public boolean isProcedure() {
        return this instanceof NewtProcedure;
    }

    public boolean isLambda() {
        return this instanceof NewtLambda;
    }

    public boolean isSubroutine() {
        return this instanceof NewtSubroutine;
    }

    public boolean isPort() {
        return this instanceof NewtPort;
    }

    public boolean isInputPort() {
        return this instanceof NewtInputPort;
    }

    public boolean isOutputPort() {
        return this instanceof NewtOutputPort;
    }

    public boolean isSingleton() {
        return this instanceof NewtSingleton;
    }

    public boolean isNil() {
        return this == OBJ_NIL;
    }

    public NewtNumber asNumber() {
        return (NewtNumber) this;
    }

    public NewtPair asPair() {
        return (NewtPair) this;
    }

    public NewtString asString() {
        return (NewtString) this;
    }

    public NewtCharacter asCharacter() {
        return (NewtCharacter) this;
    }

    public NewtSymbol asSymbol() {
        return (NewtSymbol) this;
    }

    public NewtProcedure asProcedure() {
        return (NewtProcedure) this;
    }

    public NewtLambda asLambda() {
        return (NewtLambda) this;
    }

    public NewtSubroutine asSubroutine() {
        return (NewtSubroutine) this;
    }

    public NewtPort asPort() {
        return (NewtPort) this;
    }

    public NewtOutputPort asOutputPort() {
        return (NewtOutputPort) this;
    }

    public NewtInputPort asInputPort() {
        return (NewtInputPort) this;
    }

    public boolean isList() {
        return isPair() || isNil();
    }
    public boolean isAtom() {
        return !isPair();
    }

}
