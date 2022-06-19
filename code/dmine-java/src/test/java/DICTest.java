import Utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-04
 */
class DICTest {


    String printDICResults(List<Pair<Set<Integer>, Integer>> results) {
        StringBuilder sb = new StringBuilder();

        List<Pair<List<Integer>, Integer>> results_sorted =
            new ArrayList<>(results.stream().map(p -> Pair.of(p.left.stream().sorted().toList(), p.right)).toList());

        Comparator<Pair<List<Integer>, Integer>> comp = Comparator.comparing((Pair<List<Integer>,Integer> o) -> o.left.toString().length()).thenComparing(o -> o.left.toString());

//        Collections.sort(results_sorted, Comparator.comparing(o -> o.left.toString()));
        Collections.sort(results_sorted, comp);

        for (Pair<List<Integer>, Integer> res : results_sorted) {
            sb.append(res.left + ": " + res.right).append("\n");
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

        DIC dic = new DIC(() -> l3);

        List<Pair<Set<Integer>, Integer>> ret;


        String corr_k3_min1 =
            "[1]: 4\n" +
                "[2]: 3\n" +
                "[3]: 4\n" +
                "[4]: 3\n" +
                "[5]: 1\n" +
                "[1, 2]: 2\n" +
                "[1, 3]: 3\n" +
                "[1, 4]: 2\n" +
                "[1, 5]: 1\n" +
                "[2, 3]: 2\n" +
                "[2, 4]: 2\n" +
                "[3, 4]: 2\n" +
                "[3, 5]: 1\n" +
                "[1, 2, 3]: 1\n" +
                "[1, 2, 4]: 1\n" +
                "[1, 3, 4]: 1\n" +
                "[1, 3, 5]: 1\n" +
                "[2, 3, 4]: 1\n";

        String corr_k3_min2 =
            "[1]: 4\n" +
                "[2]: 3\n" +
                "[3]: 4\n" +
                "[4]: 3\n" +
                "[1, 2]: 2\n" +
                "[1, 3]: 3\n" +
                "[1, 4]: 2\n" +
                "[2, 3]: 2\n" +
                "[2, 4]: 2\n" +
                "[3, 4]: 2\n";

        ret = dic.mine(3, 1);
        TestUtils.StringEquals("k=3, min_support_threshold=1", corr_k3_min1, printDICResults(ret));

        ret = dic.mine(3, 2);
        TestUtils.StringEquals("k=3, min_support_threshold=2", corr_k3_min2, printDICResults(ret), true);

    }
}