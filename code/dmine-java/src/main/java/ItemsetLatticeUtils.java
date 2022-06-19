import Utils.Pair;

import java.util.*;
import java.util.function.Function;

/**
 * Utils for ItemsetLattices.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class ItemsetLatticeUtils {

    public static final int TABWIDTH = 4;

    public static Function<ItemsetLattice.LatticeNode, String> to_str = (n) -> n.key.stream().sorted().toList().toString() + ": " + n.support;

    public static String print(ItemsetLattice it) {
        StringBuilder sb = new StringBuilder();

        ItemsetLattice.LatticeNode root = it.root;
        List<Pair<Integer, ItemsetLattice.LatticeNode>> queue = new ArrayList<>();

        queue.add(Pair.of(0, root));

        while (queue.size() > 0) {
            Pair<Integer, ItemsetLattice.LatticeNode> cur = queue.remove(queue.size()-1);
            sb.append(" ".repeat(cur.left)).append(to_str.apply(cur.right)).append("\n");
            List<ItemsetLattice.LatticeNode> childs = new ArrayList<>(cur.right.childs.values());
            Collections.sort(childs, (o1, o2) ->
                o2.key.stream().sorted().toList().toString().compareTo(
                    o1.key.stream().sorted().toList().toString())
            );
            for (ItemsetLattice.LatticeNode c : childs)
                queue.add(Pair.of(cur.left + TABWIDTH, c));
        }

        return sb.toString();
    }

}
