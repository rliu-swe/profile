import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The FP-tree is inserted in the order of descending frequency of the elements (singleton items) in the txn database.
 * This insertion order is imposed by the caller, but once passed to the FPTree for construction, the order given is
 * fixed into the FP-tree.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class FPTree {

    public final FPTreeNode root = new FPTreeNode(-1);

    /**
     * Inserts the given pattern in order (expected order is thus descending: ie, most frequent key at index 0, least
     * frequent key at the last index) into the FP-tree. Returns the list of FPTreeNodes it added or found for each
     * key in {@code pattern}, in the same order as given in {@code pattern}. This list can be used to update the
     * linked-lists for the elements.
     */
    public List<FPTreeNode> insertPattern(List<Integer> pattern) {
        List<FPTreeNode> nodes = new ArrayList<>();
        FPTreeNode cur = root;
        for (int key : pattern) {
            cur = cur.getOrAdd(key);
            cur.support++;
            nodes.add(cur);
        }
        return nodes;
    }

    /**
     * A FPTreeNode is both a node in the FP-tree as well as a node in a doubly-linked-list that connects all
     * nodes with the same key as itself.
     */
    public static class FPTreeNode {
        public FPTreeNode next = null;  // linked list pointer to the next such same-element FP-tree node.
        public FPTreeNode prev = null;  // linked list pointer backwards to the linking same-element FP-tree node.
        public Map<Integer, FPTreeNode> childs = new LinkedHashMap<>();
        public int key;
        public int support = 0;

        public FPTreeNode(int key) {
            this.key = key;
        }

        public FPTreeNode(int key, int support) {
            this.key = key;
            this.support = support;
        }

        public FPTreeNode get(int key) {
            return childs.getOrDefault(key, null);
        }

        public FPTreeNode getOrAdd(int key) {
            FPTreeNode find = childs.getOrDefault(key, null);
            if (find == null) {
                find = new FPTreeNode(key);
                childs.put(key, find);
            }
            return find;
        }
    }

}
