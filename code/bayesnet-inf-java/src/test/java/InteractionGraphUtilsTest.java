import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-03
 */
class InteractionGraphUtilsTest {

    @Test
    void testGetEdgeList() {
        GraphUtils.GraphStruct g1_s = TestGraphs.createGraph_1();
        Graph g1 = g1_s.graph;
        DiscreteVariable a = g1_s.vars.get("a");
        DiscreteVariable b = g1_s.vars.get("b");
        DiscreteVariable c = g1_s.vars.get("c");
        DiscreteVariable d = g1_s.vars.get("d");
        DiscreteVariable e = g1_s.vars.get("e");

        InteractionGraph ig1 = new InteractionGraph(g1);
        List<Pair<DiscreteVariable, DiscreteVariable>> edge_lst;

        String ig1_edge_lst_str =
            "[Pair{a, b}, Pair{a, c}, Pair{b, c}, Pair{b, d}, Pair{c, d}, Pair{c, e}]";

        System.out.print("Testing Interaction-Graph structure: ");
        edge_lst = InteractionGraphUtils.getEdgeList(ig1);
        //System.out.println("" + InteractionGraphUtils.formatEdgeList(edge_lst));
        Assertions.assertEquals(ig1_edge_lst_str, "" + InteractionGraphUtils.formatEdgeList(edge_lst));
        System.out.println("PASSED");

    }
}