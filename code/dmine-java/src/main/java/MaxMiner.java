import Utils.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Implementation of the MaxMiner algorithm. It is a level-wise approach that uses the same concept as FPGrowth in the
 * sense of building a set-enumeration tree in the order of frequency, such that the smallest support items will have
 * the minimal-sized projected databases (ie, project in the order: supp(z) < supp(y) < .. < supp(a)). However, it is
 * more aggressive implementation in that, because it only seeks to find max-patterns, it does more aggressive culling:
 * it checks first whether the union pattern of all li candidates meets the minimum support threshold; if so, then the
 * max pattern exists, and it skips (the effort of) counting the support of the original li candidates. Thus, MaxMiner
 * produces a support count of only the max-patterns.
 *
 * @author rliu 2022-04
 */
public class MaxMiner {
    private Supplier<Collection<Set<Integer>>> data_supp;

    public MaxMiner(Supplier<Collection<Set<Integer>>> data_supp) {
        this.data_supp = data_supp;
    }

    /**
     * Mines the data source once for the max-patterns using a level-wise approach. The built set-enumeration tree
     * can then be scanned separately for the max-patterns. This is a modified Apriori algorithm level-wise loop.
     */
    public List<Apriori.AprioriLevelStruct> mine(int min_support_threshold) {
        List<Apriori.AprioriLevelStruct> ret = new ArrayList<>();

        Collection<Set<Integer>> dataset = this.data_supp.get();

        // start with the list of 1-element candidate frequent items; we wrap each element into a set. This set will grow
        //   to size 2 (ie, {a, b}) at k=2, size 3 at k=3 (ie, {a, b, c}), etc.
        Set<Set<Integer>> li = ItemsetUtils.get_itemset_elements_as_singletons(dataset);

        Map<Set<Integer>, Integer> counts = new HashMap<>();

        while (! li.isEmpty()) {
            // Max-miner: first check the support of the union of all items in li (ex: 'abcde' of 'ab', 'ac', 'ad', 'ae');
            //   if the union meets min threshold, then skip checking the original li patterns.
            List<Pair<Set<Set<Integer>>, Set<Integer>>> max_pats = MaxPatterns.findMaxPatterns(li);

            for (Set<Integer> itemset : dataset)
                for (Set<Integer> max_p : max_pats.stream().map(p -> p.right).toList())
                    if (itemset.containsAll(max_p))
                        counts.put(max_p, counts.getOrDefault(max_p, 0) + 1);

            // Max-miner: if the max-pattern meets min-support-threshold, then we skip checking the li candidate items.
            Set<Set<Integer>> li_frequent = new LinkedHashSet<>();
            for (Pair<Set<Set<Integer>>, Set<Integer>> p : max_pats) {
                Set<Integer> max_p = p.right;
                if (counts.getOrDefault(max_p, 0) >= min_support_threshold) {
                    li_frequent.add(max_p);
                }
                else {
                    Set<Set<Integer>> li_subset = p.left;
                    for (Set<Integer> itemset : dataset) {
                        for (Set<Integer> pattern : li_subset) {
                            if (itemset.containsAll(pattern))
                                counts.put(pattern, counts.getOrDefault(pattern, 0) + 1);
                        }
                    }
                    li_frequent.addAll(li_subset.stream().filter(pat -> counts.getOrDefault(pat, 0) >= min_support_threshold).collect(Collectors.toSet()));
                }
            }


            // loop over the frequent patterns and generate the next set of k+1-sized candidates. This is called candidate-generation,
            // and there is a special algorithm for doing it. The candidate-generation algorithm generates the candidates through
            // self-join, but then the generated-candidate must also be pruned (by the anti-monotonicity property, no subsets
            // can be infrequent, or equivalently, all subsets of frequent k+1 patterns must also be frequent).

            Set<Set<Integer>> candidates = CandidateGeneration.Itemsets.self_join(li_frequent);
            Set<Set<Integer>> candidates_pruned = CandidateGeneration.Itemsets.prune(li_frequent, candidates);

            ret.add(new Apriori.AprioriLevelStruct(new HashSet<>(li), li_frequent, candidates));

            li = candidates_pruned;
        }

        return ret;
    }

}
