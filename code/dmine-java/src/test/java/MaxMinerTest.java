import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;


/**
 * Author: Nurrachman Liu   2022-04
 */
class MaxMinerTest {

    String printResults(List<Apriori.AprioriLevelStruct> results) {
        StringBuilder sb = new StringBuilder();

        int k = 1;
        for (Apriori.AprioriLevelStruct aps : results) {
            List<String> msg = List.of(
                "--------------------------",
                "Level " + k++ + ": ",
                "\tPruned: ",
                ItemsetUtils.printItemsets(aps.pruned()),
                "\tFrequent: ",
                ItemsetUtils.printItemsets(aps.frequent()),
                "\tCandidates: ",
                ItemsetUtils.printItemsets(aps.candidates())
            );
            msg.forEach(s -> sb.append(s).append("\n"));
        }

        return sb.toString();
    }


    @Test
    void mineTestBasic1() {
        List<Set<Integer>> l3 = List.of(
            Set.of(1, 2, 3, 4, 5), // abcde
            Set.of(2, 3, 4, 5),    // bcde
            Set.of(1, 3, 4, 6)     // acdf
        );

        List<Apriori.AprioriLevelStruct> ret;
        MaxMiner mm = new MaxMiner(() -> l3);

        String corr_m2 =
            "--------------------------\n" +
                "Level 1: \n" +
                "\tPruned: \n" +
                "[1]\n" +
                "[2]\n" +
                "[3]\n" +
                "[4]\n" +
                "[5]\n" +
                "[6]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1]\n" +
                "[2]\n" +
                "[3]\n" +
                "[4]\n" +
                "[5]\n" +
                "\n" +
                "\tCandidates: \n" +
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 4]\n" +
                "[2, 4]\n" +
                "[3, 4]\n" +
                "[1, 5]\n" +
                "[2, 5]\n" +
                "[3, 5]\n" +
                "[4, 5]\n" +
                "\n" +
                "--------------------------\n" +
                "Level 2: \n" +
                "\tPruned: \n" +
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 4]\n" +
                "[2, 4]\n" +
                "[1, 5]\n" +
                "[3, 4]\n" +
                "[2, 5]\n" +
                "[3, 5]\n" +
                "[4, 5]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1, 3]\n" +        // [1,2,3,4,5] is a max-pat that does not meet the supp threshold; therefore, its subsets
                "[1, 4]\n" +        //     that meet supp-threshold are in the result set intsead ([1,3], [1,4]).
                "[2, 3, 4, 5]\n" +  // [2,3] is a generating subset of max-pat [2,3,4,5]; since [2,3,4,5] meets the support, [2,3] is not in the result now.
                "[3, 4, 5]\n" +
                "[4, 5]\n" +
                "[5]\n" +
                "\n" +
                "\tCandidates: \n" +
                "[1, 3, 4]\n" +
                "[1, 4, 5]\n" +
                "\n";

        ret = mm.mine(2);
        TestUtils.StringEquals("min_support_threshold=2", corr_m2, printResults(ret), true);
    }

    @Test
    void mineTest_hw1_p1() {
        List<Set<Integer>> tdb = TestTDBs.tdb_hw1_p1();

        List<Apriori.AprioriLevelStruct> ret;
        MaxMiner mm = new MaxMiner(() -> tdb);
        ret = mm.mine(3);
        String corr = "";
        TestUtils.StringEquals("hw1 p1, min_sup=3", corr, printResults(ret), true);


    }


}