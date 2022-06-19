import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


/**
 * @author rliu 2022-03
 */
class GraphVerificationTest {

    @Test
    void verifyStructure() {
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 3);
        DiscreteVariable v_c = new DiscreteVariable("c", 2);
        DiscreteVariable v_d = new DiscreteVariable("d", 3);

        Node a = new Node(v_a);
        Node b = new Node(v_b);
        Node c = new Node(v_c);
        Node d = new Node(v_d);

        a.addChild(b);
        //b.addParent(a);
        c.addParent(b);
        //b.addChild(c);
        b.addChild(d);
        d.addParent(b);

        Graph g = new Graph();
        g.addNode(a);
        g.addNode(b);
        g.addNode(c);
        g.addNode(d);

        List<String> problems;
        System.out.print("Testing graph structure verification: ");
        problems = GraphVerification.verifyStructure(g);
        Assertions.assertEquals(
           "[Node a lists node b as a child but this child does not list it as parent., Node c lists node b as a parent but this parent does not list it as child.]" ,
            "" + problems);
        b.addParent(a);
        b.addChild(c);
        problems = GraphVerification.verifyStructure(g);
        Assertions.assertEquals(
            "[]",
            "" + problems);
        System.out.println("PASSED");
    }

    @Test
    void verifyFactors() {
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 3);
        DiscreteVariable v_c = new DiscreteVariable("c", 2);
        DiscreteVariable v_d = new DiscreteVariable("d", 3);

        Node a = new Node(v_a);
        Node b = new Node(v_b);
        Node c = new Node(v_c);
        Node d = new Node(v_d);

        a.addChild(b);
        b.addParent(a);
        c.addParent(b);
        b.addChild(c);
        b.addChild(d);
        d.addParent(b);

        Graph g = new Graph();
        g.addNode(a);
        g.addNode(b);
        g.addNode(c);
        g.addNode(d);


        Factor f_a = new Factor(Arrays.asList(v_a));
        Factor f_b = new Factor(Arrays.asList(v_a, v_b));
        Factor f_c = new Factor(Arrays.asList(v_b, v_c));
        Factor f_d = new Factor(Arrays.asList(v_b, v_d));

        Factor f_a_wrong = new Factor(Arrays.asList(v_b));
        Factor f_b_wrong = new Factor(Arrays.asList(v_b));
        Factor f_d_wrong = new Factor(Arrays.asList(v_b, v_d, v_a));

        a.setFactor(f_a);
        b.setFactor(f_b);
        c.setFactor(f_c);
        d.setFactor(f_d);

        List<String> problems;

        System.out.print("Testing graph/factor matching verification: ");
        problems = GraphVerification.verifyFactors(g);
        //System.out.println(problems);
        Assertions.assertEquals("[]", "" + problems);

        a.setFactor(f_a_wrong);
        problems = GraphVerification.verifyFactors(g);
        //System.out.println(problems);
        Assertions.assertEquals("[Node a is not in factor attached., Node a has extra variables in its attached factor: [b]]", "" + problems);

        a.setFactor(f_a);
        b.setFactor(f_b_wrong);
        problems = GraphVerification.verifyFactors(g);
        //System.out.println(problems);
        Assertions.assertEquals("[Node b has parent node a not in factor attached.]", "" + problems);

        b.setFactor(f_b);
        d.setFactor(f_d_wrong);
        problems = GraphVerification.verifyFactors(g);
        //System.out.println(problems);
        Assertions.assertEquals("[Node d has extra variables in its attached factor: [a]]", "" + problems);

        System.out.println("PASSED");
    }
}