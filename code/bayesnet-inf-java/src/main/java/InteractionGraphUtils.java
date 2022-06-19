import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utils for InteractionGraphs
 *
 * Author: Nurrachman Liu   2022-03
 */
public class InteractionGraphUtils {

    /**
     * Convenience for printing pairs of discrete variables' names only.
     */
    public static List<Pair<String, String>> formatEdgeList(List<Pair<DiscreteVariable, DiscreteVariable>> edge_lst) {
        return edge_lst.stream().map(p -> Pair.of(p.left.getName(), p.right.getName())).toList();
    }

    // produce a unique edge list for fast edge counting / iteration
    public static List<Pair<DiscreteVariable, DiscreteVariable>> getEdgeList(InteractionGraph ig) {
        List<Pair<DiscreteVariable, DiscreteVariable>> edge_lst = new ArrayList<>();

        Set<DiscreteVariable> seen = new HashSet<>();
        List<DiscreteVariable> vars = new ArrayList<>(ig.getEdges().keySet());

        for (DiscreteVariable v : vars) {
            Set<DiscreteVariable> v_edges = ig.getEdges().getOrDefault(v, new HashSet<>());
            for (DiscreteVariable v_dst : v_edges) {
                if (!seen.contains(v_dst))
                    edge_lst.add(Pair.of(v, v_dst));
            }
            seen.add(v);
        }

        return edge_lst;
    }

}
