package newt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NewtTests extends NewtTestSupport {

    @Test
    @DisplayName("Test numbers")
    public void testNumbers() {
        eval("3").yields("3");
        eval("+3").yields("3");
        eval("+0").yields("0");
        eval("0").yields("0");
        eval("-0").yields("0");
        eval("-3").yields("-3");
    }

    @Test
    @DisplayName("Test strings")
    public void testStrings() {
        eval("\"Hello, World!\"").yields("\"Hello, World!\"");
        eval("\"\"").yields("\"\"");
        eval("(string? \"hello\")").yieldsTrue();
        eval("(string? 1)").yieldsFalse();
    }

    @Test
    @DisplayName("Test booleans")
    public void testBooleans() {
        eval("#t").yieldsTrue();
        eval("#f").yieldsNil();
        eval("nil").yieldsNil();
//        test("#f", "#f");
    }

    @Test
    @DisplayName("Test arithmetic")
    public void testArithmetic() {
        eval("(+)").yields("0");
        eval("(+ 1)").yields("1");
        eval("(+ 1 -1)").yields("0");
        eval("(+ -1 1)").yields("0");
        eval("(+ 1)").yields("1");
        eval("(+ 1 1)").yields("2");
        eval("(+ 1 1 1)").yields("3");
        eval("(+ -1)").yields("-1");
        eval("(+ -1 -1)").yields("-2");
        eval("(+ -1 -1 -1)").yields("-3");

        eval("(-)").yields("0");
        eval("(- 1)").yields("1");
        eval("(- 1 1)").yields("0");
        eval("(- -1 -1)").yields("0");
        eval("(- 1)").yields("1");
        eval("(- 1 2)").yields("-1");
        eval("(- 2 1)").yields("1");
        eval("(- 2 1 1)").yields("0");
        eval("(- 2 1 1 1 1)").yields("-2");

        eval("(*)").yields("1");
        eval("(* 3)").yields("3");
        eval("(* -3)").yields("-3");
        eval("(* 1 2 3)").yields("6");
        eval("(* 0 1 2 3)").yields("0");
        eval("(* -1 2 3)").yields("-6");
        eval("(* -1 2 -3)").yields("6");
    }

    @Test
    @DisplayName("Test comparisons")
    public void testComparisons() {
        eval("(> 0 0)").yieldsFalse();
        eval("(> 1 0)").yieldsTrue();
        eval("(> 1 -1)").yieldsTrue();
        eval("(> 0 1)").yieldsFalse();
        eval("(> -1 1)").yieldsFalse();

        eval("(< 0 0)").yieldsFalse();
        eval("(< 1 0)").yieldsFalse();
        eval("(< 1 -1)").yieldsFalse();
        eval("(< 0 1)").yieldsTrue();
        eval("(< -1 1)").yieldsTrue();

        eval("(>= 0 0)").yieldsTrue();
        eval("(>= 1 0)").yieldsTrue();
        eval("(>= 1 -1)").yieldsTrue();
        eval("(>= 0 1)").yieldsFalse();
        eval("(>= -1 -1)").yieldsTrue();

        eval("(<= 0 0)").yieldsTrue();
        eval("(<= 1 0)").yieldsFalse();
        eval("(<= 1 -1)").yieldsFalse();
        eval("(<= 0 1)").yieldsTrue();
        eval("(<= -1 -1)").yieldsTrue();

        eval("(= 0 0)").yieldsTrue();
        eval("(= 1 0)").yieldsFalse();
    }

    @Test
    @DisplayName("Test if")
    public void testIf() {
        eval("(if #t 1 2)").yields(1);
        eval("(if nil 1 2)").yields(2);
        eval("(if 1 1 2)").yields(1);
    }

    @Test
    @DisplayName("Test define symbol")
    public void testDefineSymbol() {
        eval("(define foo 1)").yieldsUndefined();
        eval("(define foo 1) (define bar 2)").yieldsUndefined();
        eval("(define foo 42) foo").yields(42);
        eval("(define foo \"Hello\") foo").yields("\"Hello\"");
        eval("(define foo 42) (define bar foo) (define foo 78) bar").yields(42);
        eval("(define foo 42) (define bar foo) (define foo 78) foo").yields(78);

        eval("(define foo)").yieldsUndefined();
        eval("(define foo) foo").yieldsUnbound();
    }

    @Test
    @DisplayName("Test set")
    public void testSet() {
        eval("(define foo 1) (set! foo 2) foo").yields(2);
    }

    @Test
    @DisplayName("Test define function")
    public void testDefineFunction() {
        eval("(define (foo) 1)").yieldsUndefined();
        eval("(define (foo) 1) foo").yieldsProcedure();
        eval("(define (foo) 1) (foo)").yields(1);
        eval("(define (foo x) 1) (foo 2)").yields(1);
        eval("(define (foo x) x) (foo 2)").yields(2);
    }

    @Test
    @DisplayName("Test basic lambda")
    public void testBasicLambda() {
        eval("(lambda () 0)").yieldsProcedure();
        eval("(lambda (x) 0)").yieldsProcedure();
        eval("(lambda (x y) x)").yieldsProcedure();

        eval("((lambda () 0))").yields(0);
        eval("((lambda (x) 0) 2)").yields(0);
        eval("((lambda (x) x) 2)").yields(2);
    }

    @Test
    @DisplayName("Test nested lambda")
    public void testNestedLambda() {
        String defFooX = "(define foo (lambda (x) (lambda (y) (lambda (z) x))))";
        String defFooY = "(define foo (lambda (x) (lambda (y) (lambda (z) y))))";
        String defFooZ = "(define foo (lambda (x) (lambda (y) (lambda (z) z))))";
        eval(defFooX).yieldsUndefined();
        eval(defFooX + "(foo 1)").yieldsProcedure();
        eval(defFooX + "((foo 1) 2)").yieldsProcedure();
        eval(defFooX + "(((foo 1) 2) 3)").yields(1);
        eval(defFooY + "(((foo 1) 2) 3)").yields(2);
        eval(defFooZ + "(((foo 1) 2) 3)").yields(3);

        // test("((lambda (x) 0) 1 2 3)", "0");
        // test("(lambda (x y) x)", "#<procedure>");
        // test("(lambda (x y) z)", "#<procedure>"); => unbound variable
    }

    @Test
    @DisplayName("Test recursion")
    public void testRecursion() {
        String expr
                = "(define (fact n)"
                + "   (if (> n 0)"
                + "      (* n (fact (- n 1)))"
                + "      1))";
        eval(expr + "(fact 4)").yields("24");
    }

    @Test
    @DisplayName("Test characters")
    public void testCharacters() {
        eval("#\\a").yields("#\\a");
        eval("(eq? #\\a #\\a)").yieldsTrue();
        eval("(char? #\\a)").yieldsTrue();
        eval("(char? 2)").yieldsFalse();

        eval("(whitespace? #\\a)").yieldsFalse();
        eval("(alphabetic? #\\a)").yieldsTrue();
        eval("(alphabetic? #\\1)").yieldsFalse();
        eval("(uppercase? #\\a)").yieldsFalse();
        eval("(uppercase? #\\A)").yieldsTrue();
        eval("(lowercase? #\\a)").yieldsTrue();
        eval("(lowercase? #\\A)").yieldsFalse();
        eval("(digit? #\\a)").yieldsFalse();
        eval("(digit? #\\1)").yieldsTrue();
    }

    @Test
    @DisplayName("Test quotes")
    public void testQuotes() {
        eval("(quote 1)").yields(1);
        eval("(quote symbol)").yields("symbol");
        eval("(quote (1 2 3))").yields("(1 2 3)");
        eval("'1").yields(1);
        eval("'symbol").yields("symbol");
        eval("'(1 2 3)").yields("(1 2 3)");
        eval("(quasiquote 1)").yields(1);
        eval("(quasiquote symbol)").yields("symbol");
        eval("(quasiquote (1 2 3))").yields("(1 2 3)");
        eval("`1").yields("1");
        eval("`symbol").yields("symbol");
        eval("`(1 2 3)").yields("(1 2 3)");
        eval("(+ 1 2 3)").yields("6");
        eval("'(+ 1 2 3)").yields("(+ 1 2 3)");
        eval("`(+ 1 2 3)").yields("(+ 1 2 3)");
        eval("`,(+ 1 2 3)").yields("6");
        eval("`(+ 1 (* 3 4))").yields("(+ 1 (* 3 4))");
    }

    @Test
    @DisplayName("Test lists")
    public void testLists() {
        eval("(list)").yieldsNil();
        eval("(list 1)").yields("(1)");
        eval("(list 1 2)").yields("(1 2)");
        eval("(list (list 1))").yields("((1))");
        eval("(append 1)").yields("1");
        eval("(append nil)").yieldsNil();
        eval("(append nil nil)").yieldsNil();
        eval("(append 1 nil)").yieldsError();
        eval("(append (list 1))").yields("(1)");
        eval("(append (list 1 2) (list 3))").yields("(1 2 3)");
        eval("(append (list 1 2) (list 3) nil)").yields("(1 2 3)");
        eval("(append nil (list 1 2) nil (list 3) nil)").yields("(1 2 3)");
        eval("(append '(a b c) '(d e f) '() '(g))").yields("(A B C D E F G)");
        eval("(append '(a b c) 'd)").yields("(A B C.D)");
        eval("(append)").yieldsNil();

//        testLater("(setq lst '(a b c))", "(A B C)");
//        testLater("(append lst '(d))", "(A B C D)");
//        testLater("lst", "(A B C)");

//        test("`(+ 1 2)", "(+ 1 2)");
//        test("`(+ 1 ,2)", "(+ 1 2)");
//        test("`(,+ 1 2)", "(#<subroutine> 1 2)");
//        test("`(+ 1 ,(* 3 4))", "(+ 1 12)");
//        test("(quasiquote (0 1 2))", "(0 1 2)");
//        test("(quasiquote (0 (unquote (+ 1 2)) 4))", "(0 3 4)");
//        test("`(0 1 2)", "(0 1 2)");
//        test("`(1 ,(+ 1 2) 4)", "(1 3 4)");
//        test("(quasiquote (unquote-splicing 1))", "ERROR");
//        test("(quasiquote ((unquote-splicing 1)))", "ERROR");
//        test("(quasiquote (0 (unquote-splicing 1) 4))", "ERROR");
//        test("(quasiquote (0 (unquote-splicing (list 1 2)) 4)))", "(0 1 2 4)");
//        test("`(1 ,@(list 2 3) 4)", "(1 2 3 4)");
//        test("`(1 ```,,@,,@(list (+ 1 2)) 4)", "ERROR");
//        test("`(1 `,(+ 1 ,(+ 2 3)) 4)", "(1 (+ 1 5) 4)");
//        test("`(0 ,@1)", "(0.1)");
    }

    @Test
    @DisplayName("9.1 List predicates")
    public void testListPredicates() {
        eval("(null? nil)").yieldsTrue();
        eval("(null? 4711)").yieldsFalse();

        eval("(atom? nil)").yieldsTrue();
        eval("(atom? 1)").yieldsTrue();
        eval("(atom? (cons 1 2))").yieldsFalse();

        eval("(list? 1)").yieldsFalse();
        eval("(list? nil)").yieldsTrue();
        eval("(list? (list 1 2 3))").yieldsTrue();
        eval("(list? '(1 2 3))").yieldsTrue();

        eval("(proper-list? 1)").yieldsFalse();
        eval("(proper-list? nil)").yieldsTrue();
        eval("(proper-list? (cons 1 2))").yieldsFalse();
        eval("(proper-list? (list 1 2 3))").yieldsTrue();
        eval("(proper-list? '(1 2 3))").yieldsTrue();
    }

    @Test
    @DisplayName("9.2 List constructors")
    public void testListConstructors() {
        eval("(cons 1 2)").yields("(1.2)");
        eval("(cons 'a 'nil)").yields("(A)");
        eval("(cons 'a '(b))").yields("(A B)");
        eval("(cons 'a (cons 'b 'nil))").yields("(A B)");
        eval("(cons 'a (list 'b 'c))").yields("(A B C)");

        eval("(list)").yieldsNil();
        eval("(list 'a)").yields("(A)");
        eval("(list 'a 'b)").yields("(A B)");
        eval("(list 'a (list 'b 'c))").yields("(A (B C))");

        // TODO! cons*, copy-list
    }

    @Test
    @DisplayName("Test macros")
    public void testMacros() {
        lisp()
                .eval("(defmacro (when cond cons) `(if ,cond ,cons nil))")
                .eval("(when nil 1)")
                .yieldsNil()
                .eval("(when 1 1)")
                .yields(1);
    }

    @Test
    @DisplayName("9.3 List access")
    public void testListAccess() {
        eval("(car nil)").yieldsError();
        eval("(car '(a))").yields("A");
        eval("(car '(a b))").yields("A");

        eval("(cdr nil)").yieldsError();
        eval("(cdr '(a))").yieldsNil();
        eval("(cdr '(a b))").yields("(B)");

        eval("(nth '(a b) 0)").yields("A");
        eval("(nth '(a b) 1)").yields("B");
        eval("(nth '(a b) 2)").yieldsError();
        eval("(nth nil 0)").yieldsError();
        eval("(nth nil 1)").yieldsError();

        eval("(nthcdr '(a b) 0)").yields("(A B)");
        eval("(nthcdr '(a b) 1)").yields("(B)");
        eval("(nthcdr '(a b) 2)").yields("NIL");
        eval("(nthcdr '(a b) 3)").yieldsError();
        eval("(nthcdr nil 0)").yieldsNil();
        eval("(nthcdr nil 1)").yieldsError();

        eval("(last '(a))").yields("A");
        eval("(last '(a b c))").yields("C");

        eval("(lastcdr '(a b c))").yields("(C)");
        eval("(lastcdr '(a b . c))").yields("(B.C)");
    }

    @Test
    @DisplayName("9.4 Lists as sequences")
    public void testListsAsSequences() {
        eval("(length nil)").yields(0);
        eval("(length '(a))").yields(1);
        eval("(length '(a b c))").yields(3);

        eval("(append 1)").yields("1");
        eval("(append nil)").yieldsNil();
        eval("(append nil nil)").yieldsNil();
        eval("(append 1 nil)").yieldsError();
        eval("(append (list 1))").yields("(1)");
        eval("(append (list 1 2) (list 3))").yields("(1 2 3)");
        eval("(append (list 1 2) (list 3) nil)").yields("(1 2 3)");
        eval("(append nil (list 1 2) nil (list 3) nil)").yields("(1 2 3)");
        eval("(append '(a b c) '(d e f) '() '(g))").yields("(A B C D E F G)");
        eval("(append '(a b c) 'd)").yields("(A B C.D)");
        eval("(append)").yieldsNil();

        eval("(reverse nil)").yieldsNil();
        eval("(reverse '(a))").yields("(A)");
        eval("(reverse '(a b c))").yields("(C B A)");

        eval("(sublist '(a b c d e f) 2 3)").yields("(c d e)");

    }

    @Test
    @DisplayName("Test lists as stacks")
    public void testListsAsStacks() {

        lisp()
                .eval("(define l '(34 55))")
                .eval("l")
                .yields("(34 55)")
                .eval("(push l 21)")
                .eval("l")
                .yields("(21 34 55)")
                .eval("(push l '(a b c))")
                .eval("l")
                .yields("((A B C) 21 34 55)");
        // .eval("(push (car l) 'd)")
        // .yields("((D A B C) 21 34 55)");

        lisp()
                .eval("(define l '(21 34 55))")
                .eval("l")
                .yields("(21 34 55)")
                .eval("(pop l)")
                .yields(21)
                .eval("l").yields("(34 55)");

    }

    @Test
    @DisplayName("12.1 Character and string predicates")
    public void testCharacterAndStringPredicates() {

        eval("(char? #\\X)").yieldsTrue();
        eval("(char? 0)").yieldsFalse();

        eval("(string? \"Tapir.\")").yieldsTrue();
        eval("(string? 0)").yieldsFalse();

//        eval("(graphic? #\\X)").yieldsTrue();
//        eval("(graphic? #\\newline)").yieldsFalse();

        eval("(whitespace? #\\X)").yieldsFalse();
        eval("(whitespace? #\\newline)").yieldsTrue();

        eval("(alphabetic? #\\y)").yieldsTrue();
        eval("(alphabetic? #\\7)").yieldsFalse();

        eval("(uppercase? #\\y)").yieldsFalse();
        eval("(uppercase? #\\Y)").yieldsTrue();
        eval("(uppercase? #\\comma)").yieldsFalse();

        eval("(lowercase? #\\y)").yieldsTrue();
        eval("(lowercase? #\\Y)").yieldsFalse();
        eval("(lowercase? #\\comma)").yieldsFalse();

        // TODO: Should be with a given radix
        eval("(digit? #\\5)").yieldsTrue();
        eval("(digit? #\\a)").yieldsFalse();
    }

    @Test
    @DisplayName("12.2 Character and string comparisons")
    public void testCharacterAndStringComparisons() {

        eval("(char= #\\a #\\a)").yieldsTrue();
        eval("(char= #\\a #\\b)").yieldsFalse();

        eval("(char< #\\a #\\a)").yieldsFalse();
        eval("(char< #\\a #\\b)").yieldsTrue();
        eval("(char< #\\b #\\a)").yieldsFalse();
        eval("(char< #\\A #\\A)").yieldsFalse();
        eval("(char< #\\A #\\B)").yieldsTrue();
        eval("(char< #\\B #\\A)").yieldsFalse();
        eval("(char< #\\1 #\\1)").yieldsFalse();
        eval("(char< #\\1 #\\2)").yieldsTrue();
        eval("(char< #\\2 #\\1)").yieldsFalse();

        eval("(char> #\\a #\\a)").yieldsFalse();
        eval("(char> #\\a #\\b)").yieldsFalse();
        eval("(char> #\\b #\\a)").yieldsTrue();
        eval("(char> #\\A #\\A)").yieldsFalse();
        eval("(char> #\\A #\\B)").yieldsFalse();
        eval("(char> #\\B #\\A)").yieldsTrue();
        eval("(char> #\\1 #\\1)").yieldsFalse();
        eval("(char> #\\1 #\\2)").yieldsFalse();
        eval("(char> #\\2 #\\1)").yieldsTrue();

        eval("(char<= #\\a #\\a)").yieldsTrue();
        eval("(char<= #\\a #\\b)").yieldsTrue();
        eval("(char<= #\\b #\\a)").yieldsFalse();
        eval("(char<= #\\A #\\A)").yieldsTrue();
        eval("(char<= #\\A #\\B)").yieldsTrue();
        eval("(char<= #\\B #\\A)").yieldsFalse();
        eval("(char<= #\\1 #\\1)").yieldsTrue();
        eval("(char<= #\\1 #\\2)").yieldsTrue();
        eval("(char<= #\\2 #\\1)").yieldsFalse();

        eval("(char>= #\\a #\\a)").yieldsTrue();
        eval("(char>= #\\a #\\b)").yieldsFalse();
        eval("(char>= #\\b #\\a)").yieldsTrue();
        eval("(char>= #\\A #\\A)").yieldsTrue();
        eval("(char>= #\\A #\\B)").yieldsFalse();
        eval("(char>= #\\B #\\A)").yieldsTrue();
        eval("(char>= #\\1 #\\1)").yieldsTrue();
        eval("(char>= #\\1 #\\2)").yieldsFalse();
        eval("(char>= #\\2 #\\1)").yieldsTrue();

        // TODO: CHARN=

        eval("(string-equal? \"hello\" \"hello\")").yieldsTrue();
        eval("(string-equal? \"hello\" \"world\")").yieldsFalse();

        // TODO: (make-string length)

        eval("(string-append \"llama\" \" and \" \"alpaca\")").yields("\"llama and alpaca\"");

        eval("(copy-string \"llama\")").yields("\"llama\"");

        eval("(char->string #\\B)").yields("\"B\"");

        eval("(list->string '(#\\Z #\\e #\\b #\\u))").yields("\"Zebu\"");

        eval("(string->list \"Zebu\")").yields("(#\\Z #\\e #\\b #\\u)");


    }
}

