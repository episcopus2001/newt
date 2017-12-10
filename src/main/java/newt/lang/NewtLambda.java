package newt.lang;

public class NewtLambda extends NewtObject {

    private NewtObject formals;
    private NewtObject body;
    private NewtString name;

    public NewtLambda(NewtObject formals, NewtObject body) {
        this.formals = formals;
        this.body = body;
    }

    public NewtObject getFormals() { return formals; }
    public NewtObject getBody() { return body; }

    public NewtString getName() { return name; }

    public String toString() {
        if (name != null)
            return "#<LAMBDA " + name + ">";
        return "#<LAMBDA>";
    }

}
