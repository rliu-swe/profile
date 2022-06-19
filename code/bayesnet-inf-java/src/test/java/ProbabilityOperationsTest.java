import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Author: Nurrachman Liu   2022-03
 */
class ProbabilityOperationsTest {

    @Test
    void testComputeJointMarginalUsingVE() {
        Graph g;
        DiscreteVariable a;
        DiscreteVariable b;
        DiscreteVariable c;
        DiscreteVariable d;

        Factor result;

        GraphUtils.GraphStruct gstruct = TestGraphs.createGraph_2();
        g = gstruct.graph;
        a = gstruct.vars.get("a");
        b = gstruct.vars.get("b");
        c = gstruct.vars.get("c");
        d = gstruct.vars.get("d");

        Probability query = new Probability.JointProb(Arrays.asList(b), Arrays.asList(c.instance(1)));

        String g_jm =
            "idx    b | Value\n" +
            "---------+--------\n" +
            "  0   b0   0.4500\n" +
            "  1   b1   0.1000\n";

        System.out.print("Testing Joint Marginals: ");
        result = ProbabilityOperations.computeJointMarginalUsingVE(g, query);
        //System.out.println(result.toTable());
        Assertions.assertEquals(g_jm, result.toTable());
        System.out.println("PASSED");
    }

    @Test
    void testComputePosteriorMarginalUsingVE() {
        Graph g;
        DiscreteVariable a;
        DiscreteVariable b;
        DiscreteVariable c;
        DiscreteVariable d;

        Factor result;
        GraphUtils.GraphStruct gs;
        Probability query;

        gs = TestGraphs.createGraph_3();
        g = gs.graph;
        a = gs.vars.get("a");
        b = gs.vars.get("b");
        c = gs.vars.get("c");


        String g3_query1_pm =
            "idx    c | Value\n" +
            "---------+--------\n" +
            "  0   c0   0.6800\n" +
            "  1   c1   0.3200\n";

        String g3_query2_pm =
            "idx    c    a | Value\n" +
            "--------------+--------\n" +
            "  0   c0   a0   0.5400\n" +
            "  1   c0   a1   0.6800\n" +
            "  2   c1   a0   0.4600\n" +
            "  3   c1   a1   0.3200\n";

        System.out.print("Testing Posterior Marginals, graph 3: ");

        query = new Probability.CondProb(Arrays.asList(c), new ArrayList<>(), Arrays.asList(a.instance(1)));
        result = ProbabilityOperations.computePosteriorMarginalUsingVE(g, query);
        //System.out.println(result.toTable());
        Assertions.assertEquals(g3_query1_pm, result.toTable());

        query = new Probability.CondProb(Arrays.asList(c), Arrays.asList(a));
        result = ProbabilityOperations.computePosteriorMarginalUsingVE(g, query);
        //System.out.println(result.toTable());
    Assertions.assertEquals(g3_query2_pm, result.toTable());

        System.out.println("PASSED");



        gs = TestGraphs.createGraph_2();
        g = gs.graph;
        a = gs.vars.get("a");
        b = gs.vars.get("b");
        c = gs.vars.get("c");
        d = gs.vars.get("d");

        String g2_query1_pm =
            "idx    b | Value\n" +
            "---------+--------\n" +
            "  0   b0   0.8182\n" +
            "  1   b1   0.1818\n";

        System.out.print("Testing Posterior Marginals, graph 2: ");

        query = new Probability.CondProb(Arrays.asList(b), new ArrayList<>(), Arrays.asList(c.instance(1)));
        result = ProbabilityOperations.computePosteriorMarginalUsingVE(g, query);
        //System.out.println(result.toTable());
        Assertions.assertEquals(g2_query1_pm, result.toTable());

        System.out.println("PASSED");
    }

}