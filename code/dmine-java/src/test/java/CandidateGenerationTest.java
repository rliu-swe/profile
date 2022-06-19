import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;


/**
 * @author rliu 2022-04
 */
class CandidateGenerationTest {

    @Test
    void itemsets_selfJoinTest() {
        List<Set<Integer>> l3 = List.of(
            Set.of(1, 2, 3),   // abc
            Set.of(1, 2, 4),   // abd
            Set.of(1, 3, 4),   // acd
            Set.of(1, 3, 5),   // ace
            Set.of(2, 3, 4)    // bcd
        );

        String result;

        String corr1 =
            "[1, 2, 3, 4]\n" +
            "[1, 2, 3, 5]\n" +
            "[1, 3, 4, 5]\n";

        Set<Set<Integer>> candidates = CandidateGeneration.Itemsets.self_join(l3);
        result = ItemsetUtils.printItemsets(candidates);
        TestUtils.StringEquals("self_join", corr1, result);
    }

    @Test
    void itemsets_pruneTest() {
        List<Set<Integer>> l3 = List.of(
            Set.of(1, 2, 3),   // abc
            Set.of(1, 2, 4),   // abd
            Set.of(1, 3, 4),   // acd
            Set.of(1, 3, 5),   // ace
            Set.of(2, 3, 4)    // bcd
        );

        List<Set<Integer>> l4_candidates = List.of(
            Set.of(1, 2, 3, 4),
            Set.of(1, 2, 3, 5),
            Set.of(1, 3, 4, 5)
        );

        String result;

        String corr1 =
            // Taking a look at the above:
            //    1, 2, 3, 5:  subset 2, 3, 5  and  1, 2, 5  are not in the original list
            //    1, 3, 4, 5:  subset 1, 4, 5  and  3, 4, 5  are not in the original list
            "[1, 2, 3, 4]\n";

        Set<Set<Integer>> l4_cands_pruned = CandidateGeneration.Itemsets.prune(l3, l4_candidates);
        result = ItemsetUtils.printItemsets(l4_cands_pruned);
        TestUtils.StringEquals("prune", corr1, result, true);
    }

    @Test
    void seqs_selfJoinTest() {
        String result;
        Set<List<Set<Integer>>> candidates;


        List<List<Set<Integer>>> tdb00 = List.of(
            List.of(Set.of(1), Set.of(2)),
            List.of(Set.of(1), Set.of(3))
        );
        String corr00 =
            "";
        candidates = CandidateGeneration.Sequential.self_join(tdb00);
        result = SeqUtils.printSeqs(candidates);
        TestUtils.StringEquals("self_join", corr00, result, true);


        List<List<Set<Integer>>> tdb0 = List.of(
            List.of(Set.of(1), Set.of(2)),
            List.of(Set.of(2), Set.of(5))
        );
        String corr0 =
            "[[1], [2], [5]]\n";
        candidates = CandidateGeneration.Sequential.self_join(tdb0);
        result = SeqUtils.printSeqs(candidates);
        TestUtils.StringEquals("self_join", corr0, result, true);


        List<List<Set<Integer>>> tdb1 = List.of(
            List.of(Set.of(1, 2)),
            List.of(Set.of(2), Set.of(5))
        );
        String corr1 =
            "[[1, 2], [5]]\n";
        candidates = CandidateGeneration.Sequential.self_join(tdb1);
        result = SeqUtils.printSeqs(candidates);
        TestUtils.StringEquals("self_join", corr1, result, true);


        List<List<Set<Integer>>> tdb2 = List.of(
            List.of(Set.of(1), Set.of(2), Set.of(3)),      // {1} {2} {3}
            List.of(Set.of(1), Set.of(2, 5)),              // {1} {2 5}
            List.of(Set.of(1), Set.of(5), Set.of(3)),      // {1} {5} {3}
            List.of(Set.of(2), Set.of(3), Set.of(4)),      // {2} {3} {4}
            List.of(Set.of(2, 5), Set.of(3)),              // {2 5} {3}
            List.of(Set.of(3), Set.of(4), Set.of(5)),      // {3} {4} {5}
            List.of(Set.of(5), Set.of(3, 4))               // {5} {3 4}
        );

        String corr2 =
            "[[1], [2], [3], [4]]\n" +
            "[[1], [2, 5], [3]]\n" +
            "[[1], [5], [3, 4]]\n" +
            "[[2], [3], [4], [5]]\n" +
            "[[2, 5], [3, 4]]\n";
        candidates = CandidateGeneration.Sequential.self_join(tdb2);
        result = SeqUtils.printSeqs(candidates);
        TestUtils.StringEquals("self_join", corr2, result, true);


        List<List<Set<Integer>>> tdb3 = List.of(
            List.of(Set.of(1, 2), Set.of(3)),
            List.of(Set.of(1, 2), Set.of(4)),
            List.of(Set.of(1), Set.of(3, 4)),
            List.of(Set.of(1, 3), Set.of(5)),
            List.of(Set.of(2), Set.of(3, 4)),
            List.of(Set.of(2), Set.of(3), Set.of(5))
        );

        String corr3 =
            "[[1, 2], [3, 4]]\n" +
                "[[1, 2], [3], [5]]\n";
        candidates = CandidateGeneration.Sequential.self_join(tdb3);
        result = SeqUtils.printSeqs(candidates);
        TestUtils.StringEquals("self_join", corr3, result, true);

    }

    @Test
    void seqs_pruneTest() {
        String result;
        Set<List<Set<Integer>>> candidates;
        Set<List<Set<Integer>>> candidates_pruned;


        List<List<Set<Integer>>> tdb3 = List.of(
            List.of(Set.of(1, 2), Set.of(3)),
            List.of(Set.of(1, 2), Set.of(4)),
            List.of(Set.of(1), Set.of(3, 4)),
            List.of(Set.of(1, 3), Set.of(5)),
            List.of(Set.of(2), Set.of(3, 4)),
            List.of(Set.of(2), Set.of(3), Set.of(5))
        );

        String corr3 =
            "[[1, 2], [3, 4]]\n";
        candidates = CandidateGeneration.Sequential.self_join(tdb3);
        candidates_pruned = CandidateGeneration.Sequential.prune(tdb3, candidates, 1);
        result = SeqUtils.printSeqs(candidates_pruned);
        TestUtils.StringEquals("self_join", corr3, result, true);

    }

}