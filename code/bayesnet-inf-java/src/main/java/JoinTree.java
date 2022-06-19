import java.util.*;

/**
 * A JoinTree is a type of Elimination Tree where every node is a cluster of variables in the graph, compared to the
 * typical elimination-tree where every node corresponds to just a single variable in the graph.
 *
 * Author: Nurrachman Liu   2022-03
 */
public class JoinTree {
    public Map<Cluster, Set<Cluster>> neighbors = new LinkedHashMap<>();
    public Map<Cluster, Set<Separator>> separators = new LinkedHashMap<>();

    public JoinTree() {
    }

    /**
     * Links nodes a and b with an undirected edge.
     */
    public void link(Cluster a, Cluster b, Separator s) {
        Set<Cluster> a_neighbors = this.neighbors.getOrDefault(a, new LinkedHashSet<>());
        Set<Cluster> b_neighbors = this.neighbors.getOrDefault(b, new LinkedHashSet<>());
        a_neighbors.add(b);
        b_neighbors.add(a);
        this.neighbors.put(a, a_neighbors);
        this.neighbors.put(b, b_neighbors);

        Set<Separator> a_seps = this.separators.getOrDefault(a, new LinkedHashSet<>());
        Set<Separator> b_seps = this.separators.getOrDefault(b, new LinkedHashSet<>());
        a_seps.add(s);
        b_seps.add(s);
        this.separators.put(a, a_seps);
        this.separators.put(b, b_seps);
    }

    /**
     * Getters
     */

    public Set<Cluster> getClusters() {
        return this.neighbors.keySet();
    }

    public Set<Separator> getSeparatorsAsSet() {
        return this.separators.values().stream().reduce(new LinkedHashSet<>(), (accum, e) -> {accum.addAll(e); return accum;});
    }

    public Map<Cluster, Set<Cluster>> getNeighbors() {
        return this.neighbors;
    }

    public Map<Cluster, Set<Separator>> getSeparators() {
        return this.separators;
    }

    /**
     * Setters
     */

    public void setNeighbors(Map<Cluster, Set<Cluster>> neighbors) {
        this.neighbors = neighbors;
    }

    public void setSeparators(Map<Cluster, Set<Separator>> separators) {
        this.separators = separators;
    }
}
