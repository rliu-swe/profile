import java.util.Arrays;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Contains the test graphs.
 *
 * Author: Nurrachman Liu   2022-03
*/
public class TestGraphs {

    /**
     * Graph:       a
     *            /  \
     *           c    b
     *         /  \  /
     *        e     d
     */
    public static GraphUtils.GraphStruct createGraph_1() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 2);
        DiscreteVariable d = new DiscreteVariable("d", 2);
        DiscreteVariable e = new DiscreteVariable("e", 2);

        Node n_a = new Node(a);
        Node n_b = new Node(b);
        Node n_c = new Node(c);
        Node n_d = new Node(d);
        Node n_e = new Node(e);

        Graph g = new Graph();
        g.addNode(n_a);
        g.addNode(n_b);
        g.addNode(n_c);
        g.addNode(n_d);
        g.addNode(n_e);

        n_a.addChild(n_b);
        n_b.addParent(n_a);
        n_a.addChild(n_c);
        n_c.addParent(n_a);
        n_d.addParent(n_b);
        n_d.addParent(n_c);
        n_b.addChild(n_d);
        n_c.addChild(n_d);
        n_e.addParent(n_c);
        n_c.addChild(n_e);

        Factor f_a = new Factor(Arrays.asList(a));
        Factor f_b = new Factor(Arrays.asList(a, b));
        Factor f_c = new Factor(Arrays.asList(a, c));
        Factor f_d = new Factor(Arrays.asList(b, c, d));
        Factor f_e = new Factor(Arrays.asList(c, e));

        n_a.setFactor(f_a);
        n_b.setFactor(f_b);
        n_c.setFactor(f_c);
        n_d.setFactor(f_d);
        n_e.setFactor(f_e);

        return new GraphUtils.GraphStruct(g, Arrays.asList(n_a, n_b, n_c, n_d, n_e), Arrays.asList(a, b, c, d, e));
    }


    /**
     * Graph:    a -> b -> c
     *                \-> d
     */
    public static GraphUtils.GraphStruct createGraph_2() {
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 2);
        DiscreteVariable v_c = new DiscreteVariable("c", 2);
        DiscreteVariable v_d = new DiscreteVariable("d", 2);

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

        f_a.setRowValuesByValues(Arrays.asList(0.8, 0.2));
        f_b.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.5, 0.5));
        f_c.setRowValuesByValues(Arrays.asList(0.5, 0.5, 0.0, 1.0));
        f_d.setRowValuesByValues(Arrays.asList(0.7, 0.3, 0.25, 0.75));

        a.setFactor(f_a);
        b.setFactor(f_b);
        c.setFactor(f_c);
        d.setFactor(f_d);

        return new GraphUtils.GraphStruct(g, Arrays.asList(a, b, c, d), Arrays.asList(v_a, v_b, v_c, v_d));
    }


    /**
     * Graph:  x -> a
     *          |_> b
     *          |_> c
     *          |_> d
     */
    public static GraphUtils.GraphStruct createGraph_NaiveBayes() {
        DiscreteVariable v_x = new DiscreteVariable("x", 2);
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 2);
        DiscreteVariable v_c = new DiscreteVariable("c", 2);
        DiscreteVariable v_d = new DiscreteVariable("d", 2);

        Node x = new Node(v_x);
        Node a = new Node(v_a);
        Node b = new Node(v_b);
        Node c = new Node(v_c);
        Node d = new Node(v_d);

        for (Node n : Arrays.asList(a, b, c, d)) {
            x.addChild(n);
            n.addParent(x);
        }

        Graph g = new Graph();
        g.addNode(x);
        for (Node n : Arrays.asList(a, b, c, d)) {
            g.addNode(n);
        }


        Factor f_x = new Factor(Arrays.asList(v_x));
        Factor f_a = new Factor(Arrays.asList(v_x, v_a));
        Factor f_b = new Factor(Arrays.asList(v_x, v_b));
        Factor f_c = new Factor(Arrays.asList(v_x, v_c));
        Factor f_d = new Factor(Arrays.asList(v_x, v_d));

        f_x.setRowValuesByValues(Arrays.asList(0.2, 0.8));
        f_a.setRowValuesByValues(Arrays.asList(0.5, 0.5, 0.0, 1.0));
        f_b.setRowValuesByValues(Arrays.asList(0.5, 0.5, 0.0, 1.0));
        f_c.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.5, 0.5));
        f_d.setRowValuesByValues(Arrays.asList(0.75, 0.25, 0.3, 0.7));

        x.setFactor(f_x);
        a.setFactor(f_a);
        b.setFactor(f_b);
        c.setFactor(f_c);
        d.setFactor(f_d);

        return new GraphUtils.GraphStruct(g, Arrays.asList(x, a, b, c, d), Arrays.asList(v_x, v_a, v_b, v_c, v_d));
    }


    /**
     * Matches graph on page 132/140 of Darwiche..
     * Graph:    a -> b -> c
     */
    public static GraphUtils.GraphStruct createGraph_3() {
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 2);
        DiscreteVariable v_c = new DiscreteVariable("c", 2);

        Node a = new Node(v_a);
        Node b = new Node(v_b);
        Node c = new Node(v_c);

        a.addChild(b);
        b.addParent(a);
        c.addParent(b);
        b.addChild(c);

        Graph g = new Graph();
        g.addNode(a);
        g.addNode(b);
        g.addNode(c);

        Factor f_a = new Factor(Arrays.asList(v_a));
        Factor f_b = new Factor(Arrays.asList(v_a, v_b));
        Factor f_c = new Factor(Arrays.asList(v_b, v_c));

        f_a.setRowValuesByValues(Arrays.asList(0.4, 0.6));
        f_b.setRowValuesByValues(Arrays.asList(0.8, 0.2, 0.1, 0.9));
        f_c.setRowValuesByValues(Arrays.asList(0.5, 0.5, 0.7, 0.3));

        a.setFactor(f_a);
        b.setFactor(f_b);
        c.setFactor(f_c);

        return new GraphUtils.GraphStruct(g, Arrays.asList(a, b, c), Arrays.asList(v_a, v_b, v_c));
    }

    /**
     * For testing recursive copy updates of children or parents.
     * Graph:    a -> b -> c1 -> d1 -> e1 -> f1 <-
     *           ^    |     |_> d2 -> e2 -> f2   /
     *           |    |_______/   \_> e3________/
     *           \                    |
     *             __________________/
     */
    public static GraphUtils.GraphStruct createGraph_4() {
        DiscreteVariable v_x = new DiscreteVariable("x", 2);
        DiscreteVariable v_y = new DiscreteVariable("y", 2);
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 2);
        DiscreteVariable v_c1 = new DiscreteVariable("c1", 2);
        DiscreteVariable v_d1 = new DiscreteVariable("d1", 2);
        DiscreteVariable v_d2 = new DiscreteVariable("d2", 2);
        DiscreteVariable v_e1 = new DiscreteVariable("e1", 2);
        DiscreteVariable v_e2 = new DiscreteVariable("e2", 2);
        DiscreteVariable v_e3 = new DiscreteVariable("e3", 2);
        DiscreteVariable v_f1 = new DiscreteVariable("f1", 2);
        DiscreteVariable v_f2 = new DiscreteVariable("f2", 2);

        Node x = new Node(v_x);
        Node y = new Node(v_y);
        Node a = new Node(v_a);
        Node b = new Node(v_b);
        Node c1 = new Node(v_c1);
        Node d1 = new Node(v_d1);
        Node d2 = new Node(v_d2);
        Node e1 = new Node(v_e1);
        Node e2 = new Node(v_e2);
        Node e3 = new Node(v_e3);
        Node f1 = new Node(v_f1);
        Node f2 = new Node(v_f2);

        /**
         * Graph: x -> y ->  a -> b -> c1 -> d1 -> e1 -> f1 <-
         *                   ^    |     |_> d2 -> e2 -> f2   /
         *                   |    |_______/   \_> e3________/
         *                   \                    |
         *                     __________________/
         */

        BiConsumer<Node, Node> link = (_a, _b) -> {
            _a.addChild(_b);
            _b.addParent(_a);
        };

        // Row 1
        link.accept(x, y);
        link.accept(y, a);
        link.accept(a, b);
        link.accept(b, c1);
        link.accept(c1, d1);
        link.accept(d1, e1);
        link.accept(e1, f1);
        // Row 2
        link.accept(b, d2);
        link.accept(c1, d2);
        link.accept(d2, e2);
        link.accept(e2, f2);
        // Row 3
        link.accept(d2, e3);
        link.accept(e3, f1);
        // Row 4
        link.accept(e3, a);

        Graph g = new Graph();
        g.addNodes(Set.of(x, y, a, b, c1, d1, d2, e1, e2, e3, f1, f2));

        return new GraphUtils.GraphStruct(g,
            Arrays.asList(x, y, a, b, c1, d1, d2, e1, e2, e3, f1, f2),
            Arrays.asList(v_x, v_y, v_a, v_b, v_c1, v_d1, v_d2, v_e1, v_e2, v_e3, v_f1, v_f2));
    }


    /**
     * Matches graph on Fig 6.1 (page 127) of Darwiche, for showing pruning on Fig 6.8/6.9 (page 145)
     * Graph:        a
     *            /    \
     *           b      c
     *            \    / \
     *              d     e
     */
    public static GraphUtils.GraphStruct createGraph_5() {
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 2);
        DiscreteVariable v_c = new DiscreteVariable("c", 2);
        DiscreteVariable v_d = new DiscreteVariable("d", 2);
        DiscreteVariable v_e = new DiscreteVariable("e", 2);

        Node a = new Node(v_a);
        Node b = new Node(v_b);
        Node c = new Node(v_c);
        Node d = new Node(v_d);
        Node e = new Node(v_e);

        a.linkChild(b);
        a.linkChild(c);
        b.linkChild(d);
        c.linkChild(d);
        c.linkChild(e);

        Graph g = new Graph();
        g.addNodes(Set.of(a, b, c, d, e));

        Factor f_a = new Factor(Arrays.asList(v_a));
        Factor f_b = new Factor(Arrays.asList(v_a, v_b));
        Factor f_c = new Factor(Arrays.asList(v_a, v_c));
        Factor f_d = new Factor(Arrays.asList(v_b, v_c, v_d));
        Factor f_e = new Factor(Arrays.asList(v_c, v_e));

        f_a.setRowValuesByValues(Arrays.asList(0.4, 0.6));
        f_b.setRowValuesByValues(Arrays.asList(0.25, 0.75, 0.8, 0.2));
        f_c.setRowValuesByValues(Arrays.asList(0.9, 0.1, 0.2, 0.8));
        f_d.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.2, 0.8, 0.1, 0.9, 0.05, 0.95));
        f_e.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.3, 0.7));

        a.setFactor(f_a);
        b.setFactor(f_b);
        c.setFactor(f_c);
        d.setFactor(f_d);
        e.setFactor(f_e);

        return new GraphUtils.GraphStruct(g, Arrays.asList(a, b, c, d, e), Arrays.asList(v_a, v_b, v_c, v_d, v_e));
    }


    /**
     * A BN with some isolated islands, for testing things work correctly with separated nodes.
     * Graph:   a
     *          b->c->d
     *          e
     *          f->g
     */
    public static GraphUtils.GraphStruct createGraph_6() {
        DiscreteVariable v_a = new DiscreteVariable("a", 2);
        DiscreteVariable v_b = new DiscreteVariable("b", 2);
        DiscreteVariable v_c = new DiscreteVariable("c", 2);
        DiscreteVariable v_d = new DiscreteVariable("d", 2);
        DiscreteVariable v_e = new DiscreteVariable("e", 2);
        DiscreteVariable v_f = new DiscreteVariable("f", 2);
        DiscreteVariable v_g = new DiscreteVariable("g", 2);

        Node a = new Node(v_a);
        Node b = new Node(v_b);
        Node c = new Node(v_c);
        Node d = new Node(v_d);
        Node e = new Node(v_e);
        Node f = new Node(v_f);
        Node g = new Node(v_g);

        b.linkChild(c);
        c.linkChild(d);
        f.linkChild(g);

        Graph graph = new Graph();
        graph.addNodes(Set.of(a, b, c, d, e, f, g));

        return new GraphUtils.GraphStruct(graph,
            Arrays.asList(a, b, c, d, e, f, g),
            Arrays.asList(v_a, v_b, v_c, v_d, v_e, v_f, v_g));
    }

}
