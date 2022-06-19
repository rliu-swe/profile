import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-03
 */
class FactorUtilsTest {

    @Test
    void testRearrangeVariables() {
        Factor f;
        Factor g;
        Factor h;
        Factor fg;
        Factor fgh;
        DiscreteVariable a;
        DiscreteVariable b;
        DiscreteVariable c;
        DiscreteVariable d;

        FactorUtils.FactorsStruct fs1 = TestFactors.createFactor_1();
        f = fs1.factors.get("f");
        g = fs1.factors.get("g");
        h = fs1.factors.get("h");
        a = fs1.vars.get("a");
        b = fs1.vars.get("b");
        c = fs1.vars.get("c");
        d = fs1.vars.get("d");


        String f2_tbl =
            "idx    b    a | Value\n" +
            "--------------+--------\n" +
            "  0   b0   a0   0.3000\n" +
            "  1   b0   a1   0.1000\n" +
            "  2   b1   a0   0.7000\n" +
            "  3   b1   a1   0.9000\n";

        String h2_tbl =
            "idx    d    c    b | Value\n" +
            "-------------------+--------\n" +
            "  0   d0   c0   b0   0.1000\n" +
            "  1   d0   c0   b1   0.7000\n" +
            "  2   d0   c1   b0   0.2000\n" +
            "  3   d0   c1   b1   0.2000\n" +
            "  4   d1   c0   b0   0.3000\n" +
            "  5   d1   c0   b1   0.1000\n" +
            "  6   d1   c1   b0   0.3000\n" +
            "  7   d1   c1   b1   0.5000\n" +
            "  8   d2   c0   b0   0.6000\n" +
            "  9   d2   c0   b1   0.2000\n" +
            " 10   d2   c1   b0   0.5000\n" +
            " 11   d2   c1   b1   0.3000\n";

        System.out.print("Testing FactorUtilsTest.rearrangeVariables: ");

        Factor f2 = FactorUtils.rearrangeVariables(f, Arrays.asList(b, a));
        //System.out.println(f.toTable());
        //System.out.println(f2.toTable());
        Assertions.assertEquals(f2_tbl, f2.toTable());

        Factor h2 = FactorUtils.rearrangeVariables(h, Arrays.asList(d, c, b));
        //System.out.println(h.toTable());
        //System.out.println(h2.toTable());
        Assertions.assertEquals(h2_tbl, h2.toTable());

        System.out.println("PASSED");

    }
}