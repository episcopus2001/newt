package newt.lang;

import static newt.lang.NewtRuntime.*;

public class NewtQuasiquote {

    public static NewtObject expandQuasiquote(NewtObject exp) {

        NewtObject ans;
        if (isUnquote(exp)) {
            // (quasiquote (unquote datum)) => datum
            //             |---- exp ----|
            ans = datum(exp);
        } else if (isUnquoteSplicing(exp)) {
            // (quasiquote (unquote-splicing datum)) => illegal
            //             |-------- exp ---------|
            throw new NewtException("Illegal quasiquote expression (quasiquote on unquote-splicing).");
        } else if (isQuasiquote(exp)) {
            // (quasiquote (quasiquote datum)) => expand (quasiquote datum) first
            //             |----- exp ------|
            ans = expandQuasiquote(expandQuasiquote(datum(exp)));
        } else if (isPair(exp)) {
            // (quasiquote (first.second)) => (append (expand first) (expand second))
            //             |--- exp ----|
            NewtObject first = car(exp);
            NewtObject second = cdr(exp);
            NewtObject expansionFirst = expandQuasiquoteList(first);
            NewtObject expansionSecond = expandQuasiquote(second);
            ans = makeList(SYM_APPEND, expansionFirst, expansionSecond);
        } else {
            // (quasiquote atom) => (quote atom)
            //             |exp|
            ans = makeList(SYM_QUOTE, exp);
        }
        return ans;
    }

    private static NewtObject expandQuasiquoteList(NewtObject exp) {

        // expand into a list

        NewtObject ans;
        if (isUnquote(exp)) {
            // (append ... (quasiquote (unquote datum)) ...) => (append ... (list datum) ...)
            //                         |---- exp ----|
            ans = makeList(SYM_LIST, datum(exp));
        } else if (isUnquoteSplicing(exp)) {
            // (append ... (quasiquote (unquote-splicing datum)) ...) => (append ... datum ...)
            //                         |-------- exp ---------|
            ans = datum(exp);
        } else if (isQuasiquote(exp)) {
            // (append ... (quasiquote (quasiquote datum)) ...)  => expand inner quasiquote first
            //                         |----- exp ------|
            ans = expandQuasiquoteList(expandQuasiquote(datum(exp)));
        } else if (isPair(exp)) {
            // (append ... (quasiquote (first.second)) ...) => (... (append (expand first) (expand second)) ...)
            //                         |--- exp ----|
            NewtObject first = car(exp);
            NewtObject second = cdr(exp);
            ans = makeList(SYM_LIST, makeList(SYM_APPEND, expandQuasiquoteList(first), expandQuasiquote(second)));
        } else {
            // (append ... (quasiquote atom) ...) => (append ... '(atom) ...)
            //                         |exp|
            ans = makeList(SYM_QUOTE, makeList(exp));
        }
        return ans;
    }

    private static boolean isUnquote(NewtObject exp) {
        return exp.isPair() && car(exp).isSymbol() && car(exp).asSymbol() == SYM_UNQUOTE;
    }

    private static boolean isUnquoteSplicing(NewtObject exp) {
        return exp.isPair() && car(exp).isSymbol() && car(exp).asSymbol() == SYM_UNQUOTE_SPLICING;
    }

    private static boolean isQuasiquote(NewtObject exp) {
        return exp.isPair() && car(exp).isSymbol() && car(exp).asSymbol() == SYM_QUASIQUOTE;
    }

    private static boolean isPair(NewtObject exp) {
        return exp.isPair();
    }

    private static NewtObject datum(NewtObject exp) {
        return cadr(exp);
    }

}
