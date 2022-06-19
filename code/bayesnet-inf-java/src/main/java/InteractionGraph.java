import java.util.*;

/**
 * The interaction graph of a BN. It maps all interactions between Discrete Variables. Each node has a Discrete variable
 * attached and a Factor attached as well. Discrete Variables are contained in the Factors attached to the Nodes of the
 * given BN graph.
 *
 * @author rliu 2022-03
 */
public class InteractionGraph {
    private Set<DiscreteVariable> nodes = new LinkedHashSet<>();
    private Map<DiscreteVariable, Set<DiscreteVariable>> edges = new HashMap<>();

    public InteractionGraph(Graph g) {
        // for every node in the graph, look at its factor and add their connectivity into the interaction graph
        for (Node n : g.getNodes()) {
            if (n.getFactor() != null) {
                List<DiscreteVariable> vars = n.getFactor().getVariables();
                for (int i = 0; i < vars.size(); i++) {
                    DiscreteVariable src = vars.get(i);
                    // the connectivity info collected for this sr
                    Set<DiscreteVariable> src_edges = new HashSet<>();
                    for (int j = 0; j < vars.size(); j++) {
                        DiscreteVariable dst = vars.get(j);
                        if (!src.equals(dst)) {
                            src_edges.add(dst);
                        }
                    }
                    Set<DiscreteVariable> dsts = edges.getOrDefault(src, new HashSet<>());
                    dsts.addAll(src_edges);
                    edges.put(src, dsts);
                }
            }
            nodes.add(n.getVar());
        }
    }

    /**
     * Copy constructor that does a 1-level-deep copy of the internal nodes.
     */
    public InteractionGraph(InteractionGraph g) {
        this.nodes = new LinkedHashSet<>(g.nodes);
        this.edges = new HashMap<>(g.edges);
        this.edges.replaceAll((k, v) -> new HashSet<>(v));
    }


    /**
     * Getters
     */

    public Set<DiscreteVariable> getNodes() {
        return nodes;
    }

    public Map<DiscreteVariable, Set<DiscreteVariable>> getEdges() {
        return edges;
    }

}
