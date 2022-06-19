import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-03
 */
class NodeUtilsTest {

    @Test
    void testRecursiveUpdateChildren() {
        NodeIdSupplier.NODE_ID = 0;

        GraphUtils.GraphStruct gs;
        Graph g;
        Node a;
        Node d2;

        gs = TestGraphs.createGraph_4();
        g = gs.graph;
        a = gs.nodes.get("a");
        //DotUtils.printToFile("graph4.dot", g);

        Function<Node, String> name_id = n -> "" + n.getName() + "(" + n.getId() + ")";

        String a_orig_graph =
            "a(2)->b(3)->c1(4)->d1(5)->e1(7)->f1(10)\n" +
            "                 ->d2(6)->e2(8)->f2(11)\n" +
            "                        ->e3(9)->[loop: f1(10)]\n" +
            "                               ->[loop: a(2)]\n" +
            "          ->[loop: d2(6)]";
        String a_copied_graph =
            "a(12)->b(13)->c1(14)->d2(15)->e2(19)->f2(21)\n" +
            "                            ->e3(20)->f1(18)\n" +
            "                                    ->[loop: a(12)]\n" +
            "                    ->d1(16)->e1(17)->[loop: f1(18)]\n" +
            "            ->[loop: d2(15)]";
        String a_copied_graph_par =
            "a(12)->y(1)->x(0)\n" + // note that the parents are not copied since there is no cycle via children to them.
            "     ->e3(20)->d2(15)->c1(14)->b(13)->[loop: a(12)]\n" +
            "                     ->[loop: b(13)]";



        System.out.print("Testing recursiveUpdateChildren: ");

        Node a2 = new Node(a);
        a2.setTagName("a_copied");
        Set<Node> copied = new HashSet<>();
        NodeUtils.recursiveUpdateChildren(a2, copied);

        String a1_graph = GraphUtils.printGraph_children(a, name_id);
        String a2_graph = GraphUtils.printGraph_children(a2, name_id);
        //System.out.println(a1_graph);
        //System.out.println(a2_graph);
        Assertions.assertEquals(a_orig_graph, a1_graph);
        Assertions.assertEquals(a_copied_graph, a2_graph);

        // This tests that the parents are not properly updated for paths that are unreachable via getChildren()
        String a2_graph_par = GraphUtils.printGraph_parents(a2, name_id);
        //System.out.println(a2_graph_par);
        Assertions.assertEquals(a_copied_graph_par, a2_graph_par);

        System.out.println("PASSED");
    }


    @Test
    void testRecursiveUpdateParents() {
        NodeIdSupplier.NODE_ID = 0;

        GraphUtils.GraphStruct gs;
        Graph g;
        Node c1;

        gs = TestGraphs.createGraph_4();
        g = gs.graph;
        c1 = gs.nodes.get("c1");
        //DotUtils.printToFile("graph4.dot", g);

        Function<Node, String> name_id = n -> "" + n.getName() + "(" + n.getId() + ")";

        String c1_orig_graph =
            "c1(4)->b(3)->a(2)->y(1)->x(0)\n" +
            "                 ->e3(9)->d2(6)->[loop: b(3)]\n" +
            "                               ->[loop: c1(4)]";
        String c1_copied_graph =
            "c1(12)->b(13)->a(14)->y(15)->x(17)\n" +
            "                    ->e3(16)->d2(18)->[loop: b(13)]\n" +
            "                                    ->[loop: c1(12)]";

        String c1_copied_graph_ch =
            "c1(12)->d1(5)->e1(7)->f1(10)\n" +
            "      ->d2(18)->e2(8)->f2(11)\n" +
            "              ->e3(16)->[loop: f1(10)]\n" +
            "                      ->a(14)->b(13)->[loop: c1(12)]\n" +
            "                                    ->[loop: d2(18)]";

        System.out.print("Testing recursiveUpdateParents: ");

        Node c1_2 = new Node(c1);
        c1_2.setTagName("c1_copied");
        NodeUtils.recursiveUpdateParents(c1_2);

        String c1_graph = GraphUtils.printGraph_parents(c1, name_id);
        String c1_2_graph = GraphUtils.printGraph_parents(c1_2, name_id);
        //System.out.println(c1_graph);
        //System.out.println(c1_2_graph);
        Assertions.assertEquals(c1_orig_graph, c1_graph);
        Assertions.assertEquals(c1_copied_graph, c1_2_graph);

        // This tests that the parents are not properly updated for paths that are unreachable via getParents()
        String c1_2_graph_ch = GraphUtils.printGraph_children(c1_2, name_id);
        //System.out.println(c1_2_graph_ch);
        Assertions.assertEquals(c1_copied_graph_ch, c1_2_graph_ch);

        System.out.println("PASSED");
    }


}