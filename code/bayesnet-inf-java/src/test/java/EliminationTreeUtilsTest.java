import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


/**
 * Author: Nurrachman Liu   2022-03
 */
class EliminationTreeUtilsTest {

    @Test
    void printElimTree() {
        NodeIdSupplier.NODE_ID = 0;
        Pair<GraphUtils.GraphStruct, List<EliminationTree>> gs_et = TestElimTrees.createElimTree_Graph5();
        EliminationTree et1 = gs_et.right.get(0);
        EliminationTree et2 = gs_et.right.get(1);

        String msg;

        String et1_str =
            "b(1)->a(0)->d(3)->c(2)->e(4)";
        String et2_str =
            "b(1)->a(0)->c(2)->d(3)\n" +
            "                ->e(4)";

        System.out.print("Testing EliminationTreeUtilsTest printElimTree: ");

        msg = EliminationTreeUtils.printElimTree(et1, NodeUtils.NAME_ID);
        //System.out.println(msg);
        Assertions.assertEquals(et1_str, msg);

        msg = EliminationTreeUtils.printElimTree(et2, NodeUtils.NAME_ID);
        //System.out.println(msg);
        Assertions.assertEquals(et2_str, msg);

        System.out.println("PASSED");

    }
}