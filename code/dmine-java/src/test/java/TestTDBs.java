import java.util.List;
import java.util.Set;


/**
 * Txn databases for testing.
 *
 * @author rliu 2022-04
 */
public class TestTDBs {

    public static List<Set<Integer>> tdb_hw1_p1() {
        List<Set<Integer>> tdb = List.of(
            // e: 5, f: 6, g: 7, h: 8, i: 9, j:10, k:11
            Set.of(1, 2, 8),          // abh
            Set.of(1, 3, 10, 11),     // acjk
            Set.of(1, 2, 4, 8, 10),   // abdhj
            Set.of(2, 3, 5, 6),       // bcef
            Set.of(1, 5, 6),          // aef
            Set.of(1, 2, 3, 8)        // abch
        );
        return tdb;
    }



}
