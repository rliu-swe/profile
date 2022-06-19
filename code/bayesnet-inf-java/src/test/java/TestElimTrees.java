import java.util.Arrays;
import java.util.List;

/**
 * Contains elimination trees for testing.
 *
 * @author rliu 2022-03
*/
public class TestElimTrees {


    /**
     * Returns two possible elim trees for this graph.
     *
     * Graph:        a
     *            /    \
     *           b      c
     *            \    / \
     *              d     e
     *
     * Elim-Tree 1:    1(a)
     *              /  |    3(c)
     *          2(b)   |  /    \
     *                 4(d)    5(e)
     *
     * Elim-Tree 2:    1(a)
     *              /     \
     *           2(b)      3(c)
     *                    /   \
     *                  4(d)   5(e)
     */
    public static Pair<GraphUtils.GraphStruct, List<EliminationTree>> createElimTree_Graph5() {
        GraphUtils.GraphStruct gs = TestGraphs.createGraph_5();
        Node a = gs.nodes.get("a");
        Node b = gs.nodes.get("b");
        Node c = gs.nodes.get("c");
        Node d = gs.nodes.get("d");
        Node e = gs.nodes.get("e");

        EliminationTree et1 = new EliminationTree();
        et1.link(b, a);
        et1.link(a, d);
        et1.link(d, c);
        et1.link(c, e);

        EliminationTree et2 = new EliminationTree();
        et2.link(b, a);
        et2.link(a, c);
        et2.link(c, d);
        et2.link(c, e);

        return Pair.of(gs, Arrays.asList(et1, et2));
    }
}
