import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-03
 */
class ProbabilityTest {

    @Test
    void testCheckOverlap() {
        DiscreteVariable a = new DiscreteVariable("a", 2);
        DiscreteVariable b = new DiscreteVariable("b", 2);
        DiscreteVariable c = new DiscreteVariable("c", 2);

        boolean re;  // catching runtime exceptions

        System.out.print("Testing Probability no-overlap between vars and insts, for joint/marginal probability: ");
        re = false;
        try {
            Probability query = new Probability.JointProb(Arrays.asList(a), Arrays.asList(a.instance(1)));
        }
        catch (Exception ex) {
            re = true;
        }
        Assertions.assertEquals(true, re);
        System.out.println("PASSED");


        System.out.print("Testing Probability no-overlap between vars and insts, for conditional probability: ");
        re = false;
        try {
            Probability query = new Probability.CondProb(Arrays.asList(c), Arrays.asList(a), Arrays.asList(a.instance(1)));
        }
        catch (Exception ex) {
            re = true;
        }
        Assertions.assertEquals(true, re);
        System.out.println("PASSED");

    }
}