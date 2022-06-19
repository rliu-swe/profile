import java.util.*;
import java.util.stream.Collectors;

/**
 * Operations on Interaction Graphs
 *
 * Author: Nurrachman Liu   2022-03
 */
public class InteractionGraphOperations {

    /**
     * Returns the variable elimination order per the min-fill heuristic, using the interaction-graph of the BN.
     * Considers all variables in the BN for removal.
     */
    public static List<DiscreteVariable> getMinFillHeuristic(Graph g) {
        Set<DiscreteVariable> vars = g.getNodes().stream().map(Node::getVar).collect(Collectors.toSet());
        return getMinFillHeuristic(g, vars);
    }

    /**
     * Returns the variable elimination order per the min-fill heuristic, using the interaction-graph of the BN.
     * The second argument {@code vars} determines the variables that it will only consider for removal.
     */
    public static List<DiscreteVariable> getMinFillHeuristic(Graph g, Set<DiscreteVariable> _vars) {
        List<DiscreteVariable> var_order = new ArrayList<>();
        InteractionGraph ig = new InteractionGraph(g);

        Set<DiscreteVariable> vars = new HashSet<>(_vars);  // incoming set may be immutable.

        while (vars.size() > 0) {
            DiscreteVariable var_to_remove = null;
            InteractionGraph ig_of_var_removed = null;
            int min_edge_change = Integer.MAX_VALUE;
            for (DiscreteVariable v : vars) {
                List<Pair<DiscreteVariable, DiscreteVariable>> edge_lst_before = InteractionGraphUtils.getEdgeList(ig);
                int edges_before =  edge_lst_before.size() - ig.getEdges().getOrDefault(v, new HashSet<>()).size();
                InteractionGraph ig_copy = new InteractionGraph(ig); // copy constructor will be cheaper than making a new ig from g
                InteractionGraphOperations.eliminateVariables(ig_copy, Arrays.asList(v));
                List<Pair<DiscreteVariable, DiscreteVariable>> edge_lst_after = InteractionGraphUtils.getEdgeList(ig_copy);
                int edges_after = edge_lst_after.size();

                int edge_change = edges_after - edges_before;

                if (edge_change < min_edge_change) {
                    min_edge_change = edge_change;
                    var_to_remove = v;
                    ig_of_var_removed = ig_copy;
                }
            }
            var_order.add(var_to_remove);
            vars.remove(var_to_remove);
            ig = ig_of_var_removed;
//            DotUtils.printToFile("interaction-graph.dot", ig);
        }

        return var_order;
    }

    /**
     * Returns the variable elimination order per the min-degree heuristic, using the interaction-graph of the BN, considering
     * only the variables not in the given {@code query}.
     */
    public static List<DiscreteVariable> getMinDegreeHeuristic(Graph g, Probability query) {
        Set<DiscreteVariable> q_vars = new HashSet<>(query.getNonInstVars());
        Set<DiscreteVariable> g_vars = g.getNodes().stream().map(Node::getVar).collect(Collectors.toSet());
        g_vars.removeAll(q_vars);
        return getMinDegreeHeuristic(g, g_vars);
    }

    /**
     * Returns the variable elimination order per the min-degree heuristic, using the interaction-graph of the BN.
     * All variables in the BN are considered.
     */
    public static List<DiscreteVariable> getMinDegreeHeuristic(Graph g) {
        Set<DiscreteVariable> vars = g.getNodes().stream().map(Node::getVar).collect(Collectors.toSet());
        return getMinDegreeHeuristic(g, vars);
    }

    /**
     * Returns the variable elimination order per the min-degree heuristic, using the interaction-graph of the BN.
     */
    public static List<DiscreteVariable> getMinDegreeHeuristic(Graph g, Set<DiscreteVariable> _vars) {
        List<DiscreteVariable> var_order = new ArrayList<>();
        InteractionGraph ig = new InteractionGraph(g);

        Set<DiscreteVariable> vars = new HashSet<>(_vars);  // incoming set may be immutable.

        while (vars.size() > 0) {
            DiscreteVariable var_to_remove = null;
            int min_neighbors = Integer.MAX_VALUE;
            for (DiscreteVariable v : vars) {
                int num_neighbors = ig.getEdges().getOrDefault(v, new HashSet<>()).size();
                if (num_neighbors < min_neighbors) {
                    min_neighbors = num_neighbors;
                    var_to_remove = v;
                }
            }
            var_order.add(var_to_remove);
            eliminateVariables(ig, Arrays.asList(var_to_remove));
            if (var_to_remove != null)
                vars.remove(var_to_remove);
        }

        return var_order;
    }

    /**
     * Removes the given variables {@code vars} from the interaction graph. Modifies the given interaction graph.
     * Returns the widths of the graph for every variable removed, in the same order as the given vars.
     * todo:  consider whether to modify the ig itself or return a new one instead;
     *    pros for keeping modification: interactiongraph is used to measure width step-by-step during ve.
     *    cons for keeping modificaiton: min-fill heuristic requires multiple 'possible' igs
     *    soln: add a simple copy constructor
     */
    public static List<Integer> eliminateVariables(InteractionGraph ig, List<DiscreteVariable> vars) {
        List<Integer> widths = new ArrayList<>();

        int max_width = 0;
        for (DiscreteVariable var : vars) {
            Set<DiscreteVariable> var_neighbors = ig.getEdges().getOrDefault(var, new HashSet<>());

            // Track max width seen during this variable's eliminatation
            widths.add(var_neighbors.size());

            // Pairwise connect all of var's neighbors:
            for (DiscreteVariable n : var_neighbors) {
                Set<DiscreteVariable> n_neighbors = ig.getEdges().getOrDefault(n, new HashSet<>());

                // Pairwise connect n with every other var_neighbor
                Set<DiscreteVariable> to_add = new HashSet<>(var_neighbors);
                to_add.remove(n);
                to_add.remove(var);

                n_neighbors.addAll(to_add);
                n_neighbors.remove(var);

                ig.getEdges().put(n, n_neighbors);
            }
        }

        // Remove all vars from the interaction-graph now:
        vars.forEach(ig.getNodes()::remove);  // remove from nodes
        vars.forEach(ig.getEdges()::remove);  // remove edge entries

        return widths;
    }


}
