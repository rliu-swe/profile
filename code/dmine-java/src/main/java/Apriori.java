import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Performs the Apriori-algorithm on the input transactions. The transactions are expected in an input form of a
 * Map of integer keys (representing txn_id) to a list of integers (representing a list of the item.id's bought).
 *
 * @author rliu 2022-04
 */
public class Apriori {

    private Supplier<Collection<Set<Integer>>> data_supp;

    public Apriori(Supplier<Collection<Set<Integer>>> data_supp) {
        this.data_supp = data_supp;
    }


    /**
     * The per-level structure storing the Apriori algorithm results.
     * {@code pruned} is the starting fp's (size k).
     * {@code frequent} is the frequent fp's in {@code pruned} (size k).
     * {@code candidates} are the possible fp's aftr self-join of {@code frequent} (size: k+1). The pruned candidates are {@code pruned} in the next iteration.
     */
    public record AprioriLevelStruct(Set<Set<Integer>> pruned, Set<Set<Integer>> frequent, Set<Set<Integer>> candidates) {}

    /**
     * Mines the data source once for the frequent patterns found using the Apriori algorithm. Returns the apriori algorithm
     * information per level in a return list.
     */
    public List<AprioriLevelStruct> mine(int min_support_threshold) {
        List<AprioriLevelStruct> ret = new ArrayList<>();

        Collection<Set<Integer>> dataset = this.data_supp.get();

        // start with the list of 1-element candidate frequent items; we wrap each element into a set. This set will grow
        //   to size 2 (ie, {a, b}) at k=2, size 3 at k=3 (ie, {a, b, c}), etc.
        Set<Set<Integer>> li = ItemsetUtils.get_itemset_elements_as_singletons(dataset);

        Map<Set<Integer>, Integer> counts = new HashMap<>();

        while (! li.isEmpty()) {
            // todo : hash tree as a non-flat implementation (suppose you have 1M counters)
            // count the frquency of appearance of each k-sized fp in the itemsets
            for (Set<Integer> itemset : dataset) {
                for (Set<Integer> pattern : li) {
                    if (itemset.containsAll(pattern))
                        counts.put(pattern, counts.getOrDefault(pattern, 0) + 1);
                }
            }

            Set<Set<Integer>> li_frequent = li.stream().filter(pat -> counts.getOrDefault(pat, 0) >= min_support_threshold).collect(Collectors.toSet());

            // loop over the frequent patterns and generate the next set of k+1-sized candidates. This is called candidate-generation,
            // and there is a special algorithm for doing it. The candidate-generation algorithm generates the candidates through
            // self-join, but then the generated-candidate must also be pruned (by the anti-monotonicity property, no subsets
            // can be infrequent, or equivalently, all subsets of frequent k+1 patterns must also be frequent).

            Set<Set<Integer>> candidates = CandidateGeneration.Itemsets.self_join(li_frequent);
            Set<Set<Integer>> candidates_pruned = CandidateGeneration.Itemsets.prune(li_frequent, candidates);  // check frequency against (k-1)-subset

            ret.add(new AprioriLevelStruct(new HashSet<>(li), li_frequent, candidates));

            li = candidates_pruned;
        }

        return ret;
    }

}
