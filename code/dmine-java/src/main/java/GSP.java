import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * GSP is the Apriori-style (level-wise) algorithm for sequential pattern-mining.
 *
 * @author rliu 2022-04
 */
public class GSP {
    private Supplier<Collection<List<Set<Integer>>>> data_supp;

    public GSP(Supplier<Collection<List<Set<Integer>>>> data_supp) {
        this.data_supp = data_supp;
    }

    /**
     * The per-level structure storing the Apriori algorithm results.
     * {@code pruned} is the starting fp's (size k).
     * {@code frequent_pruned} is the frequent fp's in {@code pruned} (size k).
     * {@code candidates} are the possible fp's aftr self-join of {@code frequent} (size: k+1). The pruned candidates are {@code pruned} in the next iteration.
     * {@code frequent_candidates} are the frequent fp's in the {@code candidates}.
     */
    public record GSPLevelStruct(Set<List<Set<Integer>>> pruned, Set<List<Set<Integer>>> frequent_pruned, Set<List<Set<Integer>>> candidates) {} //, Set<List<Set<Integer>>> frequent_candidates) {}

    public List<GSPLevelStruct> mine(int min_supp) {
        List<GSPLevelStruct> ret = new ArrayList<>();

        Collection<List<Set<Integer>>> dataset = this.data_supp.get();

        // start with the list of 1-element candidate frequent items; we wrap each element into a set li.
        Set<List<Set<Integer>>> li = ItemsetUtils.get_seq_elements_as_singletons(dataset);

        Map<List<Set<Integer>>, Integer> counts = new HashMap<>();

        while (! li.isEmpty()) {
            // todo : hash tree as a non-flat implementation (suppose you have 1M counters)
            // count the frquency of appearance of each k-sized fp in the itemsets
            for (List<Set<Integer>> row : dataset) {
                for (List<Set<Integer>> seq : li) {
//                    if (SeqUtils.seqIsInRow(row, seq))
//                        counts.put(seq, counts.getOrDefault(seq, 0) + 1);
                }
            }

            Set<List<Set<Integer>>> li_frequent = li.stream().filter(pat -> counts.getOrDefault(pat, 0) >= min_supp).collect(Collectors.toSet());

            Set<List<Set<Integer>>> candidates = CandidateGeneration.Sequential.self_join(li_frequent);

            // Different from Apriori, the pruning counts the support of candidate sequences (thus using the dataset).
            // This is as opposed to Apriori which uses membership in the L_{k-1} set rather than support in the orig. dataset.
            Set<List<Set<Integer>>> candidates_pruned = CandidateGeneration.Sequential.prune(dataset, candidates, min_supp);

            ret.add(new GSPLevelStruct(new HashSet<>(li), li_frequent, candidates));

            li = candidates_pruned;
//            li = candidates;
        }

        return ret;
    }

}
