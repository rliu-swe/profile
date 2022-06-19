import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-03
 */
class JoinTreeUtilsTest {

    @Test
    void testComputeAllPaths() {
        Pair<GraphUtils.GraphStruct, List<JoinTreeUtils.JoinTreeStruct>> gs_jt = TestJoinTrees.createJoinTree_1();
        JoinTree jt1 = gs_jt.right.get(0).jointree;
        JoinTree jt2 = gs_jt.right.get(1).jointree;

        List<List<Cluster>> paths;
        String path_str;

        String jt1_paths =
            "[Cluster{vars=d.f.g}, Cluster{vars=a.d.f}]\n" +
            "[Cluster{vars=d.f.g}, Cluster{vars=a.d.f}, Cluster{vars=a.b.d}]\n" +
            "[Cluster{vars=d.f.g}, Cluster{vars=a.d.f}, Cluster{vars=a.e.f}]\n" +
            "[Cluster{vars=d.f.g}, Cluster{vars=a.d.f}, Cluster{vars=a.e.f}, Cluster{vars=a.c.e}]\n" +
            "[Cluster{vars=d.f.g}, Cluster{vars=a.d.f}, Cluster{vars=a.e.f}, Cluster{vars=e.f.h}]\n" +
            "[Cluster{vars=a.d.f}, Cluster{vars=a.b.d}]\n" +
            "[Cluster{vars=a.d.f}, Cluster{vars=a.e.f}]\n" +
            "[Cluster{vars=a.d.f}, Cluster{vars=a.e.f}, Cluster{vars=a.c.e}]\n" +
            "[Cluster{vars=a.d.f}, Cluster{vars=a.e.f}, Cluster{vars=e.f.h}]\n" +
            "[Cluster{vars=a.b.d}, Cluster{vars=a.d.f}, Cluster{vars=a.e.f}]\n" +
            "[Cluster{vars=a.b.d}, Cluster{vars=a.d.f}, Cluster{vars=a.e.f}, Cluster{vars=a.c.e}]\n" +
            "[Cluster{vars=a.b.d}, Cluster{vars=a.d.f}, Cluster{vars=a.e.f}, Cluster{vars=e.f.h}]\n" +
            "[Cluster{vars=a.e.f}, Cluster{vars=a.c.e}]\n" +
            "[Cluster{vars=a.e.f}, Cluster{vars=e.f.h}]\n" +
            "[Cluster{vars=a.c.e}, Cluster{vars=a.e.f}, Cluster{vars=e.f.h}]\n";

        String jt2_paths =
            "[Cluster{vars=d.f.g.h}, Cluster{vars=a.d.f.h}]\n" +
            "[Cluster{vars=d.f.g.h}, Cluster{vars=a.d.f.h}, Cluster{vars=a.b.d}]\n" +
            "[Cluster{vars=d.f.g.h}, Cluster{vars=a.d.f.h}, Cluster{vars=a.e.f.h}]\n" +
            "[Cluster{vars=d.f.g.h}, Cluster{vars=a.d.f.h}, Cluster{vars=a.e.f.h}, Cluster{vars=a.c.e}]\n" +
            "[Cluster{vars=d.f.g.h}, Cluster{vars=a.d.f.h}, Cluster{vars=a.e.f.h}, Cluster{vars=e.f.h}]\n" +
            "[Cluster{vars=a.d.f.h}, Cluster{vars=a.b.d}]\n" +
            "[Cluster{vars=a.d.f.h}, Cluster{vars=a.e.f.h}]\n" +
            "[Cluster{vars=a.d.f.h}, Cluster{vars=a.e.f.h}, Cluster{vars=a.c.e}]\n" +
            "[Cluster{vars=a.d.f.h}, Cluster{vars=a.e.f.h}, Cluster{vars=e.f.h}]\n" +
            "[Cluster{vars=a.b.d}, Cluster{vars=a.d.f.h}, Cluster{vars=a.e.f.h}]\n" +
            "[Cluster{vars=a.b.d}, Cluster{vars=a.d.f.h}, Cluster{vars=a.e.f.h}, Cluster{vars=a.c.e}]\n" +
            "[Cluster{vars=a.b.d}, Cluster{vars=a.d.f.h}, Cluster{vars=a.e.f.h}, Cluster{vars=e.f.h}]\n" +
            "[Cluster{vars=a.e.f.h}, Cluster{vars=a.c.e}]\n" +
            "[Cluster{vars=a.e.f.h}, Cluster{vars=e.f.h}]\n" +
            "[Cluster{vars=a.c.e}, Cluster{vars=a.e.f.h}, Cluster{vars=e.f.h}]\n";

        System.out.print("Testing JoinTreeUtils.computeAllPaths: ");

        paths = JoinTreeUtils.computeAllPaths(jt1);
        StringBuilder sb = new StringBuilder();
        paths.forEach(p -> sb.append(p).append("\n"));
        path_str = sb.toString();
        //System.out.println(path_str);
        Assertions.assertEquals(jt1_paths, path_str);

        paths = JoinTreeUtils.computeAllPaths(jt2);
        StringBuilder sb2 = new StringBuilder();
        paths.forEach(p -> sb2.append(p).append("\n"));
        path_str = sb2.toString();
        //System.out.println(path_str);
        Assertions.assertEquals(jt2_paths, path_str);

        System.out.println("PASSED");
    }


    @Test
    void testVerifyJoinTree() {
        Pair<GraphUtils.GraphStruct, List<JoinTreeUtils.JoinTreeStruct>> gs_jt = TestJoinTrees.createJoinTree_1();
        Graph graph = gs_jt.left.graph;
        JoinTree jt1 = gs_jt.right.get(0).jointree;
        JoinTree jt2 = gs_jt.right.get(1).jointree;
        Cluster jt1_c3 = gs_jt.right.get(0).clusters.get("c3");
        Cluster jt2_c3 = gs_jt.right.get(1).clusters.get("c3");

        DiscreteVariable a = gs_jt.left.vars.get("a");
        DiscreteVariable d = gs_jt.left.vars.get("d");
        DiscreteVariable f = gs_jt.left.vars.get("f");
        DiscreteVariable g = gs_jt.left.vars.get("f");

        List<String> errors;

        String jt1_c3_err =
            "Invalid jointree property: a cluster ([a, d]) along path from c_i ([d, f, g]) to c_j ([a, e, f]) does not contain vars_ij.\n" +
            "Invalid jointree property: a cluster ([a, d]) along path from c_i ([d, f, g]) to c_j ([e, f, h]) does not contain vars_ij.";


        System.out.print("Testing JoinTreeUtils.verifyJoinTree: ");

        errors = JoinTreeUtils.verifyJoinTree(graph, jt1);
        //System.out.println(errors);
        Assertions.assertEquals("[]", "" + errors);

        errors = JoinTreeUtils.verifyJoinTree(graph, jt2);
        //System.out.println(errors);
        Assertions.assertEquals("[]", "" + errors);

        // java map keys should not be mutated; therefore, pull it out and then put it back
        //   see:  https://stackoverflow.com/questions/10153063/changing-an-object-which-is-used-as-a-map-key
        Set<Cluster> neighs = jt1.getNeighbors().get(jt1_c3);
        Set<Separator> seps = jt1.getSeparators().get(jt1_c3);
        jt1_c3.setVars(new LinkedHashSet<>(List.of(a, d)));
        jt1.getNeighbors().put(jt1_c3, neighs);
        jt1.getSeparators().put(jt1_c3, seps);

        errors = JoinTreeUtils.verifyJoinTree(graph, jt1);
        //System.out.println(String.join("\n", errors));
        Assertions.assertEquals(jt1_c3_err, String.join("\n", errors));


        System.out.println("PASSED");

    }


}