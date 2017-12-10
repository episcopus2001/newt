package newt.lang;

public class NewtPair extends NewtObject {

    private NewtObject first;
    private NewtObject second;

    public NewtPair(NewtObject first, NewtObject second) {
        this.first = first;
        this.second = second;
    }

    public NewtObject getFirst() {
        return first;
    }

    public NewtObject getSecond() {
        return second;
    }

    public void setFirst(NewtObject first) {
        this.first = first;
    }

    public void setSecond(NewtObject second) {
        this.second = second;
    }

    public String toString() {

        NewtPair obj = this;
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        while (obj.second.isPair()) {
            sb.append(obj.first);
            sb.append(" ");
            obj = obj.second.asPair();
        }
        sb.append(obj.first);
        if (!obj.second.isNil()) {
            sb.append(".");
            sb.append(obj.second);
        }

        sb.append(")");
        return sb.toString();
    }

}
