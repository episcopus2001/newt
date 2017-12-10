package newt;

import newt.lang.*;
import newt.lang.NewtObject;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import static newt.lang.NewtRuntime.*;

public class NewtReader {

    private PushbackReader reader;

    public NewtReader(Reader reader) {
        this.reader = new PushbackReader(reader);
    }

    public NewtObject read() {
        try {
            return readObject();
        } catch (Exception e) {
            throw new NewtException("Could not read.", e);
        }
    }

    private NewtObject readObject() throws IOException {

        NewtObject obj = null;
        StringBuffer sb = null;

        while (true) {
            int c = reader.read();
            char d = (char) c;
            switch (c) {
                case -1:
                    return CHAR_EOF;
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    continue;
                case ';':
                    while ((c = reader.read()) != -1)
                        if (c == '\r' || c == '\n')
                            break;
                    break;
                case '#':
                    if ((c = reader.read()) != -1) {
                        if (c == 't' || c == 'T')
                            return OBJ_TRUE;
                        else if (c == 'f' || c == 'F')
                            return OBJ_FALSE;
                        else if (c == '\\' && ((c = reader.read()) != -1))
                            return makeCharacter(c);
                    }
                    throw new NewtException("Invalid character string starting with #...");
                case '"':
                    sb = new StringBuffer();
                    while ((c = reader.read()) != '"')
                        sb.append((char) c);
                    return makeString(sb.toString());
                case '\'':
                    obj = readObject();
                    return newList(SYM_QUOTE, obj);
                case ',':
                    if ((c = reader.read()) == '@') {
                        obj = readObject();
                        return newList(SYM_UNQUOTE_SPLICING, obj);
                    } else {
                        if (c != -1)
                            reader.unread(c);
                        obj = readObject();
                        return newList(SYM_UNQUOTE, obj);
                    }
                case '`':
                    obj = readObject();
                    return newList(SYM_QUASIQUOTE, obj);
                case '(':
                    return readList(')');
                case '[':
                    return readList(']');
                case '{':
                    return readList('}');
                case ')':
                case ']':
                case '}':
                    reader.unread(d);
                    return CHAR_EOF;
                case '.':
                    return CHAR_DOT;
                default:
                    sb = new StringBuffer();
                    do {
                        sb.append((char) c);
                        c = reader.read();
                    } while (c != ' ' && c != '\r' && c != '\n' && c != '\t' && c != -1 && c != '(' && c != ')' &&
                            c != '[' && c != ']' && c != '{' && c != '}' && c != '.');
                    if (c != -1)
                        reader.unread(c);
                    String value = sb.toString();
                    if ("nil".equalsIgnoreCase(value))
                        return OBJ_NIL;
                    try {
                        return makeNumber(Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        return makeSymbol(value);
                    }
            }
        }
    }

    private NewtObject readList(char delim) throws IOException {

        NewtPair head = makePair(OBJ_NIL, OBJ_NIL);
        NewtPair tail = head;

        while (true) {
            NewtObject obj = readObject();
            if (obj == CHAR_EOF)
                break;
            if (obj == CHAR_DOT) {
                obj = readObject();
                tail.setSecond(obj);
                break;
            }
            tail.setSecond(makePair(obj, OBJ_NIL));
            tail = tail.getSecond().asPair();
        }

        int c = reader.read();
        if (c != delim)
            throw new NewtException("Unexpected character at end of list: " + Character.valueOf((char) c));

        return head.getSecond();
    }


}
