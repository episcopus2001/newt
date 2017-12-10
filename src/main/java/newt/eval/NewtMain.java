package newt;

import newt.lang.NewtEvaluator;
import newt.lang.NewtInputPort;
import newt.lang.NewtObject;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static newt.lang.NewtRuntime.*;

public class NewtMain {

    public static void main(String... args) throws IOException {

        if (args.length == 0)
        {
            System.err.println("Usage:");
            System.err.println("newt <filename> <arguments passed on>");
            System.exit(0);
        }

        String filename = args[0];
        Path path = Paths.get(filename);
        String[] args2 = Arrays.copyOfRange(args, 1, args.length);

        NewtInputPort inputPort = makeInputPort(path);
        NewtEvaluator evaluator = new NewtEvaluator(args2);
        NewtObject obj1, obj2 = null;
        while ((obj1 = readObject(inputPort)) != CHAR_EOF) {
            obj2 = evaluator.eval(obj1);
        }
        System.out.println(obj2);
    }

}
