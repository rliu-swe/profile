import java.util.Collection;
import java.util.List;

/**
 * Utility methods for Discrete Variables.
 *
 * Author: Nurrachman Liu   2022-03
 */
public class DiscreteVariableUtils {

    /**
     * Returns the product form of all the variables. Ex: [a, b, c] -> "a.b.c"
     */
    public static String getProductFormName(Collection<DiscreteVariable> vars) {
        List<String> var_names = vars.stream().map(DiscreteVariable::getName).toList();
        return String.join(".", var_names);
    }

    /**
     * Returns the product form of two factors' variables.
     */
    public static String getProductFormName(String vars1, String vars2) {
        return "(" + vars1 + ")(" + vars2 + ")";
    }

    /**
     * Returns the marginalized-form of a factors' variables.
     */
    public static String getMarginalizedFormName(String vars1, Collection<DiscreteVariable> marg_vars) {
        String marg_vars_names = String.join(",", marg_vars.stream().map(DiscreteVariable::getName).toList());

        return "sum_{" + marg_vars_names + "}(" + vars1 + ")";
    }


}
