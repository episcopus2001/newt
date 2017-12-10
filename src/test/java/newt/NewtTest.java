package newt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NewtTest extends NewtTestSupport {

    @Test
    @DisplayName("Test for debugging")
    public void testForDebugging() {
        lisp()
                .eval("(length '(a))")
                .yields(1);

//        lisp()
//                .eval("(length nil)")
//                .yields(0);
//        lisp()
//                .eval("(null? nil)")
//                .yieldsTrue();
//        lisp()
//                .eval("(define l '(34 55))")
//                .eval("l")
//                .yields("(34 55)")
//                .eval("(push l 21)")
//                .eval("l")
//                .yields("(21 34 55)")
//                .eval("(push l '(a b c))")
//                .eval("l")
//                .yields("((A B C) 21 34 55)");
//        // .eval("(push (car l) 'd)")
//        // .yields("((D A B C) 21 34 55)");
//
//        lisp()
//                .eval("(define l '(21 34 55))")
//                .eval("l")
//                .yields("(21 34 55)")
//                .eval("(pop l)")
//                .yields(21)
//                .eval("l").yields("(34 55)");

    }
}
