import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;


/**
 * Author: Nurrachman Liu   2022-04
 */
class AprioriTest {

    String printApriorResults(List<Apriori.AprioriLevelStruct> results) {
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
    void mineTest() {
        List<Set<Integer>> l3 = List.of(
            Set.of(1, 2, 3),   // abc
            Set.of(1, 2, 4),   // abd
            Set.of(1, 3, 4),   // acd
            Set.of(1, 3, 5),   // ace
            Set.of(2, 3, 4)    // bcd
        );

        String result;
        List<Apriori.AprioriLevelStruct> ret;
        Apriori apriori = new Apriori(() -> l3);

        String corr_1 =
            "--------------------------\n" +
                "Level 1: \n" +
                "\tPruned: \n" +
                "[1]\n" +
                "[2]\n" +
                "[3]\n" +
                "[4]\n" +
                "[5]\n" +
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
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 4]\n" +
                "[2, 4]\n" +
                "[1, 5]\n" +
                "[3, 4]\n" +
                "[3, 5]\n" +
                "\n" +
                "\tCandidates: \n" +
                "[1, 2, 3]\n" +
                "[1, 2, 4]\n" +
                "[1, 3, 4]\n" +
                "[2, 3, 4]\n" +
                "[1, 2, 5]\n" +
                "[1, 3, 5]\n" +
                "[1, 4, 5]\n" +
                "[2, 3, 5]\n" +
                "[3, 4, 5]\n" +
                "\n" +
                "--------------------------\n" +
                "Level 3: \n" +
                "\tPruned: \n" +
                "[1, 2, 3]\n" +
                "[1, 2, 4]\n" +
                "[1, 3, 4]\n" +
                "[2, 3, 4]\n" +
                "[1, 3, 5]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1, 2, 3]\n" +
                "[1, 2, 4]\n" +
                "[1, 3, 4]\n" +
                "[2, 3, 4]\n" +
                "[1, 3, 5]\n" +
                "\n" +
                "\tCandidates: \n" +
                "[1, 2, 3, 4]\n" +
                "[1, 2, 3, 5]\n" +
                "[1, 3, 4, 5]\n" +
                "\n" +
                "--------------------------\n" +
                "Level 4: \n" +
                "\tPruned: \n" +
                "[1, 2, 3, 4]\n" +
                "\n" +
                "\tFrequent: \n" +
                "\n" +
                "\tCandidates: \n" +
                "\n";

        String corr_2 =
            "--------------------------\n" +
                "Level 1: \n" +
                "\tPruned: \n" +
                "[1]\n" +
                "[2]\n" +
                "[3]\n" +
                "[4]\n" +
                "[5]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1]\n" +
                "[2]\n" +
                "[3]\n" +
                "[4]\n" +
                "\n" +
                "\tCandidates: \n" +
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 4]\n" +
                "[2, 4]\n" +
                "[3, 4]\n" +
                "\n" +
                "--------------------------\n" +
                "Level 2: \n" +
                "\tPruned: \n" +
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 4]\n" +
                "[2, 4]\n" +
                "[3, 4]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 4]\n" +
                "[2, 4]\n" +
                "[3, 4]\n" +
                "\n" +
                "\tCandidates: \n" +
                "[1, 2, 3]\n" +
                "[1, 2, 4]\n" +
                "[1, 3, 4]\n" +
                "[2, 3, 4]\n" +
                "\n" +
                "--------------------------\n" +
                "Level 3: \n" +
                "\tPruned: \n" +
                "[1, 2, 3]\n" +
                "[1, 2, 4]\n" +
                "[1, 3, 4]\n" +
                "[2, 3, 4]\n" +
                "\n" +
                "\tFrequent: \n" +
                "\n" +
                "\tCandidates: \n" +
                "\n";

        String corr_3 =
            "--------------------------\n" +
                "Level 1: \n" +
                "\tPruned: \n" +
                "[1]\n" +
                "[2]\n" +
                "[3]\n" +
                "[4]\n" +
                "[5]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1]\n" +
                "[2]\n" +
                "[3]\n" +
                "[4]\n" +
                "\n" +
                "\tCandidates: \n" +
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 4]\n" +
                "[2, 4]\n" +
                "[3, 4]\n" +
                "\n" +
                "--------------------------\n" +
                "Level 2: \n" +
                "\tPruned: \n" +
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 4]\n" +
                "[2, 4]\n" +
                "[3, 4]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1, 3]\n" +
                "\n" +
                "\tCandidates: \n" +
                "\n";

        ret = apriori.mine(1);
        result = printApriorResults(ret);
        TestUtils.StringEquals("min_support_threshold=1", corr_1, result, true);

        ret = apriori.mine(2);
        result = printApriorResults(ret);
        TestUtils.StringEquals("min_support_threshold=2", corr_2, result);

        ret = apriori.mine(3);
        result = printApriorResults(ret);
        TestUtils.StringEquals("min_support_threshold=3", corr_3, result);

    }

    @Test
    void mineTest2_hw1_p1() {
        List<Set<Integer>> tdb = TestTDBs.tdb_hw1_p1();

        String result;
        List<Apriori.AprioriLevelStruct> ret;
        Apriori apriori = new Apriori(() -> tdb);

        ret = apriori.mine(3);
        result = printApriorResults(ret);
        String corr =
            "--------------------------\n" +
                "Level 1: \n" +
                "\tPruned: \n" +
                "[1]\n" +
                "[2]\n" +
                "[3]\n" +
                "[4]\n" +
                "[5]\n" +
                "[6]\n" +
                "[8]\n" +
                "[10]\n" +
                "[11]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1]\n" +
                "[2]\n" +
                "[3]\n" +
                "[8]\n" +
                "\n" +
                "\tCandidates: \n" +
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 8]\n" +
                "[2, 8]\n" +
                "[3, 8]\n" +
                "\n" +
                "--------------------------\n" +
                "Level 2: \n" +
                "\tPruned: \n" +
                "[1, 2]\n" +
                "[1, 3]\n" +
                "[2, 3]\n" +
                "[1, 8]\n" +
                "[2, 8]\n" +
                "[3, 8]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1, 2]\n" +
                "[1, 8]\n" +
                "[2, 8]\n" +
                "\n" +
                "\tCandidates: \n" +
                "[1, 2, 8]\n" +
                "\n" +
                "--------------------------\n" +
                "Level 3: \n" +
                "\tPruned: \n" +
                "[1, 2, 8]\n" +
                "\n" +
                "\tFrequent: \n" +
                "[1, 2, 8]\n" +
                "\n" +
                "\tCandidates: \n" +
                "\n";
        TestUtils.StringEquals("min_support_threshold=1", corr, result, true);

    }

}