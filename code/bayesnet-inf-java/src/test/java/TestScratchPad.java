import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Author: Nurrachman Liu   2022-03
 */
public class TestScratchPad {

    @Test
    void test1() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);

        Factor f = new Factor(Arrays.asList(a));
        Factor g = new Factor(Arrays.asList(b));
        Factor fg;

        f.setRowValuesByValues(Arrays.asList(0.3, 0.7));
        g.setRowValuesByValues(Arrays.asList(0.2, 0.8));

        System.out.println(f.toTable());
        System.out.println(g.toTable());

        fg = FactorOperations.multiply(f, g);
        System.out.println(fg.toTable());
    }

    @Test
    void test2() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 2);
        DiscreteVariable d = new DiscreteVariable("d", 2);

        Factor fa = new Factor(Arrays.asList(a));
        Factor fb = new Factor(Arrays.asList(a, b));
        Factor fc = new Factor(Arrays.asList(b, c));
        Factor fd = new Factor(Arrays.asList(b, d));
        Factor fab;
        Factor fcd;
        Factor fcd_cd;
        Factor fcd_b;

        fb.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.2, 0.8));
        fc.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.2, 0.8));
        fd.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.2, 0.8));

        fa.setRowValuesByValues(Arrays.asList(0.8, 0.2));
        fb.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.2, 0.8));
        fc.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.2, 0.8));
        fd.setRowValuesByValues(Arrays.asList(1.0, 0.0, 0.2, 0.8));

        System.out.println(fa.toTable());
        System.out.println(fb.toTable());
        System.out.println(fc.toTable());
        System.out.println(fd.toTable());

        fcd = FactorOperations.multiply(fc, fd);
        System.out.println(fcd.toTable());

        fab = FactorOperations.multiply(fa, fb);
        System.out.println(fab.toTable());

        fcd_cd = FactorOperations.sum_out(fcd, Arrays.asList(c, d));
        System.out.println(fcd_cd.toTable());

        fcd_b = FactorOperations.sum_out(fcd, Arrays.asList(b));
        System.out.println(fcd_b.toTable());
    }
}
