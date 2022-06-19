import java.util.*;
import java.util.function.Function;

/**
 * Utils for Elimination Trees.
 *
 * Author: Nurrachman Liu   2022-03
 */
public class EliminationTreeUtils {

    public static EliminationTree deepCopyEliminationTree(EliminationTree et) {
        EliminationTree et2 = new EliminationTree();

        Map<Node, List<Node>> neighbors2 = new LinkedHashMap<>();
        for (Map.Entry<Node, List<Node>> ent : et.getNeighbors().entrySet()) {
            neighbors2.put(ent.getKey(), new ArrayList<>(ent.getValue()));
        }
        et2.setNeighbors(neighbors2);

        Map<Node, Set<DiscreteVariable>> clusters2 = new HashMap<>();
        for (Map.Entry<Node, Set<DiscreteVariable>> ent : et.getClusters().entrySet()) {
            clusters2.put(ent.getKey(), new HashSet<>(ent.getValue()));
        }
        et2.setClusters(clusters2);

        Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> seps2 = new HashMap<>();
        for (Map.Entry<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> ent : et.getSeparators().entrySet()) {
            seps2.put(ent.getKey(), new HashSet<>(ent.getValue()));
        }
        et2.setSeparators(seps2);

        return et2;
    }

    public static String printClusters(Map<Node, Set<DiscreteVariable>> clusters) {
        StringBuilder sb = new StringBuilder();

        List<Node> nodes = new ArrayList<>(clusters.keySet());
        nodes.sort(Comparator.comparing(o -> o.getVar().getName()));

        for (Node n : nodes) {
            List<DiscreteVariable> vars = new ArrayList<>(clusters.get(n));
            List<String> var_names = new ArrayList<>(vars.stream().map(DiscreteVariable::getName).toList());
            var_names.sort(String::compareTo);
            sb.append(n.getName()).append(": ").append("" + var_names).append("\n");
        }

        return sb.toString();
    }


    /**
     * todo -- print in tree format
     */
    public static String printSeparators(Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> separator) {
        StringBuilder sb = new StringBuilder();

        for (Pair<Node, Node> edge : separator.keySet()) {
            List<DiscreteVariable> vars = new ArrayList<>(separator.get(edge));
            List<String> var_names = new ArrayList<>(vars.stream().map(DiscreteVariable::getName).toList());
            var_names.sort(String::compareTo);
            sb.append(edge.left.getName()).append("---").append("" + var_names).append("---").append(edge.right.getName()).append("\n");
        }

        return sb.toString();
    }


    public static String printElimTree(EliminationTree et, Function<Node, String> to_str) {
        Set<Node> seen = new HashSet<>();
        List<Node> nodes = new ArrayList<>(et.getNeighbors().keySet());

        StringBuilder sb = new StringBuilder();
        for (Node n : nodes) {
            if (! seen.contains(n)) {
                sb.append(_printElimTree(
                    n,
                    et.getNeighbors(),
                    to_str,
                    to_str.apply(n).length(),
                    seen)
                );
            }
        }
        return sb.toString();
    }
    public static String _printElimTree(
        Node a,
        Map<Node, List<Node>> neighbors,
        Function<Node, String> to_str,
        int tabwidth,
        Set<Node> seen)
    {
        seen.add(a);

        String tab = " ".repeat(tabwidth);

        StringBuilder sb = new StringBuilder();
        sb.append(to_str.apply(a));

        int i = 0;
        for (Node n_ch : neighbors.get(a)) {
            // go to every neighbor except seen ones; since this is a tree, there should be no loops, so the seen one is just the parent
            if (! seen.contains(n_ch)) {
                if (i > 0) {
                    sb.append("\n" + tab);
                }
                if (!seen.contains(n_ch)) {
                    sb.append("->" + _printElimTree(n_ch, neighbors, to_str, tabwidth + to_str.apply(n_ch).length() + "->".length(), seen));
                    i++;
                } else {
                    sb.append("->" + "[loop: " + to_str.apply(n_ch) + "]");
                    i++;
                }
            }
        }
        return sb.toString();
    }

}
