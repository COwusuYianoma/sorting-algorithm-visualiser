package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayGenerator {
    public static ArrayList<Integer> generateRandomIntegerArray(int size, int min, int max) {
        if (max <= min) {
            throw new IllegalArgumentException("max should be > min, but max is " + max + " and min is " + min);
        }

        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(ThreadLocalRandom.current().nextInt(min, max + 1));
        }

        return data;
    }
}
