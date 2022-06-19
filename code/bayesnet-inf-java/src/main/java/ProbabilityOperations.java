import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Methods for various computations on discrete Probability distribution.
 *
 * @author rliu 2022-03
 */
public class ProbabilityOperations {

    /**
     * Uses Variable Elimmination (VE) to compute the joint marginal, e.g., Pr(Q, e).
     */
    public static Factor computeJointMarginalUsingVE(Graph g, Probability query) {
        Set<DiscreteVariable> vars = GraphUtils.getVariablesInGraph(g);
        Set<DiscreteVariable> query_vars = ProbabilityUtils.getNonInstVariables(query);

        Set<DiscreteVariable> vars_to_sum_out = new HashSet<>(vars);
        vars_to_sum_out.removeAll(query_vars);

        List<DiscreteVariable> ve_order = InteractionGraphOperations.getMinFillHeuristic(g, vars_to_sum_out);
        Factor result = VariableElimination.RunVE(g, ve_order, query); // query is used to reduce by all instances (evidence)

        return result;
    }


    /**
     * Computes the posterior marginal by normalizing the joint marginal. Uses VE.
     * The query must contain instance(s) of variables (ie, evidence); otherwise, returns null.
     */
    public static Factor computePosteriorMarginalUsingVE(Graph g, Probability query) {
        if (query.getClass() != Probability.CondProb.class)
            throw new RuntimeException("Computing Posterior Marginals requires conditional probability");

        Factor qe = computeJointMarginalUsingVE(g, query);
        Factor e = FactorOperations.sum_out(qe, query.getVars());  // sum-out q

        // Normalize by e:
        // case 1: if e is entirely instances, then e is just a single row,
        //         which divides all rows in q (since all rows in q are implied consistent with e): ie, (Q, e) will be compatible with e.
        // case 2: if e is not all instances, ie, e=E, then division must occur between only compatible rows of Q with E.
        // case 3: if e is a mix of instances and non-instances (e, E), then e will have been implied for all rows
        //         (since every factor is up-front reduced by e), and then E divides as in case 2.
        Factor q_normalized = FactorOperations.divide(qe, e);

        return q_normalized;
    }



}
