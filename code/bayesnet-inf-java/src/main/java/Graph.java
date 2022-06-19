import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A graph is just a collection of Nodes.
 *
 * @author rliu 2022-03
 */
public class Graph {
    private Set<Node> nodes = new LinkedHashSet<>();

    public Graph() {
    }

    public Graph(Set<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * Getters
     */

    public Set<Node> getNodes() {
        return nodes;
    }

    /**
     * Setters
     */

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    public void addNodes(Set<Node> nodes) {
        this.nodes.addAll(nodes);
    }

    @Override
    public String toString() {
        return "Graph{" +
            "nodes=" + nodes +
            '}';
    }

}
