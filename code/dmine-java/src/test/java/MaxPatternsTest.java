import Utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-04
 */
class MaxPatternsTest {

    @Test
    void findMaxPatternsTest() {
        List<Set<Integer>> li = List.of(
            Set.of(1, 2),  // ab
            Set.of(1, 3),  // ac
            Set.of(1, 4),  // ad
            Set.of(1, 5),  // ae
            Set.of(2, 3),  // bc
            Set.of(2, 4),  // bd
            Set.of(2, 5),  // be
            Set.of(3, 4),  // cd
            Set.of(3, 5),  // ce
            Set.of(4, 5)   // de
            );

        List<Pair<Set<Set<Integer>>, Set<Integer>>> max_pats;

        max_pats = MaxPatterns.findMaxPatterns(li);
        String corr_1 =
            "[Pair{[[1, 2], [1, 3], [1, 4], [1, 5]], [1, 2, 3, 4, 5]}, Pair{[[2], [2, 3], [2, 4], [2, 5]], [2, 3, 4, 5]}, Pair{[[3], [3, 4], [3, 5]], [3, 4, 5]}, Pair{[[4], [4, 5]], [4, 5]}, Pair{[[5]], [5]}]";
        TestUtils.StringEquals("basic 1", corr_1, "" + max_pats, true);

    }

}