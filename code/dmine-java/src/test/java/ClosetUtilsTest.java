import Utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-04
 */
class ClosetUtilsTest {

    @Test
    void extract_straight_fps_test1() {
        List<Set<Integer>> l5 = List.of(
            Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),   // a,b,c,d,e,f,g,h,i,j
            Set.of(1, 2, 3, 4, 5, 6, 7),             // a,b,c,d,e,f,g
            Set.of(1, 2, 3, 4),                      // a,b,c,d
            Set.of(1, 2)                             // a,b
        );

        List<FPTree.FPTreeNode> f_list = FPGrowthUtils.gen_f_list_as_fpnodes(l5);

        FPTree fptree = new FPTree();

        for (Set<Integer> pat : l5) {
            List<Integer> pat_sorted = FPGrowthUtils.sortItemsetUsingFlist(pat, f_list);
            fptree.insertPattern(pat_sorted);
        }

        List<Pair<Set<Integer>, Integer>> cfps = ClosetUtils.extract_straight_fps(fptree.root, Pair.of(new HashSet<>(), -1));
        String corr_1 =
            "[Pair{[1, 2], 4}, Pair{[1, 2, 3, 4], 3}, Pair{[1, 2, 3, 4, 5, 6, 7], 2}, Pair{[1, 2, 3, 4, 5, 6, 7, 8, 9, 10], 1}]";
        TestUtils.StringEquals("straight-chain with no branches", corr_1, "" + cfps, true);
    }


    @Test
    void extract_straight_fps_test2() {
        // immediate branch (single-element straight path)
        List<Set<Integer>> l0 = List.of(
            Set.of(1, 2),
            Set.of(1, 3)
        );

        List<FPTree.FPTreeNode> f_list = FPGrowthUtils.gen_f_list_as_fpnodes(l0);

        FPTree fptree = new FPTree();

        for (Set<Integer> pat : l0) {
            List<Integer> pat_sorted = FPGrowthUtils.sortItemsetUsingFlist(pat, f_list);
            fptree.insertPattern(pat_sorted);
        }

        List<Pair<Set<Integer>, Integer>> cfps = ClosetUtils.extract_straight_fps(fptree.root, Pair.of(new HashSet<>(), -1));
        String corr_1 =
            "[Pair{[1], 2}]";
        TestUtils.StringEquals("single-element path with immediate branch", corr_1, "" + cfps, true);
    }


    @Test
    void extract_straight_fps_test3() {
        // immediate branch (single-element straight path), but starting at 2
        List<Set<Integer>> l0 = List.of(
            Set.of(1, 2, 3),
            Set.of(1, 2, 4),
            Set.of(1, 10),  // need to add 1's to make 1 the most frequent so that it is first in the insertion order
            Set.of(1, 11),
            Set.of(2)  // add one more to make its key 2's global support 3, but the returned support should be local (2)
        );

        List<FPTree.FPTreeNode> f_list = FPGrowthUtils.gen_f_list_as_fpnodes(l0);

        FPTree fptree = new FPTree();

        for (Set<Integer> pat : l0) {
            List<Integer> pat_sorted = FPGrowthUtils.sortItemsetUsingFlist(pat, f_list);
            fptree.insertPattern(pat_sorted);
        }

        List<Pair<Set<Integer>, Integer>> cfps = ClosetUtils.extract_straight_fps(fptree.root.get(1).get(2), Pair.of(new HashSet<>(), -1));
        String corr_1 =
            "[Pair{[2], 2}]";
        TestUtils.StringEquals("single-element path with immediate branch", corr_1, "" + cfps, true);
    }


}