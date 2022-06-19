import java.util.*;

/**
 * Performs operations on Factors.
 *
 * Author: Nurrachman Liu   2022-03
 */
public class FactorOperations {

    /**
     * Applies all evidence attached to factor {@code f}. Does not modify the original factor {@code f}; returns a
     * new factor with all evidences applied.
     * @param f
     */
    public static Factor applyEvidence(Factor f) {
        for (DiscreteVariable.Instance e : f.getAttachedEvidences()) {
            f = FactorOperations.reduce(f, e);
        }
        return f;
    }


    /**
     * Reduces the factor {@code f} by the discrete-variable-instance {@code inst}.
     * @param f
     * @param inst
     * @return a new factor, the reduced factor. The original factor is unchanged.
     */
    public static Factor reduce(Factor f, DiscreteVariable.Instance inst) {
        if (! f.getVariableNames().contains(inst.parent.getName()))
            return new Factor(f);

        Factor new_factor = new Factor(f);
        Map<DiscreteVariable, Integer> varSubset = new HashMap<>();
        for (int k = 0; k < inst.parent.getNumLevels(); k++) {
            if (k != inst.level)
                varSubset.put(inst.parent, k);
        }
        List<Pair<Integer, Integer>> row_ranges = f.getRowIndicesByVarSubset(varSubset);
        for (Pair<Integer, Integer> row_range : row_ranges) {
           for (int i = row_range.left; i <= row_range.right; i++) {
               new_factor.setRowValueByIndex(i, 0.0);
           }
        }
        return new_factor;
    }


    /**
     * Multiplies two factors together. The factors are expected to share some same-named variables.
     * @param f first factor
     * @param g second factor
     * @return the product factor f*g
     */
    public static Factor multiply(Factor f, Factor g) {
        List<DiscreteVariable> f_vars = new ArrayList<>(f.getVariables());
        List<DiscreteVariable> g_vars = new ArrayList<>(g.getVariables());
        List<DiscreteVariable> both_vars = new ArrayList<>(f_vars);
        both_vars.retainAll(g_vars);

        f_vars.removeAll(both_vars);
        g_vars.removeAll(both_vars);

        List<DiscreteVariable> vars = new ArrayList<>();
        vars.addAll(f_vars);
        vars.addAll(both_vars);
        vars.addAll(g_vars);

        Factor product = new Factor(DiscreteVariableUtils.getProductFormName(f.getTraceName(), g.getTraceName()), vars);

        for (int row = 0; row < product.getNumRows(); row++) {
            List<Integer> var_vals = product.computeRowValues(row);
            List<Integer> f_varValues = var_vals.subList(0, f_vars.size());
            List<Integer> b_varValues = var_vals.subList(f_vars.size(), f_vars.size() + both_vars.size());
            List<Integer> g_varValues = var_vals.subList(f_vars.size() + both_vars.size(), f_vars.size() + both_vars.size() + g_vars.size());

            List<DiscreteVariable> f_vars_all = new ArrayList<>();
            f_vars_all.addAll(f_vars);
            f_vars_all.addAll(both_vars);
            List<Integer> f_var_vals_all = new ArrayList<>();
            f_var_vals_all.addAll(f_varValues);
            f_var_vals_all.addAll(b_varValues);
            List<Integer> f_var_vals_sorted = FactorUtils.sortVarValues(f, f_vars_all, f_var_vals_all);

            List<DiscreteVariable> g_vars_all = new ArrayList<>();
            g_vars_all.addAll(g_vars);
            g_vars_all.addAll(both_vars);
            List<Integer> g_var_vals_all = new ArrayList<>();
            g_var_vals_all.addAll(g_varValues);
            g_var_vals_all.addAll(b_varValues);
            List<Integer> g_var_vals_sorted = FactorUtils.sortVarValues(g, g_vars_all, g_var_vals_all);

            double f_val = f.getRowValueByVars(f_var_vals_sorted);
            double g_val = g.getRowValueByVars(g_var_vals_sorted);

            product.setRowValueByVarValues(var_vals, f_val * g_val);
        }

        product.getAttachedFactors().addAll(f.getAttachedFactors());
        product.getAttachedFactors().addAll(g.getAttachedFactors());

        product.getAttachedEvidences().addAll(f.getAttachedEvidences());
        product.getAttachedEvidences().addAll(g.getAttachedEvidences());

        return product;
    }


    /**
     * Sums-out the given variables from the factor f.
     * @param f the given factor to sum-out the variables from.
     * @param vars the desired variables to sum-out.
     * @return a new factor that has variables {@code vars} summed out from factor {@code f}
     */
    public static Factor sum_out(Factor f, List<DiscreteVariable> vars) {
        List<DiscreteVariable> new_vars = new ArrayList<>(f.getVariables());
        new_vars.removeAll(vars);

        Factor new_f = new Factor(DiscreteVariableUtils.getMarginalizedFormName(f.getTraceName(), vars), new_vars);

        for (int row = 0; row < new_f.getNumRows(); row++) {
            List<Integer> var_vals = new_f.computeRowValues(row);
            Map<DiscreteVariable, Integer> var_vals_map = new HashMap<>();
            for (int i = 0; i < new_vars.size(); i++) {
                // assumes that computeRowValues returns the values in the order of declared variables when Factor initialized.
                var_vals_map.put(new_vars.get(i), var_vals.get(i));
            }
            List<Pair<Integer, Integer>> row_ranges = f.getRowIndicesByVarSubset(var_vals_map);
            double sum = 0;
            for (Pair<Integer, Integer> row_range : row_ranges) {
                for (int r = row_range.left; r <= row_range.right; r++) {
                    sum += f.getRowValueByIndex(r);
                }
            }
            new_f.setRowValueByIndex(row, sum);
        }

        new_f.getAttachedFactors().addAll(f.getAttachedFactors());
        new_f.getAttachedEvidences().addAll(f.getAttachedEvidences());

        return new_f;
    }

    /**
     * A symbolic, simulated multiply that does not actually perform the factor multiplication. For tracing and VE simulation.
     */
    public static Factor fake_multiply(Factor f, Factor g) {
        Set<DiscreteVariable> vars = new HashSet<>(f.getVariables());
        vars.addAll(g.getVariables());
        Factor product = new Factor(DiscreteVariableUtils.getProductFormName(f.getTraceName(), g.getTraceName()), new ArrayList<>(vars));
        product.getAttachedFactors().addAll(f.getAttachedFactors());
        product.getAttachedFactors().addAll(g.getAttachedFactors());
        product.getAttachedEvidences().addAll(f.getAttachedEvidences());
        product.getAttachedEvidences().addAll(g.getAttachedEvidences());
        return product;
    }

    /**
     * A symbolic, simulated sum_out that does not actually do any summing-out (which can be in the worst-case exponential
     * in the variables, ie, linear in the rows but the rows are exponential in the variables).
     */
    public static Factor fake_sum_out(Factor f, List<DiscreteVariable> vars) {
        Set<DiscreteVariable> f_vars = new HashSet<>(f.getVariables());
        f_vars.removeAll(vars);
        Factor f2 = new Factor(DiscreteVariableUtils.getMarginalizedFormName(f.getTraceName(), vars), new ArrayList<>(f_vars));
        f2.getAttachedFactors().addAll(f.getAttachedFactors());
        f2.getAttachedEvidences().addAll(f.getAttachedEvidences());
        return f2;
    }

    /**
     * Divides factor f by factor g. Factor f must contain the variables of g. The use case is something like Pr(Q, E)/Pr(E),
     * where the numerator factor f contains the variables of the denominator factor g.
     */
    public static Factor divide(Factor f, Factor g) {
        if (! f.getVariableNames().containsAll(g.getVariableNames()))
            throw new RuntimeException("Dividing factors is only allowed when factor f contains all variables in factor g.");

        try {
            Factor g2 = new Factor(g);
            List<Double> values = g.getValues();
            List<Double> values2 = values.stream().map(x -> x == 0 ? 0 : 1/x).toList();
            g2.setValues(values2);

            return multiply(f, g2);
        }
        catch (Factor.FactorSetValuesException ex) {
        }

        return null;
    }

    /**
     * Projects the factor over the variables q. That is, the variables F.vars()\q are summed-out.
     */
    public static Factor project(Factor f, Collection<DiscreteVariable> q) {
        List<DiscreteVariable> f_vars = new ArrayList<>(f.getVariables());
        f_vars.removeIf(q::contains);
        return FactorOperations.sum_out(f, f_vars);
    }

}
