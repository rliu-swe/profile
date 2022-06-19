import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Author: Nurrachman Liu   2022-03
 */
class EliminationTreeOperationsTest {

    @Test
    void testComputeSeparators() {
        Pair<GraphUtils.GraphStruct, List<EliminationTree>> gs_et = TestElimTrees.createElimTree_Graph5();
        EliminationTree et1 = gs_et.right.get(0);
        EliminationTree et2 = gs_et.right.get(1);

        String msg;
        Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> sep;

        System.out.print("Testing EliminationTreeOperations.computeSeparators: ");

        String graph5_et1_seps =
            "b---[a, b]---a\n" +
            "d---[a, c]---c\n" +
            "c---[c]---e\n" +
            "a---[a, b]---d\n";

        String graph5_et2_seps =
            "b---[a, b]---a\n" +
            "d---[b, c]---c\n" +
            "a---[a, b]---c\n" +
            "c---[c]---e\n";

        sep = EliminationTreeOperations.computeSeparators(et1);
        msg = EliminationTreeUtils.printSeparators(sep);
        //System.out.println(msg);
        Assertions.assertEquals(graph5_et1_seps, msg);

        sep = EliminationTreeOperations.computeSeparators(et2);
        msg = EliminationTreeUtils.printSeparators(sep);
        //System.out.println(msg);
        Assertions.assertEquals(graph5_et2_seps, msg);

        System.out.println("PASSED");
    }

    @Test
    void testComputeClusters() {
        Pair<GraphUtils.GraphStruct, List<EliminationTree>> gs_et = TestElimTrees.createElimTree_Graph5();
        EliminationTree et1 = gs_et.right.get(0);
        EliminationTree et2 = gs_et.right.get(1);

        String msg;
        Map<Node, Set<DiscreteVariable>> clust;
        Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> sep;

        System.out.print("Testing EliminationTreeOperations.computeClusters: ");

        String graph5_et1_clusts =
            "a: [a, b]\n" +
            "b: [a, b]\n" +
            "c: [a, c]\n" +
            "d: [a, b, c, d]\n" +
            "e: [c, e]\n";

        String graph5_et2_clusts =
            "a: [a, b]\n" +
            "b: [a, b]\n" +
            "c: [a, b, c]\n" +
            "d: [b, c, d]\n" +
            "e: [c, e]\n";

        sep = EliminationTreeOperations.computeSeparators(et1);
        et1.setSeparators(sep);
        clust = EliminationTreeOperations.computeClusters(et1);
        msg = EliminationTreeUtils.printClusters(clust);
        //System.out.println(msg);
        Assertions.assertEquals(graph5_et1_clusts, msg);

        sep = EliminationTreeOperations.computeSeparators(et2);
        et2.setSeparators(sep);
        clust = EliminationTreeOperations.computeClusters(et2);
        msg = EliminationTreeUtils.printClusters(clust);
        //System.out.println(msg);
        Assertions.assertEquals(graph5_et2_clusts, msg);

        System.out.println("PASSED");
    }
}