import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-04
 */
class SeqUtilsTest {

    @Test
    void seqIsInRow() {
        List<List<Set<Integer>>> test_rows = List.of(
            List.of(Set.of(1), Set.of(1, 2, 3), Set.of(1, 3), Set.of(4), Set.of(3, 6)),  // <a(abc)(ac)d(cf)
            List.of(Set.of(1, 4), Set.of(3), Set.of(2, 3), Set.of(1, 5)),                // <(ad)c(bc)(ae)>
            List.of(Set.of(5, 6), Set.of(1, 2), Set.of(4, 6), Set.of(3), Set.of(2)),     // <(ef)(ab)(df)cb>
            List.of(Set.of(5), Set.of(7), Set.of(1, 6), Set.of(3), Set.of(2), Set.of(3)) // <eg(af)cbc>
        );

        List<Set<Integer>> test_seq;
        boolean res;
        List<Boolean> results;

        test_seq = List.of(Set.of(1, 2), Set.of(3));  // <(ab)c>
        results = new ArrayList<>();
        for (List<Set<Integer>> row : test_rows) {
            res = SeqUtils.seqIsInRow(row, test_seq);
            results.add(res);
        }
        String corr_1 =
            "[true, false, true, false]";
        TestUtils.StringEquals("test1", corr_1, "" + results, true);
    }

    @Test
    void project_on_seq_testBasic1() {
        // Tests on singleton sequences

        List<List<Integer>> res;

        res = SeqUtils.project_on_seq(List.of(Set.of(1)), List.of(Set.of(1)));
        String corr_1 = "[]";
        TestUtils.StringEquals("test: same", corr_1, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(1)), List.of(Set.of(1, 2)));
        String corr_2 = "[[-1, 2]]";
        TestUtils.StringEquals("test: simple 1", corr_2, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(1)), List.of(Set.of(1, 2, 3)));
        String corr_3 = "[[-1, 2, 3]]";
        TestUtils.StringEquals("test: simple 2", corr_3, "" + res, true);

        // despite 1 being 'last', canonical ordering will be used, and so it will be (1, 2, 3) -> (-1, 2, 3)
        res = SeqUtils.project_on_seq(List.of(Set.of(1)), List.of(Set.of(2, 3, 1)));
        String corr_4 = "[[-1, 2, 3]]";
        TestUtils.StringEquals("test: simple 4", corr_4, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(1)), List.of(Set.of(1, 2), Set.of(2, 3, 1)));
        String corr_5 = "[[-1, 2], [1, 2, 3]]";
        TestUtils.StringEquals("test: simple 5", corr_5, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(1)), List.of(Set.of(1), Set.of(1, 2), Set.of(2, 3, 1)));
        String corr_6 = "[[1, 2], [1, 2, 3]]";
        TestUtils.StringEquals("test: simple 6", corr_6, "" + res, true);
    }

    @Test
    void project_on_seq_testBasic2() {
        // Tests on itemset sequence

        List<List<Integer>> res;

        res = SeqUtils.project_on_seq(List.of(Set.of(1, 2)), List.of(Set.of(1, 2)));
        String corr_1 = "[]";
        TestUtils.StringEquals("test: match entirely", corr_1, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(1, 2)), List.of(Set.of(1, 2, 3)));
        String corr_2 = "[[-1, 3]]";
        TestUtils.StringEquals("test: match subset begin", corr_2, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(2, 3)), List.of(Set.of(1, 2, 3, 4)));
        String corr_3 = "[[-1, 4]]";
        TestUtils.StringEquals("test: match subset inner", corr_3, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(3, 4)), List.of(Set.of(1, 2, 3, 4)));
        String corr_4 = "[]";
        TestUtils.StringEquals("test: match subset end", corr_4, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(3, 4)), List.of(Set.of(1, 2, 3, 4), Set.of(5, 6)));
        String corr_5 = "[[5, 6]]";
        TestUtils.StringEquals("test: match subset end, with another itemset left", corr_5, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(3, 4)), List.of(Set.of(1, 2), Set.of(1, 2, 3, 4, 5)));
        String corr_6 = "[[-1, 5]]";
        TestUtils.StringEquals("test: match seq after first itemset, with elem left in itemset", corr_6, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(3, 4)), List.of(Set.of(1, 2), Set.of(1, 2, 3, 4)));
        String corr_7 = "[]";
        TestUtils.StringEquals("test: match seq after first itemset, with no elem left in itemset", corr_7, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(3, 4)), List.of(Set.of(1, 2), Set.of(1, 2, 3, 4), Set.of(7, 8)));
        String corr_8 = "[[7, 8]]";
        TestUtils.StringEquals("test: match seq after first itemset, but with more itemsets, with no elem left in itemset", corr_8, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(3, 4)), List.of(Set.of(1, 2), Set.of(1, 2, 3, 4, 5), Set.of(7, 8)));
        String corr_9 = "[[-1, 5], [7, 8]]";
        TestUtils.StringEquals("test: match seq after first itemset, but with more itemsets, with elems left in itemset", corr_9, "" + res, true);
    }

    @Test
    void project_on_seq_testBasic3() {
        // Tests on complex sequences (combo sequences)

        List<List<Set<Integer>>> tdb = List.of(
            List.of(Set.of(1), Set.of(1, 2, 3), Set.of(1, 3), Set.of(4), Set.of(3, 6)),   // <a(abc)(ac)d(cf)>
            List.of(Set.of(1, 4), Set.of(3), Set.of(2,3), Set.of(1, 5)),                  // <(ad)c(bc)(ae)
            List.of(Set.of(5, 6), Set.of(1, 2), Set.of(4, 6), Set.of(3), Set.of(2)),      // (ef)(ab)(df)cb
            List.of(Set.of(5), Set.of(7), Set.of(1, 6), Set.of(3), Set.of(2), Set.of(3))  // <eg(af)cbc>
        );

        /**
         * [[1], [1, 2, 3], [1, 3], [4], [3, 6]]
         * [[1, 4], [3], [2, 3], [1, 5]]
         * [[5, 6], [1, 2], [4, 6], [3], [2]]
         * [[5], [7], [1, 6], [3], [2], [3]]
         */
        //System.out.println(SeqUtils.printSeqs(tdb));

        List<Set<Integer>> ab = List.of(Set.of(1), Set.of(2));
        List<Set<Integer>> abc = List.of(Set.of(1), Set.of(2), Set.of(3));
        List<Set<Integer>> a_bc = List.of(Set.of(1), Set.of(2, 3));
        List<Set<Integer>> a_bc_a = List.of(Set.of(1), Set.of(2, 3), Set.of(1));

        List<List<Integer>> res;
        String corr;

        // ab-projected db
        res = SeqUtils.project_on_seq(ab, tdb.get(0));
        corr = "[[-1, 3], [1, 3], [4], [3, 6]]";
        TestUtils.StringEquals("test: ab on tid 1", corr, "" + res, true);

        res = SeqUtils.project_on_seq(ab, tdb.get(1));
        corr = "[[-1, 3], [1, 5]]";
        TestUtils.StringEquals("test: ab on tid 2", corr, "" + res, true);

        res = SeqUtils.project_on_seq(ab, tdb.get(3));
        corr = "[[3]]";
        TestUtils.StringEquals("test: ab on tid 4", corr, "" + res, true);

        // a(bc)a-projected db
        res = SeqUtils.project_on_seq(a_bc, tdb.get(0));
        corr = "[[1, 3], [4], [3, 6]]";
        TestUtils.StringEquals("test: a(bc) on tid 1", corr, "" + res, true);

        res = SeqUtils.project_on_seq(a_bc, tdb.get(1));
        corr = "[[1, 5]]";
        TestUtils.StringEquals("test: a(bc) on tid 2", corr, "" + res, true);

        // a(bc)a-projected db
        res = SeqUtils.project_on_seq(a_bc_a, tdb.get(0));
        corr = "[[-1, 3], [4], [3, 6]]";
        TestUtils.StringEquals("test: a(bc)a on tid 1", corr, "" + res, true);

        res = SeqUtils.project_on_seq(a_bc_a, tdb.get(1));
        corr = "[[-1, 5]]";
        TestUtils.StringEquals("test: a(bc)a on tid 2", corr, "" + res, true);

        // abc-projected db
        res = SeqUtils.project_on_seq(abc, tdb.get(0));
        corr = "[[4], [3, 6]]";
        TestUtils.StringEquals("test: abc on tid 1", corr, "" + res, true);


    }

    @Test
    void project_on_seq_testBasic4() {
        // Tests on placeholder itemsets

        List<List<Integer>> res;
        String corr;
        Executable run;

        System.out.print("Testing projected placeholder-item is not in sequence: ");
        Assertions.assertThrowsExactly(RuntimeException.class, () -> SeqUtils.project_on_seq(List.of(Set.of(-1, 1)), List.of(Set.of(-1, 2))));
        System.out.println("PASSED");

        System.out.print("Testing projected placeholder-item is not in itemset with placeholder removed: ");
        Assertions.assertThrowsExactly(RuntimeException.class, () -> SeqUtils.project_on_seq(List.of(Set.of(-1, 1)), List.of(Set.of(1))));
        System.out.println("PASSED");

        res = SeqUtils.project_on_seq(List.of(Set.of(-1, 1)), List.of(Set.of(-1, 1)));
        corr = "[]";
        TestUtils.StringEquals("test: same", corr, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(-1, 1)), List.of(Set.of(-1, 1, 2)));
        corr = "[[-1, 2]]";
        TestUtils.StringEquals("test: contained in placeholder itemset", corr, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(-1, 1)), List.of(Set.of(-1, 1, 2, 3)));
        corr = "[[-1, 2, 3]]";
        TestUtils.StringEquals("test: contained in larger placeholder itemset", corr, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(-1, 1)), List.of(Set.of(1, 2), Set.of(-1, 1, 2, 3)));
        corr = "[[-1, 2, 3]]";
        TestUtils.StringEquals("test: contained in larger placeholder itemset, with itemset in front", corr, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(-1, 1)), List.of(Set.of(-1, 1, 2, 3), Set.of(1, 3)));
        corr = "[[-1, 2, 3], [1, 3]]";
        TestUtils.StringEquals("test: contained in larger placeholder itemset, with itemset in back", corr, "" + res, true);

        res = SeqUtils.project_on_seq(List.of(Set.of(-1, 1)), List.of(Set.of(1, 2), Set.of(-1, 1, 2, 3), Set.of(1, 3)));
        corr = "[[-1, 2, 3], [1, 3]]";
        TestUtils.StringEquals("test: contained in larger placeholder itemset, with itemset in front and back", corr, "" + res, true);

    }


}