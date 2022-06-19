import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * List Utilities
 *
 * Author: Nurrachman Liu   2022-03
 */
public class ListUtils {

    public static void permute(int arr[], int pos, List<List<Integer>> seqs, int limit) {
        if (pos == arr.length) {
            seqs.add(Arrays.stream(arr).boxed().toList());
            return;
        }
        for (int i = 0; i <= limit; i++) {
            arr[pos] = i;
            permute(arr, pos+1, seqs, limit);
        }
    }

}
