import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;

class MergeSortTest {
    private MergeSort mergeSort = new MergeSort();
//    private ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
//    private int[] expected = new int[] {1, 2, 3, 4, 5, 6};

    @Test
    public void sortExecutesCorrectlyWhenDataIsReversed() {
        ArrayList<Integer> data = new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2, 1));
        mergeSort.sort(data, 0, data.size() - 1);
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        Assertions.assertEquals(expected, data);

//        int[] data = new int[] {6, 5, 4, 3, 2, 1};
//        mergeSort.sort(data, 0, data.length - 1);
//        int[] expected = new int[] {1, 2, 3, 4, 5, 6};
//        System.out.print("Sorted array: ");
//        for(int i = 0; i < data.length; i++) {
//            System.out.print(data[i] + " ");
//        }
//
//        Assertions.assertTrue(Arrays.equals(expected, data));
    }
}