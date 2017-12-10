package newt.lang;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class NewtRuntime {

    public static final NewtObject OBJ_NIL = makeSingleton("NIL");
    public static final NewtObject OBJ_TRUE = makeSingleton("#T");
    public static final NewtObject OBJ_FALSE = OBJ_NIL;
    public static final NewtObject OBJ_UNDEFINED = makeSingleton("#<UNDEFINED>");
    public static final NewtObject OBJ_UNBOUND = makeSingleton("#<UNBOUND>");
    public static final NewtObject OBJ_ERROR = makeSingleton("#<ERROR>");

    public static final NewtObject CHAR_EOF = makeCharacter(-1);
    public static final NewtObject CHAR_SPACE = makeCharacter(' ');
    public static final NewtObject CHAR_SEMICOLON = makeCharacter(';');
    public static final NewtObject CHAR_TAB = makeCharacter('\t');
    public static final NewtObject CHAR_NEWLINE = makeCharacter('\n');
    public static final NewtObject CHAR_FORMFEED = makeCharacter('\r');
    public static final NewtObject CHAR_DOT = makeCharacter('.');
    public static final NewtObject CHAR_LEFT_PAREN = makeCharacter('(');
    public static final NewtObject CHAR_RIGHT_PAREN = makeCharacter(')');
    public static final NewtObject CHAR_LEFT_BRACKET = makeCharacter('[');
    public static final NewtObject CHAR_RIGHT_BRACKET = makeCharacter(']');
    public static final NewtObject CHAR_LEFT_BRACE = makeCharacter('{');
    public static final NewtObject CHAR_RIGHT_BRACE = makeCharacter('}');
    public static final NewtObject CHAR_QUOTE = makeCharacter('\'');
    public static final NewtObject CHAR_DOUBLEQUOTE = makeCharacter('\"');
    public static final NewtObject CHAR_QUASIQUOTE = makeCharacter('`');
    public static final NewtObject CHAR_BACKSLASH = makeCharacter('\\');
    public static final NewtObject CHAR_COMMA = makeCharacter(',');
    public static final NewtObject CHAR_AT_SIGN = makeCharacter('@');

    public static final NewtObject SYM_LAMBDA = makeSymbol("lambda");
    public static final NewtObject SYM_LET = makeSymbol("let");
    public static final NewtObject SYM_QUOTE = makeSymbol("quote");
    public static final NewtObject SYM_UNQUOTE = makeSymbol("unquote");
    public static final NewtObject SYM_UNQUOTE_SPLICING = makeSymbol("unquote-splicing");
    public static final NewtObject SYM_QUASIQUOTE = makeSymbol("quasiquote");
    public static final NewtObject SYM_DEFINE = makeSymbol("define");
    public static final NewtObject SYM_BEGIN = makeSymbol("begin");
    public static final NewtObject SYM_DEFMACRO = makeSymbol("defmacro");
    public static final NewtObject SYM_IF = makeSymbol("if");
    public static final NewtObject SYM_COND = makeSymbol("cond");
    public static final NewtObject SYM_SET_BANG = makeSymbol("set!");
    public static final NewtObject SYM_APPEND = makeSymbol("append");
    public static final NewtObject SYM_LIST = makeSymbol("list");

    public static final NewtObject PORT_STDIN = makeInputPort(System.in);
    public static final NewtObject PORT_STDOUT = makeOutputPort(System.out);
    public static final NewtObject PORT_STDERR = makeOutputPort(System.err);

    public static NewtPair makePair(NewtObject first, NewtObject second) {
        return new NewtPair(first, second);
    }

    public static NewtString makeString(java.lang.String value) {
        return new NewtString(value);
    }

    public static NewtNumber makeNumber(int value) {
        return new NewtNumber(value);
    }

    public static NewtSymbol makeSymbol(String value) {
        return NewtSymbol.intern(value);
    }

    public static NewtProcedure makeProcedure(NewtLambda fn, NewtObject env) {
        return new NewtProcedure(fn, env);
    }

    public static NewtSubroutine makeSubroutine(Function<NewtObject, NewtObject> fn) {
        return new NewtSubroutine(fn);
    }

    public static NewtLambda makeLambda(NewtObject formals, NewtObject body) {
        return new NewtLambda(formals, body);
    }

    public static NewtInputPort makeInputPort(InputStream inputStream) {
        return new NewtInputPort(new InputStreamReader(inputStream));
    }

    public static NewtInputPort makeInputPort(java.lang.String str) {
        return new NewtInputPort(new StringReader(str));
    }

    public static NewtInputPort makeInputPort(Path path) {
        try {
            return new NewtInputPort(Files.newBufferedReader(path));
        } catch (IOException e) {
            throw new NewtException(e);
        }
    }

    public static NewtOutputPort makeOutputPort(OutputStream outputStream) {
        Writer writer = new OutputStreamWriter(outputStream);
        return new NewtOutputPort(writer);
    }

    public static NewtOutputPort makeOutputPort(Path path) {
        try {
            return new NewtOutputPort(Files.newBufferedWriter(path));
        } catch (IOException e) {
            throw new NewtException(e);
        }
    }

    public static NewtObject makeList(NewtObject... objs) {
        NewtObject ans = OBJ_NIL;
        for (int i = objs.length - 1; i >= 0; i--)
            ans = makePair(objs[i], ans);
        return ans;
    }

    public static NewtCharacter makeCharacter(int character) {
        NewtCharacter ans = NewtCharacter.intern(character);
        return ans;
    }

    public static NewtSingleton makeSingleton(String value) {
        NewtSingleton ans = new NewtSingleton(value);
        return ans;
    }

    public static NewtObject cons(NewtObject obj1, NewtObject obj2) {
        return makePair(obj1, obj2);
    }

    public static NewtObject car(NewtObject obj) {
        return obj.asPair().getFirst();
    }

    public static NewtObject caar(NewtObject obj) {
        return car(car(obj));
    }

    public static NewtObject cdr(NewtObject obj) {
        return obj.asPair().getSecond();
    }

    public static NewtObject cddr(NewtObject obj) {
        return cdr(cdr(obj));
    }

    public static NewtObject cdddr(NewtObject obj) {
        return cdr(cddr(obj));
    }

    public static NewtObject cddddr(NewtObject obj) {
        return cdr(cdddr(obj));
    }

    public static NewtObject cadr(NewtObject obj) {
        return car(cdr(obj));
    }

    public static NewtObject caddr(NewtObject obj) {
        return car(cddr(obj));
    }

    public static NewtObject cadddr(NewtObject obj) {
        return car(cdddr(obj));
    }

    public static NewtObject cdar(NewtObject obj) {
        return cdr(car(obj));
    }

    public static NewtObject cadar(NewtObject obj) {
        return car(cdar(obj));
    }

    public static NewtObject reverse(NewtObject list) {
        NewtObject ans = OBJ_NIL;
        for (; list.isPair(); list = cdr(list))
            ans = makePair(car(list), ans);
        return ans;
    }

    public static NewtObject charP(NewtObject obj) {
        NewtObject ans = obj.isCharacter() ? OBJ_TRUE : OBJ_FALSE;
        return ans;
    }

    public static NewtObject whitespaceP(NewtObject obj) {
        int ch = obj.asCharacter().getCharacter();
        NewtObject ans = java.lang.Character.isWhitespace(ch) ? OBJ_TRUE : OBJ_FALSE;
        return ans;
    }

    public static NewtObject alphabeticP(NewtObject obj) {
        int ch = obj.asCharacter().getCharacter();
        NewtObject ans = java.lang.Character.isAlphabetic(ch) ? OBJ_TRUE : OBJ_FALSE;
        return ans;
    }

    public static NewtObject uppercaseP(NewtObject obj) {
        int ch = obj.asCharacter().getCharacter();
        NewtObject ans = java.lang.Character.isUpperCase(ch) ? OBJ_TRUE : OBJ_FALSE;
        return ans;
    }

    public static NewtObject lowercaseP(NewtObject obj) {
        int ch = obj.asCharacter().getCharacter();
        NewtObject ans = java.lang.Character.isLowerCase(ch) ? OBJ_TRUE : OBJ_FALSE;
        return ans;
    }

    public static NewtObject digitP(NewtObject obj) {
        int ch = obj.asCharacter().getCharacter();
        NewtObject ans = java.lang.Character.isDigit(ch) ? OBJ_TRUE : OBJ_FALSE;
        return ans;
    }


    public static NewtObject extend(NewtObject env, java.lang.String name, Function<NewtObject, NewtObject> fn) {
        NewtSymbol symbol = makeSymbol(name);
        NewtSubroutine subroutine = makeSubroutine(fn);
        return makePair(makePair(symbol, subroutine), env);
    }


    public static NewtObject extend(NewtObject env, java.lang.String name, NewtObject obj) {
        NewtSymbol symbol = makeSymbol(name);
        return makePair(makePair(symbol, obj), env);
    }

    public static NewtObject length(NewtObject list) {
        int len = 0;
        for (; list.isPair(); list = cdr(list))
            ++len;
        return makeNumber(len);
    }

    public static NewtObject standardOutput() {
        return PORT_STDOUT;
    }

    public static NewtObject standardError() {
        return PORT_STDERR;
    }

    public static NewtObject standardInput() {
        return PORT_STDIN;
    }

    public static NewtObject plus(NewtObject... args) {
        int val = 0;
        for (int i = 0; i < args.length; ++i)
            val += args[i].asNumber().getValue();
        return makeNumber(val);
    }

    public static NewtObject minus(NewtObject... args) {
        if (args.length == 0)
            return makeNumber(0);
        int val = args[0].asNumber().getValue();
        for (int i = 1; i < args.length; ++i)
            val -= args[i].asNumber().getValue();
        return makeNumber(val);
    }

    public static NewtObject mul(NewtObject... args) {
        int val = 1;
        for (int i = 0; i < args.length; ++i)
            val *= args[i].asNumber().getValue();
        return makeNumber(val);
    }

    public static NewtObject div(NewtObject... args) {
        throw new NewtException("Not implemented.");
    }


    public static void assertSymbol(NewtObject obj) {
        if (!obj.isSymbol())
            throw new NewtException("NewtSymbol expected: " + obj);
    }

    public static void assertList(NewtObject obj) {
        NewtObject iter = obj;
        for (; iter.isPair(); iter = cdr(iter))
            ;
        if (!iter.isNil())
            throw new NewtException("List expected: " + obj);
    }

    public static NewtCharacter readChar(NewtInputPort port) {
        NewtCharacter ans;
        if (port.getNextChar() != null) {
            NewtCharacter ch = port.getNextChar();
            port.setNextChar(null);
            port.setPrevChar(ch);
            ans = ch;
        } else {
            ans = CHAR_EOF.asCharacter();
            int c = NewtUtils.read(port.getReader());
            if (c != -1) {
                NewtCharacter ch = makeCharacter(c);
                port.setNextChar(null);
                port.setPrevChar(ch);
                ans = ch;
            }
        }
        return ans;
    }

    public static NewtObject charReady(NewtInputPort port) {
        NewtObject ans = NewtUtils.ready(port.getReader()) ? NewtRuntime.OBJ_TRUE : NewtRuntime.OBJ_FALSE;
        return ans;
    }

    public static NewtObject unreadChar(NewtObject port) {
        NewtInputPort inputPort = port.asInputPort();
        if (inputPort.getPrevChar() == null)
            throw new NewtException("Cannot unread.");
        if (inputPort.getNextChar() != null)
            throw new NewtException("Cannot unread twice");
        inputPort.setNextChar(inputPort.getPrevChar());
        inputPort.setPrevChar(null);
        return NewtRuntime.OBJ_UNDEFINED;
    }

    public static NewtObject peekChar(NewtObject port) {
        NewtInputPort inputPort = port.asInputPort();
        NewtObject ans = readChar(inputPort);
        unreadChar(port);
        return ans;
    }

    public static NewtObject readLine(NewtObject port) {
        java.lang.String line = NewtUtils.readLine(port.asInputPort().getReader());
        NewtObject ans = makeString(line);
        return ans;
    }

    public static NewtObject write(NewtObject port, NewtObject obj) {
        NewtOutputPort outputPort = port.asOutputPort();
        java.lang.String str = obj.toString();
        outputPort.write(str);
        return OBJ_UNDEFINED;
    }

    public static NewtObject writeString(NewtObject port, NewtObject obj) {
        NewtOutputPort outputPort = port.asOutputPort();
        java.lang.String value = obj.asString().getValue();
        outputPort.write(value);
        return OBJ_UNDEFINED;
    }

    public static NewtObject writeLine(NewtObject port, NewtObject obj) {
        NewtOutputPort outputPort = port.asOutputPort();
        java.lang.String value = obj.asString().getValue();
        outputPort.write(value);
        outputPort.write("\n");
        return OBJ_UNDEFINED;
    }


    public static NewtObject close(NewtObject port) {
        port.asPort().close();
        return NewtRuntime.OBJ_UNDEFINED;
    }

    public static NewtObject read(NewtObject port) {
        return readObject(port);
    }

    public static NewtObject open(NewtObject filename1, NewtObject mode1) {
        try {
            java.lang.String filename = filename1.asString().getValue();
            java.lang.String mode = mode1.asString().getValue();
            NewtPort port = null;
            switch (mode.toUpperCase()) {
                case "IN":
                    port = makeInputPort(Paths.get(filename));
                    break;
                case "OUT":
                    // Writer writer1 = Files.newBufferedWriter(Paths.get(filename), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    port = makeOutputPort(Paths.get(filename));
                    break;
                case "APPEND":
                    //Writer writer2 = Files.newBufferedWriter(Paths.get(filename), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    port = makeOutputPort(Paths.get(filename));
                    break;
                default:
                    throw new NewtException("Invalid open mode: " + mode.toUpperCase());
            }
            return port;
        } catch (Exception e) {
            throw new NewtException("Could not open port.", e);
        }
    }


    public static NewtObject append(NewtObject... args) {
        if (args.length == 0)
            return OBJ_NIL;
        // append lists to each other
        NewtObject ans = makePair(OBJ_NIL, OBJ_NIL);
        NewtObject end = ans;
        for (int i = 0; i < args.length - 1; ++i) {
//        for (; argl.isPair() && !argl.asPair().getSecond().isNil(); argl = cdr(argl)) {
            if (!args[i].isList())
                throw new NewtException("Only lists may be appended.");
            NewtObject iter;
            for (iter = args[i]; iter.isPair(); iter = cdr(iter)) {
                end.asPair().setSecond(makePair(car(iter), OBJ_NIL));
                end = cdr(end);
            }
        }
//        if (argl.isPair())
        end.asPair().setSecond(args[args.length - 1]);
        ans = cdr(ans);
        return ans;
    }


    public static NewtObject readObject(NewtObject port) {
        NewtObject obj = null;
        StringBuffer sb = null;
        NewtInputPort inputPort = port.asInputPort();

        while (true) {
            NewtCharacter c = readChar(inputPort);
            if (c == CHAR_EOF)
                return CHAR_EOF;
            else if (c == CHAR_SPACE ||
                    c == CHAR_TAB ||
                    c == CHAR_NEWLINE ||
                    c == CHAR_FORMFEED) {
                // continue
            } else if (c == CHAR_SEMICOLON) {
                while ((c = readChar(inputPort)) != CHAR_EOF)
                    if (c == CHAR_FORMFEED || c == CHAR_NEWLINE)
                        break;
            } else if (c == CHAR_DOUBLEQUOTE) {
                sb = new StringBuffer();
                while ((c = readChar(inputPort)) != CHAR_DOUBLEQUOTE)
                    sb.append(c.getCharacter());
                return makeString(sb.toString());
            } else if (c == CHAR_QUOTE) {
                obj = readObject(port);
                return newList(SYM_QUOTE, obj);
            } else if (c == CHAR_COMMA) {
                if ((c = readChar(inputPort)) == CHAR_AT_SIGN) {
                    obj = readObject(port);
                    return newList(SYM_UNQUOTE_SPLICING, obj);
                } else {
                    if (c != CHAR_EOF)
                        unreadChar(port);
                    obj = readObject(port);
                    return newList(SYM_UNQUOTE, obj);
                }
            } else if (c == CHAR_QUASIQUOTE) {
                obj = readObject(port);
                return newList(SYM_QUASIQUOTE, obj);
            } else if (c == CHAR_LEFT_PAREN) {
                return readList(inputPort);
            } else if (c == CHAR_RIGHT_PAREN) {
                unreadChar(port);
                return CHAR_EOF;
            } else if (c == CHAR_DOT) {
                return CHAR_DOT;
            } else {
                sb = new StringBuffer();
                do {
                    sb.append((char) (c.getCharacter()));
                    c = readChar(inputPort);
                } while (c != CHAR_SPACE && c != CHAR_FORMFEED && c != CHAR_NEWLINE &&
                        c != CHAR_TAB && c != CHAR_EOF && c != CHAR_LEFT_PAREN && c != CHAR_RIGHT_PAREN &&
                        c != CHAR_DOT);
                if (c != CHAR_EOF)
                    unreadChar(port);
                String value = sb.toString();
                if ("NIL".equalsIgnoreCase(value))
                    return OBJ_NIL;
                else if ("#T".equalsIgnoreCase(value))
                    return OBJ_TRUE;
                else if ("#F".equalsIgnoreCase(value))
                    return OBJ_FALSE;
                else if ("#\\LEFT-PAREN".equalsIgnoreCase(value))
                    return CHAR_LEFT_PAREN;
                else if ("#\\RIGHT-PAREN".equalsIgnoreCase(value))
                    return CHAR_RIGHT_PAREN;
                else if ("#\\LEFT-BRACKET".equalsIgnoreCase(value))
                    return CHAR_LEFT_BRACKET;
                else if ("#\\RIGHT-BRACKET".equalsIgnoreCase(value))
                    return CHAR_RIGHT_BRACKET;
                else if ("#\\LEFT-BRACE".equalsIgnoreCase(value))
                    return CHAR_LEFT_BRACE;
                else if ("#\\RIGHT-BRACE".equalsIgnoreCase(value))
                    return CHAR_RIGHT_BRACE;
                else if ("#\\BACKSLASH".equalsIgnoreCase(value))
                    return CHAR_BACKSLASH;
                else if ("#\\QUOTE".equalsIgnoreCase(value))
                    return CHAR_QUOTE;
                else if ("#\\QUASIQUOTE".equalsIgnoreCase(value))
                    return CHAR_QUASIQUOTE;
                else if ("#\\DOUBLEQUOTE".equalsIgnoreCase(value))
                    return CHAR_DOUBLEQUOTE;
                else if ("#\\COMMA".equalsIgnoreCase(value))
                    return CHAR_COMMA;
                else if ("#\\NEWLINE".equalsIgnoreCase(value))
                    return CHAR_NEWLINE;
                else if ("#\\TAB".equalsIgnoreCase(value))
                    return CHAR_TAB;
                else if ("#\\SPACE".equalsIgnoreCase(value))
                    return CHAR_SPACE;
                else if ("#\\FORM".equalsIgnoreCase(value))
                    return CHAR_FORMFEED;
                else if (value.startsWith("#\\")) {
                    char c1 = value.charAt(2);
                    return NewtCharacter.intern(c1);
                }

                try {
                    return makeNumber(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    return makeSymbol(value);
                }
            }
        }
    }

    private static NewtObject readList(NewtInputPort port) {

        NewtPair head = makePair(OBJ_NIL, OBJ_NIL);
        NewtPair tail = head;

        while (true) {
            NewtObject obj = readObject(port);
            if (obj == CHAR_EOF)
                break;
            if (obj == CHAR_DOT) {
                obj = readObject(port);
                tail.setSecond(obj);
                break;
            }
            tail.setSecond(makePair(obj, OBJ_NIL));
            tail = tail.getSecond().asPair();
        }

        NewtObject ch = readChar(port);
        if (ch != CHAR_RIGHT_PAREN)
            throw new NewtException("Unexpected character at end of list: " + ch);

        return head.getSecond();
    }

    public static NewtObject newList(NewtObject... objs) {
        NewtObject ans = OBJ_NIL;
        for (int i = objs.length - 1; i >= 0; --i)
            ans = makePair(objs[i], ans);
        return ans;
    }

    public static NewtObject list(NewtObject... objs) {
        NewtObject ans = OBJ_NIL;
        for (int i = objs.length - 1; i >= 0; --i)
            ans = makePair(objs[i], ans);
        return ans;
    }

    public static NewtObject numberP(NewtObject obj) {
        return obj instanceof NewtNumber ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject stringP(NewtObject obj) {
        return obj instanceof NewtString ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject lambdaP(NewtObject obj) {
        return obj instanceof NewtLambda ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject procedureP(NewtObject obj) {
        return obj instanceof NewtProcedure ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject subroutineP(NewtObject obj) {
        return obj instanceof NewtSubroutine ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject symbolP(NewtObject obj) {
        return obj instanceof NewtSymbol ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject pairP(NewtObject obj) {
        return obj instanceof NewtPair ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject lt(NewtObject obj1, NewtObject obj2) {
        return obj1.asNumber().getValue() < obj2.asNumber().getValue() ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject gt(NewtObject obj1, NewtObject obj2) {
        return obj1.asNumber().getValue() > obj2.asNumber().getValue() ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject le(NewtObject obj1, NewtObject obj2) {
        return obj1.asNumber().getValue() <= obj2.asNumber().getValue() ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject ge(NewtObject obj1, NewtObject obj2) {
        return obj1.asNumber().getValue() >= obj2.asNumber().getValue() ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject eq(NewtObject obj1, NewtObject obj2) {
        return obj1.asNumber().getValue() == obj2.asNumber().getValue() ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject ne(NewtObject obj1, NewtObject obj2) {
        return obj1.asNumber().getValue() != obj2.asNumber().getValue() ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject charEq(NewtObject obj1, NewtObject obj2) {
        return obj1.asCharacter().getCharacter() == obj2.asCharacter().getCharacter() ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject charLt(NewtObject obj1, NewtObject obj2) {
        return obj1.asCharacter().getCharacter() < obj2.asCharacter().getCharacter() ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject nullP(NewtObject obj) {
        return obj == OBJ_NIL ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject atomP(NewtObject obj) {
        return not(pairP(obj));
    }

    public static NewtObject listP(NewtObject obj) {
        return or(nullP(obj), pairP(obj));
    }

    public static NewtObject properListP(NewtObject obj) {
        for (; obj.isPair(); obj = cdr(obj))
            ;
        NewtObject ans = obj == OBJ_NIL ? OBJ_TRUE : OBJ_FALSE;
        return ans;
    }

    public static NewtObject stringEqualP(NewtObject obj1, NewtObject obj2) {
        String val1 = obj1.asString().getValue();
        String val2 = obj2.asString().getValue();
        NewtObject ans = val1.equals(val2) ? OBJ_TRUE : OBJ_FALSE;
        return ans;
    }

    public static NewtObject stringAppend(NewtObject... strings) {
        String tmp = "";
        for (int i = 0; i < strings.length; ++i) {
            String val = strings[i].asString().getValue();
            tmp += val;
        }
        NewtObject ans = makeString(tmp);
        return ans;
    }

    public static NewtObject copyString(NewtObject string) {
        NewtObject ans = makeString(string.asString().getValue());
        return ans;
    }

    public static NewtObject charToString(NewtObject chr) {
        String tmp = "" + chr.asCharacter().getCharacter();
        NewtObject ans = makeString(tmp);
        return ans;
    }

    public static NewtObject stringToList(NewtObject string) {
        NewtObject ans = OBJ_NIL;
        String val = string.asString().getValue();
        for (int i = val.length() - 1; i >= 0; --i)
        {
            char c = val.charAt(i);
            ans = makePair(makeCharacter(c), ans);
        }
        return ans;
    }

    public static NewtObject listToString(NewtObject list) {
        String value = "";
        for (; list.isPair(); list = cdr(list))
            value += car(list).asCharacter().getCharacter();
        NewtObject ans = makeString(value);
        return ans;
    }

    public static NewtObject eqP(NewtObject obj1, NewtObject obj2) {
        NewtObject ans = obj1 == obj2 ? OBJ_TRUE : OBJ_FALSE;
        return ans;
    }


    public static NewtObject not(NewtObject obj) {
        return obj == OBJ_NIL ? OBJ_TRUE : OBJ_FALSE;
    }

    public static NewtObject or(NewtObject... obj) {
        for (int i = 0; i < obj.length; ++i)
            if (obj[i] != OBJ_FALSE)
                return OBJ_TRUE;
        return OBJ_FALSE;
    }

    public static NewtObject setCar(NewtObject obj, NewtObject val) {
        obj.asPair().setFirst(val);
        return obj;
    }

    public static NewtObject setCdr(NewtObject obj, NewtObject val) {
        obj.asPair().setSecond(val);
        return obj;
    }
//    public static NewtObject nth(NewtObject list, NewtObject index) {
//        int idx = index.asNumber().getValue();
//        NewtObject ans = list;
//        for ( ; idx > 0 && ans.isPair(); idx--, ans = cdr(ans))
//            ;
//        if (!ans.isPair())
//            throw new NewtException("Index out of bounds: " + index);
//        ans = car(ans);
//        return ans;
//    }
//
//    public static NewtObject nthcdr(NewtObject list, NewtObject index) {
//        int idx = index.asNumber().getValue();
//        NewtObject ans = list;
//        for ( ; idx > 0 && ans.isPair(); idx--, ans = cdr(ans))
//            ;
//        if (!ans.isPair())
//            throw new NewtException("Index out of bounds: " + index);
//        ans = car(ans);
//        return ans;
//    }
}
