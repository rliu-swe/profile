import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-04
 */
class FPGrowthUtilsTest {

    @Test
    void gen_itemsets_Test1() {
        List<Set<Integer>> l3 = List.of(
            Set.of(1, 2, 3),   // abc
            Set.of(1, 2, 4),   // abd
            Set.of(1, 3, 4),   // acd
            Set.of(1, 3, 5),   // ace
            Set.of(2, 3, 4)    // bcd
        );
        FPGrowth fpgrowth = new FPGrowth(() -> l3);
        FPTree fptree;

        String corr_fptree =
            "-1: 0\n" +
                "    3: 1\n" +
                "        2: 1\n" +
                "            4: 1\n" +
                "    1: 4\n" +
                "        3: 3\n" +
                "            4: 1\n" +
                "            2: 1\n" +
                "        2: 1\n" +
                "            4: 1\n";


        List<Set<Integer>> res;

        fpgrowth.mine(2);
        fptree = fpgrowth.getFPTree();

        TestUtils.StringEquals("fptree is as expected",  corr_fptree, FPTreeUtils.print(fptree), true);

        res = FPGrowthUtils.gen_itemsets(fptree.root.get(1));
        String corr_key1 = "[[3], [2], [2, 4], [2, 3], [3, 4]]";
        TestUtils.StringEquals("node with key '1'",  corr_key1, "" + res);

        res = FPGrowthUtils.gen_itemsets(fptree.root.get(3));
        String corr_key2 = "[[2], [2, 4]]";
        TestUtils.StringEquals("node with key '3'",  corr_key2, "" + res);

        // when using root, you just get all itemsets
        res = FPGrowthUtils.gen_itemsets(fptree.root);
        String corr_key0 = "[[1], [3], [2, 3], [2, 3, 4], [1, 3], [1, 2], [1, 2, 4], [1, 2, 3], [1, 3, 4]]";
        TestUtils.StringEquals("root node",  corr_key0, "" + res);
    }
}