import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utilities for graphs.
 *
 * @author rliu 2022-03
 */
public class GraphUtils {

    /**
     * Returns all variables in the graph. The underlying assumption is that each node has one attached variable only.
     */
    public static Set<DiscreteVariable> getVariablesInGraph(Graph g) {
        return g.getNodes().stream().map(Node::getVar).collect(Collectors.toSet());
    }

    /**
     * Collects all the variable names mentioned for all factors attached to nodes in the graph {@code g}.
     */
    public static Set<String> getAllVariableNames(Graph g) {
        Set<String> varNames = new HashSet<>();
        for (Node n : g.getNodes()) {
            varNames.addAll(n.getFactor().getVariableNames());
        }
        return varNames;
    }

    /**
     * Prints the entire graph, including all isolated nodes. Finds all roots and then prints only from the roots.
     */
    public static String printGraph(Graph g, Function<Node, String> to_str) {
        return printGraph(g.getNodes(), to_str);
    }
    public static String printGraph(Set<Node> _nodes, Function<Node, String> to_str) {
        Set<Node> roots = new LinkedHashSet<>();
        Set<Node> seen = new HashSet<>();

        List<Node> nodes = new ArrayList<>(_nodes);
        nodes.sort(Comparator.comparing(n -> n.getVar().getName()));

        for (Node n : nodes) {
            if (n.getParents().size() == 0) {
                roots.add(n);
                seen.add(n);
            }
            else {
                List<Node> queue = new ArrayList<>(n.getParents());
                while (queue.size() > 0) {
                    Node par = queue.remove(0);
                    if (seen.contains(par))
                        continue;
                    seen.add(par);
                    if (par.getParents().size() == 0) {
                        roots.add(par);
                    } else {
                        queue.addAll(par.getParents());
                    }
                }
            }
        }

        StringBuilder graph_str = new StringBuilder();
        for (Node root : roots) {
            graph_str.append(printGraph_children(root, to_str));
            graph_str.append("\n");
        }
        return graph_str.toString();
    }

    /**
     * Prints the graph moving in the children direction.
     */
    public static String printGraph_children(Node a, Function<Node, String> to_str) {
        return _printGraph_children(a, to_str, to_str.apply(a).length(), new HashSet<>());
    }
    public static String _printGraph_children(Node a, Function<Node, String> to_str, int tabwidth, Set<Node> seen) {
        seen.add(a);

        String tab = " ".repeat(tabwidth);
        String msg = to_str.apply(a);

        int i = 0;
        for (Node n_ch : a.getChildren()) {
            if (i > 0) {
                msg += "\n" + tab;
            }
            if (! seen.contains(n_ch)) {
                msg += "->" + _printGraph_children(n_ch, to_str, tabwidth + to_str.apply(n_ch).length() + "->".length(), seen);
                i++;
            }
            else {
                msg += "->" + "[loop: " + to_str.apply(n_ch) + "]";
                i++;
            }
        }
        return msg;
    }

    /**
     * Prints the graph moving in the parents direction.
     */
    public static String printGraph_parents(Node a, Function<Node, String> to_str) {
        return _printGraph_parents(a, to_str, to_str.apply(a).length(), new HashSet<>());
    }
    public static String _printGraph_parents(Node a, Function<Node, String> to_str, int tabwidth, Set<Node> seen) {
        seen.add(a);

        String tab = " ".repeat(tabwidth);
        String msg = to_str.apply(a);

        int i = 0;
        for (Node n_ch : a.getParents()) {
            if (i > 0) {
                msg += "\n" + tab;
            }
            if (! seen.contains(n_ch)) {
                msg += "->" + _printGraph_parents(n_ch, to_str, tabwidth + to_str.apply(n_ch).length() + "->".length(), seen);
                i++;
            }
            else {
                msg += "->" + "[loop: " + to_str.apply(n_ch) + "]";
                i++;
            }
        }
        return msg;
    }


    /**
     * Creates a copy of the graph where all nodes are copied. Nodes' variables and factors are NOT copied.
     */
    public static Graph deepCopyGraph(Graph g) {
        Map<DiscreteVariable, Node> copy_map = new LinkedHashMap<>();

        // Sort the nodes to get a deterministic order
        List<Node> nodes = new ArrayList<>(g.getNodes());
        nodes.sort((n1, n2) -> n1.getVar().toString().compareTo(n2.getVar().toString()));

        for (Node n : nodes) {
            copy_map.put(n.getVar(), new Node(n));
        }

        for (Node n : nodes) {
            for (Node n_ch : n.getChildren()) {
                Node n_ch2 = copy_map.get(n_ch.getVar());
                n_ch2.setParents(
                    n_ch2.getParents().stream().map(np -> copy_map.get(np.getVar())).collect(Collectors.toSet())
                );
            }
            for (Node n_par : n.getParents()) {
                Node n_par2 = copy_map.get(n_par.getVar());
                n_par2.setChildren(
                    n_par2.getChildren().stream().map(nc -> copy_map.get(nc.getVar())).collect(Collectors.toSet())
                );
            }
        }

        return new Graph(new LinkedHashSet<>(copy_map.values()));
    }


    /**
     * When creating Graphs in other scopes, it is convenient to retain access to the created internal variables.
     * This container allows for map-access to these internal variables.
     */
    public static final class GraphStruct {
        public final Graph graph;
        public final Map<String, Node> nodes = new HashMap<>();
        public final Map<String, DiscreteVariable> vars = new HashMap<>();
        public final Map<String, Factor> factors = new HashMap<>();

        public GraphStruct(Graph g, List<Node> nodes_lst, List<DiscreteVariable> vars_lst) {
            this.graph = g;
            for (Node n : nodes_lst) {
                nodes.put(n.getName(), n);
                factors.put(n.getName(), n.getFactor());
            }
            for (DiscreteVariable v : vars_lst) {
                vars.put(v.getName(), v);
            }
        }
    }


}
