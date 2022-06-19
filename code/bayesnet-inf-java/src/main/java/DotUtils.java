import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utilties for converting/outputting as a GraphViz Dot format.
 *
 * Author: Nurrachman Liu   2022-03
 */
public class DotUtils {

    public static void printToFile(String fname, Graph g) {
        printToFile(fname, toDot(g));
    }

    public static void printToFile(String fname, InteractionGraph g) {
        printToFile(fname, toDot(g));
    }

    public static void printToFile(String fname, String contents) {
        try (PrintWriter out = new PrintWriter(fname)) {
            out.println(contents);
        }
        catch (FileNotFoundException ex) {
        }

        try {
            Runtime.getRuntime().exec("dot -Tjpg " + fname + " -o " + fname + ".jpg");
        }
        catch (IOException ex) {
        }
    }

    public static String toDot(Graph g) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph graphname {");

        for (Node n : g.getNodes()) {
            if (n.getChildren().size() > 0) {
                for (Node ch : n.getChildren()) {
                    sb.append("\n\t");
                    sb.append(n.getName());
                    sb.append(" -> ");
                    sb.append(ch.getName());
                }
            }
            else {
                sb.append("\n\t");
                sb.append(n.getName());
            }
        }

        sb.append("\n}");

        return sb.toString();
    }

    public static String toDot(InteractionGraph ig) {
        StringBuilder sb = new StringBuilder();
        sb.append("graph graphname {");

        Set<DiscreteVariable> seen = new HashSet<>();
        for (DiscreteVariable n : ig.getNodes()) {
            Set<DiscreteVariable> n_dsts = ig.getEdges().getOrDefault(n, null);
            if (n_dsts != null && n_dsts.size() > 0) {
                for (DiscreteVariable n2 : n_dsts) {
                    if (n2.equals(n))
                        continue;

                    if (!seen.contains(n2)) {
                        sb.append("\n\t");
                        sb.append(n.getName());
                        sb.append(" -- ");
                        sb.append(n2.getName());
                    }
                }
                seen.add(n);
            }
            else {
                sb.append("\n\t");
                sb.append(n.getName());
            }
        }

        sb.append("\n}");

        return sb.toString();
    }

}
