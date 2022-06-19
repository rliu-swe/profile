import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-03
 */
class GraphUtilsTest {

    Function<Node, String> name_id = n -> "" + n.getName() + "(" + n.getId() + ")";

    @Test
    void printGraph() {

        GraphUtils.GraphStruct gs;
        Graph g;
        String graph;


        System.out.print("Testing printGraph: ");

        String g4_str =
            "x(0)->y(1)->a(2)->b(3)->c1(4)->d1(5)->e1(7)->f1(10)\n" +
            "                             ->d2(6)->e2(8)->f2(11)\n" +
            "                                    ->e3(9)->[loop: f1(10)]\n" +
            "                                           ->[loop: a(2)]\n" +
            "                      ->[loop: d2(6)]\n";
        NodeIdSupplier.NODE_ID = 0;
        gs = TestGraphs.createGraph_4();
        g = gs.graph;
        graph = GraphUtils.printGraph(g, name_id);
        //System.out.println(graph);
        Assertions.assertEquals(g4_str, graph);

        String g6_str =
            "a(0)\n" +
            "b(1)->c(2)->d(3)\n" +
            "e(4)\n" +
            "f(5)->g(6)\n";
        NodeIdSupplier.NODE_ID = 0;
        gs = TestGraphs.createGraph_6();
        g = gs.graph;
        graph = GraphUtils.printGraph(g, name_id);
        //System.out.println(graph);
        Assertions.assertEquals(g6_str, graph);

        System.out.println("PASSED");
    }

    @Test
    void printGraph_children() {
        NodeIdSupplier.NODE_ID = 0;

        GraphUtils.GraphStruct gs;
        Graph g;
        Node x;
        Node a;
        Node d2;
        String msg;

        gs = TestGraphs.createGraph_4();
        g = gs.graph;
        x = gs.nodes.get("x");
        a = gs.nodes.get("a");
        //DotUtils.printToFile("graph4.dot", g);

        String a_1 =
            "a->b->c1->d1->e1->f1\n" +
            "        ->d2->e2->f2\n" +
            "            ->e3->[loop: f1]\n" +
            "                ->[loop: a]\n" +
            "    ->[loop: d2]";
        String a_2 =
            "a(2)->b(3)->c1(4)->d1(5)->e1(7)->f1(10)\n" +
            "                 ->d2(6)->e2(8)->f2(11)\n" +
            "                        ->e3(9)->[loop: f1(10)]\n" +
            "                               ->[loop: a(2)]\n" +
            "          ->[loop: d2(6)]";

        String x_2 =
            "x(0)->y(1)->a(2)->b(3)->c1(4)->d1(5)->e1(7)->f1(10)\n" +
            "                             ->d2(6)->e2(8)->f2(11)\n" +
            "                                    ->e3(9)->[loop: f1(10)]\n" +
            "                                           ->[loop: a(2)]\n" +
            "                      ->[loop: d2(6)]";

        System.out.print("Testing printGraph_children: ");

        msg = GraphUtils.printGraph_children(a, Node::getName);
        //System.out.println(msg);
        Assertions.assertEquals(a_1, msg);

        msg = GraphUtils.printGraph_children(a, n -> "" + n.getName() + "(" + n.getId() + ")");
        //System.out.println(msg);
        Assertions.assertEquals(a_2, msg);

        msg = GraphUtils.printGraph_children(x, n -> "" + n.getName() + "(" + n.getId() + ")");
        //System.out.println(msg);
        Assertions.assertEquals(x_2,  msg);

        System.out.println("PASSED");
    }


    @Test
    void printGraph_parents() {
        NodeIdSupplier.NODE_ID = 0;

        GraphUtils.GraphStruct gs;
        Graph g;
        Node a;
        Node d2;
        Node f1;
        String msg;

        gs = TestGraphs.createGraph_4();
        g = gs.graph;
        a = gs.nodes.get("a");
        f1 = gs.nodes.get("f1");
        //DotUtils.printToFile("graph4.dot", g);

        String a_1 =
            "a->y->x\n" +
            " ->e3->d2->b->[loop: a]\n" +
            "         ->c1->[loop: b]";
        String a_2 =
            "a(2)->y(1)->x(0)\n" +
            "    ->e3(9)->d2(6)->b(3)->[loop: a(2)]\n" +
            "                  ->c1(4)->[loop: b(3)]";
        String f1_2 =
            "f1(10)->e1(7)->d1(5)->c1(4)->b(3)->a(2)->y(1)->x(0)\n" +
            "                                       ->e3(9)->d2(6)->[loop: b(3)]\n" +
            "                                                     ->[loop: c1(4)]\n" +
            "      ->[loop: e3(9)]";

        System.out.print("Testing printGraph_parents: ");

        msg = GraphUtils.printGraph_parents(a, Node::getName);
        //System.out.println(msg);
        Assertions.assertEquals(a_1, msg);

        msg = GraphUtils.printGraph_parents(a, n -> "" + n.getName() + "(" + n.getId() + ")");
        //System.out.println(msg);
        Assertions.assertEquals(a_2, msg);

        msg = GraphUtils.printGraph_parents(f1, n -> "" + n.getName() + "(" + n.getId() + ")");
        //System.out.println(msg);
        Assertions.assertEquals(f1_2, msg);

        System.out.println("PASSED");
    }


    @Test
    void testDeepCopyGraph() {

        GraphUtils.GraphStruct gs;
        Graph g;
        Graph g2;
        String orig_graph;
        String copy_graph;

        System.out.print("Testing GraphUtils.deepCopy, graph 5: ");

        String orig_graph_5 =
            "a(0)->b(1)->d(3)\n" +
                "    ->c(2)->[loop: d(3)]\n" +
                "          ->e(4)";
        String copy_graph_5 =
            "a(5)->b(6)->d(8)\n" +
                "    ->c(7)->[loop: d(8)]\n" +
                "          ->e(9)";
        NodeIdSupplier.NODE_ID = 0;
        gs = TestGraphs.createGraph_5();
        g = gs.graph;
        Node a = gs.nodes.get("a");
        g2 = GraphUtils.deepCopyGraph(g);
        Node a2 = g2.getNodes().stream().filter(_n -> _n.equals(a)).findFirst().get();
        orig_graph = GraphUtils.printGraph_children(a, name_id);
        copy_graph = GraphUtils.printGraph_children(a2, name_id);
        //System.out.println(orig_graph);
        //System.out.println(copy_graph);
        Assertions.assertEquals(orig_graph_5, orig_graph);
        Assertions.assertEquals(copy_graph_5, copy_graph);

        System.out.println("PASSED");



        System.out.print("Testing GraphUtils.deepCopy, graph 4: ");

        String orig_graph_4_x =
            "x(0)->y(1)->a(2)->b(3)->c1(4)->d1(5)->e1(7)->f1(10)\n" +
            "                             ->d2(6)->e2(8)->f2(11)\n" +
            "                                    ->e3(9)->[loop: f1(10)]\n" +
            "                                           ->[loop: a(2)]\n" +
            "                      ->[loop: d2(6)]";
        String copy_graph_4_x =
            "x(22)->y(23)->a(12)->b(13)->c1(14)->d1(15)->e1(17)->f1(20)\n" +
            "                                  ->d2(16)->e2(18)->f2(21)\n" +
            "                                          ->e3(19)->[loop: f1(20)]\n" +
            "                                                  ->[loop: a(12)]\n" +
            "                          ->[loop: d2(16)]";
        String orig_graph_4_a =
            "a(2)->b(3)->c1(4)->d1(5)->e1(7)->f1(10)\n" +
            "                 ->d2(6)->e2(8)->f2(11)\n" +
            "                        ->e3(9)->[loop: f1(10)]\n" +
            "                               ->[loop: a(2)]\n" +
            "          ->[loop: d2(6)]";
        String copy_graph_4_a =
            "a(12)->b(13)->c1(14)->d1(15)->e1(17)->f1(20)\n" +
            "                    ->d2(16)->e2(18)->f2(21)\n" +
            "                            ->e3(19)->[loop: f1(20)]\n" +
            "                                    ->[loop: a(12)]\n" +
            "            ->[loop: d2(16)]";
        NodeIdSupplier.NODE_ID = 0;
        gs = TestGraphs.createGraph_4();
        g = gs.graph;
        Node a_4 = gs.nodes.get("a");
        Node x = gs.nodes.get("x");
        g2 = GraphUtils.deepCopyGraph(g);

        // Test from x
        Node x2 = g2.getNodes().stream().filter(_n -> _n.equals(x)).findFirst().get();
        orig_graph = GraphUtils.printGraph_children(x, name_id);
        copy_graph = GraphUtils.printGraph_children(x2, name_id);
        //System.out.println(orig_graph);
        //System.out.println(copy_graph);
        Assertions.assertEquals(orig_graph_4_x, orig_graph);
        Assertions.assertEquals(copy_graph_4_x, copy_graph);

        // Test from a
        a2 = g2.getNodes().stream().filter(_n -> _n.equals(a_4)).findFirst().get();
        orig_graph = GraphUtils.printGraph_children(a_4, name_id);
        copy_graph = GraphUtils.printGraph_children(a2, name_id);
        //System.out.println(orig_graph);
        //System.out.println(copy_graph);
        Assertions.assertEquals(orig_graph_4_a, orig_graph);
        Assertions.assertEquals(copy_graph_4_a, copy_graph);

        System.out.println("PASSED");
    }

}