import java.util.*;

/**
 * An Elimination Tree guides the process for Factor Elimination. Each Elimination tree thus embodies a particular
 * factor elimination strategy. Each node/variable/factor in the graph is assigned to exactly one node of the elimination tree.
 *
 * An Elimination Tree re-uses the same Node defined for a Graph as its internal nodes. Since all Nodes in an Elim-Tree
 * are connected, the map {@code neighbors} is enough to track all Node's of an Elim-Tree.
 *
 * @author rliu 2022-03
 */
public class EliminationTree {
    private Map<Node, List<Node>> neighbors = new LinkedHashMap<>();
    private Map<Node, Set<DiscreteVariable>> clusters;
    private Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> separators;

    public EliminationTree() {
    }

    /**
     * Getters
     */

    public Set<Node> getNodes() {
        return this.neighbors.keySet();
    }

    public Map<Node, List<Node>> getNeighbors() {
        return this.neighbors;
    }

    public int numNodes() {
        return this.neighbors.keySet().size();
    }

    public Map<Node, Set<DiscreteVariable>> getClusters() {
        return clusters;
    }

    public Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> getSeparators() {
        return separators;
    }

    /**
     * Setters
     */

    /**
     * Links nodes a and b with an undirected edge.
     */
    public void link(Node a, Node b) {
        List<Node> a_neighbors = this.neighbors.getOrDefault(a, new ArrayList<>());
        List<Node> b_neighbors = this.neighbors.getOrDefault(b, new ArrayList<>());

        a_neighbors.add(b);
        b_neighbors.add(a);

        this.neighbors.put(a, a_neighbors);
        this.neighbors.put(b, b_neighbors);
    }

    /**
     * Removes node n from this elim tree.
     */
    public void removeNode(Node n ) {
        List<Node> n_neighbors = this.neighbors.getOrDefault(n, new ArrayList<>());
        // Remove n's entry from the main map
        this.neighbors.remove(n);
        // Also remove it from all of its neighbors' neighbor-list
        for (Node n_neigh : n_neighbors) {
           this.neighbors.getOrDefault(n_neigh, new ArrayList<>()).remove(n);
        }
    }

    public void setNeighbors(Map<Node, List<Node>> neighbors) {
        this.neighbors = neighbors;
    }

    public void setSeparators(Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> sep) {
        this.separators = sep;
    }

    public void setClusters(Map<Node, Set<DiscreteVariable>> clust) {
        this.clusters = clust;
    }


}
