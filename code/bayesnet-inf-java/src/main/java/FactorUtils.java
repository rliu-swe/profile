import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilities for Factors.
 *
 * @author rliu 2022-03
 */
public class FactorUtils {

    /**
     * Returns a new factor that is the same as f but with the variables rearranged in the order specified by {@code var_order}.
     */
    public static Factor rearrangeVariables(Factor f, List<DiscreteVariable> var_order) {
        if (var_order.size() != f.getVariables().size() ||  ! f.getVariables().containsAll(var_order))
            throw new RuntimeException("FactorUtils.rearrangeVariables: variables in f must be correct and fully specified.");

        Factor f2 = new Factor(var_order);

        Map<DiscreteVariable, Integer> index_map = new HashMap<>();
        for (int i = 0; i < f.getVariables().size(); i++) {
            DiscreteVariable var = f.getVariables().get(i);
            index_map.put(var, f.getVariables().indexOf(var));
        }

        for (int i = 0; i < f2.getNumRows(); i++) {
            List<Integer> f2_row_values = f2.computeRowValues(i);
            f.getRowValueByIndex(i);

            List<Integer> f1_row_values = new ArrayList<>(f2_row_values);
            for (int j = 0; j < f2_row_values.size(); j++) {
                int f2_row_val = f2_row_values.get(j);
                f1_row_values.set(index_map.get(var_order.get(j)), f2_row_val);
            }

            f2.setRowValueByIndex(i, f.getRowValueByVars(f1_row_values));
        }

        return f2;
    }

    /**
     * Sorts the given pairs of variables, variable values, in the order of the variables in the Factor.
     * Vars, varValues must fully-specify all variables in the factor, otherwise returns null.
     */
    public static List<Integer> sortVarValues(Factor factor, List<DiscreteVariable> vars, List<Integer> varValues) {
        List<Integer> sortedVarValues = new ArrayList<>();

        Map<DiscreteVariable, Integer> varValuesMap = new HashMap<>();

        if (vars.size() != varValues.size())
            return null;

        for (int i = 0; i < vars.size(); i++) {
            DiscreteVariable v = vars.get(i);
            Integer val = varValues.get(i);
            varValuesMap.put(v, val);
        }

        for (DiscreteVariable v : factor.getVariables()) {
            sortedVarValues.add(varValuesMap.get(v));
        }

        return sortedVarValues;
    }

    /**
     * When creating multiple Factors, it is convenient to retain access to them all and to the Discrete Variables used
     * to create them. This convenience container allows for map-access to these variables.
     */
    public static final class FactorsStruct {
        public final Map<String, Factor> factors = new HashMap<>();
        public final Map<String, DiscreteVariable> vars = new HashMap<>();

        public FactorsStruct(List<String> f_names, List<Factor> factors, List<String> vars_names, List<DiscreteVariable> vars) {
            for (int i = 0; i < f_names.size(); i++) {
                this.factors.put(f_names.get(i), factors.get(i));
            }
            for (int i = 0; i < vars_names.size(); i++) {
                this.vars.put(vars_names.get(i), vars.get(i));
            }
        }
    }
}
