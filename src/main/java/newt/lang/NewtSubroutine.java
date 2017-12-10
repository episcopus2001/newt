package newt.lang;

import java.util.function.Function;

public class NewtSubroutine extends NewtObject {

    private Function<NewtObject, NewtObject> fn;

    public NewtSubroutine(Function<NewtObject, NewtObject> fn) {
        this.fn = fn;
    }

    public Function<NewtObject, NewtObject> getFunction() {
        return fn;
    }

    public String toString() {
        return "#<SUBROUTINE>";
    }


}
