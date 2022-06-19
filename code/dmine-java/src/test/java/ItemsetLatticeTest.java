import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author rliu 2022-04
 */
class ItemsetLatticeTest {

    @Test
    void testBasic1() {
        List<Set<Integer>> l3 = List.of(
            Set.of(1, 2, 3),   // abc
            Set.of(1, 2, 4),   // abd
            Set.of(1, 3, 4),   // acd
            Set.of(1, 3, 5),   // ace
            Set.of(2, 3, 4)    // bcd
        );

        String result;

        String lattice_k1 =
            "[]: 0\n" +
                "    [1]: 4\n" +
                "        [1, 2]: 1\n" +
                "        [1, 3]: 2\n" +
                "            [1, 3, 5]: 1\n" +
                "        [1, 4]: 2\n" +
                "            [1, 3, 4]: 1\n" +
                "        [1, 5]: 1\n" +
                "    [2]: 3\n" +
                "        [1, 2]: 1\n" +
                "        [2, 3]: 1\n" +
                "        [2, 4]: 2\n" +
                "            [2, 3, 4]: 1\n" +
                "    [3]: 4\n" +
                "        [1, 3]: 2\n" +
                "            [1, 3, 5]: 1\n" +
                "        [2, 3]: 1\n" +
                "        [3, 4]: 2\n" +
                "            [2, 3, 4]: 1\n" +
                "        [3, 5]: 1\n" +
                "    [4]: 3\n" +
                "        [1, 4]: 2\n" +
                "            [1, 3, 4]: 1\n" +
                "        [2, 4]: 2\n" +
                "            [2, 3, 4]: 1\n" +
                "        [3, 4]: 2\n" +
                "            [2, 3, 4]: 1\n" +
                "    [5]: 1\n";

        String lattice_k2 =
            "[]: 0\n" +
                "    [1]: 4\n" +
                "        [1, 2]: 2\n" +
                "            [1, 2, 3]: 1\n" +
                "            [1, 2, 4]: 1\n" +
                "        [1, 3]: 3\n" +
                "            [1, 2, 3]: 1\n" +
                "            [1, 3, 4]: 1\n" +
                "            [1, 3, 5]: 1\n" +
                "        [1, 4]: 2\n" +
                "            [1, 2, 4]: 1\n" +
                "            [1, 3, 4]: 1\n" +
                "        [1, 5]: 1\n" +
                "    [2]: 3\n" +
                "        [1, 2]: 2\n" +
                "            [1, 2, 3]: 1\n" +
                "            [1, 2, 4]: 1\n" +
                "        [2, 3]: 2\n" +
                "            [1, 2, 3]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "        [2, 4]: 2\n" +
                "            [1, 2, 4]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "    [3]: 4\n" +
                "        [1, 3]: 3\n" +
                "            [1, 2, 3]: 1\n" +
                "            [1, 3, 4]: 1\n" +
                "            [1, 3, 5]: 1\n" +
                "        [2, 3]: 2\n" +
                "            [1, 2, 3]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "        [3, 4]: 2\n" +
                "            [1, 3, 4]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "        [3, 5]: 1\n" +
                "    [4]: 3\n" +
                "        [1, 4]: 2\n" +
                "            [1, 2, 4]: 1\n" +
                "            [1, 3, 4]: 1\n" +
                "        [2, 4]: 2\n" +
                "            [1, 2, 4]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "        [3, 4]: 2\n" +
                "            [1, 3, 4]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "    [5]: 1\n";

        String lattice_k3 =
            "[]: 0\n" +
                "    [1]: 4\n" +
                "        [1, 2]: 2\n" +
                "            [1, 2, 3]: 1\n" +
                "            [1, 2, 4]: 1\n" +
                "        [1, 3]: 3\n" +
                "            [1, 2, 3]: 1\n" +
                "            [1, 3, 4]: 1\n" +
                "            [1, 3, 5]: 1\n" +
                "        [1, 4]: 2\n" +
                "            [1, 2, 4]: 1\n" +
                "            [1, 3, 4]: 1\n" +
                "        [1, 5]: 1\n" +
                "    [2]: 3\n" +
                "        [1, 2]: 2\n" +
                "            [1, 2, 3]: 1\n" +
                "            [1, 2, 4]: 1\n" +
                "        [2, 3]: 2\n" +
                "            [1, 2, 3]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "        [2, 4]: 2\n" +
                "            [1, 2, 4]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "    [3]: 4\n" +
                "        [1, 3]: 3\n" +
                "            [1, 2, 3]: 1\n" +
                "            [1, 3, 4]: 1\n" +
                "            [1, 3, 5]: 1\n" +
                "        [2, 3]: 2\n" +
                "            [1, 2, 3]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "        [3, 4]: 2\n" +
                "            [1, 3, 4]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "        [3, 5]: 1\n" +
                "    [4]: 3\n" +
                "        [1, 4]: 2\n" +
                "            [1, 2, 4]: 1\n" +
                "            [1, 3, 4]: 1\n" +
                "        [2, 4]: 2\n" +
                "            [1, 2, 4]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "        [3, 4]: 2\n" +
                "            [1, 3, 4]: 1\n" +
                "            [2, 3, 4]: 1\n" +
                "    [5]: 1\n";

        // Below, we number the db-scans (k) 1, 2, 3. They can be any number, as long as they are different from each other,
        //   so that the incr_on updating logic does not get confused by same k's.

        ItemsetLattice it = new ItemsetLattice(2);

        // DB SCAN 1 (k=1)
        for (int i = 0; i < l3.size(); i++) {
            it.countItemset(1, i, l3.get(i));
        }
        result = ItemsetLatticeUtils.print(it);
        TestUtils.StringEquals("k=1", lattice_k1, result);

        // DB SCAN 2 (k=2)
        for (int i = 0; i < l3.size(); i++) {
            it.countItemset(2, i, l3.get(i));
        }
        result = ItemsetLatticeUtils.print(it);
        TestUtils.StringEquals("k=2", lattice_k2, result);

        // DB SCAN 3 (k=3)
        for (int i = 0; i < l3.size(); i++) {
            it.countItemset(3, i, l3.get(i));
        }
        result = ItemsetLatticeUtils.print(it);
        TestUtils.StringEquals("k=3", lattice_k3, result);

        // DB SCAN 4 (k=4)
        //    successive more scans should not change any more counters (they all turn off after one db-scan cycle).
        for (int i = 0; i < l3.size(); i++) {
            it.countItemset(4, i, l3.get(i));
        }
        result = ItemsetLatticeUtils.print(it);
        TestUtils.StringEquals("k=4", lattice_k3, result);
    }


    @Test
    void get_k_groups_test() {
        Set<Set<Integer>> results = ItemsetLattice.get_k_groups(2, Set.of(1, 2, 3, 4, 5, 6));
        System.out.println(results);
    }
}