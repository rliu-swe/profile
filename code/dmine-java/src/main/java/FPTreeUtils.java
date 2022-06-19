import Utils.Pair;

import java.util.*;
import java.util.function.Function;

/**
 * Utilities for FP-Tree's and FPGrowth.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class FPTreeUtils {

    public static final int TABWIDTH = 4;

    public static Function<FPTree.FPTreeNode, String> to_str = (n) -> n.key + ": " + n.support;

    public static String print(FPTree fptree) {
        StringBuilder sb = new StringBuilder();

        FPTree.FPTreeNode root = fptree.root;
        List<Pair<Integer, FPTree.FPTreeNode>> queue = new ArrayList<>();

        queue.add(Pair.of(0, root));

        while (queue.size() > 0) {
            Pair<Integer, FPTree.FPTreeNode> cur = queue.remove(queue.size()-1);

            sb.append(" ".repeat(cur.left)).append(to_str.apply(cur.right)).append("\n");

            List<FPTree.FPTreeNode> childs = new ArrayList<>(cur.right.childs.values());
            Collections.sort(childs, Comparator.comparingInt(o -> o.key));
            for (FPTree.FPTreeNode c : childs)
                queue.add(Pair.of(cur.left + TABWIDTH, c));
        }

        return sb.toString();
    }

    public static String print(FPGrowth fpgrowth) {
        StringBuilder sb = new StringBuilder();

        sb.append(print(fpgrowth.getFPTree()));
        sb.append("\n");
        for (FPTree.FPTreeNode n : fpgrowth.getFList()) {
            int i = 0;
            while (n != null) {
                if (i++ > 0) sb.append(" -> ");
                sb.append(to_str.apply(n));
                n = n.next;
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
