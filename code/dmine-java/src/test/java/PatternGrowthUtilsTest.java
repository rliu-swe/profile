import Utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-04
 */
class PatternGrowthUtilsTest {

    static String printFList(List<Pair<Integer, Integer>> flist) {
        StringBuilder sb = new StringBuilder();
        for (Pair<Integer, Integer> f : flist) {
            sb.append(f.left).append(": ").append(f.right)
              .append("\n");
        }
        return sb.toString();
    }

    @Test
    void gen_f_list_testBasic1() {
        List<List<Set<Integer>>> tdb = List.of(
            List.of(Set.of(1), Set.of(1, 2, 3), Set.of(1, 3), Set.of(4), Set.of(3, 6)),   // <a(abc)(ac)d(cf)>
            List.of(Set.of(1, 4), Set.of(3), Set.of(2, 3), Set.of(1, 5)),                  // <(ad)c(bc)(ae)
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

        List<Pair<Integer, Integer>> res;
        String corr;

        res = PatternGrowthUtils.gen_f_list(tdb);
        corr =
            "3: 4\n" +
                "2: 4\n" +
                "1: 4\n" +
                "6: 3\n" +
                "5: 3\n" +
                "4: 3\n" +
                "7: 1\n";
        TestUtils.StringEquals("basic 1", corr, printFList(res), true);
    }


    @Test
    void gen_f_list_testBasic2() {
        List<List<Set<Integer>>> tdb2 = List.of(
            List.of(Set.of(1), Set.of(-1, 2, 3), Set.of(1, 3), Set.of(4), Set.of(-1, 3, 6)),   // <a(_bc)(ac)d(_cf)>
            List.of(Set.of(1, 4), Set.of(-1, 3), Set.of(-1, 2, 3), Set.of(1, 5)),              // <(ad)(_c)(_bc)(ae)
            List.of(Set.of(5, 6), Set.of(-1, 3, 6), Set.of(4, 6), Set.of(-1, 3), Set.of(2), Set.of(3)),   // (ef)(_cf)(df)(_c)bc
            List.of(Set.of(5), Set.of(7), Set.of(1, 6), Set.of(3), Set.of(2), Set.of(3))       // <eg(af)cbc>
        );
        /**
         * [[1], [-1, 2, 3], [1, 3], [4], [-1, 3, 6]]
         * [[1, 4], [-1, 3], [-1, 2, 3], [1, 5]]
         * [[5, 6], [-1, 3, 6], [4, 6], [-1, 3], [2], [3]]
         * [[5], [7], [1, 6], [3], [2], [3]]
         */
        //System.out.println(SeqUtils.printSeqs(tdb2));

        List<Pair<Integer, Integer>> res;
        String corr;

        res = PatternGrowthUtils.gen_f_list(tdb2);
        corr =
            "5: 3\n" +
                "4: 3\n" +
                "3: 3\n" +
                "1: 3\n" +
                "6: 2\n" +
                "2: 2\n" +
                "-3: 2\n" +
                "-6: 2\n" +
                "7: 1\n" +
                "-2: 1\n";
        TestUtils.StringEquals("basic 2: with placeholders", corr, printFList(res), true);
    }


}