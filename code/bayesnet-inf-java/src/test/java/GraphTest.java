import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-03
 */
class GraphTest {

    @Test
    void testBasic() {
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 2);

        Node a = new Node(v_a);
        Node b = new Node(v_b);
        a.addChild(b);
        b.addParent(a);
        // System.out.println("Node a: " + a);
        // System.out.println("Node b: " + b);

        Graph g1 = new Graph();
        g1.addNode(a);
        g1.addNode(b);
        // System.out.println("Graph: " + g);

        String graph_g1 =
            "digraph graphname {\n" +
            "\ta -> b\n" +
            "\tb\n" +
            "}";

        // System.out.println(DotUtils.toDot(g1));
        // DotUtils.printToFile("graph.dot", g1);
        System.out.print("Testing graph structure, graph g1: ");
        Assertions.assertEquals(graph_g1, DotUtils.toDot(g1));
        System.out.println("PASSED");

        DiscreteVariable v_c = new DiscreteVariable("c", 2);
        DiscreteVariable v_d = new DiscreteVariable("d", 2);

        Node c = new Node(v_c);
        Node d = new Node(v_d);

        Graph g2 = new Graph();
        c.addParent(b);
        b.addChild(c);
        b.addChild(d);
        d.addParent(b);
        g2.addNode(a);
        g2.addNode(b);
        g2.addNode(c);
        g2.addNode(d);

        String graph_g2 =
            "digraph graphname {\n" +
            "\ta -> b\n" +
            "\tb -> c\n" +
            "\tb -> d\n" +
            "\tc\n" +
            "\td\n" +
            "}";

        // System.out.println(DotUtils.toDot(g2));
        // DotUtils.printToFile("graph.dot", g2);
        System.out.print("Testing graph structure, graph g2: ");
        Assertions.assertEquals(graph_g2, DotUtils.toDot(g2));
        System.out.println("PASSED");
    }

}