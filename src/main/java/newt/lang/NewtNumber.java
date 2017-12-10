package newt.lang;

public class NewtNumber extends NewtObject {

    private int value;

    public NewtNumber(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return "" + value;
    }
}
