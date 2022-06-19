import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Operations on Probability objects.
 *
 * @author rliu 2022-03
 */
public class ProbabilityUtils {

    /**
     * Collects all variable names mentioned in the Probability {@code p}.
     */
    public static Set<String> getAllVariableNames(Probability p) {
        return getAllVariables(p).stream().map(DiscreteVariable::getName).collect(Collectors.toSet());
    }

    /**
     * Collects all variables mentioned in the Probability {@code p}.
     */
    public static Set<DiscreteVariable> getAllVariables(Probability p) {
        Set<DiscreteVariable> vars = new HashSet<>();

        vars.addAll(p.getVars());
        vars.addAll(p.getInsts().stream().map(inst -> inst.parent).toList());

        if (p.getClass() == Probability.CondProb.class) {
            vars.addAll(((Probability.CondProb) p).getCondVars());
        }

        return vars;
    }

    /**
     * Collects only non-instanced variables mentioned in the Probability {@code p}.
     */
    public static Set<DiscreteVariable> getNonInstVariables(Probability p) {
        Set<DiscreteVariable> vars = new HashSet<>();

        vars.addAll(p.getVars());
        if (p.getClass() == Probability.CondProb.class) {
            vars.addAll(((Probability.CondProb) p).getCondVars());
        }

        return vars;
    }
}
