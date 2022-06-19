import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Utilities for Nodes of the graph.
 *
 * @author rliu 2022-03
 */
public class NodeUtils {

    public static final Function<Node, String> NAME_ID = n -> "" + n.getName() + "(" + n.getId() + ")";

    /**
     *  Updates all children of node {@code n} to point to node {@code n}. This is needed when node {@code n} is a
     *  newly created copy of a node; thus {@code n} is assumed to be a newly duplicated node (n = new Node(orig_node)).
     *  All copies of nodes made will reside in returned set. This can be used for top-level updating of a graph's nodes.
     */
    public static Set<Node> recursiveUpdateChildren(Node n, Set<Node> seen) {
        // This method only works because Node equality is based on Discrete Variable equality (which is based on varname)

        seen.add(n);

        for (Node n_ch : n.getChildren().stream().filter(seen::contains).toList()) {
            Node n_ch2 = seen.stream().filter(_n_ch2 -> _n_ch2.equals(n_ch)).findFirst().get();
            n_ch2.getParents().remove(n);
            n_ch2.getParents().add(n);
            n.getChildren().remove(n_ch2);
            n.getChildren().add(n_ch2);
        }

        // Only create copies of unseen children. This prevents cycles.
        Set<Node> ch_copies = n.getChildren().stream()
            .filter(Predicate.not(seen::contains))
            .map(Node::new)
            .collect(Collectors.toSet());

        seen.addAll(ch_copies);

        for (Node n_ch : ch_copies) {
            n.getChildren().remove(n_ch);
            n.getChildren().add(n_ch);

            n_ch.getParents().remove(n);
            n_ch.getParents().add(n);

            recursiveUpdateChildren(n_ch, seen);
        }

        return seen;
    }


    /**
     *  Updates all parents of node {@code n} to point to node {@code n}. This is needed when node {@code n} is a
     *  newly created copy of a node.
     */
    public static void recursiveUpdateParents(Node n) {
        recursiveUpdateParents(n, new HashSet<>());
    }
    public static void recursiveUpdateParents(Node n, Set<Node> seen) {
        // This method only works because Node equality is based on Discrete Variable equality (which is based on varname)

        seen.add(n);

        for (Node n_p : n.getParents().stream().filter(seen::contains).toList()) {
            Node n_p2 = seen.stream().filter(_n_ch2 -> _n_ch2.equals(n_p)).findFirst().get();
            n_p2.getChildren().remove(n);
            n_p2.getChildren().add(n);
            n.getParents().remove(n_p2);
            n.getParents().add(n_p2);
        }

        // Only create copies of unseen parents. This prevents cycles.
        Set<Node> p_copies = n.getParents().stream()
            .filter(Predicate.not(seen::contains))
            .map(Node::new)
            .collect(Collectors.toSet());

        seen.addAll(p_copies);

        for (Node n_p : p_copies) {
            n.getParents().remove(n_p);
            n.getParents().add(n_p);

            n_p.getChildren().remove(n);
            n_p.getChildren().add(n);

            recursiveUpdateParents(n_p, seen);
        }

    }
}
