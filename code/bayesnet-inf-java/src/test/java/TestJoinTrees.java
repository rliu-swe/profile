import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Contains join-trees for testing.
 *
 * @author rliu 2022-03
*/
public class TestJoinTrees {


    /**
     * Returns two jointrees shown on page 165, Fig 7.12.
     *
     * Graph:            a
     *             /  /  |  \  \
     *           b   /   |   \  c
     *           |  /    |    \ |
     *            d      |     e
     *            |      f     |
     *            |   /     \  |
     *              g         h
     *
     * Joint-Tree 1:    1 DFG         2 ACE
     *                  |             |
     *                  | DF          | AE
     *                  |     AF      |
     *              ADF 3 ----------- 4 AEF
     *                  |             |
     *                  | AD          | EF
     *                  |             |
     *              ABD 5             6 EFH
     *
     * Joint-Tree 2:    1 DFGH        2 ACE
     *                  |             |
     *                  | DFH         | AE
     *                  |     AFH     |
     *             ADFH 3 ----------- 4 AEFH
     *                  |             |
     *                  | AD          | EFH
     *                  |             |
     *              ABD 5             6 EFH
     *
     */
    public static Pair<GraphUtils.GraphStruct, List<JoinTreeUtils.JoinTreeStruct>> createJoinTree_1() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 2);
        DiscreteVariable d = new DiscreteVariable("d", 2);
        DiscreteVariable e = new DiscreteVariable("e", 2);
        DiscreteVariable f = new DiscreteVariable("f", 2);
        DiscreteVariable g = new DiscreteVariable("g", 2);
        DiscreteVariable h = new DiscreteVariable("h", 2);

        Node n_a = new Node(a);
        Node n_b = new Node(b);
        Node n_c = new Node(c);
        Node n_d = new Node(d);
        Node n_e = new Node(e);
        Node n_f = new Node(f);
        Node n_g = new Node(g);
        Node n_h = new Node(h);

        Factor f_a = new Factor("a", List.of(a));
        Factor f_b = new Factor("ab", List.of(a, b));
        Factor f_c = new Factor("ac", List.of(a, c));
        Factor f_d = new Factor("abd", List.of(a, b, d));
        Factor f_e = new Factor("ace", List.of(a, c, e));
        Factor f_f = new Factor("af", List.of(a, f));
        Factor f_g = new Factor("dfg", List.of(d, f , g));
        Factor f_h = new Factor("efh", List.of(e, f , h));

        n_a.setFactor(f_a);
        n_b.setFactor(f_b);
        n_c.setFactor(f_c);
        n_d.setFactor(f_d);
        n_e.setFactor(f_e);
        n_f.setFactor(f_f);
        n_g.setFactor(f_g);
        n_h.setFactor(f_h);

        Graph graph = new Graph(Set.of(n_a, n_b, n_c, n_d, n_e, n_f, n_g, n_h));

        GraphUtils.GraphStruct gs = new GraphUtils.GraphStruct(
            graph,
            List.of(n_a, n_b, n_c, n_d, n_e, n_f, n_g, n_h),
            List.of(a, b, c, d, e, f, g, h));

        // for join-trees, the nodes represent clusters rather than variables, so we'll create new Nodes:
        Cluster c1 = new Cluster(List.of(d, f, g));
        Cluster c2 = new Cluster(List.of(a, c, e));
//        Cluster c3 = new Cluster(List.of(a, d)); //, f));
        Cluster c3 = new Cluster(List.of(a, d, f));
        Cluster c4 = new Cluster(List.of(a, e, f));
        Cluster c5 = new Cluster(List.of(a, b, d));
        Cluster c6 = new Cluster(List.of(e, f, h));

        Separator s13 = new Separator(List.of(d, f));
        Separator s35 = new Separator(List.of(a, d));
        Separator s34 = new Separator(List.of(a, f));
        Separator s24 = new Separator(List.of(a, e));
        Separator s46 = new Separator(List.of(e, f));

        JoinTree jt1 = new JoinTree();
        jt1.link(c1, c3, s13);
        jt1.link(c3, c5, s35);
        jt1.link(c3, c4, s34);
        jt1.link(c4, c2, s24);
        jt1.link(c4, c6, s46);

        Cluster c1_2 = new Cluster(List.of(d, f, g, h));
        Cluster c3_2 = new Cluster(List.of(a, d, f, h));
        Cluster c4_2 = new Cluster(List.of(a, e, f, h));
        Separator s13_2 = new Separator(List.of(d, f,h));
        Separator s34_2 = new Separator(List.of(a, f, h));
        Separator s46_2 = new Separator(List.of(e, f, h));

        JoinTree jt2 = new JoinTree();
        jt2.link(c1_2, c3_2, s13_2);
        jt2.link(c3_2, c5, s35);
        jt2.link(c3_2, c4_2, s34_2);
        jt2.link(c4_2, c2, s24);
        jt2.link(c4_2, c6, s46_2);

        return Pair.of(gs,
            Arrays.asList(
                new JoinTreeUtils.JoinTreeStruct(
                    jt1,
                    Arrays.asList("c1", "c2", "c3", "c4", "c5", "c6"), Arrays.asList(c1, c2, c3, c4, c5, c6),
                    Arrays.asList("s13", "s35", "s34", "s24", "s46"), Arrays.asList(s13, s35, s34, s24, s46)),
                new JoinTreeUtils.JoinTreeStruct(
                    jt2,
                    Arrays.asList("c1", "c2", "c3", "c4", "c5", "c6"), Arrays.asList(c1_2, c2, c3_2, c4_2, c5, c6),
                    Arrays.asList("s13", "s35", "s34", "s24", "s46"), Arrays.asList(s13_2, s35, s34_2, s24, s46_2))
            )
        );
    }

}
