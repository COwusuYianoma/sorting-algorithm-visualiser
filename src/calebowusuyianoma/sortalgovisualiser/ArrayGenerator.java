package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class ArrayGenerator {
    public ArrayList<Integer> generateRandomIntegerArray(int size, int minimumPossibleValue, int maximumPossibleValue) {
        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(generateRandomIntegerInRange(minimumPossibleValue, maximumPossibleValue));
        }

        return data;
    }

    public int generateRandomIntegerInRange(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("max should be >= min, but max is " + max + " and min is " + min);
        }

        return (int) ((Math.random() * (max - min + 1)) + min);
    }
}
