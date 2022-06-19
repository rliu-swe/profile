import Utils.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Implementation of Closet: mining of frequent closed patterns. Closet is a depth-first style algorithm using similar
 * ideas as FP-growth. However, instead of sorting every itemset by the global f_list, Closet builds a set-enumeration
 * tree using FPTree but with the itemsets explicitly ordered (inserted into the FPTree) according to the discrete partition
 * of the entire space into disjoint subsets split by the singleton elements (all elements in dataset). The ordering of
 * *insertion* (not just visitation, as in FPGrowth) into this fptree will be in the inverse-order of the f_list: ie,
 * the least frequent global singleton element first.
 * An example of the disjoint subsets (ie, every sub-tree is a non-overlapping partition of the space) is, assuming the
 * F-list is:  (most frequent) e c a d c (least frequent):
 *          'a' but not 'd', 'c' but not 'a', 'd', 'e' but not 'a', 'd', 'c'.
 *
 * @author rliu 2022-04
 */
public class Closet {
    private Supplier<Collection<Set<Integer>>> data_supp;

    private Set<Pair<Set<Integer>, Integer>> fci = new LinkedHashSet<>();

    public Closet(Supplier<Collection<Set<Integer>>> data_supp) {
        this.data_supp = data_supp;
    }


    public Set<Pair<Set<Integer>, Integer>> getFCI() {
        return this.fci;
    }


    /**
     * Top-level closet algorithm entry point.
     */
    public List<Pair<Set<Integer>, Integer>> closet(int min_sup) {
        List<Set<Integer>> tdb = new ArrayList<>(this.data_supp.get());  // Convert data source to ordered list
        List<FPTree.FPTreeNode> f_list = FPGrowthUtils.gen_f_list_as_fpnodes(tdb, min_sup);
        return _closet(min_sup, Set.of(), tdb, f_list, fci);
    }

    /**
     * The Closet recursive algorithm.
     * @param x the itemset that the db is conditioned on, yielding {@code proj_db}, the projected database.
     * @param cdb the projected database computed by projecting tdb by {@code x}.
     * @param f_list the frequent item list of the projected db {@code proj_db}
     * @param fci the globally seen set of frequent closed itemsets already found.
     * @return the set of frequent closed itemsets found, paired with their support.
     */
    public List<Pair<Set<Integer>, Integer>> _closet(
        int min_sup,
        Set<Integer> x,
        Collection<Set<Integer>> cdb,
//        FPTree.FPTreeNode cdb,  // the projected-db 'cdb' should be an fp-tree built that matches with the given f-list
        List<FPTree.FPTreeNode> f_list,
        Set<Pair<Set<Integer>, Integer>> fci)
    {
        // Let Y be the set of items in f_list that appear in every txn in proj_db.
        Set<Integer> y = new LinkedHashSet<>();
        for (FPTree.FPTreeNode fn : f_list) {
            boolean all_contains = true;
            for (Set<Integer> row : cdb) {
                if (! row.contains(fn.key)) {
                    all_contains = false;
                    break;
                }
            }
            if (all_contains)
                y.add(fn.key);
        }
        // y supp is always cdb.size(), even if y is empty; since the projected x is in every row anyways, and then x is unioned with y.
        int y_supp = cdb.size();

        Set<Integer> x_u_y = new HashSet<>(y);
        x_u_y.addAll(x);

        // Insert X U Y into FCI if it (X U Y) is not a proper subset of some itemset in FCI with the same support.
        boolean is_subset = false;
        for (Pair<Set<Integer>, Integer> fci_p : fci) {
            if (fci_p.left.containsAll(x_u_y) && fci_p.right == y_supp) {
                is_subset = true;
                break;
            }
        }
        if (! is_subset)
            if (! x_u_y.isEmpty())
                fci.add(Pair.of(x_u_y, y_supp));

        // Build FP-tree for proj-db:
        FPTree fptree = new FPTree();

        for (Set<Integer> row : cdb) {
            List<Integer> row_sorted = FPGrowthUtils.sortItemsetUsingFlist(row, f_list);
            fptree.insertPattern(row_sorted);
        }

        // Extract frequent closed itemsets using Optimization 3
        List<Pair<Set<Integer>, Integer>> direct_cfps = ClosetUtils.extract_straight_fps(fptree.root, Pair.of(new HashSet<>(), -1));

        // Calculate the remaining f_list items that were not in Y:
        List<FPTree.FPTreeNode> f_list2 = new ArrayList<>(f_list.stream().filter(n -> !y.contains(n.key)).toList());
        // Walk through the projected databases by the smallest first
        Collections.reverse(f_list2);

        for (FPTree.FPTreeNode i : f_list2) {
            Set<Integer> ix = new HashSet<>(x);
            ix.add(i.key);
            if (fci.stream().noneMatch(p -> p.left.containsAll(ix) && p.right == i.support)) {  // i.support will be the correct support locally for i, as long as f_list was generated correctly when passed in
                // conditional database conditioned on ix will be the local fptree.root.get(i)
                //FPTree.FPTreeNode cdb = fptree.root.get(i.key);
                List<Set<Integer>> cdb2 = ItemsetUtils.gen_projected_db(Set.of(i.key), cdb);
                List<FPTree.FPTreeNode> f_list_cdb2 = FPGrowthUtils.gen_f_list_as_fpnodes(cdb2, min_sup);
                _closet(min_sup, ix, cdb2, f_list_cdb2, fci);
            }
        }

        return null;
    }

//
// My original implementation:
//
//    /**
//     * Given a key (2nd level singleton-element), starting at the FPTree node corresponding to this key is equivalent
//     * to searching this key-projected database. Therefore, we can search downwards recursively from the key's FPTreeNode
//     * to find all paths in which the support is constant along the path (ie, once you add a node to the path and the
//     * path's support changes, then the path before adding that element, is the closed-pattern). Essentially, a closed-pattern
//     * is a leaf in the set-enumeration tree (resulting FP-tree after running the FPGrowth algorithm).
//     */
//    public List<Pair<Set<Integer>, Integer>> getLocalClosedPatterns(FPTree fptree, Integer key) {
//        List<Pair<Set<Integer>, Integer>> results = new ArrayList<>();
//
//        FPTree.FPTreeNode cur = fptree.root.get(key);
//        if (cur == null)
//            throw new RuntimeException(String.format("Error: fptree does not contain the key '%s' from the fpgrowth's flist.", key));
//
//
//        // algorithm
//        // 1. start with candidates being all leaves of the set-enumeration tree
//        // 1.5.  early-pruning:  all candidates with support < min_supp_threshold can be eliminated.
//        // 2. any candidate that is a subset of any other candidate, both having the *same* support, can be eliminated
//        // 3. traverse backwards up the tree from each candidate; all nodes reached will be subsets of the candidate,
//        //    but if the node has a *different* support than the candidate, then the node is another closed-pattern.
//
//        List<Pair<FPTree.FPTreeNode, Integer>> queue = new ArrayList<>();
//        queue.add(Pair.of(cur, cur.support));
//        while (queue.size() > 0) {
//            Pair<FPTree.FPTreeNode, Integer> cur_p = queue.remove(0);
//            cur = cur_p.left;
//            int cur_supp_chain = cur_p.right;
//            for (FPTree.FPTreeNode c : cur.childs.values()) {
//                if (c.support == cur_supp_chain) {
//                }
//            }
//        }
//
//        return results;
//    }
//
//    /**
//     * Returns a list of closed patterns, paired with their support.
//     */
//    public List<Pair<Set<Integer>, Integer>> mine(int min_support_threshold) {
//        // Convert data source to ordered list
//        List<Set<Integer>> dataset = new ArrayList<>(this.data_supp.get());
//
//        List<Pair<Integer, Integer>> freq_list_rev;
//        freq_list_rev = FPGrowthUtils.gen_f_list_rev(dataset);
//        freq_list_rev = freq_list_rev.stream().filter(p -> p.right >= min_support_threshold).toList();
//        f_list_rev = freq_list_rev.stream().map(p -> new FPTree.FPTreeNode(p.left, p.right)).toList();
//
//        // Project-out all infrequent singleton elements from all itemsets:
//        Set<Integer> f_list_keys = f_list_rev.stream().map(n -> n.key).collect(Collectors.toSet());
//        List<Set<Integer>> dataset2 = dataset.stream().map(it -> it.stream().filter(f_list_keys::contains).collect(Collectors.toSet())).toList();
//
//        // Visit the x-projected databases in reverse order of the f-list; each projection modifies the itemsets.
//        for (FPTree.FPTreeNode n : f_list_rev) {
//            for (Set<Integer> itemset : dataset2) {
//                if (itemset.contains(n.key)) {  // we are operating in the n.key-projected db: this filters the dataset to the n.key-projected db
//                    Set<Integer> itemset2 = new HashSet<>(itemset);
//                    itemset2.remove(n.key);
//                    List<Integer> it_sorted = new ArrayList<>(itemset2);
//                    Collections.sort(it_sorted);  // use natural ordering (1,2,3..)
//                    it_sorted.add(0, n.key);  // insert the current n.key at front of list so that fptree.insert uses that as the root-element node.
//                    fptree.insertPattern(it_sorted);
//                }
//            }
//            // project-out n.key from every itemset
//            dataset2 = dataset2.stream().map(it -> it.stream().filter(e -> e != n.key).collect(Collectors.toSet())).toList();
//        }
//
//        return null;
//    }

}
