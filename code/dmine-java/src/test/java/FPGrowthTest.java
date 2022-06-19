import Utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-04
 */
class FPGrowthTest {

    @Test
    void mineTestBasic1() {
        List<Set<Integer>> l3 = List.of(
            Set.of(1, 2, 3),   // abc
            Set.of(1, 2, 4),   // abd
            Set.of(1, 3, 4),   // acd
            Set.of(1, 3, 5),   // ace
            Set.of(2, 3, 4)    // bcd
        );

        FPGrowth fpgrowth = new FPGrowth(() -> l3);

        /**
         * Notes: notice that there are multiple ways of reaching sub-patterns smaller than max-pattern size k=3.
         *   for example, for k=2: {1,2}, support of {1,2} is 2: {1,3,2}:1 and {1,2}:1. This highlights a downside
         *   of the FP-Growth algorithm in that it has to be searched to get the total support for a particular pattern.
         */
        String corr_m2 =
            "-1: 0\n" +
                "    3: 1\n" +
                "        2: 1\n" +
                "            4: 1\n" +
                "    1: 4\n" +
                "        3: 3\n" +
                "            4: 1\n" +
                "            2: 1\n" +
                "        2: 1\n" +
                "            4: 1\n" +
                "\n" +
                "1: 4 -> 1: 4\n" +
                "3: 4 -> 3: 3 -> 3: 1\n" +
                "2: 3 -> 2: 1 -> 2: 1 -> 2: 1\n" +
                "4: 3 -> 4: 1 -> 4: 1 -> 4: 1\n";

        fpgrowth.mine(2);
        TestUtils.StringEquals("min_support_threshold=2", corr_m2, FPTreeUtils.print(fpgrowth), true);

    }

    @Test
    void mineTest_hw1_p2() {
        List<Set<Integer>> tdb = TestTDBs.tdb_hw1_p1();
        FPGrowth fpgrowth = new FPGrowth(() -> tdb);
        fpgrowth.mine(3);
        String corr = "";
        TestUtils.StringEquals("min_support_threshold=2", corr, FPTreeUtils.print(fpgrowth), true);

    }

}