import java.util.*;
import java.util.stream.Collectors;

/**
 * A Factor represents a table over a set of variables. A Factor's name represents it throughout a trace of Factor
 * operations; the name is therefore considered an immmutable part of the factor.
 * The same Factors can be attached to multiple nodes, and so factors don't contain a pointer to the node(s) it is
 * attached to.
 *
 * Internally, the Factor class represents rows as indices over the given variable order:
 * for ex:  Row 7 for a (binary, ternary, binary)-variables would be [0,2,1]
 *
 * @author rliu 2022-03
 */
public class Factor {

    private final String trace_name;
    private Map<String, DiscreteVariable> name2var = new LinkedHashMap<>();
    private Map<DiscreteVariable, Integer> var2idx = new LinkedHashMap<>();
    private List<DiscreteVariable> variables = new ArrayList<>();
    private List<Integer> levels = new ArrayList<>();  // stores the levels of the variables
    private List<Integer> rows_per_level = new ArrayList<>();

    private int num_rows;
    private List<Double> values = new ArrayList<>();

    private List<Factor> attached_factors = new ArrayList<>();
    private List<DiscreteVariable.Instance> attached_evidences = new ArrayList<>();

    /**
     * Ordering of variables in all rows given will be fixed according to this given order.
     */
    public Factor(String trace_name, List<DiscreteVariable> variables) {
        this.trace_name = trace_name;
        this.setVariables(variables);
    }

    /**
     * If no name is given, will conjoin the names manually into product form.
     */
    public Factor(List<DiscreteVariable> variables) {
        this(DiscreteVariableUtils.getProductFormName(variables), variables);
    }

    /**
     * Copy constructor.
     * todo: copy over attached factors/evidences
     */
    public Factor(Factor f) {
        this.trace_name = f.trace_name;
        this.setVariables(f.getVariables());
        this.values = new ArrayList<>(f.getValues());

        this.attached_evidences = new ArrayList<>(f.getAttachedEvidences());
        this.attached_factors = new ArrayList<>(f.getAttachedFactors());
    }


    public void setVariables(List<DiscreteVariable> variables)
    {
        this.variables = variables;

        this.name2var.clear();
        for (DiscreteVariable v : variables) {
            if (this.name2var.get(v.getName()) != null)
                throw new RuntimeException("Error: repeated variable in Factor creation: " + v.getName());
            this.name2var.put(v.getName(), v);
        }

        this.var2idx.clear();
        int i = 0;
        for (DiscreteVariable v : variables) {
            this.var2idx.put(v, i++);
        }

        this.levels.clear();
        for (DiscreteVariable v : variables) {
            this.levels.add(v.getNumLevels());
        }

        this.rows_per_level = new ArrayList<>();
        this.rows_per_level.add(1);
        int prev = 1;
        for (i = this.levels.size()-2; i >= 0; i--) {
            int rows = this.levels.get(i+1) * prev;
            this.rows_per_level.add(rows);
            prev = rows;
        }
        Collections.reverse(this.rows_per_level);

        this.num_rows = this.levels.stream()
            .reduce(1, (accum, e) -> accum * e);

        Double[] values = new Double[this.num_rows];
        Arrays.fill(values, 0.0);
        this.values = Arrays.asList(values);
    }


    /**
     * Computes the row index that maps to the given list of variable values.
     * @return -1 if any given variable value is invalid.
     */
    public int computeRowIndex(List<Integer> varValues) {
        int row_index = 0;
        for (int i = 0; i < varValues.size(); i++) {
            int val = varValues.get(i);
            if (val < 0 || val >= this.levels.get(i))
                return -1;
            row_index += this.rows_per_level.get(i) * val;
        }
        return row_index;
    }

    /**
     * Computes the list of values for the variables represented by the given row index.
     * @return null if row_index is invalid
     */
    public List<Integer> computeRowValues(int row_index) {
        if (row_index < 0 || row_index >= this.num_rows)
            return null;

        List<Integer> var_values = new ArrayList<>();
        for (int i = 0; i < this.rows_per_level.size(); i++) {
            int val = row_index / this.rows_per_level.get(i);
            row_index = row_index % this.rows_per_level.get(i);
            var_values.add(val);
        }
        return var_values;
    }

    /**
     * Returns the factor value corresponding to the <it>full-specified</it> row of variables' values.
     */
    public double getRowValueByVars(List<Integer> varValues) {
        int row_index = computeRowIndex(varValues);
        return this.values.get(row_index);
    }

    public double getRowValueByIndex(int row_index) {
        return this.values.get(row_index);
    }

    /**
     * Returns a range of row indices corresponding to the partially-specified variables' values. If the variables
     * are fully-specify a row, then the list is just 1 element in size.
     */
    public List<Pair<Integer, Integer>> getRowIndicesByVarSubset(Map<DiscreteVariable, Integer> varValues) {
        List<Integer> matching_rows = new ArrayList<>();
        List<Pair<Integer, Integer>> ranges = new ArrayList<>();
        ranges.add(Pair.of(0, this.num_rows-1));

        for (int i = 0; i < this.variables.size(); i++) {
            DiscreteVariable v = this.variables.get(i);
            Integer varVal = varValues.getOrDefault(v, null);
            if (varVal != null) {
                List<Pair<Integer, Integer>> next_ranges = new ArrayList<>();
                Pair<Integer, Integer> next_range;
                for (Pair<Integer, Integer> range : ranges) {
                    if ((range.right - range.left + 1)/this.rows_per_level.get(i) <= v.getNumLevels()) {
                        int offset = this.rows_per_level.get(i) * varVal;
                        next_range = Pair.of(
                            range.left + offset,
                            range.left + offset + this.rows_per_level.get(i) - 1
                        );
                        next_ranges.add(next_range);
                    }
                    else {
                        // Split the range
                        int num_splits = (range.right - range.left + 1) / this.rows_per_level.get(i-1);
                        for (int j = 0; j < num_splits; j++) {
                            int offset = this.rows_per_level.get(i) * varVal;
                            next_range = Pair.of(
                                range.left + j*this.rows_per_level.get(i-1) + offset,
                                range.left + j*this.rows_per_level.get(i-1) + offset + this.rows_per_level.get(i) - 1
                            );
                            next_ranges.add(next_range);
                        }
                    }
                }
                ranges = next_ranges;
            }
        }

        return ranges;
    }


    /**
     * Getters
     */

    public List<String> getVariableNames() {
        return new ArrayList<>(this.name2var.keySet());
    }

    public List<DiscreteVariable> getVariables() {
        return this.variables;
    }

    public List<Integer> getRowsPerLevel() {
        return this.rows_per_level;
    }

    public int getNumRows() {
        return this.num_rows;
    }

    public List<Double> getValues() {
        return values;
    }

    public String getTraceName() {
        return trace_name;
    }

    public List<Factor> getAttachedFactors() {
        return this.attached_factors;
    }

    public List<DiscreteVariable.Instance> getAttachedEvidences() {
        return this.attached_evidences;
    }



    /**
     * Setters
     */

    public void setRowValueByVarValues(List<Integer> varValues, double value) {
        int row_index = computeRowIndex(varValues);
        this.values.set(row_index, value);
    }

    public void setRowValueByIndex(int row_index, double value) {
        this.values.set(row_index, value);
    }

    public void setRowValuesByValues(List<Double> values) {
        if (values.size() != this.num_rows)
            throw new RuntimeException("Incorrect size of values given to factor.");
        for (int row = 0; row < this.num_rows; row++) {
            this.values.set(row, values.get(row));
        }
    }

    /**
     * Expects as many values as there are rows.
     * todo: make this declaration of values follow the declared ordering of parents
     */
    public void setValues(List<Double> values)
        throws FactorSetValuesException
    {
        if (this.num_rows != values.size())
            throw new FactorSetValuesException(this.num_rows, values.size());
        this.values.clear();
        this.values.addAll(values);
    }

    /**
     * Attach another factor for (future) evaluation
     */
    public void attachFactor(Factor f) {
        this.attached_factors.add(f);
    }

    /**
     * Attach an evidence variable instance for (future) evaluation/reduction
     */
    public void attachEvidence(DiscreteVariable.Instance e) {
        this.attached_evidences.add(e);
    }




    /**
     * Prints the Factor nicely formatted as a Table.
     */
    public String toTable() {
        List<String> varNames = new ArrayList<>(this.name2var.keySet());
        int maxNameLen = varNames.size() > 0
            ? Collections.max(varNames, Comparator.comparing(String::length)).length()
            : 0;

        // Create the row entries for variables' values. max computed here tracks max for the variable names.
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < this.num_rows; i++) {
            List<Integer> varVals = computeRowValues(i);
            List<String> row = new ArrayList<>();
            row.add("" + i);
            for (int j = 0; j < varNames.size(); j++) {
                String varName = varNames.get(j);
                int varVal = varVals.get(j);
                row.add(varName + varVal);
            }
            rows.add(row);
            maxNameLen = Math.max(Collections.max(row, Comparator.comparing(String::length)).length(), maxNameLen);
        }

        // Create the row entry for the row's value.
        int maxValLen = 0;
        for (int i = 0; i < this.num_rows; i++) {
            List<String> row = rows.get(i);
            String val = String.format(" %.4f", this.values.get(i));
            row.add(val);
            maxValLen = Math.max(val.length(), maxValLen);
        }

        String sformat = "%" + (maxNameLen+1) + "s";
//        System.out.println("maxNameLen: " + maxNameLen + ", sformat: " + sformat);
        List<String> header = new ArrayList<>();
        header.add("idx");
        header.addAll(varNames);
        String header_str = header.stream().map(x -> String.format(sformat, x)).collect(Collectors.joining("  "));

        StringBuilder sb = new StringBuilder();
        sb.append(header_str);
        sb.append(" | Value");
        sb.append("\n");
        sb.append("-".repeat(header_str.length()));
        sb.append("-+");
        sb.append("-".repeat(maxValLen + 1));
        sb.append("\n");

        for (List<String> row : rows) {
            String rowStr = row.stream().map(x -> String.format(sformat, x)).collect(Collectors.joining("  "));
            sb.append(rowStr);
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "Factor{" +
            "trace_name=" + trace_name +
            ", variables=" + variables +
            ", name2var=" + name2var +
            ", var2idx=" + var2idx +
            ", levels=" + levels +
            ", rows_per_level=" + rows_per_level +
            ", num_rows=" + num_rows +
            ", values=" + values +
            '}';
    }

    public static final class FactorSetValuesException extends Exception {
        public FactorSetValuesException(int num_rows, int num_values) {
            super("Factor variables have " + num_rows + ", but given values is only size: " + num_values + "; they should match.");
        }
    }

}
