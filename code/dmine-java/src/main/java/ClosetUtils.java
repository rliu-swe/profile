import Utils.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utilties related to Closed Patterns for the Closet algorithm.
 *
 * @author rliu 2022-04
 */
public class ClosetUtils {

    /**
     * Recursively mines starting at {@code node} for straight-line paths that represent closed itemsets.
     */
    public static List<Pair<Set<Integer>, Integer>> extract_straight_fps(FPTree.FPTreeNode node, Pair<Set<Integer>, Integer> running) {
        List<Pair<Set<Integer>, Integer>> cfps = new ArrayList<>();

        if (node.childs.size() == 0) {
            return cfps;
        }

        FPTree.FPTreeNode n = node;
        while (n.childs.size() == 1) {
            if (running.right == n.support)
                running.left.add(n.key);
            else {
                // the chain continues but the support changes (decreases, by construction of fp-tree) to that of this node.
                if (running.right >= n.support) {
                    // It is impossible for the support to be increasing in a depth-first traversal downwards. This filters out the necessary
                    //    cases of starting the chain-up from an empty set and the root node.
                    cfps.add(running);
                }
                Set<Integer> new_chain = new HashSet<>(running.left);
                if (n.key != -1) // the root doesn't count
                    new_chain.add(n.key);
                running = Pair.of(new_chain, n.support);
            }
            n = n.childs.values().iterator().next();
        }

        // the chain just ended; add the running pair to the return set
        if (running.right == n.support)
            if (n.key != -1)  // the root doesn't count
                running.left.add(n.key);
        else {
            if (running.right >= n.support)
                cfps.add(running);
            Set<Integer> new_chain = new HashSet<>(running.left);
            new_chain.add(n.key);
            new_chain.add(n.key);
            running = Pair.of(new_chain, n.support);
        }
        cfps.add(running);

        // recursively branch to the children:
        for (FPTree.FPTreeNode n_ch : n.childs.values()) {
            cfps.addAll(extract_straight_fps(n_ch, running));
        }

        return cfps;
    }


}
