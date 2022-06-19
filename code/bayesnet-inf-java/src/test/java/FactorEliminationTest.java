import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author rliu 2022-03
 */
class FactorEliminationTest {

    @Test
    void testFE1() {
        GraphUtils.GraphStruct gs = TestGraphs.createGraph_3();
        Graph g = gs.graph;
        DiscreteVariable c = gs.vars.get("c");

        String g3_result =
            "idx    c | Value\n" +
            "---------+--------\n" +
            "  0   c0   0.6240\n" +
            "  1   c1   0.3760\n";

        Factor result;

        System.out.print("Testing Factor Elimination FE1: ");
        result = FactorElimination.FE1(g, c);
        //System.out.println(result.toTable());
        Assertions.assertEquals(g3_result, result.toTable());
        System.out.println("PASSED");
    }

    @Test
    void testFE2() {
        Pair<GraphUtils.GraphStruct, List<EliminationTree>> gs_et = TestElimTrees.createElimTree_Graph5();
        GraphUtils.GraphStruct gs = gs_et.left;
        List<EliminationTree> et_lst = gs_et.right;

        EliminationTree et1 = et_lst.get(0);
        EliminationTree et2 = et_lst.get(1);

        Graph g = gs.graph;
        DiscreteVariable a = gs.vars.get("a");
        DiscreteVariable c = gs.vars.get("c");
        DiscreteVariable b = gs.vars.get("b");
        DiscreteVariable d = gs.vars.get("d");
        DiscreteVariable e = gs.vars.get("e");
        Probability query;

        Factor result_ve;
        Factor result_fe2;

        String result_ve_str =
            "idx    c    a | Value\n" +
            "--------------+--------\n" +
            "  0   c0   a0   0.3600\n" +
            "  1   c0   a1   0.1200\n" +
            "  2   c1   a0   0.0400\n" +
            "  3   c1   a1   0.4800\n";

        String result_fe2_str =
            "idx    a    c | Value\n" +
            "--------------+--------\n" +
            "  0   a0   c0   0.3600\n" +
            "  1   a0   c1   0.0400\n" +
            "  2   a1   c0   0.1200\n" +
            "  3   a1   c1   0.4800\n";

        query = new Probability.JointProb(Arrays.asList(a, c));

        System.out.print("Testing Factor Elimination FE2: ");
        result_ve = VariableElimination.RunVE(g, Arrays.asList(e, d, b), query);
        //System.out.println(result_ve.toTable());
        Assertions.assertEquals(result_ve_str, result_ve.toTable());
        result_fe2 = FactorElimination.FE2(et1, query);
        //System.out.println(result_fe2.toTable());
        Assertions.assertEquals(result_fe2_str, result_fe2.toTable());
        System.out.println("PASSED");
    }


    @Test
    void testFE3() {
        Pair<GraphUtils.GraphStruct, List<EliminationTree>> gs_et = TestElimTrees.createElimTree_Graph5();
        GraphUtils.GraphStruct gs = gs_et.left;
        List<EliminationTree> et_lst = gs_et.right;

        EliminationTree et1 = et_lst.get(0);
        EliminationTree et2 = et_lst.get(1);

        Graph g = gs.graph;
        DiscreteVariable a = gs.vars.get("a");
        DiscreteVariable c = gs.vars.get("c");
        DiscreteVariable b = gs.vars.get("b");
        DiscreteVariable d = gs.vars.get("d");
        DiscreteVariable e = gs.vars.get("e");
        Probability query;

        Factor result_ve;
        Factor result_fe3;

        String result_ve_str =
            "idx    c    a | Value\n" +
            "--------------+--------\n" +
            "  0   c0   a0   0.3600\n" +
            "  1   c0   a1   0.1200\n" +
            "  2   c1   a0   0.0400\n" +
            "  3   c1   a1   0.4800\n";

        String result_fe3_str =
            "idx    c    a | Value\n" +
                "--------------+--------\n" +
                "  0   c0   a0   0.3600\n" +
                "  1   c0   a1   0.1200\n" +
                "  2   c1   a0   0.0400\n" +
                "  3   c1   a1   0.4800\n";

        query = new Probability.JointProb(Arrays.asList(a, c));

        System.out.print("Testing Factor Elimination FE3: ");
        result_ve = VariableElimination.RunVE(g, Arrays.asList(e, d, b), query);
        //System.out.println(result_ve.toTable());
        Assertions.assertEquals(result_ve_str, result_ve.toTable());

        et1.setSeparators(EliminationTreeOperations.computeSeparators(et1));
        et1.setClusters(EliminationTreeOperations.computeClusters(et1));

        result_fe3 = FactorElimination.FE3(et1, query);
        //System.out.println(result_fe3.toTable());
        Assertions.assertEquals(result_fe3_str, result_fe3.toTable());

        System.out.println("PASSED");
    }


    @Test
    void testFE() {
        Pair<GraphUtils.GraphStruct, List<EliminationTree>> gs_et = TestElimTrees.createElimTree_Graph5();
        GraphUtils.GraphStruct gs = gs_et.left;
        List<EliminationTree> et_lst = gs_et.right;

        EliminationTree et1 = et_lst.get(0);
        EliminationTree et2 = et_lst.get(1);

        Graph g = gs.graph;
        DiscreteVariable a = gs.vars.get("a");
        DiscreteVariable c = gs.vars.get("c");
        DiscreteVariable b = gs.vars.get("b");
        DiscreteVariable d = gs.vars.get("d");
        DiscreteVariable e = gs.vars.get("e");

        Probability query;
        Map<Node, Factor> joint_marginals;
        Factor result_ve;
        Factor result_fe3;


        System.out.print("Testing Factor Elimination FE, joint marginals with no evidence: ");

        et1.setSeparators(EliminationTreeOperations.computeSeparators(et1));
        et1.setClusters(EliminationTreeOperations.computeClusters(et1));

        query = new Probability.JointProb(Arrays.asList(a, c));
        joint_marginals = FactorElimination.FE(EliminationTreeUtils.deepCopyEliminationTree(et1), query);

        // Compare every cluster marginal to the results obtained from FE3 and VE:
        for (Map.Entry<Node, Factor> ent : joint_marginals.entrySet()) {
            Node n = ent.getKey();
            Factor c_i = ent.getValue();   // joint marginal at cluster for node i

            Probability query2 = new Probability.JointProb(new ArrayList<>(et1.getClusters().get(n)));
            EliminationTree et1_copy = EliminationTreeUtils.deepCopyEliminationTree(et1);
            result_fe3 = FactorElimination.FE3(et1_copy, query2);

            Set<DiscreteVariable> ve_vars = g.getNodes().stream().map(Node::getVar).collect(Collectors.toSet());
            query2.getNonInstVars().forEach(ve_vars::remove);
            result_ve = VariableElimination.RunVE(g, new ArrayList<>(ve_vars), query2);

            c_i = FactorUtils.rearrangeVariables(c_i, query2.getNonInstVars());
            result_fe3 = FactorUtils.rearrangeVariables(result_fe3, query2.getNonInstVars());
            result_ve = FactorUtils.rearrangeVariables(result_ve, query2.getNonInstVars());

            //System.out.println("-------------");
            //System.out.println("Node: " + ent.getKey().getName());
            //System.out.println(c_i.toTable());
            //System.out.println(result_fe3.toTable());
            //System.out.println(result_ve.toTable());

            Assertions.assertEquals(result_fe3.toTable(), result_ve.toTable());
            Assertions.assertEquals(c_i.toTable(), result_fe3.toTable());
        }

//        System.out.println("Clusters:\n" + EliminationTreeUtils.printClusters(et1.getClusters()));
//        result_fe3 = FactorElimination.FE3(et1, query);
//        System.out.println(result_fe3.toTable());
//        System.out.println(FactorOperations.project(joint_marginals.get(gs.nodes.get("c")), Arrays.asList(a, c)).toTable());
//        System.out.println(FactorOperations.project(joint_marginals.get(gs.nodes.get("a")), Arrays.asList(a, c)).toTable());
//        Assertions.assertEquals("", "" + joint_marginals);


        // todo: test with evidence and evidence indicators

        System.out.println("PASSED");


        System.out.print("Testing Factor Elimination FE, joint marginals with evidence: ");

        query = new Probability.JointProb(List.of(d), Arrays.asList(a.instance(1), c.instance(0)));
        joint_marginals = FactorElimination.FE(EliminationTreeUtils.deepCopyEliminationTree(et1), query);

        String d_marginal_correct =
            "idx    d | Value\n" +
            "---------+--------\n" +
            "  0   d0   0.0984\n" +
            "  1   d1   0.0216\n";

        // Apply the evidence at every joint marginal factor:
        for (Map.Entry<Node, Factor> ent : joint_marginals.entrySet()) {
            Factor c_i = ent.getValue();   // joint marginal at cluster for node i
            c_i = FactorOperations.applyEvidence(c_i);
            joint_marginals.put(ent.getKey(), c_i);
        }

        Factor d_marginal = FactorOperations.project(joint_marginals.get(gs.nodes.get("d")), List.of(d));
        Assertions.assertEquals(d_marginal_correct, d_marginal.toTable());

        //System.out.println(d_marginal.toTable());

        System.out.println("PASSED");

    }
}