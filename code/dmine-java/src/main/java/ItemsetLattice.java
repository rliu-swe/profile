import java.util.*;
import java.util.stream.Collectors;

/**
 * A lattice for itemsets. The lattice is constructed bottom-up in tiers, with each added itemset assumed to be complete.
 * When all dependencies of an itemset have been satisfied, it automatically creates the next tier of nodes that depend
 * on it.
 *
 * Implementation notes:
 *   1. It is difficult to do this without referring to an explicit db-scan iteration identifier 'k'. This is because
 *      for any row (pattern), it is difficult to know by just looking at the current row number, whether to stop its
 *      counting or not. If you try to end up start_row-1, the pattern may not appear in that row, and so you'll miss its
 *      stopping criterion. Thus, it is easiest to pass the db-scan iteration identifier (ie, counter k).
 *   2. It is also difficult to do this recursively by brute-force recusrive transveral to all combinatorial subsets
 *      of the itemset. This brings trouble with support-counting, since the same subset may be visited multiple times
 *      from different different supersets: ex: {1,3} from {1,3,5,7} and {1,3,6,7}. The solution to this is a 'seen'
 *      map, but then instead of recursive traversal, might as well do iterative with an explicit stack.
 *
 * @author rliu 2022-04
 */
public class ItemsetLattice {

    public final Map<Set<Integer>, LatticeNode> global = new HashMap<>();
    public final LatticeNode root = new LatticeNode(0, 0, global, Set.of());
    public final int min_support_threshold;

    public ItemsetLattice(int min_support_threshold) {
        this.min_support_threshold = min_support_threshold;
    }


    public void countItemset(int k, int cur_row, Set<Integer> itemset) {
        List<LatticeNode> queue = new ArrayList<>();
        queue.add(root);

        // in each call of countItemset, there are multiple combinatorial paths to a LatticeNode;
        // but each LatticeNode, if visited, should only be incremented once.
        Set<LatticeNode> seen = new HashSet<>();

        while (queue.size() > 0) {
            LatticeNode cur = queue.remove(0);
            if (cur == root) {
                for (int i : itemset) {
                    LatticeNode n = cur.getOrAdd(k, cur_row, Set.of(i));
                    n.update_incr_on(k, cur_row);
                    n.increment();
                    if (n.support >= min_support_threshold)
                        queue.add(n);
                }
            }
            else {
                Set<Set<Integer>> k_groups = get_k_groups(cur.key.size() + 1, itemset);
                k_groups = k_groups.stream().filter(it -> it.containsAll(cur.key)).collect(Collectors.toSet());
                for (Set<Integer> it : k_groups) {
                    LatticeNode n = cur.getOrAdd(k, cur_row, it);
                    n.update_incr_on(k, cur_row);
                    if (! seen.contains(n)) {
                        n.increment();
                        seen.add(n);
                    }
                    if (n.support >= min_support_threshold)
                        queue.add(n);
                }
            }
        }

    }

    public static class LatticeNode {
        public final Map<Set<Integer>, LatticeNode> global;
        public final Set<Integer> key;
        public final Map<Set<Integer>, LatticeNode> childs = new LinkedHashMap<>();
        public final int start_row;
        public boolean incr_on = true;
        public boolean just_created = true;
        public int support = 0;
        public int k;

        public LatticeNode(int k, int cur_row, Map<Set<Integer>, LatticeNode> global, Set<Integer> key) {
            this.k = k;
            this.start_row = cur_row;
            this.global = global;
            this.key = key;
        }

        public LatticeNode getOrAdd(int k, int cur_row, Set<Integer> child_key) {
            LatticeNode c = this.childs.get(child_key);
            if (c == null) {
                // check lattice global map:
                c = this.global.get(child_key);
                if (c == null) {
                    c = new LatticeNode(k, cur_row, global, child_key);
                    this.global.put(child_key, c);
                }
                this.childs.put(child_key, c);
            }
            return c;
        }

        public void increment() {
            if (this.incr_on) {
                this.support++;
            }
        }

        public void update_incr_on(int k, int cur_row) {
            if (k != this.k) {
                if (cur_row == start_row) {
                    this.incr_on = false;
                }
            }
        }

        @Override
        public String toString() {
            return "" + this.key;
        }

    }

    public static Set<Set<Integer>> get_k_groups(int k, Set<Integer> itemset) {
        if (itemset.size() == k) {
            return Set.of(itemset);
        }
        else {
            Set<Set<Integer>> results = new HashSet<>();
            for (int i : itemset) {
                Set<Integer> subset = new HashSet<>(itemset);
                subset.remove(i);
                results.addAll(get_k_groups(k, subset));
            }
            return results;
        }
    }

}
