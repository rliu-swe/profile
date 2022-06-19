import Utils.Pair;

import java.util.*;
import java.util.function.Supplier;

/**
 * Implementation of the FP-Growth algorithm, a depth-first based frequent pattern mining. The fundamental idea is that
 * to find child Xy of X, only a search in the X-projected database is needed, which saves alot of potential
 * search over all transactions. Then, find the ordering the singleton items (i.e., elements 'a', 'b', 'c', etc) by
 * frequency, and apply this ordering to all transactions. Then, build the FP-tree using the re-ordered transactions;
 * a depth-first traversal of this FP-tree is thus equivalent to looking at only projected databases in the inverse order
 * of the descending frequency order of the singleton elements found above. This descending frequency order of the singleton
 * elements is encoded in the FP-list, which also serves as the linked-list headers for linked-lists that link directly to
 * all nodes of its respective element.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class FPGrowth {

    private Supplier<Collection<Set<Integer>>> data_supp;

    private List<FPTree.FPTreeNode> f_list = new ArrayList<>();
    private FPTree fptree = new FPTree();

    public FPGrowth(Supplier<Collection<Set<Integer>>> data_supp) {
        this.data_supp = data_supp;
    }

    public FPTree getFPTree() {
        return this.fptree;
    }

    public List<FPTree.FPTreeNode> getFList() {
        return this.f_list;
    }

    public void mine(int min_supp) {
        // Convert data source to ordered list
        List<Set<Integer>> dataset = new ArrayList<>(this.data_supp.get());

        // These are the linked-list headers, but note that they are NOT inserted into the FP-tree (it doesn't know about these).
        f_list = FPGrowthUtils.gen_f_list_as_fpnodes(dataset);
        f_list = f_list.stream().filter(n -> n.support >= min_supp).toList();

        // Sort every pattern in the txn database by the freq_list:
        List<List<Integer>> dataset_sorted = new ArrayList<>();
        for (Set<Integer> pat : dataset)
            dataset_sorted.add(FPGrowthUtils.sortItemsetUsingFlist(pat, f_list));

        // Insert every (sorted) pattern into the FP-tree:
        for (List<Integer> pat : dataset_sorted) {
            List<FPTree.FPTreeNode> ret = fptree.insertPattern(pat);

            // Add every returned node to the linked-list
            for (FPTree.FPTreeNode n : ret) {
                FPTree.FPTreeNode found = null;
                for (FPTree.FPTreeNode f : f_list) {
                    if (f.key == n.key) {
                        found = f;
                        break;
                    }
                }
                if (found != null) {
                    FPTree.FPTreeNode prev = null;
                    FPTree.FPTreeNode next = found;
                    while (next != null) {
                        prev = next;
                        next = next.next;
                    }
                    prev.next = n;
                    n.next = null;
                    n.prev = prev;
                }
            }
        }
    }


}
