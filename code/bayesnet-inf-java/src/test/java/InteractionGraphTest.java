import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-03
 */
class InteractionGraphTest {

    @Test
    void testBasic() {

        GraphUtils.GraphStruct g1_s = TestGraphs.createGraph_1();
        Graph g1 = g1_s.graph;
        DiscreteVariable a = g1_s.vars.get("a");
        DiscreteVariable b = g1_s.vars.get("b");
        DiscreteVariable c = g1_s.vars.get("c");
        DiscreteVariable d = g1_s.vars.get("d");
        DiscreteVariable e = g1_s.vars.get("e");

        InteractionGraph ig1 = new InteractionGraph(g1);

        String ig1_str =
            "graph graphname {\n" +
            "\ta -- b\n" +
            "\ta -- c\n" +
            "\tb -- c\n" +
            "\tb -- d\n" +
            "\tc -- d\n" +
            "\tc -- e\n" +
            "}";

        System.out.print("Testing Interaction-Graph structure: ");
        //System.out.println(DotUtils.toDot(g));
        //DotUtils.printToFile("graph.dot", g);
        //System.out.println(DotUtils.toDot(ig));
        //DotUtils.printToFile("interaction-graph.dot", ig);
        Assertions.assertEquals(ig1_str, DotUtils.toDot(ig1));
        System.out.println("PASSED");

    }

}