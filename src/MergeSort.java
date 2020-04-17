import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MergeSort {
    public void sort(ArrayList<Integer> data, int lowIndex, int highIndex) {
        if(lowIndex < highIndex) {
            int middleIndex = (int)Math.floor((double)(lowIndex + highIndex) / 2);
            sort(data, lowIndex, middleIndex);
            sort(data, middleIndex + 1, highIndex);
            merge(data, lowIndex, middleIndex, highIndex);
        }
    }

    private void merge(ArrayList<Integer> data, int lowIndex, int middleIndex, int highIndex) {
        // TODO: Delete these if not necessary
//        int leftSubArraySize = middleIndex - lowIndex + 2;
//        int rightSubArraySize = highIndex - middleIndex + 1;
//        System.out.println("leftSubArraySize: " + leftSubArraySize);
//        System.out.println("rightSubArraySize: " + rightSubArraySize);

        ArrayList<Integer> leftSubArray = new ArrayList<>(data.subList(lowIndex, middleIndex + 1));
        ArrayList<Integer> rightSubArray = new ArrayList<>(data.subList(middleIndex + 1, highIndex + 1));

//        int[] leftSubArray = new int[leftSubArraySize];
//        int[] rightSubArray = new int[rightSubArraySize];

//        for(int c = 0; c < leftSubArraySize - 1; c++) {
//            leftSubArray[c] = data[lowIndex + c];
//        }
//
//        for(int d = 0; d < rightSubArraySize - 1; d++) {
//            rightSubArray[d] = data[middleIndex + d + 1];
//        }
//
//        leftSubArray = Arrays.copyOfRange(data, lowIndex, middleIndex + 1);
//        rightSubArray = Arrays.copyOfRange(data, middleIndex + 1, highIndex + 1);

        System.out.print("leftSubArray: ");
        for(int a = 0; a < leftSubArray.size(); a++) {
            System.out.print(leftSubArray.get(a) + " ");
        }

        System.out.println();
        System.out.print("rightSubArray: ");
        for(int b = 0; b < rightSubArray.size(); b++) {
            System.out.print(rightSubArray.get(b) + " ");
        }

        System.out.println();
        System.out.println();

        leftSubArray.add(Integer.MAX_VALUE);
        rightSubArray.add(Integer.MAX_VALUE);

//        leftSubArray[leftSubArraySize - 1] = Integer.MAX_VALUE;
//        rightSubArray[rightSubArraySize - 1] = Integer.MAX_VALUE;

        int i = 0, j = 0;
        for(int k = lowIndex; k < highIndex + 1; k++) {
            System.out.println("loop variable k: " + k);
            System.out.println("leftSubArray.get(i): " + leftSubArray.get(i));
            System.out.println("rightSubArray.get(j): " + rightSubArray.get(j));
            if(leftSubArray.get(i) < rightSubArray.get(j)) {
                System.out.println("Getting value at index " + i + " from leftSubArray");
                data.set(k, leftSubArray.get(i));
//                data[k] = leftSubArray[i];
                i++;
                System.out.println("i was just incremented to: " + i);
                System.out.println();
            } else {
                System.out.println("Getting value at index " + i + " from rightSubArray");
                data.set(k, rightSubArray.get(j));
//                data[k] = rightSubArray[j];
                j++;
                System.out.println("j was just incremented to: " + j);
                System.out.println();
            }
        }

//        for(int e = 0; e < data.length; e++) {
//            System.out.print(data[e] + " ");
//        }
//        System.out.println();
    }
}
