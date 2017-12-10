package newt.lang;

import java.io.*;

public class NewtOutputPort extends NewtPort {

    private Writer writer;

    public NewtOutputPort(Writer writer) {
        this.writer = writer;
    }

    public final String toString() {
        return "#<output port>";
    }

    public void write(String value) {
        try {
            writer.write(value);
            writer.flush();
        } catch (IOException e) {
            throw new NewtException("Could not write to output port.", e);
        }
    }

    public void close() {
        NewtUtils.close(writer);
    }
}
