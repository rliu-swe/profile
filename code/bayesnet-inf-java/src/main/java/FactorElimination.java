import java.util.*;
import java.util.List;

/**
 * Factor Elimination Algorithms for Bayesian Networks.
 *
 * @author rliu 2022-03
 */
public class FactorElimination {

    /**
     * Runs the basic Factor Elimination FE1 (ie, without using Elimination Trees). The only difference with VE for this basic FE
     * is that basic FE sums out multiple variables at once, while VE does only one variable at a time. Also, FE1 only
     * supports a query of one variable.
     */
    public static Factor FE1(Graph g, DiscreteVariable q) {
        List<Factor> factors = new ArrayList<>(g.getNodes().stream().map(Node::getFactor).toList());

        Factor f_r = factors.stream().filter(f -> f.getVariables().contains(q)).findFirst().get();

        while (factors.size() > 1) {
            int i = 0;
            Factor f_i = factors.get(i);
            while (f_i == f_r) {
                i++;
                f_i = factors.get(i);
            }
            factors.remove(i);

            Set<DiscreteVariable> all_vars = new HashSet<>();
            for (Factor f : factors)
                all_vars.addAll(f.getVariables());

            List<DiscreteVariable> v_fi = new ArrayList<>(f_i.getVariables());
            v_fi.removeIf(all_vars::contains);  // vars in f_i but not in S
            Factor f_j = factors.remove(0);
            f_j = FactorOperations.multiply(f_j, FactorOperations.sum_out(f_i, v_fi));
            factors.add(0, f_j); // add the modified f_j back to the beginning
        }

        f_r = factors.get(0); // get the updated f_r which resides as the (only) factor left in factors list
        return FactorOperations.project(f_r, Arrays.asList(q));
    }


    /**
     * FE2 requires that the query variables be contained in the variables of a single node.
     *
     * todo: since this doe not take Graph g, technically if G has disconnected nodes that aren't listed in et, this
     * algorithm will not multiply with these factors, and the resulting joint distribution will be incorrect.
     */
    public static Factor FE2(EliminationTree et, Probability query) {
        // find node r whose factor contains all variables in the query.
        Node r = null;
        for (Node n : et.getNodes()) {
            if (n.getFactor().getVariables().containsAll(query.getNonInstVars())) {
                r = n;
                break;
            }
        }
        if (r == null)
            throw new RuntimeException("FE: No root node found with factor that contains the query Q in elim-tree.");

        // copy all factors, as they may be modified during FE2
        Map<Node, Factor> node_factors = new LinkedHashMap<>();
        for (Node n : et.getNodes()) {
            node_factors.put(n, new Factor(n.getFactor()));
        }

        while (et.numNodes() > 1) {
            Node i = null;
            for (Node n : et.getNeighbors().keySet()) {
                // Remove node i != r that has a single number
                if (n != r && et.getNeighbors().getOrDefault(n, new ArrayList<>()).size() == 1) {
                    i = n;
                    break;
                }
            }
            if (r == null)
                throw new RuntimeException("FE: No root node found with cluster that contains the query Q in elim-tree.");

            Node j = et.getNeighbors().get(i).get(0);  // node j is the single-neighbor of node i.
            et.removeNode(i);

            Factor f_j = node_factors.get(j);
            Factor f_i = node_factors.get(i);

            Set<DiscreteVariable> all_vars = new HashSet<>();
            for (Node n : et.getNeighbors().keySet())
                all_vars.addAll(n.getFactor().getVariables());

            List<DiscreteVariable> v_fi = new ArrayList<>(f_i.getVariables());
            v_fi.removeIf(all_vars::contains);

            f_j = FactorOperations.multiply(f_j, FactorOperations.sum_out(f_i, v_fi));
            node_factors.put(j, f_j);
        }

        // The only factor left in elim-tree is r's factor:
        return FactorOperations.project(node_factors.get(r), query.getNonInstVars());
    }

    /**
     * FE3 is the formulation of FE2 that uses clusters and separators. A cluster contains or is equal to the variables
     * in a factor. Ie, the final factor of node r must be over the cluster of its node r (a root node, since it contained
     * only 1 neighbor by definition). That is, FE2 can be used to compute the marginal over any subset of cluster C_r,
     * the cluster for this node r.
     */
    public static Factor FE3(EliminationTree et, Probability query) {
        Map<Node, Set<DiscreteVariable>> clusters = et.getClusters();
        Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> separators = et.getSeparators();

        // find node r whose cluster contains all variables in the query.
        Node r = null;
        for (Node n : et.getNodes()) {
            if (clusters.get(n).containsAll(query.getNonInstVars())) {
                r = n;
                break;
            }
        }
        if (r == null)
            throw new RuntimeException("FE: No root node found with cluster that contains the query Q in elim-tree.");

        // copy all factors, as they may be modified during FE3
        Map<Node, Factor> node_factors = new LinkedHashMap<>();
        for (Node n : et.getNodes()) {
            node_factors.put(n, new Factor(n.getFactor()));
        }

        while (et.numNodes() > 1) {
            Node i = null;
            for (Node n : et.getNeighbors().keySet()) {
                // Remove node i != r that has a single number
                if (n != r && et.getNeighbors().getOrDefault(n, new ArrayList<>()).size() == 1) {
                    i = n;
                    break;
                }
            }
            if (i == null)
                return null;

            Node j = et.getNeighbors().get(i).get(0);  // node j is the single-neighbor of node i.
            et.removeNode(i);

            Factor f_j = node_factors.get(j);
            Factor f_i = node_factors.get(i);

            f_j = FactorOperations.multiply(f_j, FactorOperations.project(f_i, separators.get(Pair.Symmetric.of(i, j)))); // FactorOperations.sum_out(f_i, v_fi));
            node_factors.put(j, f_j);
        }

        // The only factor left in elim-tree is r's factor:
        return FactorOperations.project(node_factors.get(r), query.getNonInstVars());
    }


    /**
     * Collects all messages needed for node r. Recursive; will collect messages from all connected nodes to r.
     * @param src the node that called node r to collect messages.
     */
    public static Factor collectMessages(Node r, Node src, EliminationTree et, Map<Pair<Node, Node>, Factor> message_map) {
        Factor msg = r.getFactor();  // if no suitable neighbor is found, r *is* a root and it sends its own factor as the mesage

        for (Node r_neigh : et.getNeighbors().get(r)) {
            if (! r_neigh.equals(src)) { // do not backtrack to the calling src; ie, node r pulls messages from every neighbor other than src
                Factor msg_r_neigh = message_map.get(r_neigh);
                if (msg_r_neigh == null) {
                    msg_r_neigh = collectMessages(r_neigh, r, et, message_map);
                }
                msg = FactorOperations.multiply(msg, msg_r_neigh);
            }
        }

        Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> separators = et.getSeparators();

        if (src != null ) {  // do not project if the r is the root node of the computation.
            // M_ij = project(phi_i * prod_{i!=j}mki, S_ij)
            Set<DiscreteVariable> sep = separators.get(Pair.Symmetric.of(r, src));
            msg = FactorOperations.project(msg, sep);
        }

        message_map.put(Pair.of(r, src), msg);

        return msg;
    }


    /**
     * Pushes messages out from node {@code r}. Each node can only push a message to all of its neighbors after it has
     * pulled the messages from all of its neighbors (and then multiplied them with its own factor).
     */
    public static void pushMessages(Node r, Node src, EliminationTree et, Map<Pair<Node, Node>, Factor> message_map) {
        Factor msg = r.getFactor();
        Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> separators = et.getSeparators();

        if (src != null) { // if not the original root node, then there *is* an incoming message
//            Factor incoming_msg = message_map.get(Pair.of(r, src)); // original bug: the incoming message should be from src to r
            Factor incoming_msg = message_map.get(Pair.of(src, r));
            msg = FactorOperations.multiply(r.getFactor(), incoming_msg);
        }

        for (Node r_neigh : et.getNeighbors().get(r)) {
            // push-out to r_neigh from r
            Factor msg_ij = new Factor(msg); // duplicate the factor so far, which is (r.factor * incoming.factor)

            // compute product with all other incoming other than the src-incoming and any from r_neigh
            for (Node r_neigh2 : et.getNeighbors().get(r)) {
                if (! r_neigh2.equals(src) && ! r_neigh2.equals(r_neigh)) {
                    msg_ij = FactorOperations.multiply(msg_ij, message_map.get(Pair.of(r_neigh2, r)));
                }
            }

            if (! r_neigh.equals(src)) {
                Set<DiscreteVariable> sep = separators.get(Pair.Symmetric.of(r, r_neigh));
                msg_ij = FactorOperations.project(msg_ij, sep);
                message_map.put(Pair.of(r, r_neigh), msg_ij);
                pushMessages(r_neigh, r, et, message_map);
            }
        }
    }


    /**
     * Runs the FE messsage-passing implementation and computes all joint-marginals at every node, at once. This is
     * full 're-use' of all computed messages and so is O(n*exp(w)) vs VE's O(n^2 * exp(w)) if VE were called on every
     * node (O(n * n exp(w))).
     */
    public static Map<Node, Factor> FE(EliminationTree et, Probability query) {
        Map<Node, Set<DiscreteVariable>> clusters = et.getClusters();
        Map<Pair.Symmetric<Node, Node>, Set<DiscreteVariable>> separators = et.getSeparators();

        Map<Pair<Node, Node>, Factor> message_map = new LinkedHashMap<>();

        for (DiscreteVariable.Instance e : query.getInsts()) {
            DiscreteVariable E = e.parent;
            // find node i whose cluster contains E.
            Node i = null;
            for (Node n : et.getNodes()) {
                if (clusters.get(n).contains(E)) {
                    i = n;
                    // attach evidence indicator to this node i
                    //i.getFactor().attachFactor(new EvidenceIndicator("lambda_" + E.getName(), E));
                    i.getFactor().attachEvidence(e);
                }
            }
        }

//        // copy all factors, as they may be modified during FE3
//        Map<Node, Factor> node_factors = new LinkedHashMap<>();
//        for (Node n : et.getNodes()) {
//            node_factors.put(n, new Factor(n.getFactor()));
//        }

        // choose a root node r in elim-tree et such that cluster at r contains Q:
        Node r = null;
        for (Node n : et.getNodes()) {
            if (clusters.get(n).containsAll(query.getNonInstVars())) {
                r = n;
                break;
            }
        }
        if (r == null)
            throw new RuntimeException("FE: No root node found with cluster that contains the query Q in elim-tree.");

        //
        // One push-pull phase on root r will compute all 2m-1 messages for the BN for this evidence. As long as the
        // evidence remains compatible, the messages will not need to be re-computed.
        //

        // pull messages towards root r
        FactorElimination.collectMessages(r, null, et, message_map);

        // push messages away from root r
        FactorElimination.pushMessages(r, null, et, message_map);

        Map<Node, Factor> joint_marginals = new HashMap<>();
        // compute all cluster marginals
        for (Node n : et.getNodes()) {
            // get all messages to this node:
            List<Factor> n_incoming_msgs = new ArrayList<>();
            for (Map.Entry<Pair<Node, Node>, Factor> ent : message_map.entrySet()) {
                Pair<Node, Node> src_dst = ent.getKey();
                Factor msg = ent.getValue();

                if (n.equals(src_dst.right))
                    n_incoming_msgs.add(msg);
            }
            // multiply all messages together, along with its cpt:
            Factor msg = new Factor(n.getFactor());
            for (Factor in_msg : n_incoming_msgs) {
                msg = FactorOperations.multiply(msg, in_msg);
            }
            joint_marginals.put(n, msg);
        }

        return joint_marginals;
    }

}
