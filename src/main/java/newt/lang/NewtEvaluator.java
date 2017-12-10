package newt.lang;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static newt.lang.NewtRuntime.*;
import static newt.lang.NewtQuasiquote.*;
import static newt.lang.NewtLibrary.*;

public class NewtEvaluator {

    private NewtObject genv;
    private Set<NewtObject> macros = new HashSet<>();

    public NewtEvaluator() {
        this(new String[0]);
    }

    public NewtEvaluator(String... args) {
        genv = makePair(createInitialEnv(), OBJ_NIL);

        InputStream inputStream = getClass().getResourceAsStream("/newt/prologue.newt");
        NewtObject inputPort = makeInputPort(inputStream);
        NewtObject obj;
        while ((obj = read(inputPort)) != CHAR_EOF)
            eval(obj);

        if (args != null) {
            obj = OBJ_NIL;
            for (String arg : args)
                obj = makePair(makeString(arg), obj);

            environmentExtend(genv, makeSymbol("*cmd-line-args*"), obj);
        }
    }

    public NewtObject eval(NewtObject expr) {
        NewtObject ans;
        ans = eval(expr, genv);
        return ans;
    }

    private NewtObject eval(NewtObject obj, NewtObject env) {
        NewtObject ans;
        if (obj.isAtom()) {
            if (obj.isSymbol())
                ans = cdr(environmentLookup(env, obj));
            else
                ans = obj;
        }
        else {
            NewtObject head = car(obj);
            NewtObject tail = cdr(obj);
            if (head == SYM_LAMBDA)
                ans = evalLambda(tail, env);
            else if (head == SYM_DEFINE)
                ans = evalDefine(tail, false);
            else if (head == SYM_DEFMACRO)
                ans = evalDefine(tail, true);
            else if (head == SYM_IF)
                ans = evalIf(tail, env);
            else if (head == SYM_BEGIN)
                ans = evalBegin(tail, env);
            else if (head == SYM_LET)
                ans = evalLet(tail, env);
            else if (head == SYM_SET_BANG)
                ans = evalSet(tail, env);
            else if (head == SYM_QUOTE)
                ans = evalQuote(tail, env);
            else if (head == SYM_QUASIQUOTE)
                ans = evalQuasiquote(tail, env);
            else {
                boolean isMacro = macros.contains(head);
                if (isMacro) {
                    head = eval(head, env);
                    ans = apply(head, tail);
                    ans = eval(ans, env);
                } else {
                    head = eval(head, env);
                    tail = evalList(tail, env);
                    ans = apply(head, tail);
                }
            }
        }
        return ans;
    }

    private NewtObject evalList(NewtObject list, NewtObject env) {
        NewtObject ans = OBJ_NIL;
        NewtObject tmp, last;
        if (list.isPair()) {
            tmp = car(list);
            tmp = eval(tmp, env);
            tmp = makePair(tmp, OBJ_NIL);
            last = tmp;
            ans = tmp;
            for (list = cdr(list); list.isPair(); list = cdr(list)) {
                tmp = car(list);
                tmp = eval(tmp, env);
                tmp = makePair(tmp, OBJ_NIL);
                setCdr(last, tmp);
                last = tmp;
            }
        }
        return ans;
    }

    private NewtObject apply(NewtObject obj, NewtObject args) {
        NewtObject ans = OBJ_NIL;
        if (obj.isProcedure()) {
            NewtLambda lambda = obj.asProcedure().getLambda();
            NewtObject env = obj.asProcedure().getEnvironment();
            NewtObject formals = lambda.getFormals();
            NewtObject body = lambda.getBody();
            env = environmentSpawn(env, formals, args);
            for (; body.isPair(); body = cdr(body))
                ans = eval(car(body), env);
        } else if (obj.isSubroutine()) {
            ans = obj.asSubroutine().getFunction().apply(args);
        } else {
            throw new NewtException("Cannot apply " + obj + " to " + args);
        }
        return ans;
    }

    private NewtObject evalIf(NewtObject argl, NewtObject env) {
        NewtObject ans;
        NewtObject cnd = car(argl);
        NewtObject tmp = eval(cnd, env);
        if (tmp != OBJ_FALSE) {
            NewtObject csq = cadr(argl);
            ans = eval(csq, env);
        } else {
            NewtObject alt = caddr(argl);
            ans = eval(alt, env);
        }
        return ans;
    }

    private NewtObject evalBegin(NewtObject argl, NewtObject env) {
        NewtObject ans = OBJ_NIL;
        for (; argl.isPair(); argl = cdr(argl)) {
            NewtObject exp = car(argl);
            ans = eval(exp, env);
        }
        return ans;
    }

    private NewtObject evalDefine(NewtObject argl, boolean isMacro) {
        if (car(argl).isPair()) {
            //    (define (f x y z) b c d)
            // => (define f (lambda (x y z) b c d))

            NewtObject var = car(car(argl));
            NewtObject formals = cdr(car(argl));
            NewtObject body = cdr(argl);

            NewtObject l = cons(SYM_LAMBDA, cons(formals, body));
            argl = makeList(var, l);
            return evalDefine(argl, isMacro);
        } else {
            // define symbol
            NewtObject var = car(argl);
            if (isMacro)
                macros.add(var);
            environmentExtend(genv, var, OBJ_UNBOUND);
            argl = cdr(argl);
            if (argl.isPair()) {
                NewtObject body = car(argl);
                body = eval(body, genv);
                environmentSet(genv, var, body);
            }
        }
        return OBJ_UNDEFINED;
    }

    private NewtObject evalSet(NewtObject argl, NewtObject env) {
        NewtObject var = car(argl);
        NewtObject body = cadr(argl);
        body = eval(body, env);
        environmentSet(env, var, body);
        return OBJ_UNDEFINED;
    }

    private NewtObject evalQuote(NewtObject argl, NewtObject env) {
        NewtObject ans = car(argl);
        return ans;
    }

    private NewtObject evalLambda(NewtObject argl, NewtObject env) {
        NewtObject ans;
        NewtObject formals = car(argl);
        NewtObject body = cdr(argl);
        NewtLambda fn = makeLambda(formals, body);
        ans = makeProcedure(fn, env);
        return ans;
    }

    private NewtObject evalQuasiquote(NewtObject argl, NewtObject env) {
        NewtObject exp = car(argl);
        NewtObject expansion = expandQuasiquote(exp);
        NewtObject ans = eval(expansion, env);
        return ans;
    }

    private NewtObject evalLet(NewtObject argl, NewtObject env) {
        // (let ((x a) ...) body ...)
        NewtObject ans = OBJ_NIL;
        NewtObject lets = car(argl);
        NewtObject bodies = cdr(argl);
        NewtObject formals = OBJ_NIL;
        NewtObject args = OBJ_NIL;
        NewtObject iter;
        for (iter = lets; iter.isPair(); iter = cdr(iter)) {
            args = makePair(OBJ_NIL, args);
            formals = makePair(caar(iter), formals);
        }
        env = environmentSpawn(env, formals, args);
        for (; lets.isPair(); lets = cdr(lets)) {
            NewtObject x = caar(lets);
            NewtObject a = eval(cadar(lets), env);
            environmentSet(env, x, a);
        }
        for (; bodies.isPair(); bodies = cdr(bodies))
            ans = eval(car(bodies), env);
        return ans;
    }

    // environment functions

    private NewtObject environmentLookup(NewtObject env, NewtObject key) {
        assertSymbol(key);
        NewtObject ans;
        NewtObject iter;
        for (; env.isPair(); env = cdr(env))
            for (iter = car(env); iter.isPair(); iter = cdr(iter))
                if (caar(iter) == key) {
                    ans = car(iter);
                    return ans;
                }
        throw new NewtException("Undefined symbol: " + key);
    }

    private NewtObject environmentSet(NewtObject env, NewtObject key, NewtObject value) {
        assertSymbol(key);
        NewtObject entry = environmentLookup(env, key);
        entry.asPair().setSecond(value);
        return value;
    }

    private void environmentExtend(NewtObject env, NewtObject key, NewtObject value) {
        assertSymbol(key);
        NewtObject ass = makePair(key, value);
        setCar(env, makePair(ass, car(env)));
    }

    private void environmentExtendList(NewtObject env, NewtObject keyl, NewtObject valuel) {
        assertList(keyl);
        assertList(valuel);
        for (; keyl.isPair() && valuel.isPair(); keyl = cdr(keyl), valuel = cdr(valuel)) {
            NewtObject ass = makePair(car(keyl), car(valuel));
            setCar(env, makePair(ass, car(env)));
        }
        if (keyl.isPair() || valuel.isPair())
            throw new NewtException("Length of key list must match length of value list.");
    }

    private NewtObject environmentSpawn(NewtObject env, NewtObject keys, NewtObject values) {
        NewtObject ans;
        ans = makePair(OBJ_NIL, env);
        environmentExtendList(ans, keys, values);
        return ans;
    }
}

