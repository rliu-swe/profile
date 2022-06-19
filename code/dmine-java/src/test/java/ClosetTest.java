import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-04
 */
class ClosetTest {

    @Test
    void mineTestBasic1() {
        List<Set<Integer>> l4 = List.of(
            Set.of(1, 3, 4, 5, 6),   // a,c,d,e,f
            Set.of(1, 2, 5),     // a,b,e
            Set.of(3, 5, 6),     // c,e,f
            Set.of(1, 3, 4, 6),  // a,c,d,f
            Set.of(3, 5, 6)      // c,e,f
        );
        Closet closet = new Closet(() -> l4);


        closet.closet(2);
        String corr_1 =
            "[Pair{[1, 3, 4, 6], 2}, Pair{[1], 3}, Pair{[1, 5], 2}, Pair{[3, 6], 4}, Pair{[3, 5, 6], 3}, Pair{[5], 4}]";
        TestUtils.StringEquals("closet-paper example", corr_1, "" + closet.getFCI(), true);
    }
}