import java.util.ArrayList;

import java.util.List;

/**
 * Models different types of Probability statements. These are simply containers hat model the respective Pr's using
 * {@code DiscreteVariable}. There should be no logic or functional code here.
 * Variables should *not* repeat in vars/cond_vars & insts. Ie, if a variable is instanced (e.g., c=T), then it should not
 * appear in either vars or cond_vars.
 *
 * @author rliu 2022-03
 */
public abstract class Probability {
    private List<DiscreteVariable> vars = new ArrayList<>();
    private List<DiscreteVariable.Instance> insts = new ArrayList<>();

    public Probability(List<DiscreteVariable> vars) {
        this.vars = vars;
    }

    public Probability(List<DiscreteVariable> vars, List<DiscreteVariable.Instance> insts) {
        if (checkOverlap(vars, insts))
            throw new RuntimeException("Probability variables cannot overlap with variable instances.");
        this.vars = vars;
        this.insts = insts;
    }

    protected boolean checkOverlap(List<DiscreteVariable> vars, List<DiscreteVariable.Instance> insts) {
        if (vars.stream().anyMatch(insts.stream().map(i -> i.parent).toList()::contains))
            return true;
        return false;
    }

    /**
     * Getters
     */

    public List<DiscreteVariable> getVars() {
        return vars;
    }

    /**
     * Returns all non-instanced vars.
     */
    public List<DiscreteVariable> getNonInstVars() {
        return vars;
    }

    public List<DiscreteVariable.Instance> getInsts() {
        return insts;
    }

    public List<DiscreteVariable> getInstsAsVars() {
        return insts.stream().map(e -> e.parent).toList();
    }

    public List<DiscreteVariable> getAllVars() {
        List<DiscreteVariable> ret = new ArrayList<>();
        ret.addAll(getVars());
        ret.addAll(getInstsAsVars());
        return ret;
    }

    /**
     * Implementations.
     */

    public static class JointProb extends Probability {
        public JointProb(List<DiscreteVariable> vars) {
            super(vars);
        }
        public JointProb(List<DiscreteVariable> vars, List<DiscreteVariable.Instance> insts) {
            super(vars, insts);
        }
    }

    public static class MarginalProb extends Probability {
        private List<DiscreteVariable> vars = new ArrayList<>();
        private List<DiscreteVariable.Instance> insts = new ArrayList<>();
        public MarginalProb(List<DiscreteVariable> vars) {
            super(vars);
        }
        public MarginalProb(List<DiscreteVariable> vars, List<DiscreteVariable.Instance> insts) {
            super(vars, insts);
        }
    }

    public static class CondProb extends Probability {
        private List<DiscreteVariable> cond_vars = new ArrayList<>();
        public CondProb(List<DiscreteVariable> vars, List<DiscreteVariable> cond_vars) {
            super(vars);
            if (checkOverlap(cond_vars, this.getInsts()))
                throw new RuntimeException("Probability variables cannot overlap with variable instances.");
            this.cond_vars = cond_vars;
        }
        public CondProb(List<DiscreteVariable> vars, List<DiscreteVariable> cond_vars, List<DiscreteVariable.Instance> insts) {
            super(vars, insts);
            if (checkOverlap(cond_vars, this.getInsts()))
                throw new RuntimeException("Probability variables cannot overlap with variable instances.");
            this.cond_vars = cond_vars;
        }

        public List<DiscreteVariable> getCondVars() {
            return cond_vars;
        }

        @Override
        public List<DiscreteVariable> getNonInstVars() {
            List<DiscreteVariable> ret = new ArrayList<>(this.getVars());
            ret.addAll(cond_vars);
            return ret;
        }

        @Override
        public List<DiscreteVariable> getAllVars() {
            List<DiscreteVariable> ret = super.getAllVars();
            ret.addAll(getCondVars());
            return ret;
        }
    }

}
