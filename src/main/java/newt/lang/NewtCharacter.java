package newt.lang;


public class NewtCharacter extends NewtObject {

    private char character;

    private static NewtCharacter chars[] = new NewtCharacter[256];
    private static final NewtCharacter eof = new NewtCharacter(-1);

    static {
        for (int i = 0; i < 256; ++i)
            chars[i] = new NewtCharacter(i);
    }

    public static NewtCharacter intern(int ch) {
        if (ch < 0)
            return eof;
        return chars[ch];
//        if (0 <= character && character < 256)
//            return chars[ch + 1];
//        NewtCharacter c = characterMap.get(character);
//        if (c == null) {
//            c = new NewtCharacter(character);
//            characterMap.put(character, c);
//        }
//        return c;
    }

    private NewtCharacter(int character) {
        this.character = (char) character;
    }

    public final String toString() {
        return "#\\" + character;
    }

    public char getCharacter() {
        return character;
    }
}
