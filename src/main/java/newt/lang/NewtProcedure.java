package newt.lang;

public class NewtProcedure extends NewtObject {

    private NewtLambda lambda;
    private NewtObject environment;

    public NewtProcedure(NewtLambda lambda, NewtObject environment) {
        this.lambda = lambda;
        this.environment = environment;
    }

    public NewtLambda getLambda() {
        return lambda;
    }

    public NewtObject getEnvironment() {
        return environment;
    }

    public String toString() {
        if (lambda.getName() != null)
            return "#<procedure " + lambda.getName() + ">";
        else
            return "#<procedure>";
    }
}
