import Utils.Pair;

import java.util.*;

/**
 * Utilities for sequential patterns.
 *
 * @author rliu 2022-04
 */
public class PatternGrowthUtils {

    /**
     * Given a set of sequences, returns a list of the most frequent items, paired with support.
     * Thus, note that only individual items in itemsets are counted as items; an entire itemset is not counted
     * as one item. Any placeholder itemsets (itemsets with _, eg (_,b,c)) are also treated as individual elements,
     * with the exception that each item in a placeholder itemset is treated as its own unit: (_, x), which is not
     * equal to x. For example, (_, b) does not generate a +1 count for 'b', but rather for (_, b), all projected
     * itemsets that have 'b'. For multi-length itemsets, (_,b,c), each item is treated individually, but as above combo'ed
     * with the placeholder: (_, b) and (_, c). Thus, (_, b, c) contributes +1 to (_, b) and +1 to (_, c), but not to
     * support of 'c' and 'b' singleton itemsets (items). Ie, (_, b) matches only with 'b's in itemsets that have
     * a placeholder '_'.
     * Additionally, there is not the issue of 'placeholder confusion, where there is confusion as to what item a
     * placeholder is for. This is because there cannot be more than one placeholder in an itemset, by definition of
     * the placeholder, since the placeholder always marks the most recent projection.
     */
    public static List<Pair<Integer, Integer>> gen_f_list(Collection<List<Set<Integer>>> dataset) {
        // Comparator for building the F-list, compare first by frequency (desc) and then key value (asc).
        Comparator<Map.Entry<Integer, Integer>> comp = Comparator
            .comparing((Map.Entry<Integer, Integer> e)-> e.getValue())
            .thenComparing(e -> e.getKey());
//            .reversed();  comment this out: we want to project in canonical order (first in itemsets, not last)

        return _gen_f_list(dataset, comp);
    }

    // Generates the f_list using the given comparator to sort.
    public static List<Pair<Integer, Integer>> _gen_f_list(
        Collection<List<Set<Integer>>> dataset,
        Comparator<Map.Entry<Integer, Integer>> comp)
    {
        // Build frequency map of singleton items (elements). For PrefixSpan, we encode placeholder sets with -ve to their items.
        Map<Integer, Integer> freq_map = new HashMap<>();

        for (List<Set<Integer>> seq : dataset) {
            Set<Integer> seen_reg = new HashSet<>();
            Set<Integer> seen_plc = new HashSet<>();
            for (Collection<Integer> itemset : seq) {
                for (int i : itemset) {
                    if (itemset.contains(-1)) {
                        if (i != -1) {
                            if (! seen_plc.contains(i)) {
                                freq_map.put(-i, freq_map.getOrDefault(-i, 0) + 1);
                                seen_plc.add(i);
                            }
                        }
                    } else {
                        if (! seen_reg.contains(i)) {
                            freq_map.put(i, freq_map.getOrDefault(i, 0) + 1);
                            seen_reg.add(i);
                        }
                    }
                }
            }
        }

        // Build return F-list using frequency-map
        List<Pair<Integer, Integer>> freq_list = freq_map.entrySet().stream()
            .sorted(comp)
            .map(ent -> Pair.of(ent.getKey(), ent.getValue())).toList();

        return freq_list;
    }
}
