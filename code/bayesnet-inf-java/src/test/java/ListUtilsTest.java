import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Author: Nurrachman Liu   2022-03
 */
class ListUtilsTest {

    @Test
    void testPermute() {
        int[] arr = new int[2];
        List<List<Integer>> seqs = new ArrayList<>();
        ListUtils.permute(arr, 0, seqs, 3);

        StringBuilder sb = new StringBuilder();
        for (List<Integer> seq : seqs) {
            sb.append(seq.toString()).append("\n");
        }
        String ret = sb.toString();

        String correct =
            "[0, 0]\n" +
            "[0, 1]\n" +
            "[0, 2]\n" +
            "[0, 3]\n" +
            "[1, 0]\n" +
            "[1, 1]\n" +
            "[1, 2]\n" +
            "[1, 3]\n" +
            "[2, 0]\n" +
            "[2, 1]\n" +
            "[2, 2]\n" +
            "[2, 3]\n" +
            "[3, 0]\n" +
            "[3, 1]\n" +
            "[3, 2]\n" +
            "[3, 3]\n";

        System.out.print("Testing ListUtils.permute: ");
        //System.out.println(ret);
        Assertions.assertEquals(correct, ret);
        System.out.println("PASSED");

    }
}