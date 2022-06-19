import java.util.*;

/**
 * A cluster is a set of Discrete Variables.
 *
 * @author rliu 2022-03
 */
public class Cluster implements Iterable<DiscreteVariable> {
    private Set<DiscreteVariable> vars;

    public Cluster(Collection<DiscreteVariable> vars) {
        this.vars = new LinkedHashSet<>(vars);
    }

    /**
     * Copy constructor.
     */
    public Cluster(Cluster c) {
        this.vars = new LinkedHashSet<>(c.vars);
    }

    /**
     * Getters
     */

    /**
     * The name for the cluster is just the names of the variables it represents.
     */
    public String getName() {
        return "" + vars.stream().map(DiscreteVariable::getName).toList();
    }

    public Set<DiscreteVariable> getVars() {
        return this.vars;
    }

    /**
     * Setters
     */

    public void setVars(Set<DiscreteVariable> vars) {
        this.vars = vars;
    }

    @Override
    public Iterator<DiscreteVariable> iterator() {
        return this.vars.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster that = (Cluster) o;
        return Objects.equals(vars, that.vars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vars);
    }

    @Override
    public String toString() {
        return "Cluster{" +
            "vars=" + DiscreteVariableUtils.getProductFormName(vars) +
            '}';
    }

}
