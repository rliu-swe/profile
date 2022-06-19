import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * Author: Nurrachman Liu   2022-03
 */
class InteractionGraphOperationsTest {


    @Test
    void testEliminateVariables() {

        GraphUtils.GraphStruct g1_s = TestGraphs.createGraph_1();
        Graph g = g1_s.graph;
        DiscreteVariable a = g1_s.vars.get("a");
        DiscreteVariable b = g1_s.vars.get("b");
        DiscreteVariable c = g1_s.vars.get("c");
        DiscreteVariable d = g1_s.vars.get("d");
        DiscreteVariable e = g1_s.vars.get("e");


        InteractionGraph ig_orig = new InteractionGraph(g);
        InteractionGraph ig; // ig var elim modifies the ig; we run igve on a copy of the ig
        List<Integer> widths = new ArrayList<>();

        String ig_b =
            "graph graphname {\n" +
                "\ta -- c\n" +
                "\ta -- d\n" +
                "\tc -- d\n" +
                "\tc -- e\n" +
                "}";

        String ig_bc =
            "graph graphname {\n" +
                "\ta -- d\n" +
                "\ta -- e\n" +
                "\td -- e\n" +
                "}";

        String ig_bca =
            "graph graphname {\n" +
                "\td -- e\n" +
                "}";

        String ig_bcad =
            "graph graphname {\n" +
                "\te\n" +
                "}";


        System.out.print("Testing InteractionGraphOperations eliminateVariables: ");

        //System.out.println(DotUtils.toDot(ig));
        ig = new InteractionGraph(ig_orig);
        widths = InteractionGraphOperations.eliminateVariables(ig, Arrays.asList(b));
        //System.out.println(DotUtils.toDot(ig));
        //DotUtils.printToFile("interaction-graph.dot", ig);
        //System.out.println(widths);
        Assertions.assertEquals(ig_b, DotUtils.toDot(ig));
        Assertions.assertEquals("[3]", "" + widths);

        //System.out.println(DotUtils.toDot(ig));
        ig = new InteractionGraph(ig_orig);
        widths = InteractionGraphOperations.eliminateVariables(ig, Arrays.asList(b, c));
        //System.out.println(DotUtils.toDot(ig));
        //DotUtils.printToFile("interaction-graph.dot", ig);
        //System.out.println(widths);
        Assertions.assertEquals(ig_bc, DotUtils.toDot(ig));
        Assertions.assertEquals("[3, 3]", "" + widths);

        //System.out.println(DotUtils.toDot(ig));
        ig = new InteractionGraph(ig_orig);
        widths = InteractionGraphOperations.eliminateVariables(ig, Arrays.asList(b, c, a));
        //System.out.println(DotUtils.toDot(ig));
        //DotUtils.printToFile("interaction-graph.dot", ig);
        //System.out.println(widths);
        Assertions.assertEquals(ig_bca, DotUtils.toDot(ig));
        Assertions.assertEquals("[3, 3, 2]", "" + widths);

        //System.out.println(DotUtils.toDot(ig));
        ig = new InteractionGraph(ig_orig);
        widths = InteractionGraphOperations.eliminateVariables(ig, Arrays.asList(b, c, a, d));
        //System.out.println(DotUtils.toDot(ig));
        //DotUtils.printToFile("interaction-graph.dot", ig);
        //System.out.println(widths);
        Assertions.assertEquals(ig_bcad, DotUtils.toDot(ig));
        Assertions.assertEquals("[3, 3, 2, 1]", "" + widths);
        System.out.println("PASSED");
    }


    @Test
    void testGetMinDegreeHeuristic() {
        GraphUtils.GraphStruct gs;
        Graph g;
        InteractionGraph ig;
        DiscreteVariable a;
        DiscreteVariable b;
        DiscreteVariable c;
        DiscreteVariable d;
        DiscreteVariable e;

        Probability query;
        List<DiscreteVariable> veorder_mindegree;

        Pattern g1_mindegree_pat = Pattern.compile("\\[e, (a|d),.+\\]"); // e:1; (a|d):2; then b, c or (d|a)

        System.out.print("Testing InteractionGraphOperations minDegree heuristic, graph1: ");

        gs = TestGraphs.createGraph_1();
        g = gs.graph;
        veorder_mindegree = InteractionGraphOperations.getMinDegreeHeuristic(g);
        //ig = new InteractionGraph(g);
        //System.out.println(DotUtils.toDot(ig));
        //DotUtils.printToFile("interaction-graph.dot", ig);
        Assertions.assertEquals(true,
            g1_mindegree_pat.matcher("" + veorder_mindegree.stream().map(DiscreteVariable::getName).toList()).matches());

        System.out.println("PASSED");


        System.out.print("Testing InteractionGraphOperations minDegree heuristic, graph5 (Fig 6.1, 6.9): ");

        Pattern g2_mindegree_pat = Pattern.compile("\\[a, (b|c),.+\\]"); // a:2, (b|c):3->2

        gs = TestGraphs.createGraph_5();
        g = gs.graph;
        a = gs.vars.get("a");
        c = gs.vars.get("c");
        d = gs.vars.get("d");
        query = new Probability.JointProb(Arrays.asList(d), Arrays.asList(a.instance(1), c.instance(0)));
        VariableElimination.pruneNetwork(g, query);
        veorder_mindegree = InteractionGraphOperations.getMinDegreeHeuristic(g, query);
        ig = new InteractionGraph(g);
        //System.out.println(DotUtils.toDot(ig));
        //DotUtils.printToFile("interaction-graph.dot", ig);
        //System.out.println(veorder_mindegree.stream().map(DiscreteVariable::getName).toList());
        Assertions.assertEquals(true,
            g2_mindegree_pat.matcher("" + veorder_mindegree.stream().map(DiscreteVariable::getName).toList()).matches());

        System.out.println("PASSED");

    }

    @Test
    void testGetMinFillHeuristic() {
        GraphUtils.GraphStruct g1_s = TestGraphs.createGraph_1();
        Graph g1 = g1_s.graph;
        DiscreteVariable g1_a = g1_s.vars.get("a");
        DiscreteVariable g1_b = g1_s.vars.get("b");
        DiscreteVariable g1_c = g1_s.vars.get("c");
        DiscreteVariable g1_d = g1_s.vars.get("d");
        DiscreteVariable g1_e = g1_s.vars.get("e");

        List<DiscreteVariable> veorder_minfill;
        List<String> veorder_minfill_names;

        Pattern veorder_minfill_pat = Pattern.compile("\\[.+(c, (a|b|d|e)|(a|b|d|e), c)\\]"); // c must be last or 2nd to last
        Pattern veorder_minfill_allvars_pat = Pattern.compile("\\[e, b, c\\]"); // c must be last or 2nd to last

        System.out.print("Testing InteractionGraphOperations minFill heuristic: ");

        veorder_minfill = InteractionGraphOperations.getMinFillHeuristic(g1);
        //InteractionGraph ig1 = new InteractionGraph(g1);
        //System.out.println(DotUtils.toDot(ig1));
        //DotUtils.printToFile("interaction-graph.dot", ig1);
        veorder_minfill_names = veorder_minfill.stream().map(DiscreteVariable::getName).toList();
        //System.out.println(veorder_minfill_names);
        Assertions.assertEquals(true,
            veorder_minfill_pat.matcher("" + veorder_minfill_names).matches());

        Set<DiscreteVariable> vars = Set.of(g1_c, g1_b, g1_e);  // order should be: e, b, c
        veorder_minfill = InteractionGraphOperations.getMinFillHeuristic(g1, vars);
        //InteractionGraph ig1 = new InteractionGraph(g1);
        //System.out.println(DotUtils.toDot(ig1));
        //DotUtils.printToFile("interaction-graph.dot", ig1);
        veorder_minfill_names = veorder_minfill.stream().map(DiscreteVariable::getName).toList();
        //System.out.println(veorder_minfill_names);
        Assertions.assertEquals(true,
            veorder_minfill_allvars_pat.matcher("" + veorder_minfill_names).matches());

        System.out.println("PASSED");
    }


}