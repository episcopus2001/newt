package newt.lang;

import java.io.*;

public class NewtInputPort extends NewtPort {

    private BufferedReader reader;
    private NewtCharacter nextCharacter;
    private NewtCharacter prevCharacter;

    public NewtInputPort(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public NewtCharacter getNextChar() {
        return nextCharacter;
    }

    public NewtCharacter getPrevChar() {
        return prevCharacter;
    }

    public void setNextChar(NewtCharacter ch) {
        nextCharacter = ch;
    }

    public void setPrevChar(NewtCharacter ch) {
        prevCharacter = ch;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public final String toString() {
        return "#<input port>";
    }

    @Override
    public void close() {
        NewtUtils.close(reader);
    }

}
