import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Author: Nurrachman Liu   2022-03
 */
class FactorOperationsTest {


    @Test
    void testMultiply() {
        Factor f;
        Factor g;
        Factor h;
        Factor fg;
        Factor fgh;
        DiscreteVariable a;
        DiscreteVariable b;

        FactorUtils.FactorsStruct fs0 = TestFactors.createFactor_0();
        f = fs0.factors.get("f");
        g = fs0.factors.get("g");
        a = fs0.vars.get("a");
        b = fs0.vars.get("b");

        String fg0_str =
            "idx    a    b | Value\n" +
            "--------------+--------\n" +
            "  0   a0   b0   0.0600\n" +
            "  1   a0   b1   0.2400\n" +
            "  2   a1   b0   0.1400\n" +
            "  3   a1   b1   0.5600\n";

        System.out.print("Testing multiplying independent factors f,g: ");
        fg = FactorOperations.multiply(f, g);
        Assertions.assertEquals(fg0_str, fg.toTable());
        System.out.println("PASSED");


        FactorUtils.FactorsStruct fs1 = TestFactors.createFactor_1();
        f = fs1.factors.get("f");
        g = fs1.factors.get("g");
        h = fs1.factors.get("h");

        String fg1_tbl =
            "idx    a    b    c | Value\n" +
            "-------------------+--------\n" +
            "  0   a0   b0   c0   0.0600\n" +
            "  1   a0   b0   c1   0.2400\n" +
            "  2   a0   b1   c0   0.3500\n" +
            "  3   a0   b1   c1   0.3500\n" +
            "  4   a1   b0   c0   0.0200\n" +
            "  5   a1   b0   c1   0.0800\n" +
            "  6   a1   b1   c0   0.4500\n" +
            "  7   a1   b1   c1   0.4500\n";

        String fgh1_tbl =
            "idx    a    b    c    d | Value\n" +
            "------------------------+--------\n" +
            "  0   a0   b0   c0   d0   0.0060\n" +
            "  1   a0   b0   c0   d1   0.0180\n" +
            "  2   a0   b0   c0   d2   0.0360\n" +
            "  3   a0   b0   c1   d0   0.0480\n" +
            "  4   a0   b0   c1   d1   0.0720\n" +
            "  5   a0   b0   c1   d2   0.1200\n" +
            "  6   a0   b1   c0   d0   0.2450\n" +
            "  7   a0   b1   c0   d1   0.0350\n" +
            "  8   a0   b1   c0   d2   0.0700\n" +
            "  9   a0   b1   c1   d0   0.0700\n" +
            " 10   a0   b1   c1   d1   0.1750\n" +
            " 11   a0   b1   c1   d2   0.1050\n" +
            " 12   a1   b0   c0   d0   0.0020\n" +
            " 13   a1   b0   c0   d1   0.0060\n" +
            " 14   a1   b0   c0   d2   0.0120\n" +
            " 15   a1   b0   c1   d0   0.0160\n" +
            " 16   a1   b0   c1   d1   0.0240\n" +
            " 17   a1   b0   c1   d2   0.0400\n" +
            " 18   a1   b1   c0   d0   0.3150\n" +
            " 19   a1   b1   c0   d1   0.0450\n" +
            " 20   a1   b1   c0   d2   0.0900\n" +
            " 21   a1   b1   c1   d0   0.0900\n" +
            " 22   a1   b1   c1   d1   0.2250\n" +
            " 23   a1   b1   c1   d2   0.1350\n";

        fg = FactorOperations.multiply(f, g);
        // System.out.println(f.toTable());
        // System.out.println(g.toTable());
        // System.out.println(fg.toTable());
        System.out.print("Testing multiplying factors f, g: ");
        Assertions.assertEquals(fg1_tbl, fg.toTable());
        System.out.println("PASSED");

        fgh = FactorOperations.multiply(fg, h);
        // System.out.println(fg.toTable());
        // System.out.println(h.toTable());
        // System.out.println(fgh.toTable());
        System.out.print("Testing multiplying factors fg, h: ");
        Assertions.assertEquals(fgh1_tbl, fgh.toTable());
        System.out.println("PASSED");
    }

    @Test
    void testSumOut() {
        FactorUtils.FactorsStruct fs1 = TestFactors.createFactor_1();
        Factor f = fs1.factors.get("f");
        Factor g = fs1.factors.get("g");
        Factor h = fs1.factors.get("h");

        DiscreteVariable a = fs1.vars.get("a");
        DiscreteVariable b = fs1.vars.get("b");
        DiscreteVariable c = fs1.vars.get("c");
        DiscreteVariable d = fs1.vars.get("d");

        String f_a_tbl =
            "idx    b | Value\n" +
            "---------+--------\n" +
            "  0   b0   0.4000\n" +
            "  1   b1   1.6000\n";

        String f_ab_tbl =
            "idx | Value\n" +
            "----+--------\n" +
            " 0   2.0000\n";

        String h_b_tbl =
            "idx    c    d | Value\n" +
            "--------------+--------\n" +
            "  0   c0   d0   0.8000\n" +
            "  1   c0   d1   0.4000\n" +
            "  2   c0   d2   0.8000\n" +
            "  3   c1   d0   0.4000\n" +
            "  4   c1   d1   0.8000\n" +
            "  5   c1   d2   0.8000\n";

        String h_bd_tbl =
            "idx    c | Value\n" +
            "---------+--------\n" +
            "  0   c0   2.0000\n" +
            "  1   c1   2.0000\n";

        String h_cd_tbl =
            "idx    b | Value\n" +
            "---------+--------\n" +
            "  0   b0   2.0000\n" +
            "  1   b1   2.0000\n";

        String h_bc_tbl =
            "idx    d | Value\n" +
            "---------+--------\n" +
            "  0   d0   1.2000\n" +
            "  1   d1   1.2000\n" +
            "  2   d2   1.6000\n";

        String h_bc_d_tbl =
            "idx | Value\n" +
            "----+--------\n" +
            " 0   4.0000\n";

        String h_b_c_tbl =
            "idx    d | Value\n" +
            "---------+--------\n" +
            "  0   d0   1.2000\n" +
            "  1   d1   1.2000\n" +
            "  2   d2   1.6000\n";

        System.out.print("Testing sum-out of variables from factor f: ");
        Factor f_a = FactorOperations.sum_out(f, Arrays.asList(a));
        Factor f_ab = FactorOperations.sum_out(f, Arrays.asList(a,b));
        // System.out.println(f.toTable());
        // System.out.println(f_a.toTable());
        // System.out.println(f_ab.toTable());
        Assertions.assertEquals(f_a_tbl, f_a.toTable());
        Assertions.assertEquals(f_ab_tbl, f_ab.toTable());
        System.out.println("PASSED");

        System.out.print("Testing sum-out of variables from factor h: ");
        Factor h_b = FactorOperations.sum_out(h, Arrays.asList(b));
        Factor h_bd = FactorOperations.sum_out(h, Arrays.asList(b,d));
        Factor h_cd = FactorOperations.sum_out(h, Arrays.asList(c,d));
        Factor h_bc = FactorOperations.sum_out(h, Arrays.asList(b,c));
        Factor h_bc_d = FactorOperations.sum_out(h_bc, Arrays.asList(d));
        Factor h_b_c = FactorOperations.sum_out(h_b, Arrays.asList(c));
        // System.out.println(h.toTable());
        // System.out.println(h_b.toTable());
        // System.out.println(h_bd.toTable());
        // System.out.println(h_cd.toTable());
        // System.out.println(h_bc.toTable());
        // System.out.println(h_bc_d.toTable());
        // System.out.println(h_b_c.toTable());
        Assertions.assertEquals(h_b_tbl, h_b.toTable());
        Assertions.assertEquals(h_bd_tbl, h_bd.toTable());
        Assertions.assertEquals(h_cd_tbl, h_cd.toTable());
        Assertions.assertEquals(h_bc_tbl, h_bc.toTable());
        Assertions.assertEquals(h_bc_d_tbl, h_bc_d.toTable());
        Assertions.assertEquals(h_b_c_tbl, h_b_c.toTable());
        System.out.println("PASSED");

    }

    // todo
    @Test
    void testDivide() {
    }

    @Test
    void testProject() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 2);
        DiscreteVariable d = new DiscreteVariable("d", 2);


        Factor f_a = new Factor("a", Arrays.asList(a));
        Factor f_b = new Factor("b", Arrays.asList(b));
        Factor f_c = new Factor("c", Arrays.asList(c));
        Factor f_d = new Factor("d", Arrays.asList(d));

        f_a.setRowValuesByValues(Arrays.asList(0.8, 0.2));
        f_b.setRowValuesByValues(Arrays.asList(0.3, 0.7));
        f_c.setRowValuesByValues(Arrays.asList(0.5, 0.5));
        f_d.setRowValuesByValues(Arrays.asList(0.4, 0.6));

        Factor f_abcd = FactorOperations.multiply(FactorOperations.multiply(FactorOperations.multiply(f_a, f_b), f_c), f_d);
        Factor res;

        String str_a =
            "idx    a | Value\n" +
            "---------+--------\n" +
            "  0   a0   0.8000\n" +
            "  1   a1   0.2000\n";

        String str_b =
            "idx    b | Value\n" +
            "---------+--------\n" +
            "  0   b0   0.3000\n" +
            "  1   b1   0.7000\n";

        String str_ab =
            "idx    a    b | Value\n" +
            "--------------+--------\n" +
            "  0   a0   b0   0.2400\n" +
            "  1   a0   b1   0.5600\n" +
            "  2   a1   b0   0.0600\n" +
            "  3   a1   b1   0.1400\n";

        System.out.print("Testing FactorOperations.project: ");

        //System.out.println(f_abcd.toTable());

        res = FactorOperations.project(f_abcd, Arrays.asList(a));
        //System.out.println(res.toTable());
        Assertions.assertEquals(str_a, res.toTable());

        res = FactorOperations.project(f_abcd, Arrays.asList(b));
        //System.out.println(res.toTable());
        Assertions.assertEquals(str_b, res.toTable());

        res = FactorOperations.project(f_abcd, Arrays.asList(a, b));
        //System.out.println(res.toTable());
        Assertions.assertEquals(str_ab, res.toTable());

        System.out.println("PASSED");
    }
}