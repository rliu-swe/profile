import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-03
 */
class DiscreteVariableTest {

    @Test
    void testBasicInstantiation() {

        DiscreteVariable dv1 = new DiscreteVariable("a", 2);

        System.out.print("Testing basic instantiation with 2 levels: ");
        Assertions.assertEquals(dv1.toString(), "DiscreteVariable{name='a', num_levels=2, level_names={}}");
        System.out.println("PASSED");


    }


}