package newt.lang;

public class NewtString extends NewtObject {

    private java.lang.String value;

    public NewtString(java.lang.String value) {
        this.value = value;
    }

    public java.lang.String getValue() {
        return value;
    }

    public java.lang.String toString() {
        return "\"" + value + "\"";
    }

}
