import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * Author: Nurrachman Liu   2022-03
 */
class VariableEliminationTest {


    /**
     * NB Test: order 1: Can sum-out immediately since only 1 factor mentions 'a'; 1 factor mentions 'b'; etc. Finally
     *                   by the time we've summed-out a, b, c, d, only 1 factor remains that mentions 'x'.
     *                   The width is the minimal which is 1.
     *          order 2: Can only sum-out after multiplying all factors that mention the var. Since all factors mention x,
     *                   we must keep their entire product before starting to sum-out x. Thus this is the maximum width
     *                   with the max width being the first in the trace.
     */
    @Test
    void testSimVE() {
        GraphUtils.GraphStruct g1_s = TestGraphs.createGraph_2();
        Graph g1 = g1_s.graph;
        DiscreteVariable g1_v_a = g1_s.vars.get("a");
        DiscreteVariable g1_v_c = g1_s.vars.get("c");
        DiscreteVariable g1_v_d = g1_s.vars.get("d");

        GraphUtils.GraphStruct gnb_s = TestGraphs.createGraph_NaiveBayes();
        Graph gnb = gnb_s.graph;
        DiscreteVariable gnb_v_x = gnb_s.vars.get("x");
        DiscreteVariable gnb_v_a = gnb_s.vars.get("a");
        DiscreteVariable gnb_v_b = gnb_s.vars.get("b");
        DiscreteVariable gnb_v_c = gnb_s.vars.get("c");
        DiscreteVariable gnb_v_d = gnb_s.vars.get("d");

        String trace_g1 = "[[b, d], [b, c], [a, b]]"; // marginalized-out-vars: d, c, a
        String trace_gnb_order1 = "[[x, a], [b, x], [c, x], [d, x], [x]]";
        String trace_gnb_order2 = "[[a, b, c, d, x], [a, b, c, d], [b, c, d], [c, d], [d]]";
        // The trace is also the name of the final factor
        String tracename_gnb_order1 = "(sum_{d}((sum_{c}((sum_{b}((sum_{a}(x.a))(x.b)))(x.c)))(x.d)))(x)";
        String tracename_gnb_order2 = "sum_{c}(sum_{b}(sum_{a}(sum_{x}(((((x)(x.a))(x.b))(x.c))(x.d)))))";

        int trace_g1_width = 2;
        int trace_gnb_order1_width = 2;
        int trace_gnb_order2_width = 5;

        System.out.print("Testing *Simulated* Variable Elimination: ");
        List<Factor> trace;
        int width;

        // Test g1
        trace = VariableElimination.SimVE(g1, Arrays.asList(g1_v_d, g1_v_c, g1_v_a));
        width = VariableElimination.getWidthOfSimVETrace(trace);
        //System.out.println(trace);
        //System.out.println(width);
        Assertions.assertEquals(trace_g1, "" + trace.stream().map(Factor::getVariableNames).toList());
        Assertions.assertEquals(trace_g1_width, width);

        // Test gnb, order 1
        trace = VariableElimination.SimVE(gnb, Arrays.asList(gnb_v_a, gnb_v_b, gnb_v_c, gnb_v_d, gnb_v_x));
        width = VariableElimination.getWidthOfSimVETrace(trace);
        //System.out.println(trace);
        //System.out.println(trace.get(trace.size()-1).getName());
        //System.out.println(width);
        Assertions.assertEquals(trace_gnb_order1, "" + trace.stream().map(Factor::getVariableNames).toList());
        Assertions.assertEquals(tracename_gnb_order1, "" + trace.get(trace.size()-1).getTraceName());
        Assertions.assertEquals(trace_gnb_order1_width, width);

        // Test gnb, order 2
        trace = VariableElimination.SimVE(gnb, Arrays.asList(gnb_v_x, gnb_v_a, gnb_v_b, gnb_v_c, gnb_v_d));
        width = VariableElimination.getWidthOfSimVETrace(trace);
        //System.out.println(trace);
        //System.out.println(trace.get(trace.size()-1).getName());
        //System.out.println(width);
        Assertions.assertEquals(trace_gnb_order2, "" + trace.stream().map(Factor::getVariableNames).toList());
        Assertions.assertEquals(tracename_gnb_order2, "" + trace.get(trace.size()-1).getTraceName());
        Assertions.assertEquals(trace_gnb_order2_width, width);

        System.out.println("PASSED");
    }

    @Test
    void testRunVE() {
        GraphUtils.GraphStruct gstruct = TestGraphs.createGraph_2();
        Graph g = gstruct.graph;
        Node a = gstruct.nodes.get("a");
        Node b = gstruct.nodes.get("b");
        Node c = gstruct.nodes.get("c");
        Node d = gstruct.nodes.get("d");
        DiscreteVariable v_a = gstruct.vars.get("a");
        DiscreteVariable v_b = gstruct.vars.get("b");
        DiscreteVariable v_c = gstruct.vars.get("c");
        DiscreteVariable v_d = gstruct.vars.get("d");
        Factor f_a = gstruct.factors.get("a");
        Factor f_b = gstruct.factors.get("b");
        Factor f_c = gstruct.factors.get("c");
        Factor f_d = gstruct.factors.get("d");


        Probability query;
        Factor result;

        String ve_result1 =
            "idx    b | Value\n" +
            "---------+--------\n" +
            "  0   b0   0.4500\n" +
            "  1   b1   0.1000\n";

        String ve_result2 =
            "idx    b    c | Value\n" +
            "--------------+--------\n" +
            "  0   b0   c0   0.4500\n" +
            "  1   b0   c1   0.4500\n" +
            "  2   b1   c0   0.0000\n" +
            "  3   b1   c1   0.1000\n";

        System.out.print("Testing Variable Elimination without pruning: ");

        // Test 1.1: Pr(b,c=1), with ve order: d, c, a
        query = new Probability.JointProb(Arrays.asList(v_b), Arrays.asList(v_c.instance(1)));
        result = VariableElimination.RunVE(g, Arrays.asList(v_d, v_c, v_a), query);
        //System.out.println(result.toTable());
        Assertions.assertEquals(ve_result1, result.toTable());

        // Test 1.2: Pr(b,c=1), with ve order: a, d, c. Should give same result as Test 1.1
        result = VariableElimination.RunVE(g, Arrays.asList(v_a, v_d, v_c), query);
        //System.out.println(result.toTable());
        Assertions.assertEquals(ve_result1, result.toTable());


        // Test 2: Pr(b,c) with ve-order a,d
        query = new Probability.JointProb(Arrays.asList(v_b, v_c));
        result = VariableElimination.RunVE(g, Arrays.asList(v_a, v_d), query);
        //System.out.println(result.toTable());
        Assertions.assertEquals(ve_result2, result.toTable());

        System.out.println("PASSED");
    }

    @Test
    void testPruneNetwork() {
        Probability query;
        Factor result;
        String graph_str;

        Function<Node, String> name_id = n -> "" + n.getName() + "(" + n.getId() + ")";

        System.out.print("Testing pruneNetwork: ");

        // Test Figure 6.1/6.9 pg 145
        String g5_pruned =
            "a(0)\n" +
            "b(1)->d(3)\n" +
            "c(2)\n";

        NodeIdSupplier.NODE_ID = 0;
        GraphUtils.GraphStruct gs = TestGraphs.createGraph_5();
        Graph g = gs.graph;
        DiscreteVariable v_a = gs.vars.get("a");
        DiscreteVariable v_c = gs.vars.get("c");
        DiscreteVariable v_d = gs.vars.get("d");

        query = new Probability.JointProb(Arrays.asList(v_d), Arrays.asList(v_a.instance(1), v_c.instance(0)));
        VariableElimination.pruneNetwork(g, query); // prunes network to fig 6.9 (a, c isolated, b->d)
        graph_str = GraphUtils.printGraph(g, name_id);
        //System.out.println(graph_str);
        Assertions.assertEquals(g5_pruned, graph_str);

        System.out.println("PASSED");

    }

    @Test
    void testRunVEWithPruning() {
        GraphUtils.GraphStruct gs = TestGraphs.createGraph_5();
        Graph g = gs.graph;
        Node a = gs.nodes.get("a");
        Node b = gs.nodes.get("b");
        Node c = gs.nodes.get("c");
        Node d = gs.nodes.get("d");
        Node e = gs.nodes.get("e");
        DiscreteVariable v_a = gs.vars.get("a");
        DiscreteVariable v_b = gs.vars.get("b");
        DiscreteVariable v_c = gs.vars.get("c");
        DiscreteVariable v_d = gs.vars.get("d");
        DiscreteVariable v_e = gs.vars.get("e");
        Factor f_a = gs.factors.get("a");
        Factor f_b = gs.factors.get("b");
        Factor f_c = gs.factors.get("c");
        Factor f_d = gs.factors.get("d");
        Factor f_e = gs.factors.get("e");

        Probability query;
        Factor result_wo_prune;
        Factor result_w_prune;

        System.out.print("Testing Variable Elimination with pruning: ");

        String result_d_tbl =
            "idx    d | Value\n" +
            "---------+--------\n" +
            "  0   d0   0.0984\n" +
            "  1   d1   0.0216\n";

        // Test 2.1: query={D}, e=A=t,C=f, with ve order: a, c, b
        query = new Probability.JointProb(Arrays.asList(v_d), Arrays.asList(v_a.instance(1), v_c.instance(0)));
        result_wo_prune = VariableElimination.RunVE(g, Arrays.asList(v_e, v_a, v_c, v_b), query);
        //System.out.println(result_wo_prune.toTable());
        Assertions.assertEquals(result_d_tbl, result_wo_prune.toTable());
        VariableElimination.pruneNetwork(g, query); // prunes network to fig 6.9 (a, c isolated, b->d)
        result_w_prune = VariableElimination.RunVE(g, Arrays.asList(v_a, v_c, v_b), query);
        //System.out.println(result_w_prune.toTable());
        Assertions.assertEquals(result_d_tbl, result_w_prune.toTable());

        System.out.println("PASSED");
    }


    @Test
    void testComputeTreeWidth() {
        GraphUtils.GraphStruct gs = TestGraphs.createGraph_5();
        Graph g = gs.graph;
        Map<List<DiscreteVariable>, Integer> tree_widths;
        String res;

        tree_widths = VariableElimination.computeTreeWidths(g);

        int count_3_cor = 48;
        int count_4_cor = 48;
        int count_5_cor = 24;


        System.out.print("Testing computing tree widths: ");
        StringBuilder sb = new StringBuilder();
        //for (Map.Entry<List<DiscreteVariable>, Integer> ent : tree_widths.entrySet()) {
        //    String vars = ent.getKey().stream().map(DiscreteVariable::getName).toList().toString();
        //    sb.append(vars).append(": ").append(ent.getValue()).append("\n");
        //}
        //res = sb.toString();
        //System.out.println(res);
        int count_3 = Collections.frequency(tree_widths.values(), 3);
        int count_4 = Collections.frequency(tree_widths.values(), 4);
        int count_5 = Collections.frequency(tree_widths.values(), 5);
        Assertions.assertEquals(count_3_cor, count_3);
        Assertions.assertEquals(count_4_cor, count_4);
        Assertions.assertEquals(count_5_cor, count_5);
        System.out.println("PASSED");
    }

    @Test
    void computeEffectiveTreeWidths() {
        GraphUtils.GraphStruct gs = TestGraphs.createGraph_5();
        Graph g = gs.graph;

        DiscreteVariable v_a = gs.vars.get("a");
        DiscreteVariable v_b = gs.vars.get("b");
        DiscreteVariable v_c = gs.vars.get("c");
        DiscreteVariable v_d = gs.vars.get("d");
        DiscreteVariable v_e = gs.vars.get("e");

        Map<List<DiscreteVariable>, Integer> tree_widths;
        String res;
        Probability query;

        query = new Probability.JointProb(Arrays.asList(v_d), Arrays.asList(v_a.instance(1), v_c.instance(0)));
        tree_widths = VariableElimination.computeEffectiveTreeWidths(g, query);

        int count_1_cor = 0;
        int count_2_cor = 24;
        int count_3_cor = 0;

        System.out.print("Testing effective computing tree widths: ");
        StringBuilder sb = new StringBuilder();
        //for (Map.Entry<List<DiscreteVariable>, Integer> ent : tree_widths.entrySet()) {
        //    String vars = ent.getKey().stream().map(DiscreteVariable::getName).toList().toString();
        //    sb.append(vars).append(": ").append(ent.getValue()).append("\n");
        //}
        //res = sb.toString();
        //System.out.println(res);
        int count_1 = Collections.frequency(tree_widths.values(), 1);
        int count_2 = Collections.frequency(tree_widths.values(), 2);
        int count_3 = Collections.frequency(tree_widths.values(), 3);
        //System.out.println(count_2);
        Assertions.assertEquals(count_1_cor, count_1);
        Assertions.assertEquals(count_2_cor, count_2);
        Assertions.assertEquals(count_3_cor, count_3);
        System.out.println("PASSED");
    }
}