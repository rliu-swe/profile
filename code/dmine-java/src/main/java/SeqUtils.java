import java.util.*;

/**
 * Sequence utilties.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class SeqUtils {

    /**
     * Prints all given sequences as a line de-limited string.
     */
    public static String printSeqs(Collection<List<Set<Integer>>> seqs) {
        StringBuilder sb = new StringBuilder();
        for (List<Set<Integer>> seq : seqs) {
            sb.append("[");
            int i = 0;
            for (Set<Integer> itemset : seq) {
                List<Integer> itemset_ordered = new ArrayList<>(itemset);
                Collections.sort(itemset_ordered);
                sb.append(itemset_ordered);
                if (i != seq.size() - 1)
                    sb.append(", ");
                i++;
            }
            sb.append("]");
            sb.append("\n");
        }
        return sb.toString();
    }


    /**
     *  Returns the canonically ordered sequence of {@code seq}.
     */
    public static List<Integer> canonical_order(Set<Integer> seq) {
        List<Integer> seq_ordered = new ArrayList<>(seq);
        Collections.sort(seq_ordered);
        return seq_ordered;
    }

    /**
     * Returns true if the given sequence {@code seq} is in the row {@code row}, false otherwise. A sequence is in a row
     * if any subsequence of the row matches the sequence. The match does not need to occur along consecutive elements of
     * the row, just that both match in the order of the sequence. Ex: seq: a(bc) matches row:  a(d)(e)b(fc)
     */
    public static boolean seqIsInRow(List<Set<Integer>> row, List<Set<Integer>> seq) {
        List<Set<Integer>> row_copy = new ArrayList<>(row);
        List<Set<Integer>> seq_copy = new ArrayList<>(seq);
        while (! seq_copy.isEmpty()) {
            Set<Integer> cur = seq_copy.get(0);

            // look for first match in row, then remove all elements before the match in the row.
            int i = 0;
            while (! row_copy.isEmpty()) {
                Set<Integer> it = row_copy.get(i++);
                if (it.containsAll(cur)) {
                    seq_copy.remove(0);
                    if (seq_copy.isEmpty())
                        break;

                    if (i != row_copy.size()) {  // i is +1 from what you'd expect since it was ++ above
                        row_copy = row_copy.subList(i, row_copy.size());
                        break;
                    }
                }
                if (i >= row_copy.size())  // i is +1 from what you'd expect since it was ++ above
                    return false;
            }
            if (row_copy.isEmpty())
                return false;
        }
        return true;
    }


    /**
     * Projects sequence {@code x} on sequence {@code seq} and returns the projected result (suffix). -1 in the returned
     * sequence denotes '_' (e.g. (_bc) is [-1, 2, 3]). All input sequences ({@code x}, {@code seq}) are sorted into
     * canonical ordering (natural ordering).
     *
     * A note on the placeholder '-1' ('_'). In any projected database, the '_' serves to mark that the current
     * projected-db (ex: a(bc)-projected database) matches up to the 'middle' of some itemset (ex: ad(abcd) matches for
     * a(bc) to give (_d); so, all occurrences of (_d) would indicate necessarily that it is the placeholder for THAT
     * current projection-variable (a(bc)) rather than some other previous recursion's projection variable, since by
     * construction, another previous layer's projection variable that matched up to the middle of some itemset, thus
     * creating a possible '(_d)', would either have been frequent, upon which it would been further projected on, thus
     * getting rid of it completely, or not frequent, meaning another frequent variable would have been then projectd on
     * (such as the a(bc)-projection variable). That other frequent variable could only match AFTER (_d), since a placeholder
     * is invariantly always the first in the sequence by definition of the suffix-projection that we're using as projection.
     * So if it were to match AFTER (_d), it could only create a new placeholder (say, another '(_d')) that could not possibly
     * be confused with the first '(_d)'.
     *
     * Thus, it suffices to use a single character '_' (-1) to denote the placeholder, rather than -n for variable 'n'.
     * This is because as above, at any one time, there is only one valid placeholder in any sequence.
     *
     * Thus, all (-1, ...) are treated THE SAME in any sequence, per the above invariant/discussion, and so if they
     * end up being frequent, then in PrefixSpan, each of the pairwise combinations of the items in the itemset with the
     * placeholder, ie (_,a), (_,b), (_,c) for (_abc) will then be projected on; note that this is semantic in meaning,
     * and merely indicates that we continue to project on the next item in the itemset that's currently being projected
     * on, ie, (abc) -> (_bc), move on to 'b' in (_bc), rather than moving on to the next itemset in the sequence.
     *
     * Note: counting the support of placeholders: (-1, 2, 3) will thus contribute +1 to (_,2), (_,3); if (-1, 2, 3, 4)
     * is also in the same sequence, then (_2), (_3) sees +2 support, (_4) sees +1 support.
     *
     * Note: in PrefixSpan, we project on only a single item in each recursion, so the 'x' set will always be of size 1.
     */
    public static List<List<Integer>> project_on_seq(List<Set<Integer>> x, List<Set<Integer>> seq) {
        List<List<Integer>> x_ordered = new ArrayList<>(x.stream().map(SeqUtils::canonical_order).toList());

        if (x_ordered.size() == 0)
            return seq.stream().map(SeqUtils::canonical_order).toList();

        for (int i = 0; i < seq.size(); i++) {
            Set<Integer> itemset = seq.get(i);
            List<Integer> cur_x_itemset = x_ordered.get(0);
            if (itemset.containsAll(cur_x_itemset)) {
                x_ordered.remove(0);
            }
            if (x_ordered.size() == 0) {
                if (itemset.size() == 1)
                    return seq.subList(i + 1, seq.size()).stream()
                        .map(SeqUtils::canonical_order).toList();
                else {
                    List<Integer> itemset_ordered = canonical_order(itemset);
                    int idx = itemset_ordered.indexOf(cur_x_itemset.get(cur_x_itemset.size() - 1));

                    // if x seq matches up to the very last ordered element, then it matched the entire ordered itemset.
                    if (idx == itemset_ordered.size() - 1)
                        return seq.subList(i + 1, seq.size()).stream()
                                .map(SeqUtils::canonical_order).toList();

                    // x seq matched up to some index in the ordered itemset, but not the entire itemset.
                    itemset_ordered.set(idx, -1);
                    itemset_ordered = itemset_ordered.subList(idx, itemset_ordered.size()); // keep only -1 and after

                    List<List<Integer>> ret = new ArrayList<>(seq.subList(i, seq.size()).stream()
                        .map(SeqUtils::canonical_order).toList());
                    ret.set(0, itemset_ordered);
                    return ret;
                }
            }
        }

        throw new RuntimeException(String.format("Sequence x: %s is not contained in sequence %s", x, seq));
    }


}
