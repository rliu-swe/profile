import java.util.*;
import java.util.stream.Collectors;

/**
 * Utilities for Join-Trees.
 *
 * Author: Nurrachman Liu   2022-03
 */
public class JoinTreeUtils {


    /**
     * Returns a deep-copy of the given join-tree {@code jt}, including all of its clusters and separators.
     * The Discrete Variables in the clusters and separators are *not* copied.
     *
     * todo test this
     */
    public static JoinTree deepCopyJoinTree(JoinTree jt) {
        JoinTree jt2 = new JoinTree();

        // make a copy of all the clusters
        Map<Cluster, Cluster> cluster_copy_map = new LinkedHashMap<>();
        for (Cluster c : jt.getClusters()) {
            cluster_copy_map.put(c, new Cluster(c));
        }

        // make a copy of all the separators
        Map<Separator, Separator> sep_copy_map = new LinkedHashMap<>();
        for (Separator s : jt.getSeparatorsAsSet()) {
            sep_copy_map.put(s, new Separator(s));
        }

        Map<Cluster, Set<Cluster>> neighbors2 = new LinkedHashMap<>();
        Map<Cluster, Set<Separator>> separators2  = new LinkedHashMap<>();
        for (Cluster c : cluster_copy_map.keySet()) {
            Set<Cluster> neigh2 = jt.getNeighbors().get(c).stream().map(cluster_copy_map::get).collect(Collectors.toSet());
            Set<Separator> sep2 = jt.getSeparators().get(c).stream().map(sep_copy_map::get).collect(Collectors.toSet());
            neighbors2.put(c, neigh2);
            separators2.put(c, sep2);
        }

        jt2.setNeighbors(neighbors2);
        jt2.setSeparators(separators2);

        return jt2;
    }



    /**
     * Computes all possible paths between clusters in the given elimination-tree {@code jt}.
     */
    public static List<List<Cluster>> computeAllPaths(JoinTree jt) {
        List<List<Cluster>> all_paths = new ArrayList<>();

        List<Cluster> clusters = new ArrayList<>(jt.getClusters());

        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i+1; j < clusters.size(); j++) {
                Cluster c_i = clusters.get(i);
                Cluster c_j = clusters.get(j);

                List<List<Cluster>> queue = new ArrayList<>();
                queue.add(new ArrayList<>(List.of(c_i)));

                // per-path parallel list of seen nodes to prevent cycles
                List<Set<Cluster>> seens = new ArrayList<>();
                seens.add(new HashSet<>(Set.of(c_i)));

                while (queue.size() > 0) {
                    List<Cluster> path = queue.remove(0);
                    Set<Cluster> seen = seens.remove(0);
                    Cluster n = path.get(path.size() - 1);
                    Set<Cluster> n_neighbors = jt.getNeighbors().get(n);
                    for (Cluster n_neigh : n_neighbors) {
                        // spawn a new path,seen for each neighbor
                        List<Cluster> path_copy = new ArrayList<>(path);
                        Set<Cluster> seen_copy = new HashSet<>(seen);
                        if (! seen.contains(n_neigh)) {
                            path_copy.add(n_neigh);
                            seen_copy.add(n_neigh);
                            if (n_neigh.equals(c_j)) {
                                all_paths.add(path_copy);
                            }
                            else {
                                queue.add(path_copy);
                                seens.add(seen_copy);
                            }
                        }
                    }
                }
            }
        }

        return all_paths;
    }

    /**
     * A JoinTree is an EliminationTree with the special property that its nodes are instead viewed by the clusters it
     * represents, rather than as the variable (BN node). Thus, this function verifies that the given elimination tree
     * {@code jt} is a jointree for the graph {@code g}.
     * Returns a list of errors that make the elimination-tree not a valid join-tree. If it is a valid join-tree, the list
     * has size 0.
     */
    public static List<String> verifyJoinTree(Graph g, JoinTree jt) {
        List<String> errors = new ArrayList<>();

        String ERR_FAMILY_NOT_IN_CLUSTER = "Invalid jointree property: node %s's family (%s) not in a cluster of the given elimination-tree.";
        String ERR_PATH_PROPERTY = "Invalid jointree property: a cluster (%s) along path from c_i (%s) to c_j (%s) does not contain vars_ij.";

        // Verify that every family in graph g is mapped to a cluster
        for (Node n : g.getNodes()) {
            boolean appears = false;
            for (Cluster c_i : jt.getClusters()) {
                if (c_i.getVars().containsAll(n.getFactor().getVariables())) {
                    appears = true;
                    break;
                }
            }
            if (! appears)
                errors.add(String.format(ERR_FAMILY_NOT_IN_CLUSTER, n.getName(), n.getFactor().getVariableNames()));
        }

        // For every pairwise path between any two clusters, variables appearing in these two clusters must also appear
        //   in every cluster on the path.
        List<List<Cluster>> all_paths = computeAllPaths(jt);
        for (List<Cluster> path : all_paths) {
            Cluster n_beg = path.get(0);
            Cluster n_end = path.get(path.size() - 1);
            Set<DiscreteVariable> vars_ij = new HashSet<>(n_beg.getVars());
            vars_ij.retainAll(n_end.getVars());

            for (Cluster n : path) {
                if (! n.getVars().containsAll(vars_ij)) {
                    errors.add(String.format(ERR_PATH_PROPERTY, n.getName(), n_beg.getName(), n_end.getName()));
                    break;
                }
            }
        }

        return errors;
    }


    /**
     * When creating join-trees, it is convenient to retain access to the clusters and separators by a named map.
     * This convenience container allows for map-access to these variables.
     */
    public static final class JoinTreeStruct {
        public final JoinTree jointree;
        public final Map<String, Cluster> clusters = new HashMap<>();
        public final Map<String, Separator> separators = new HashMap<>();

        public JoinTreeStruct(JoinTree jt, List<String> c_names, List<Cluster> clusters, List<String> s_names, List<Separator> seps) {
            this.jointree = jt;
            for (int i = 0; i < c_names.size(); i++) {
                this.clusters.put(c_names.get(i), clusters.get(i));
            }
            for (int i = 0; i < s_names.size(); i++) {
                this.separators.put(s_names.get(i), seps.get(i));
            }
        }
    }
}
