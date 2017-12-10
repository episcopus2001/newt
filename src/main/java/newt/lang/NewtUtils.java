package newt.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class NewtUtils {

    public static void close(Reader reader) {
        try {
            reader.close();
        } catch (IOException e) {
            throw new NewtException(e);
        }
    }

    public static void close(Writer writer) {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new NewtException(e);
        }
    }

    public static int read(Reader reader) {
        try {
            return reader.read();
        } catch (IOException e) {
            throw new NewtException(e);
        }
    }

    public static boolean ready(Reader reader) {
        try {
            return reader.ready();
        } catch (IOException e) {
            throw new NewtException(e);
        }
    }

    public static String readLine(Reader reader) {
        try {
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new NewtException(e);
        }
    }

}
