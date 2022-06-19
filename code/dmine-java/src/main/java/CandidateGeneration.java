import java.util.*;

/**
 * Candidate generation methods for itemssets (patterns) and sequential itemsets (sequential patterns).
 *
 * Author: Nurrachman Liu   2022-04
 */
public class CandidateGeneration {

    /**
     * Generates candidate frequent patterns of size k+1 given a set of size-k frequent items. Candidates are generated
     * through self-join, which means that only patterns that have size k-1 items in common are joined together, with
     * only their last item allowed to differ.
     *
     * This class also provides the pruning method that follows candidate-generation.
     */
    public static class Itemsets {

        /**
         * Performs a self-join of a list of frequent patterns with itself. All (k-1)-similar patterns in the fp set are
         * joined together: (k-1)+1+1 = k+1 sized fp. Thus, it returns a set of size k+1 candidate frequent patterns.
         */
        public static Set<Set<Integer>> self_join(Collection<Set<Integer>> fp_set) {
            Set<Set<Integer>> candidates = new LinkedHashSet<>();

            Map<Set<Integer>, List<Set<Integer>>> similarity_map = new HashMap<>();

            // For every pattern, hash every possible (k-1) sized set into the similarity map. If it already exists, then
            //   a pair of candidates has been found. If not, store it.
            for (Set<Integer> pat : fp_set) {
                for (int i : pat) {
                    Set<Integer> pat_km1 = new HashSet<>(pat);
                    pat_km1.remove(i);
                    List<Set<Integer>> pat2_lst = similarity_map.getOrDefault(pat_km1, new ArrayList<>());
                    if (!pat2_lst.isEmpty()) {
                        for (Set<Integer> pat2 : pat2_lst) {
                            // the candidate is the union of the two (k-1)-sharing patterns, so len is (k-1)+1+1 = k+1
                            Set<Integer> cand = new HashSet<>();
                            cand.addAll(pat);
                            cand.addAll(pat2);
                            candidates.add(cand);
                        }
                    }
                    pat2_lst.add(pat);
                    similarity_map.put(pat_km1, pat2_lst);
                }
            }

            return candidates;
        }

        /**
         * Uses the monotonicity property to prune the (k+1)-sized candidate fp's using the k-sized fp's. The underlying
         * assumption is that *all* (k-1) subsets must also be frequent; ie, all 'paths' (patterns) that can be used to
         * build (get-to) the fp must also be frequent, and not just *one* path. This is a stronger condition than requiring
         * just one path to the fp to be frequent.
         */
        public static Set<Set<Integer>> prune(Collection<Set<Integer>> k_fp_set, Collection<Set<Integer>> kp1_fp_set) {
            Set<Set<Integer>> pruned_kp1_fp_set = new HashSet<>();

            for (Set<Integer> cand : kp1_fp_set) {
                // for any (k-1) subset of cand, it must be in the k-sized fp_set:
                boolean add = true;
                for (int i : cand) {
                    Set<Integer> cand_m1 = new HashSet<>(cand);
                    cand_m1.remove(i);
                    if (!k_fp_set.contains(cand_m1)) {
                        add = false;
                        break;
                    }
                }
                if (add)
                    pruned_kp1_fp_set.add(cand);
            }

            return pruned_kp1_fp_set;
        }
    }


    /**
     * Generates new candidates for sequential patterns self-joined.
     */
    public static class Sequential {

        /**
         * Self-join condition for sequential patterns is:  if you remove the first item in w1, and the last item in w2,
         * that the two sets become the same. We stress item in that if the first or last item in w1 or w2 is an itemset,
         * you still remove just one item from it. We assume a canonical ordering in the itemsets, so that if the first
         * or last item is an itemset such as {2, 3, 4}, then removing the first gives {3, 4} and removing the last gives
         * {2, 3}.
         */
        public static Set<List<Set<Integer>>> self_join(Collection<List<Set<Integer>>> seq_set) {
            Set<List<Set<Integer>>> candidates = new LinkedHashSet<>();

            for (List<Set<Integer>> seq1 : seq_set) {
                for (List<Set<Integer>> seq2 : seq_set) {
                    List<Set<Integer>> seq1_copy = new ArrayList<>(seq1);
                    List<Integer> seq1_e1_copy = new ArrayList<>(seq1.get(0));
                    List<Set<Integer>> seq2_copy = new ArrayList<>(seq2);
                    List<Integer> seq2_en_copy = new ArrayList<>(seq2.get(seq2.size()-1));

                    // Remove first from seq1 elem 1, last from seq2 elem n
                    Collections.sort(seq1_e1_copy);
                    Collections.sort(seq2_en_copy);
                    seq1_e1_copy.remove(0);
                    seq2_en_copy.remove(seq2_en_copy.size() - 1);
                    // Put the modified elements of seq1, seq2 back into seq1, seq2 copies.
                    seq1_copy.set(0, new HashSet<>(seq1_e1_copy));
                    seq2_copy.set(seq2.size()-1, new HashSet<>(seq2_en_copy));

                    if (seq1_copy.get(0).size() == 0)
                        seq1_copy.remove(0);
                    if (seq2_copy.get(seq2_copy.size()-1).size() == 0)
                        seq2_copy.remove(seq2_copy.size() - 1);

                    if (seq1_copy.equals(seq2_copy)) {
                        List<Set<Integer>> seq_new = new ArrayList<>(seq1);
                        if (seq2.get(seq2.size()-1).size() > 1) { // if its a >1 itemset, merge it into the last instead
                            Set<Integer> seq_new_last = new HashSet<>(seq_new.get(seq_new.size() - 1));
                            seq_new_last.addAll(seq2.get(seq2.size() - 1));
                            seq_new.set(seq_new.size() - 1, seq_new_last);
                        }
                        else
                            seq_new.add(seq2.get(seq2.size()-1));
                        candidates.add(seq_new);

                        // Special case: joining L1 with L1 (see GSP96 paper): handle (ab) as well.
                        if (seq1_copy.size() == 0 && ! seq1.get(0).equals(seq2.get(seq2.size() -1))) {
                            seq_new = new ArrayList<>();
                            Set<Integer> merged_itemset = new HashSet<>();
                            merged_itemset.addAll(seq1.get(0));
                            merged_itemset.addAll(seq2.get(seq2.size() - 1));
                            seq_new.add(merged_itemset);

                            candidates.add(seq_new);
                        }
                    }
                }
            }

            return candidates;

//            Map<List<Set<Integer>>, List<List<Set<Integer>>>> similarity_map = new HashMap<>();
//
//            // For each sequence, hash its (k-1)-sized list into the similarity map. If it already exists, then
//            //   a pair of candidates has been found. If not, store it.
//            for (List<Set<Integer>> seq : seq_set) {
//                List<Set<Integer>> seq_m1 = seq.subList(0, seq.size()-1);
//
//                List<List<Set<Integer>>> seqs = similarity_map.getOrDefault(seq_m1, null);
//
//                if (seqs == null) {
//                    // no match, so put it into the map
//                    seqs = new ArrayList<>();
//                    seqs.add(seq);
//                    similarity_map.put(seq_m1, seqs);
//                }
//                else {
//                    // a match, generate candidates
//
//                    List<List<Set<Integer>>> new_seqs = new ArrayList<>();
//
//                    Set<Integer> seq1_new = seq.get(seq.size()-1);
//
//                    for (List<Set<Integer>> seq2 : seqs) {
//                        Set<Integer> seq2_new = seq2.get(seq2.size()-1);
//                        List<Set<Integer>> new_seq;
//
//                        // generate pairwise ordered candidates (n^2)
//
//                        // aa
//                        new_seq = new ArrayList<>(seq_m1);
//                        new_seq.add(seq1_new);
//                        new_seq.add(seq1_new);
//                        new_seqs.add(new_seq);
//
//                        // ab
//                        new_seq = new ArrayList<>(seq_m1);
//                        new_seq.add(seq1_new);
//                        new_seq.add(seq2_new);
//                        new_seqs.add(new_seq);
//
//                        // ba
//                        new_seq = new ArrayList<>(seq_m1);
//                        new_seq.add(seq2_new);
//                        new_seq.add(seq1_new);
//                        new_seqs.add(new_seq);
//
//                        // bb
//                        new_seq = new ArrayList<>(seq_m1);
//                        new_seq.add(seq2_new);
//                        new_seq.add(seq2_new);
//                        new_seqs.add(new_seq);
//
//                        // generate pairwise unordered candidates (n(n-1)/2)
//
//                        // (ab)
//                        new_seq = new ArrayList<>(seq_m1);
//                        Set<Integer> new_itemset = new HashSet<>();
//                        new_itemset.addAll(seq1_new);
//                        new_itemset.addAll(seq2_new);
//                        new_seq.add(new_itemset);
//                        new_seqs.add(new_seq);
//                    }
//
//                    seqs.add(seq);  // add seq so future matches to seq_m1 also combine with this seq
//
//                    candidates.addAll(new_seqs);
//                }
//            }
//
//            return candidates;
        }


        /**
         * THe same pruning assumption as for itemsets is done for sequences, as well: we have candidates created from
         * self-joining two (k-1)-sharing prefixes, to form a (k-1)+1+1 = k+1 length candidate. Notice that we have
         * added two new elements (the +1, +1): so we must check that all possible (k+1)-1=k length subsets of the k+1-
         * length candidate is also frequent. We already know 1 subset is frequent: the (k-1)-prefix.
         * Now, we must check all other k-length subsets. The only other difference with itemset pruning is that here,
         * the ordering matters. Also, here, for each itemset, if it is chosen as the element to be included, we must
         * check with every single possible element.
         *
         * Per the GSP96 paper, pruning occurs by counting support in the original dataset (this is opposed to Apriori
         * which uses membership in the L_{k-1} set, rather than re-counting support). This is because sequences may
         * match rows (support + 1) in different ways, so using the previous L_{k-1} won't be able to accurately
         * capture the support of the candidate sequences.
         */
        public static Set<List<Set<Integer>>> prune(Collection<List<Set<Integer>>> dataset, Collection<List<Set<Integer>>> candidates, int min_supp) {
            Set<List<Set<Integer>>> pruned = new HashSet<>();

            Map<List<Set<Integer>>, Integer> counts = new HashMap<>();
//            // count the frequency of appearance of each k-sized fp in the itemsets
//            for (List<Set<Integer>> row : dataset) {
//                for (List<Set<Integer>> seq : candidates)
//                    if (SeqUtils.seqIsInRow(row, seq))
//                        counts.put(seq, counts.getOrDefault(seq, 0) + 1);

            for (List<Set<Integer>> cand : candidates) {
                boolean add = true;
                for (int i = 0; i< cand.size(); i++) {
                    Set<Integer> pat = cand.get(i);
                    for (int j : pat) {
                        Set<Integer> pat2 = new HashSet<>();
                        pat2.add(j);
                        // the (k-1) subsequence of cand must be in the k-sized seq_set:
                        List<Set<Integer>> cand_m1 = new ArrayList<>(cand);
                        cand_m1.set(i, pat2);
                        int supp = dataset.stream().filter(row -> SeqUtils.seqIsInRow(row, cand_m1)).toList().size();
                        if (supp < min_supp) {
                            add = false;
                            break;
                        }
                    }
                }
                if (add)
                    pruned.add(cand);
            }

            return pruned;
        }

    }

}
