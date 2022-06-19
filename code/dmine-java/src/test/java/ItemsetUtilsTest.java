import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;


/**
 * @author rliu 2022-04
 */
class ItemsetUtilsTest {

    @Test
    void sortTest() {
        List<Set<Integer>> it1 = List.of(
            Set.of(1, 2),
            Set.of(1, 3),
            Set.of(1, 4),
            Set.of(1, 5),
            Set.of(1, 2, 3),
            Set.of(2, 3),
            Set.of(2, 4),
            Set.of(2, 5),
            Set.of(2, 3, 4),
            Set.of(3, 4),
            Set.of(3, 5),
            Set.of(3, 4, 5),
            Set.of(4, 5),
            Set.of(4, 5, 6)
        );

        List<Set<Integer>> ret;

        ret = ItemsetUtils.sort(it1);
        String corr_1 =
            "[[1, 2], [1, 3], [1, 4], [1, 5], [2, 3], [2, 4], [2, 5], [3, 4], [3, 5], [4, 5], [1, 2, 3], [2, 3, 4], [3, 4, 5], [4, 5, 6]]";
        TestUtils.StringEquals("basic 1", corr_1, "" + ItemsetUtils.order_keys(ret), true);
    }
}