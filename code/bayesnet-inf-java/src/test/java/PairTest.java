import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-03
 */
class PairTest {

    @Test
    void testSymmetricPair() {
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 2);
        Node a = new Node(v_a);
        Node b = new Node(v_b);

        Pair<Node, Node> pair_edge_ab = Pair.of(a, b);
        Pair<Node, Node> pair_edge_ba = Pair.of(b, a);

        Pair<Node, Node> edge_ab = Pair.Symmetric.of(a, b);
        Pair<Node, Node> edge_ba = Pair.Symmetric.of(b, a);

        Pair<DiscreteVariable, DiscreteVariable> edge_vab = Pair.Symmetric.of(v_a, v_b);
        Pair<DiscreteVariable, DiscreteVariable> edge_vba = Pair.Symmetric.of(v_b, v_a);

        System.out.print("Testing Symmetric Pair");

        Assertions.assertEquals(false, pair_edge_ab.equals(pair_edge_ba));
        Assertions.assertEquals(true, edge_ab.equals(edge_ba));
        Assertions.assertEquals(true, edge_vab.equals(edge_vba));

        System.out.println("PASSED");

    }

}