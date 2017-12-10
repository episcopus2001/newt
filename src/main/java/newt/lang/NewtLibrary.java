package newt.lang;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static newt.lang.NewtRuntime.*;

public class NewtLibrary {

    private static NewtObject extend0(NewtObject env, String name, Supplier<NewtObject> fn) {
        NewtSymbol symbol = makeSymbol(name);
        NewtSubroutine subroutine = makeSubroutine(argl -> fn.get());
        return makePair(makePair(symbol, subroutine), env);
    }

    private static NewtObject extend1(NewtObject env, String name, Function<NewtObject, NewtObject> fn) {
        NewtSymbol symbol = makeSymbol(name);
        NewtSubroutine subroutine = makeSubroutine(argl -> fn.apply(car(argl)));
        return makePair(makePair(symbol, subroutine), env);
    }

    private static NewtObject extend2(NewtObject env, String name, BiFunction<NewtObject, NewtObject, NewtObject> fn) {
        NewtSymbol symbol = makeSymbol(name);
        NewtSubroutine subroutine = makeSubroutine(argl -> fn.apply(car(argl), cadr(argl)));
        return makePair(makePair(symbol, subroutine), env);
    }

    private static NewtObject extendv(NewtObject env, String name, Function<NewtObject[], NewtObject> fn) {
        NewtSymbol symbol = makeSymbol(name);
        NewtSubroutine subroutine = makeSubroutine(
                argl -> {
                    int len = 0;
                    for (NewtObject iter = argl; iter.isPair(); iter = cdr(iter))
                        len += 1;

                    NewtObject args[] = new NewtObject[len];

                    int index = 0;
                    for (; argl.isPair(); argl = cdr(argl))
                        args[index++] = car(argl);
                    return fn.apply(args);
                }
        );
        return makePair(makePair(symbol, subroutine), env);
    }

    public static NewtObject createInitialEnv() {
        NewtObject env = OBJ_NIL;
        env = extend1(env, "car", NewtRuntime::car);
        env = extend1(env, "cdr", NewtRuntime::cdr);

        env = extendv(env, "+", NewtRuntime::plus);
        env = extendv(env, "-", NewtRuntime::minus);
        env = extendv(env, "*", NewtRuntime::mul);
        env = extendv(env, "/", NewtRuntime::div);

        env = extend2(env, "=", NewtRuntime::eq);
        env = extend2(env, "<", NewtRuntime::lt);

        env = extend1(env, "number?", NewtRuntime::numberP);
        env = extend1(env, "string?", NewtRuntime::stringP);
        env = extend1(env, "symbol?", NewtRuntime::symbolP);
        env = extend1(env, "procedure?", NewtRuntime::procedureP);
        env = extend1(env, "lambda?", NewtRuntime::lambdaP);
        env = extend1(env, "pair?", NewtRuntime::pairP);
        env = extend1(env, "subroutine?", NewtRuntime::subroutineP);

        env = extend2(env, "cons", NewtRuntime::cons);
        env = extendv(env, "list", NewtRuntime::list);
        env = extend0(env, "standard-output", NewtRuntime::standardOutput);
        env = extend0(env, "standard-input", NewtRuntime::standardInput);
        env = extend0(env, "standard-error", NewtRuntime::standardError);
        env = extend2(env, "write", NewtRuntime::write);
        env = extend2(env, "write-string", NewtRuntime::writeString);
        env = extend2(env, "write-line", NewtRuntime::writeLine);
        env = extend1(env, "read-line", NewtRuntime::readLine);
        env = extend2(env, "open", NewtRuntime::open);
        env = extend1(env, "close", NewtRuntime::close);
        env = extend1(env, "char?", NewtRuntime::charP);
        env = extend1(env, "whitespace?", NewtRuntime::whitespaceP);
        env = extend1(env, "alphabetic?", NewtRuntime::alphabeticP);
        env = extend1(env, "digit?", NewtRuntime::digitP);
        env = extend1(env, "uppercase?", NewtRuntime::uppercaseP);
        env = extend1(env, "lowercase?", NewtRuntime::lowercaseP);
        env = extendv(env, "append", NewtRuntime::append);
//        env = extend1(env, "null?", NewtRuntime::nullP);
//        env = extend1(env, "atom?", NewtRuntime::atomP);
        env = extend1(env, "list?", NewtRuntime::listP);
        env = extend1(env, "proper-list?", NewtRuntime::properListP);
        env = extend2(env, "eq?", NewtRuntime::eqP);

        return env;
    }

}
