import Utils.Pair;

import java.util.*;

/**
 * Utilities for FP-Growth.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class FPGrowthUtils {

    /**
     * Generates the downwards itemsets starting at the given fptree node (ie, the datasets corresponding to the
     * projected database at {@code FPTreeNode node}.
     */
    public static List<Set<Integer>> gen_itemsets(FPTree.FPTreeNode node) {
        List<Set<Integer>> n_proj_itemsets = new ArrayList<>();

        List<Pair<Set<Integer>, FPTree.FPTreeNode>> stack = new ArrayList<>();

        // we do *not* add node.key to the new path, since it is a given that the initial
        //    node.key will be a part of every returned result from the projected database.
        stack.add(Pair.of(new HashSet<>(), node));

        while (stack.size() > 0) {
            Pair<Set<Integer>, FPTree.FPTreeNode> cur = stack.remove(stack.size() - 1);
            Set<Integer> cur_path = cur.left;
            FPTree.FPTreeNode cur_node = cur.right;

            cur_node.childs.entrySet().forEach(ent -> {
                Set<Integer> new_itemset = new HashSet<>(cur_path);
                new_itemset.add(ent.getKey());
                n_proj_itemsets.add(new_itemset);
                stack.add(Pair.of(new_itemset, ent.getValue()));
            });
        }

        return n_proj_itemsets;
    }

    public static List<FPTree.FPTreeNode> filter_by_min_supp(List<FPTree.FPTreeNode> f_list, int min_supp) {
        return f_list.stream().filter(n -> n.support >= min_supp).toList();
    }

    /**
     * Returns the generated f-list converted into FPTreeNodes.
     */
    public static List<FPTree.FPTreeNode> gen_f_list_as_fpnodes(Collection<Set<Integer>> dataset, int min_supp) {
        return filter_by_min_supp(gen_f_list_as_fpnodes(dataset), min_supp);
    }

    public static List<FPTree.FPTreeNode> gen_f_list_as_fpnodes(FPTree.FPTreeNode node) {
        List<Pair<Integer, Integer>> freq_list = gen_f_list(node);
        return freq_list.stream().map(p -> new FPTree.FPTreeNode(p.left, p.right)).toList();
    }

    /**
     * Returns the generated f-list converted into FPTreeNodes.
     */
    public static List<FPTree.FPTreeNode> gen_f_list_as_fpnodes(Collection<Set<Integer>> dataset) {
        List<Pair<Integer, Integer>> freq_list = gen_f_list(dataset);
        return freq_list.stream().map(p -> new FPTree.FPTreeNode(p.left, p.right)).toList();
    }

    /**
     * Generates the f_list starting at the given FPTreeNode {@code node}.
     */
    public static List<Pair<Integer, Integer>> gen_f_list(FPTree.FPTreeNode node) {
        Map<Integer, Integer> counts = new HashMap<>();
        List<FPTree.FPTreeNode> stack = new ArrayList<>();
        stack.add(node);
        while (! stack.isEmpty()) {
            FPTree.FPTreeNode cur = stack.remove(stack.size() - 1);
            counts.put(cur.key, counts.getOrDefault(cur.key, 0) + 1);
            stack.addAll(cur.childs.values());
        }
        List<Pair<Integer, Integer>> f_list = counts.entrySet().stream()
            .map(e -> Pair.of(e.getKey(), e.getValue())).sorted((p1, p2) -> p2.right - p1.right).toList();
        return f_list;
    }

    /**
     * Given a set of txn rows, returns a list of the singleton keys of the frequent items, paired with support.
     */
    public static List<Pair<Integer, Integer>> gen_f_list(Collection<Set<Integer>> dataset) {
        // Comparartor for building the F-list, compare first by frequency (desc) and then key value (asc).
        Comparator<Map.Entry<Integer, Integer>> comp = Comparator
            .comparing((Map.Entry<Integer, Integer> e)-> e.getValue())
            .reversed()
            .thenComparing(e -> e.getKey());
        return _gen_f_list(dataset, comp);
    }

    public static List<Pair<Integer, Integer>> gen_f_list_rev(Collection<Set<Integer>> dataset) {
        // Comparartor for building the F-list, compare first by frequency (desc) and then key value (asc).
        Comparator<Map.Entry<Integer, Integer>> comp = Comparator
            .comparing((Map.Entry<Integer, Integer> e)-> e.getValue())
            .thenComparing(e -> e.getKey())
            .reversed();
        List<Pair<Integer, Integer>> ret = new ArrayList<>(_gen_f_list(dataset, comp));
        Collections.reverse(ret);
        return ret;
    }

    // Generates the f_list using the given comparator to sort.
    public static List<Pair<Integer, Integer>> _gen_f_list(
        Collection<Set<Integer>> dataset,
        Comparator<Map.Entry<Integer, Integer>> comp)
    {
        // Build frequency map of singleton items (elements)
        Map<Integer, Integer> freq_map = new HashMap<>();
        for (Set<Integer> pat : dataset)
            for (int key : pat)
                freq_map.put(key, freq_map.getOrDefault(key, 0)+1);

        // Build return F-list using frequency-map
        List<Pair<Integer, Integer>> freq_list = freq_map.entrySet().stream()
            .sorted(comp)
            .map(ent -> Pair.of(ent.getKey(), ent.getValue())).toList();

        return freq_list;
    }

    /**
     * Returns the itemset {@code it} sorted in order according to the {@code f_list}. The {@code f_list} is assumed
     * to be sorted in desc order (ie, index 0 holds the most frequent item).
     */
    public static List<Integer> sortItemsetUsingFlist(Set<Integer> it, List<FPTree.FPTreeNode> f_list) {
        List<Integer> it_sorted = new ArrayList<>();
        for (int i : f_list.stream().map(n -> n.key).toList())
            if (it.contains(i))
                it_sorted.add(i);
        return it_sorted;
    }


}
