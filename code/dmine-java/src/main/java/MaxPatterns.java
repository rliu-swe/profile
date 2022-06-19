import Utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Max-pattern operations and utiltiies.
 *
 * Author: Nurrachman Liu   2022-04
 */
public class MaxPatterns {


    /**
     * Given a level-wise candidate set of patterns ('li'), returns all potential max-patterns paired with their generating-patterns.
     */
    public static List<Pair<Set<Set<Integer>>, Set<Integer>>> findMaxPatterns(Collection<Set<Integer>> li) {
        List<Pair<Set<Set<Integer>>, Set<Integer>>> max_pats = new ArrayList<>();

        // Map of: (li minus 1)-patterns back to their li patterns
        Map<Set<Integer>, Set<Set<Integer>>> lim1_map = new HashMap<>();

        for (Set<Integer> pat : li) {
            for (int i : pat) {
                Set<Integer> pat2 = new HashSet<>(pat);
                pat2.remove(i);
                Set<Set<Integer>> bucket = lim1_map.getOrDefault(pat2, new HashSet<>());
                bucket.add(pat);
                lim1_map.put(pat2, bucket);
            }
        }

        // The max patterns are the unions of every bucket; but the buckets are modified after each max-pattern is generated:
        //    the shared-variable generating the max-pattern is projected out from all remaining buckets.
        List<Set<Integer>> common_prefixes = new ArrayList<>();
        List<Set<Integer>> keys = new ArrayList<>(lim1_map.keySet());
        List<Set<Integer>> keys_sorted = ItemsetUtils.sort(keys);
        for (int i = 0; i < keys_sorted.size(); i++) {
            Set<Integer> common_prefix = keys_sorted.get(i);
            Set<Set<Integer>> bucket = lim1_map.get(common_prefix);

            // Project-out all common_prefix's we've seen, from every pattern in the bucket
            bucket = bucket.stream().map(pat -> {
                Set<Integer> pat2 = new HashSet<>(pat);
                common_prefixes.forEach(pat2::removeAll);
                return pat2;
            }).collect(Collectors.toSet());

            Set<Integer> max_p = new HashSet<>();
            bucket.forEach(max_p::addAll);
            max_pats.add(Pair.of(bucket, max_p));

            common_prefixes.add(common_prefix);
        }

        return max_pats;
    }

}
