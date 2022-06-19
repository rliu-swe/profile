import java.util.ArrayList;
import java.util.List;


/**
 * Utilities for verifying that the graphical model (Bayesian network) is in the correct (expected) structure.
 *
 * Author: Nurrachman Liu   2022-03
 */
public class GraphVerification {

    /**
     * Verifies that the structure of the graph {@code g} is consistent with all nodes' declared parents and children.
     */
    public static List<String> verifyStructure(Graph g) {
        String UNLISTED_PARENT = "Node %s lists node %s as a child but this child does not list it as parent.";
        String UNLISTED_CHILD = "Node %s lists node %s as a parent but this parent does not list it as child.";

        List<String> problems = new ArrayList<>();
        for (Node n : g.getNodes()) {
            // Check that n is listed as a parent in each child
            for (Node ch : n.getChildren()) {
                if (! ch.getParents().contains(n))
                    problems.add(String.format(UNLISTED_PARENT, n.getName(), ch.getName()));
            }
            for (Node par : n.getParents()) {
                if (! par.getChildren().contains(n))
                    problems.add(String.format(UNLISTED_CHILD, n.getName(), par.getName()));
            }
        }

        return problems;
    }

    /**
     * Verifies that the factors attached to the graph {@code g} match the structure.
     */
    public static List<String> verifyFactors(Graph g) {
        String NO_FACTOR = "Node %s does not have a factor attached.";
        String NODE_NOT_IN_FACTOR = "Node %s is not in factor attached.";
        String PARENT_NOT_IN_FACTOR = "Node %s has parent node %s not in factor attached.";
        String FACTOR_HAS_EXTRA_VARS = "Node %s has extra variables in its attached factor: ";

        List<String> problems = new ArrayList<>();
        for (Node n : g.getNodes()) {
            Factor f = n.getFactor();
            if (f == null)
                problems.add(String.format(NO_FACTOR, n.getName()));

            List<String> f_vars = f.getVariableNames();
            List<String> seen = new ArrayList<>();
            if (! f_vars.contains(n.getName()))
                problems.add(String.format(NODE_NOT_IN_FACTOR, n.getName()));
            else
                seen.add(n.getName());

            for (Node par : n.getParents()) {
                if (! f_vars.contains(par.getName()))
                    problems.add(String.format(PARENT_NOT_IN_FACTOR, n.getName(), par.getName()));
                else
                    seen.add(par.getName());
            }

            if (f_vars.size() != seen.size()) {
                f_vars.removeAll(seen);
                problems.add(String.format(FACTOR_HAS_EXTRA_VARS + f_vars, n.getName()));
            }
        }
        return problems;
    }

}
