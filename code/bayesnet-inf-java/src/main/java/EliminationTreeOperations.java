import java.util.*;

/**
 * Operations on/for Elimination Trees.
 *
 * @author rliu 2022-03
 */
public class EliminationTreeOperations {

    /**
     * While Elimination Trees are disparate from ve orders, both are analogous in that they represent a strategy
     * for their respective method. In this sense, it is meaningful to create an Elimination Tree according to a desired
     * ve order.
     * todo
     */
    public static EliminationTree createElimTree(Graph g, List<DiscreteVariable> ve_order) {
        Set<Node> nodes = g.getNodes();
        Map<Node, List<Node>> neighbors = new LinkedHashMap<>();

        return null;
    }

    /**
     * Computes the separators of the given Elimination Tree {@code et}. Starts at roots and computes in waves inwards,
     * then takes the final intersection of all waves' variables at every edge.
     */
    public static Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> computeSeparators(EliminationTree et) {
        Set<Node> roots = new HashSet<>();

        // Every edge can only be crossed in 2 directions. If it is not in this map, then continue that direction; else,
        //   end.
        Map<Pair<Node, Node>, Set<DiscreteVariable>> vars_map = new HashMap<>();

        List<Pair<Node, Node>> frontier_edges = new ArrayList<>();  // edges to explore

        // Find all roots
        for (Node n : et.getNodes()) {
            if (et.getNeighbors().get(n).size() == 1) {
                roots.add(n);
            }
        }

        // Seed the vars_map with the roots' variables
        for (Node n : roots) {
            Pair<Node, Node> root_edge = Pair.of(n, n);
            frontier_edges.add(root_edge);
        }

        // Start at roots, and then grow inwards until all nodes have been filled.
        while (frontier_edges.size() > 0) {
            Pair<Node, Node> edge = frontier_edges.remove(0);

            // Populate edge vars:
            Set<DiscreteVariable> vars = vars_map.getOrDefault(edge, new HashSet<>());
            vars_map.put(edge, vars);
            vars.addAll(edge.left.getFactor().getVariables());  //  ex:  2-->1, put 2's vars here; we are centered on 1
            // Find var_map entry where edge.left is in edge.right: (there could be multiple);
            //    this is correct since we're centered on 1, and we're doing it for all 2's as dst here. Thus 2 must
            //    have been explored since we are at 1 which is to the right (after) of 2.
            for (Pair<Node, Node> ent : vars_map.keySet()) {
                if (ent.right.equals(edge.left)) {
                    if (ent.left == edge.right)
                        continue;  // this is our current edge, so skip it.
                    vars.addAll(vars_map.get(ent));
                }
            }

            // Grow the frontier
            List<Pair<Node, Node>> n_edges = new ArrayList<>();
            for (Node n2 : et.getNeighbors().get(edge.right)) {
                if (n2 == edge.left) // do not travel directly backwards on the same edge
                    continue;
                Pair<Node, Node> n2_edge = Pair.of(edge.right, n2);
                if (vars_map.getOrDefault(n2_edge, null) == null)
                    n_edges.add(n2_edge);
            }
            frontier_edges.addAll(n_edges);
        }

        // for all same-edges, intersect the var-sets:
        Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> separators = new HashMap<>();
        for (Map.Entry<Pair<Node, Node>, Set<DiscreteVariable>> ent : vars_map.entrySet()) {
            Pair<Node, Node> edge = ent.getKey();

            // skip root nodes
            if (edge.left.equals(edge.right))
                continue;

            Set<DiscreteVariable> vars = ent.getValue();
            Pair.Symmetric<Node, Node> undirected_edge = Pair.Symmetric.of(edge.left, edge.right);

            Set<DiscreteVariable> seps = separators.getOrDefault(undirected_edge, new HashSet<>(vars));
            separators.put(undirected_edge, seps);
            seps.retainAll(vars);
        }

        return separators;
    }

    /**
     * Computes the clusters at every node.
     */
    public static Map<Node, Set<DiscreteVariable>> computeClusters(EliminationTree et) {
        Map<Node, Set<DiscreteVariable>> clusters = new HashMap<>();

        for (Node n : et.getNodes()) {
            Set<DiscreteVariable> vars = new HashSet<>(n.getFactor().getVariables());

            // find all separators with src or dst of n
            for (Map.Entry<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> ent : et.getSeparators().entrySet()) {
                Pair.Symmetric<Node, Node> edge = ent.getKey();
                if (edge.left.equals(n) || edge.right.equals(n))
                    vars.addAll(ent.getValue());
            }

            clusters.put(n, vars);
        }

        return clusters;
    }

}
