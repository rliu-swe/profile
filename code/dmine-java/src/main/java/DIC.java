import Utils.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Implementation of Dynamic Item Counting. An itemset lattice is used to track dependencies of individual k-length
 * itemsets. When all dependencies of a k-length itemset have been satisfied (a path exists from {} to it through only
 * satisfied (ie, frequent) itemsets, then its counting can begin).
 *
 * Author: Nurrachman Liu   2022-04
 */
public class DIC {

    private Supplier<Collection<Set<Integer>>> data_supp;

    public DIC(Supplier<Collection<Set<Integer>>> data_supp) {
        this.data_supp = data_supp;
    }


    /**
     * Mines the data source for all frequent patterns meeting the {@code min_support_threshold} using {@code num_k} database scans.
     * As this is a level-wise algorithm, the number of data scans corresponds to the maximum size of itemset desired to be mined
     * (i.e., {@code num_k=3} mines up to and only size-3 itemsets.
     */
    public List<Pair<Set<Integer>, Integer>> mine(int num_k, int min_support_threshold) {
        Collection<Set<Integer>> _dataset = this.data_supp.get();
        List<Set<Integer>> dataset = new ArrayList<>(_dataset);

        ItemsetLattice it = new ItemsetLattice(2);

        for (int k = 0; k < num_k; k++) {
            for (int i = 0; i < dataset.size(); i++) {
                it.countItemset(k, i, dataset.get(i));
            }
        }

        List<Pair<Set<Integer>, Integer>> freq_patterns = new HashSet<>(it.global.values()).stream()
            .filter(n -> n.support >= min_support_threshold)
            .map(n -> Pair.of(n.key, n.support))
            .toList();

        return freq_patterns;
    }


}
