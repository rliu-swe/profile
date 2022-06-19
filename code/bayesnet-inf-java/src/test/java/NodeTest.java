import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-03
 */
class NodeTest {

    /**
     * Removes the id portion of the node's toString() output.
     */
    String removeId(String node_str) {
        return node_str.replaceFirst("id=\\d+,?\\s*", "");
    }

    @Test
    void testEquality() {
        DiscreteVariable a1 = new DiscreteVariable("a", 2);
        DiscreteVariable a2 = new DiscreteVariable("a", 3);
        DiscreteVariable b = new DiscreteVariable("b", 3);
        DiscreteVariable c = new DiscreteVariable("b", 4);

        Factor f1 = new Factor("ab", Arrays.asList(a1, b));
        Factor f2 = new Factor("ac", Arrays.asList(a2, c));
        Node n1 = new Node(a1);
        Node n2 = new Node(a2);
        n1.setFactor(f1);
        n2.setFactor(f2);

        System.out.print("Testing Node equality, based on the attached DiscreteVariable only:: ");
        Assertions.assertEquals(true, n1.equals(n2));
        System.out.println("PASSED");
    }

    @Test
    void testBasic() {
        DiscreteVariable var_a = new DiscreteVariable("a", 2);
        DiscreteVariable var_b = new DiscreteVariable("b", 2);

        Node node_a = new Node(var_a);
        Node node_b = new Node(var_b);

        Factor f_a = new Factor(Arrays.asList(var_a));
        Factor f_ba = new Factor(Arrays.asList(var_a, var_b));

        node_a.setFactor(f_a);
        node_b.setFactor(f_ba);

        String node_a_str =
            "Node{var='DiscreteVariable{name='a', num_levels=2, level_names={}}', tag_name='', parents=[], children=[], factor=\n" +
            "idx    a | Value\n" +
            "---------+--------\n" +
            "  0   a0   0.0000\n" +
            "  1   a1   0.0000\n" +
            "}";

        String node_b_str =
            "Node{var='DiscreteVariable{name='b', num_levels=2, level_names={}}', tag_name='', parents=[], children=[], factor=\n" +
            "idx    a    b | Value\n" +
            "--------------+--------\n" +
            "  0   a0   b0   0.0000\n" +
            "  1   a0   b1   0.0000\n" +
            "  2   a1   b0   0.0000\n" +
            "  3   a1   b1   0.0000\n" +
            "}";

        // System.out.println(node_a);
        // System.out.println(node_b);
        System.out.print("Testing basic structure: ");
        Assertions.assertEquals(node_a_str, "" + removeId("" + node_a));
        Assertions.assertEquals(node_b_str, "" + removeId("" + node_b));
        System.out.println("PASSED");
    }

}