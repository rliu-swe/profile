import java.util.*;

/**
 * A Separator is a set of Discrete Variables.
 *
 * @author rliu 2022-03
 */
public class Separator implements Iterable<DiscreteVariable> {

    private Set<DiscreteVariable> vars;

    public Separator(Collection<DiscreteVariable> vars) {
        this.vars = new LinkedHashSet<>(vars);
    }

    /**
     * Copy constructor.
     */
    public Separator(Separator s) {
        this.vars = new LinkedHashSet<>(s.vars);
    }

    /**
     * The name of the separator is just the names of the variables it represents.
     */
    public String getName() {
        return "" + vars.stream().map(DiscreteVariable::getName).toList();
    }

    @Override
    public Iterator<DiscreteVariable> iterator() {
        return this.vars.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Separator that = (Separator) o;
        return Objects.equals(vars, that.vars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vars);
    }

    @Override
    public String toString() {
        return "Separator{" +
            "vars=" + DiscreteVariableUtils.getProductFormName(vars) +
            '}';
    }
}
