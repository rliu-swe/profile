import Utils.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * An implementation of the PrefixSpan algorithm. The PrefixSpan algorithm recursively mines the projected databases
 * to count the frequent patterns.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class PrefixSpan {

    private Supplier<Collection<List<Set<Integer>>>> data_supp;

    public PrefixSpan(Supplier<Collection<List<Set<Integer>>>> data_supp) {
        this.data_supp = data_supp;
    }

    /**
     *
     */
    public Set<Pair<List<Set<Integer>>, Integer>> recursive_mine(int min_supp, List<List<Set<Integer>>> db) {
        Set<Pair<List<Set<Integer>>, Integer>> locally_freq = new HashSet<>();

        List<Pair<Integer, Integer>> f_list;

        // Gather the f_list
        f_list = PatternGrowthUtils.gen_f_list(db);

        // Filter the f_list down to only the frequent items
        f_list = f_list.stream().filter(p -> p.right >= min_supp).toList();

        // The items in f_list are locally-frequent, so add them:
        for (Pair<Integer, Integer> p : f_list) {
            int item = p.left;
            if (item < 0)
                locally_freq.add(Pair.of(List.of(Set.of(-1, -item)), p.right));
            else
                locally_freq.add(Pair.of(List.of(Set.of(item)), p.right));
        }

        // For each item in the f_list, we recursively project on it and recursively mine it:
        for (Pair<Integer, Integer> p : f_list) {
            int item = p.left;
            List<Set<Integer>> proj_var2;
            if (item < 0) {
                // this was a placeholder, ie, (_, item)
                proj_var2 = List.of(Set.of(-1, -item));
            } else {
                // this was a regular item, ie, item
                proj_var2 = List.of(Set.of(item));
            }

            List<List<Set<Integer>>> cdb = new ArrayList<>();
            for (List<Set<Integer>> seq : db) {
                List<List<Integer>> row;
                try {
                    row = SeqUtils.project_on_seq(proj_var2, seq);
                }
                catch (RuntimeException re) {
                    // if our projected var isn't in the row, skip the row entirely (ie, don't add it to the cdb)
                    continue;
                }

                if (row.size() == 0)
                    continue;

                List<Set<Integer>> row2 = new ArrayList<>();
                for (List<Integer> itemset : row) {
                    Set<Integer> itemset2 = new HashSet<>(itemset);
                    row2.add(itemset2);
                }
                cdb.add(row2);
            }

            Set<Pair<List<Set<Integer>>, Integer>> ret = recursive_mine(min_supp, cdb);

            if (ret.size() == 0) {
                // base case: if nothing was returned, then just add item
                if (item < 0)
                    locally_freq.add(Pair.of(List.of(Set.of(-1, -item)), p.right)); // this should never be hit; you can't project on the last item in an itemset, given how the project is written.
                else
                    locally_freq.add(Pair.of(List.of(Set.of(item)), p.right));
            }
            else {
                // append item to every returned element
                for (Pair<List<Set<Integer>>, Integer> rp : ret) {
                    List<Set<Integer>> new_seq = new ArrayList<>(rp.left);
                    if (item < 0) {
                        new_seq.add(0, Set.of(-1, -item));
                    }
                    else {
                        if (new_seq.get(0).contains(-1)) {
                            // if the first of the returned sequence has a -1, that means we just returned from a projection;
                            //   so the currently projected item should replace the -1 that was just returned in the returned sequence.
                            Set<Integer> merged_itemset = new HashSet<>(new_seq.get(0));
                            merged_itemset.remove(-1);
                            merged_itemset.add(item);
                            new_seq.set(0, merged_itemset);
                        }
                        else
                            new_seq.add(0, Set.of(item));
                    }
                    locally_freq.add(Pair.of(new_seq, rp.right));
                }
            }
        }

        return locally_freq;
    }

    /**
     * Top-level entry to recursive_mine.
     */
    public Set<Pair<List<Set<Integer>>, Integer>> mine(int min_supp) {
        // Convert data source to ordered list
        List<List<Set<Integer>>> dataset = new ArrayList<>(this.data_supp.get());
        return recursive_mine(min_supp, dataset);
    }

}
