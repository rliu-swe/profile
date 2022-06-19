import java.util.Arrays;


/**
 * Author: Nurrachman Liu   2022-03
 */
public class TestFactors {

    /**
     * Two independent factors for testing basic multiplication of indepenent factors (CPTs).
     * variables:levels :  a:2, b:2
     * factors:variables :  f:a   g:b
     */
    static FactorUtils.FactorsStruct createFactor_0() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);

        Factor f = new Factor(Arrays.asList(a));
        Factor g = new Factor(Arrays.asList(b));

        f.setRowValuesByValues(Arrays.asList(0.3, 0.7));
        g.setRowValuesByValues(Arrays.asList(0.2, 0.8));

        return new FactorUtils.FactorsStruct(
            Arrays.asList("f", "g"), Arrays.asList(f, g),
            Arrays.asList("a", "b"), Arrays.asList(a, b));
    }


    /**
     * variables:levels :  a:2, b:2, c:2, d:3
     * factors:variables :  f:a,b   g:b,c   h:b,c,d
     */
    static FactorUtils.FactorsStruct createFactor_1() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 2);
        DiscreteVariable d = new DiscreteVariable("d", 3);

        Factor f = new Factor(Arrays.asList(a, b));
        Factor g = new Factor(Arrays.asList(b, c));
        Factor h = new Factor(Arrays.asList(b, c, d));

        f.setRowValuesByValues(Arrays.asList(0.3, 0.7, 0.1, 0.9));
        g.setRowValuesByValues(Arrays.asList(0.2, 0.8, 0.5, 0.5));
        h.setRowValuesByValues(Arrays.asList(0.1, 0.3, 0.6, 0.2, 0.3, 0.5, 0.7, 0.1, 0.2, 0.2, 0.5, 0.3));

        return new FactorUtils.FactorsStruct(
            Arrays.asList("f", "g", "h"), Arrays.asList(f, g, h),
            Arrays.asList("a", "b", "c", "d"), Arrays.asList(a, b, c, d));
    }

}
