package newt.lang;

public class NewtSingleton extends NewtObject {

    private String name;

    public NewtSingleton(String name) {
        this.name = name;
    }

    public final String toString() {
        return name;
    }
}
