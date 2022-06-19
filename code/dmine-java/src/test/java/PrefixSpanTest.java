import Utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-04
 */
class PrefixSpanTest {

    String printResults(Set<Pair<List<Set<Integer>>, Integer>> res) {
        StringBuilder sb = new StringBuilder();
        for (Pair<List<Set<Integer>>, Integer> fs : res) {
            sb.append(fs.left.stream().map(SeqUtils::canonical_order).toList()).append(": ").append(fs.right)
                .append("\n");
        }
        return sb.toString();
    }

    @Test
    void mine_testBasic1() {
        List<List<Set<Integer>>> tdb = List.of(
            List.of(Set.of(1, 2), Set.of(3), Set.of(1)),  // (ab)ca
            List.of(Set.of(1, 2), Set.of(2), Set.of(3)),  // (ab)bc
            List.of(Set.of(2), Set.of(3), Set.of(4)),     // bcd
            List.of(Set.of(2), Set.of(1, 2), Set.of(3))   // b(ab)c
        );
        /**
         * [[1, 2], [3], [1]]
         * [[1, 2], [2], [3]]
         * [[2], [3], [4]]
         * [[2], [1, 2], [3]]
         */
        //System.out.println(SeqUtils.printSeqs(tdb));

        Set<Pair<List<Set<Integer>>, Integer>> res;
        String corr;

        PrefixSpan prefixSpan = new PrefixSpan(() -> tdb);

        res = prefixSpan.mine(3);
        corr =
            "[[1], [3]]: 3\n" +
                "[[1, 2]]: 3\n" +
                "[[3]]: 4\n" +
                "[[1, 2], [3]]: 3\n" +
                "[[2], [3]]: 4\n" +
                "[[1]]: 3\n" +
                "[[2]]: 4\n";
        TestUtils.StringEquals("spmf PrefixSpan example, min sup = 3", corr, printResults(res));
    }


    @Test
    void mine_testBasic2() {
        List<List<Set<Integer>>> tdb = List.of(
            List.of(Set.of(1), Set.of(1, 3), Set.of(2), Set.of(4), Set.of(5)),  // a(ac)bde
            List.of(Set.of(2), Set.of(1), Set.of(7)),                           // bag
            List.of(Set.of(1), Set.of(2, 3), Set.of(4), Set.of(5), Set.of(4), Set.of(6), Set.of(2)), // a(bc)dedfb
            List.of(Set.of(1), Set.of(1, 2), Set.of(3))                         // a(ab)c
        );
        /**
         * [[1], [1, 3], [2], [4], [5]]
         * [[2], [1], [7]]
         * [[1], [2, 3], [4], [5], [4], [6], [2]]
         * [[1], [1, 2], [3]]
         */
        //System.out.println(SeqUtils.printSeqs(tdb));

        Set<Pair<List<Set<Integer>>, Integer>> res;
        String corr;

        PrefixSpan prefixSpan = new PrefixSpan(() -> tdb);

        res = prefixSpan.mine(2);
        corr =
            "[[4]]: 2\n" +
                "[[3], [5]]: 2\n" +
                "[[4], [5]]: 2\n" +
                "[[1], [3]]: 3\n" +
                "[[1], [1]]: 2\n" +
                "[[1], [2], [4], [5]]: 2\n" +
                "[[2]]: 4\n" +
                "[[1], [4], [5]]: 2\n" +
                "[[1], [2], [5]]: 2\n" +
                "[[3], [4], [5]]: 2\n" +
                "[[1], [3], [5]]: 2\n" +
                "[[1], [3], [4], [5]]: 2\n" +
                "[[5]]: 2\n" +
                "[[1], [4]]: 2\n" +
                "[[2], [4]]: 2\n" +
                "[[3], [4]]: 2\n" +
                "[[3]]: 3\n" +
                "[[1], [2]]: 3\n" +
                "[[3], [2]]: 2\n" +
                "[[1]]: 4\n" +
                "[[1], [3], [2]]: 2\n" +
                "[[1], [3], [4]]: 2\n" +
                "[[2], [4], [5]]: 2\n" +
                "[[1], [2], [4]]: 2\n" +
                "[[1], [5]]: 2\n" +
                "[[2], [5]]: 2\n";
        TestUtils.StringEquals("hw1 q3, min sup = 2", corr, printResults(res));

        res = prefixSpan.mine(3);
        corr =
            "[[1], [3]]: 3\n" +
                "[[3]]: 3\n" +
                "[[1], [2]]: 3\n" +
                "[[2]]: 4\n" +
                "[[1]]: 4\n";
        TestUtils.StringEquals("hw1 q3, min sup = 3", corr, printResults(res));

    }

}