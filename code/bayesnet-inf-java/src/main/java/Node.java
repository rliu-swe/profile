import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a variable in the BN. A node contains pointers to its parents and children.
 *
 * Node ids are globally unique and are supplied.
 *
 * @author rliu 2022-03
 */
public class Node {
    private int id = NodeIdSupplier.getInstance().get();
    private String tag_name = "";  // a descriptive name, not used for any real purpose except display
    private DiscreteVariable var;  // an attached discrete variable is what identifies a Node and establishes equality
    private Factor factor;
    private Set<Node> parents = new LinkedHashSet<>();
    private Set<Node> children = new LinkedHashSet<>();

    private Node() {
    }

    public Node(DiscreteVariable var) {
        this.var = var;
    }

    public Node(String tag_name, DiscreteVariable var) {
        this(var);
        this.tag_name = tag_name;
    }

    /**
     * Copy constructor.
     */
    public Node(Node n) {
        this(n.tag_name, n.var);
        this.parents = new LinkedHashSet<>(n.parents);
        this.children = new LinkedHashSet<>(n.children);
        this.factor = n.factor;
    }


    /**
     * Getters
     */

    public int getId() {
        return id;
    }

    public String getName() {
        return this.var.getName();
    }

    public DiscreteVariable getVar() {
        return var;
    }

    public Factor getFactor() {
        return factor;
    }

    public Set<Node> getParents() {
        return parents;
    }

    public Set<Node> getChildren() {
        return children;
    }


    /**
     * Setters
     */

    public void addParent(Node parent) {
        this.parents.add(parent);
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public void addParents(Set<Node> parents) {
        this.parents.addAll(parents);
    }

    public void addChildren(Set<Node> children) {
        this.children.addAll(children);
    }

    public void setParents(Set<Node> parents) {
        this.parents = parents;
    }

    public void setChildren(Set<Node> children) {
        this.children = children;
    }

    public void linkChild(Node child) {
        this.addChild(child);
        child.addParent(this);
    }

    public void unlinkChild(Node child) {
        this.getChildren().remove(child);
        child.getParents().remove(this);
    }

    public void linkParent(Node parent) {
        this.addParent(parent);
        parent.addChild(this);
    }

    public void unlinkParent(Node parent) {
        this.getParents().remove(parent);
        parent.getChildren().remove(this);
    }

    public void setFactor(Factor f) {
        this.factor = f;
    }

    public void setTagName(String tag_name) {
        this.tag_name = tag_name;
    }


    /**
     * Node equality is based only on the Discrete Variable.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(var, node.var);
    }

    @Override
    public int hashCode() {
        return Objects.hash(var);
    }

    @Override
    public String toString() {
        return "Node{" +
            "id=" + id +
            ", var='" + var + '\'' +
            ", tag_name='" + tag_name + '\'' +
            ", parents=" + parents.stream().map(n -> n.id).collect(Collectors.toList()) +
            ", children=" + children.stream().map(n -> n.id).collect(Collectors.toList()) +
            ", factor=" + (factor == null ? "none" : "\n"+factor.toTable()) +
            '}';
    }
}
