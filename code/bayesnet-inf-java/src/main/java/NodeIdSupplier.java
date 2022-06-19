import java.util.function.Supplier;


/**
 * A Supplier for Node ids. This is meant to decouple the id-tracking/generation for nodes from node-creation itself.
 *
 * @author rliu 2022-03
 */
public class NodeIdSupplier implements Supplier<Integer> {

    private static final NodeIdSupplier singleton = new NodeIdSupplier();

    public static int NODE_ID = 0;

    private NodeIdSupplier() {
    }

    public static NodeIdSupplier getInstance() {
        return singleton;
    }

    public Integer get() {
        return NODE_ID++;
    }

}
