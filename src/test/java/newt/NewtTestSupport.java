package newt;

import newt.lang.NewtEvaluator;
import newt.lang.NewtObject;
import newt.lang.NewtRuntime;

import static newt.lang.NewtRuntime.*;

public abstract class NewtTestSupport {
    public static class Lisp {
        private NewtObject ans;
        NewtEvaluator evaluator = new NewtEvaluator();

        public Lisp eval(String expression) {
            NewtObject port = makeInputPort(expression);
            NewtObject expr;
            ans = OBJ_NIL;
            try {
                while ((expr = NewtRuntime.read(port)) != CHAR_EOF)
                    ans = evaluator.eval(expr);
            } catch (Exception e) {
                ans = OBJ_ERROR;
            }
            return this;
        }

        public Lisp yieldsError() {
            if (ans != OBJ_ERROR)
                throw new RuntimeException("Expected #<ERROR>, got " + ans);
            return this;
        }

        public Lisp yieldsUnbound() {
            if (ans != OBJ_UNBOUND)
                throw new RuntimeException("Expected #<UNBOUND>, got " + ans);
            return this;
        }

        public Lisp yieldsProcedure() {
            if (!ans.isProcedure())
                throw new RuntimeException("Expected #<PROCEDURE>, got " + ans);
            return this;
        }

        public Lisp yieldsUndefined() {
            if (ans != OBJ_UNDEFINED)
                throw new RuntimeException("Expected #<UNDEFINED>, got " + ans);
            return this;
        }

        public Lisp yieldsNil() {
            if (ans != OBJ_NIL)
                throw new RuntimeException("Expected NIL, got " + ans);
            return this;
        }

        public Lisp yieldsTrue() {
            if (ans != OBJ_TRUE)
                throw new RuntimeException("Expected #T, got " + ans);
            return this;
        }

        public Lisp yieldsFalse() {
            if (ans != OBJ_FALSE)
                throw new RuntimeException("Expected #F, got " + ans);
            return this;
        }

        public Lisp yields(String expected) {
            if (!ans.toString().equalsIgnoreCase(expected))
                throw new RuntimeException("Not equal: " + ans.toString() + " <-> " + expected);
            return this;
        }

        public Lisp yields(int expected) {
            if (!ans.isNumber())
                throw new RuntimeException("Expected a number, got " + ans);

            if (!ans.toString().equalsIgnoreCase("" + expected))
                throw new RuntimeException("Expected " + expected + ", got " + ans);
            return this;
        }

        public Lisp yields(NewtObject expected) {
            if (ans != expected)
                throw new RuntimeException("Not identical: " + ans.toString() + " <-> " + expected.toString());
            return this;
        }
    }

    public Lisp lisp() {
        return new Lisp();
    }

    public Lisp eval(String expr) {
        Lisp l = lisp();
        return l.eval(expr);
    }
}
