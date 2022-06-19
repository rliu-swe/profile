import java.util.*;
import java.util.stream.Collectors;

/**
 * Utilities for itemsets. Currently, itemset elements are represented by integers.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class ItemsetUtils {

    public static final Comparator<Set<Integer>> by_size = Comparator.comparingInt(Set::size);
    public static final Comparator<Set<Integer>> by_min = Comparator.comparingInt(Collections::min);

    /**
     * Returns the elements in the itemsets wrapped into singleton sets, as a set.
     */
    public static Set<Set<Integer>> get_itemset_elements_as_singletons(Collection<Set<Integer>> itemsets) {
        Set<Integer> elems = new HashSet<>();
        itemsets.forEach(elems::addAll);
        return elems.stream().map(Set::of).collect(Collectors.toSet());
    }

    /**
     * Returns all elements in the sequences wrapped into singleton itemsets in sequences, as a set.
     */
    public static Set<List<Set<Integer>>> get_seq_elements_as_singletons(Collection<List<Set<Integer>>> seqs) {
        Set<Integer> elems = new HashSet<>();
        seqs.forEach(itemset -> itemset.forEach(elems::addAll));
        return elems.stream().map(e -> List.of(Set.of(e))).collect(Collectors.toSet());
    }

    /**
     * Sorts a list of itemsets by their size and then by their minimum.
     */
    public static List<Set<Integer>> sort(Collection<Set<Integer>> itemsets) {
        List<Set<Integer>> itemsets_sorted = new ArrayList<>(itemsets);
        itemsets_sorted.sort(by_size.thenComparing(by_min));
        return itemsets_sorted;
    }

    /**
     * Converts a collection of itemsets into a list of itemsets where the itemsets are sorted by their natural order.
     */
    public static List<List<Integer>> order_keys(Collection<Set<Integer>> itemsets) {
        List<List<Integer>> res = new ArrayList<>();
        itemsets.forEach(it -> {
            List<Integer> sorted = new ArrayList<>(it);
            Collections.sort(sorted);
            res.add(sorted);
        });
        return res;
    }

    /**
     * Returns the conditional database conditioned on the given elements {@code x}, ie, tdb|x
     */
    public static List<Set<Integer>> gen_projected_db(Set<Integer> x, Collection<Set<Integer>> db) {
        List<Set<Integer>> cdb = new ArrayList<>();
        for (Set<Integer> row : db) {
            if (row.containsAll(x)) {
                Set<Integer> row2 = new HashSet<>(row);
                row2.removeAll(x);
                cdb.add(row2);
            }
        }
        return cdb;
    }


    /**
     * Prints all given FPs as a line de-limited string.
     */
    public static String printItemsets(Collection<Set<Integer>> fps) {
        StringBuilder sb = new StringBuilder();
        for (Set<Integer> fp : fps) {
            List<Integer> fp_sorted = new ArrayList<>(fp);
            Collections.sort(fp_sorted);
            sb.append(fp_sorted).append("\n");
        }
        return sb.toString();
    }
}
