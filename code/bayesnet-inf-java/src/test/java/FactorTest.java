import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-03
 */
class FactorTest {

    @Test
    void shouldShowSimpleAssertion() {
        Assertions.assertEquals(1, 1);
    }

    @Test
    void testRowsPerLevel() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 3);
        DiscreteVariable d = new DiscreteVariable("d", 2);
        DiscreteVariable e = new DiscreteVariable("e", 5);


        String test;

        Factor f2x2 = new Factor(Arrays.asList(a, b));
        test = "" + f2x2.getRowsPerLevel();

        System.out.print("Testing with factor 2x2: ");
        Assertions.assertEquals("[2, 1]", test);
        System.out.println("PASSED");

        Factor f2x2x3 = new Factor(Arrays.asList(a, b, c));
        test = "" + f2x2x3.getRowsPerLevel();

        System.out.print("Testing with factor 2x2x3: ");
        Assertions.assertEquals("[6, 3, 1]", test);
        System.out.println("PASSED");

        Factor f2x2x3x2 = new Factor(Arrays.asList(a, b, c, d));
        test = "" + f2x2x3x2.getRowsPerLevel();

        System.out.print("Testing with factor 2x2x3x2: ");
        Assertions.assertEquals("[12, 6, 2, 1]", test);
        System.out.println("PASSED");

        Factor f2x2x3x2x5 = new Factor(Arrays.asList(a, b, c, d, e));
        test = "" + f2x2x3x2x5.getRowsPerLevel();

        System.out.print("Testing with factor 2x2x3x2x5: ");
        Assertions.assertEquals("[60, 30, 10, 5, 1]", test);
        System.out.println("PASSED");
    }

    @Test
    void toTable() {
        DiscreteVariable u1 = new DiscreteVariable("u1", 1);
        DiscreteVariable u2 = new DiscreteVariable("u2", 2);
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 3);
        DiscreteVariable d = new DiscreteVariable("d", 4);

        String f1_tbl =
            " idx    u1 | Value\n" +
            "-----------+--------\n" +
            "   0   u10   0.0000\n";

        String f2_tbl =
            " idx    u2 | Value\n" +
            "-----------+--------\n" +
            "   0   u20   0.0000\n" +
            "   1   u21   0.0000\n";

        String f2x2_tbl =
            "idx    a    b | Value\n" +
            "--------------+--------\n" +
            "  0   a0   b0   0.0000\n" +
            "  1   a0   b1   0.0000\n" +
            "  2   a1   b0   0.0000\n" +
            "  3   a1   b1   0.0000\n";

        String f2x3_tabl =
            "idx    a    c | Value\n" +
            "--------------+--------\n" +
            "  0   a0   c0   0.0000\n" +
            "  1   a0   c1   0.0000\n" +
            "  2   a0   c2   0.0000\n" +
            "  3   a1   c0   0.0000\n" +
            "  4   a1   c1   0.0000\n" +
            "  5   a1   c2   0.0000\n";

        String f2x3x2_tbl =
            "idx    a    c    b | Value\n" +
            "-------------------+--------\n" +
            "  0   a0   c0   b0   0.0000\n" +
            "  1   a0   c0   b1   0.0000\n" +
            "  2   a0   c1   b0   0.0000\n" +
            "  3   a0   c1   b1   0.0000\n" +
            "  4   a0   c2   b0   0.0000\n" +
            "  5   a0   c2   b1   0.0000\n" +
            "  6   a1   c0   b0   0.0000\n" +
            "  7   a1   c0   b1   0.0000\n" +
            "  8   a1   c1   b0   0.0000\n" +
            "  9   a1   c1   b1   0.0000\n" +
            " 10   a1   c2   b0   0.0000\n" +
            " 11   a1   c2   b1   0.0000\n";

        String f2x2x3_tbl =
            "idx    a    b    c | Value\n" +
            "-------------------+--------\n" +
            "  0   a0   b0   c0   0.0000\n" +
            "  1   a0   b0   c1   0.0000\n" +
            "  2   a0   b0   c2   0.0000\n" +
            "  3   a0   b1   c0   0.0000\n" +
            "  4   a0   b1   c1   0.0000\n" +
            "  5   a0   b1   c2   0.0000\n" +
            "  6   a1   b0   c0   0.0000\n" +
            "  7   a1   b0   c1   0.0000\n" +
            "  8   a1   b0   c2   0.0000\n" +
            "  9   a1   b1   c0   0.0000\n" +
            " 10   a1   b1   c1   0.0000\n" +
            " 11   a1   b1   c2   0.0000\n";

        String f2x4x2x3_tbl =
            "idx    a    d    b    c | Value\n" +
            "------------------------+--------\n" +
            "  0   a0   d0   b0   c0   0.0000\n" +
            "  1   a0   d0   b0   c1   0.0000\n" +
            "  2   a0   d0   b0   c2   0.0000\n" +
            "  3   a0   d0   b1   c0   0.0000\n" +
            "  4   a0   d0   b1   c1   0.0000\n" +
            "  5   a0   d0   b1   c2   0.0000\n" +
            "  6   a0   d1   b0   c0   0.0000\n" +
            "  7   a0   d1   b0   c1   0.0000\n" +
            "  8   a0   d1   b0   c2   0.0000\n" +
            "  9   a0   d1   b1   c0   0.0000\n" +
            " 10   a0   d1   b1   c1   0.0000\n" +
            " 11   a0   d1   b1   c2   0.0000\n" +
            " 12   a0   d2   b0   c0   0.0000\n" +
            " 13   a0   d2   b0   c1   0.0000\n" +
            " 14   a0   d2   b0   c2   0.0000\n" +
            " 15   a0   d2   b1   c0   0.0000\n" +
            " 16   a0   d2   b1   c1   0.0000\n" +
            " 17   a0   d2   b1   c2   0.0000\n" +
            " 18   a0   d3   b0   c0   0.0000\n" +
            " 19   a0   d3   b0   c1   0.0000\n" +
            " 20   a0   d3   b0   c2   0.0000\n" +
            " 21   a0   d3   b1   c0   0.0000\n" +
            " 22   a0   d3   b1   c1   0.0000\n" +
            " 23   a0   d3   b1   c2   0.0000\n" +
            " 24   a1   d0   b0   c0   0.0000\n" +
            " 25   a1   d0   b0   c1   0.0000\n" +
            " 26   a1   d0   b0   c2   0.0000\n" +
            " 27   a1   d0   b1   c0   0.0000\n" +
            " 28   a1   d0   b1   c1   0.0000\n" +
            " 29   a1   d0   b1   c2   0.0000\n" +
            " 30   a1   d1   b0   c0   0.0000\n" +
            " 31   a1   d1   b0   c1   0.0000\n" +
            " 32   a1   d1   b0   c2   0.0000\n" +
            " 33   a1   d1   b1   c0   0.0000\n" +
            " 34   a1   d1   b1   c1   0.0000\n" +
            " 35   a1   d1   b1   c2   0.0000\n" +
            " 36   a1   d2   b0   c0   0.0000\n" +
            " 37   a1   d2   b0   c1   0.0000\n" +
            " 38   a1   d2   b0   c2   0.0000\n" +
            " 39   a1   d2   b1   c0   0.0000\n" +
            " 40   a1   d2   b1   c1   0.0000\n" +
            " 41   a1   d2   b1   c2   0.0000\n" +
            " 42   a1   d3   b0   c0   0.0000\n" +
            " 43   a1   d3   b0   c1   0.0000\n" +
            " 44   a1   d3   b0   c2   0.0000\n" +
            " 45   a1   d3   b1   c0   0.0000\n" +
            " 46   a1   d3   b1   c1   0.0000\n" +
            " 47   a1   d3   b1   c2   0.0000\n";


        String test;

        Factor f1 = new Factor(Arrays.asList(u1));
        test = "" + f1.toTable();

        System.out.print("Testing with factor 1: ");
        Assertions.assertEquals(f1_tbl, test);
        System.out.println("PASSED");

        Factor f2 = new Factor(Arrays.asList(u2));
        test = "" + f2.toTable();

        System.out.print("Testing with factor 2: ");
        Assertions.assertEquals(f2_tbl, test);
        System.out.println("PASSED");

        Factor f2x2 = new Factor(Arrays.asList(a, b));
        test = "" + f2x2.toTable();

        System.out.print("Testing with factor 2x2: ");
        Assertions.assertEquals(f2x2_tbl, test);
        System.out.println("PASSED");

        Factor f2x3 = new Factor(Arrays.asList(a, c));
        test = "" + f2x3.toTable();

        System.out.print("Testing with factor 2x3: ");
        Assertions.assertEquals(f2x3_tabl, test);
        System.out.println("PASSED");

        Factor f2x3x2 = new Factor(Arrays.asList(a, c, b));
        test = "" + f2x3x2.toTable();

        System.out.print("Testing with factor 2x3x2: ");
        Assertions.assertEquals(f2x3x2_tbl, test);
        System.out.println("PASSED");

        Factor f2x2x3 = new Factor(Arrays.asList(a, b, c));
        test = "" + f2x2x3.toTable();

        System.out.print("Testing with factor 2x2x3: ");
        Assertions.assertEquals(f2x2x3_tbl, test);
        System.out.println("PASSED");

        Factor f2x4x2x3 = new Factor(Arrays.asList(a, d, b, c));
        test = "" + f2x4x2x3.toTable();

        System.out.print("Testing with factor 2x4x2x3: ");
        Assertions.assertEquals(f2x4x2x3_tbl, test);
        System.out.println("PASSED");

    }


    @Test
    void testComputeRowValues() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 3);
        DiscreteVariable d = new DiscreteVariable("d", 4);


        Factor f2 = new Factor(Arrays.asList(a));

        System.out.print("Testing with factor 2: ");
        Assertions.assertEquals("[0]", "" + f2.computeRowValues(0));
        Assertions.assertEquals("[1]", "" + f2.computeRowValues(1));
        Assertions.assertEquals(null, f2.computeRowValues(2));
        Assertions.assertEquals(null, f2.computeRowValues(3));
        Assertions.assertEquals(null, f2.computeRowValues(-1));
        System.out.println("PASSED");

        Factor f2x2 = new Factor(Arrays.asList(a, b));
        System.out.print("Testing with factor 2x2: ");
        Assertions.assertEquals("[0, 0]", "" + f2x2.computeRowValues(0));
        Assertions.assertEquals("[0, 1]", "" + f2x2.computeRowValues(1));
        Assertions.assertEquals("[1, 0]", "" + f2x2.computeRowValues(2));
        Assertions.assertEquals("[1, 1]", "" + f2x2.computeRowValues(3));
        Assertions.assertEquals(null, f2x2.computeRowValues(4));
        System.out.println("PASSED");

        Factor f2x3 = new Factor(Arrays.asList(a, c));
        System.out.print("Testing with factor 2x3: ");
        Assertions.assertEquals("[0, 0]", "" + f2x3.computeRowValues(0));
        Assertions.assertEquals("[0, 1]", "" + f2x3.computeRowValues(1));
        Assertions.assertEquals("[0, 2]", "" + f2x3.computeRowValues(2));
        Assertions.assertEquals("[1, 0]", "" + f2x3.computeRowValues(3));
        Assertions.assertEquals("[1, 1]", "" + f2x3.computeRowValues(4));
        Assertions.assertEquals("[1, 2]", "" + f2x3.computeRowValues(5));
        Assertions.assertEquals(null, f2x2.computeRowValues(6));
        System.out.println("PASSED");

    }

    @Test
    void testComputeRowIndex() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 3);
        DiscreteVariable d = new DiscreteVariable("d", 4);


        Factor f2 = new Factor(Arrays.asList(a));
        System.out.print("Testing with factor 2: ");
        Assertions.assertEquals(0, f2.computeRowIndex(Arrays.asList(0)));
        Assertions.assertEquals(1, f2.computeRowIndex(Arrays.asList(1)));
        Assertions.assertEquals(-1, f2.computeRowIndex(Arrays.asList(2)));
        Assertions.assertEquals(-1, f2.computeRowIndex(Arrays.asList(3)));
        Assertions.assertEquals(-1, f2.computeRowIndex(Arrays.asList(-1)));
        Assertions.assertEquals(-1, f2.computeRowIndex(Arrays.asList(-5)));
        System.out.println("PASSED");

        Factor f2x2 = new Factor(Arrays.asList(a, b));
        System.out.print("Testing with factor 2x2: ");
        Assertions.assertEquals(0, f2x2.computeRowIndex(Arrays.asList(0, 0)));
        Assertions.assertEquals(1, f2x2.computeRowIndex(Arrays.asList(0, 1)));
        Assertions.assertEquals(2, f2x2.computeRowIndex(Arrays.asList(1, 0)));
        Assertions.assertEquals(3, f2x2.computeRowIndex(Arrays.asList(1, 1)));
        Assertions.assertEquals(-1, f2x2.computeRowIndex(Arrays.asList(0, 3)));
        Assertions.assertEquals(-1, f2x2.computeRowIndex(Arrays.asList(0, -1)));
        Assertions.assertEquals(-1, f2x2.computeRowIndex(Arrays.asList(3, 0)));
        Assertions.assertEquals(-1, f2x2.computeRowIndex(Arrays.asList(-1, 0)));
        System.out.println("PASSED");
    }

    @Test
    void getRowIndicesByVarSubset() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 3);
        DiscreteVariable d = new DiscreteVariable("d", 4);

        Map<DiscreteVariable, Integer> varSubset = new HashMap<>();


        Factor f2x2x3x4 = new Factor(Arrays.asList(a, b, c, d));
        //System.out.println(f2x2x3x4.toTable());
        System.out.print("Testing with factor 2x2x3x4: ");
        Assertions.assertEquals("[Pair{0, 47}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.put(a, 1);
        Assertions.assertEquals("[Pair{24, 47}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.put(b, 0);
        Assertions.assertEquals("[Pair{24, 35}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.put(c, 2);
        Assertions.assertEquals("[Pair{32, 35}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.put(d, 3);
        Assertions.assertEquals("[Pair{35, 35}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));

        varSubset.clear();
        varSubset.put(d, 2);
        Assertions.assertEquals(
            "[Pair{2, 2}, Pair{6, 6}, Pair{10, 10}, Pair{14, 14}, Pair{18, 18}, Pair{22, 22}, Pair{26, 26}, Pair{30, 30}, Pair{34, 34}, Pair{38, 38}, Pair{42, 42}, Pair{46, 46}]",
            "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.clear();
        varSubset.put(c, 1);
        Assertions.assertEquals("[Pair{4, 7}, Pair{16, 19}, Pair{28, 31}, Pair{40, 43}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.clear();
        varSubset.put(b, 0);
        Assertions.assertEquals("[Pair{0, 11}, Pair{24, 35}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.clear();
        varSubset.put(b, 1);
        Assertions.assertEquals("[Pair{12, 23}, Pair{36, 47}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.clear();
        varSubset.put(a, 0);
        Assertions.assertEquals("[Pair{0, 23}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.clear();
        varSubset.put(a, 1);
        Assertions.assertEquals("[Pair{24, 47}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));

        varSubset.clear();
        varSubset.put(b, 1);
        varSubset.put(d, 2);
        Assertions.assertEquals("[Pair{14, 14}, Pair{18, 18}, Pair{22, 22}, Pair{38, 38}, Pair{42, 42}, Pair{46, 46}]",
            "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));
        varSubset.clear();
        varSubset.put(b, 1);
        varSubset.put(c, 2);
        Assertions.assertEquals("[Pair{20, 23}, Pair{44, 47}]", "" + f2x2x3x4.getRowIndicesByVarSubset(varSubset));

        System.out.println("PASSED");
    }

}
