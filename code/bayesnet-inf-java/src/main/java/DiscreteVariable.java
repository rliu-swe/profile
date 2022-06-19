import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A discrete variable is basically a pair representing a variable and the number of levels it takes on.
 * Optionally, names can be provided for the levels, but this is only for external display. Internally, the raw
 * integer indices for levels will still be used.
 *
 * Author: Nurrachman Liu   2022-03
 */
public class DiscreteVariable {

    private String name;
    private int num_levels;
    private Map<Integer, String> level_names = new HashMap<>();

    public DiscreteVariable(String name, int num_levels) {
        this.name = name;
        this.num_levels = num_levels;
    }

    public DiscreteVariable(String name, int num_levels, Map<Integer, String> level_names) {
        this(name, num_levels);
        this.level_names = level_names;
    }

    /**
     * Getters
     */

    public int getNumLevels() {
        return this.num_levels;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Setters
     */

    public void setLevelNames(Map<Integer, String> level_names) {
        this.level_names.putAll(level_names);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscreteVariable that = (DiscreteVariable) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "DiscreteVariable{" +
            "name='" + name + '\'' +
            ", num_levels=" + num_levels +
            ", level_names=" + level_names +
            '}';
    }

    /**
     * A discrete variable can be instanced, which produces an Instance object that points back to this DiscreteVariable.
     */
    public Instance instance(int level) {
        return new Instance(this, level);
    }

    public static class Instance {
        public final DiscreteVariable parent;
        public final int level;

        public Instance(DiscreteVariable parent, int level) {
            this.parent = parent;
            this.level = level;
        }
    }

}
