package com.calebkoy.sortingalgovisualiser;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ArrayGenerator {
    public static ArrayList<Integer> generateRandomPositiveIntegerArray(int size, int min, int max) {
        if ((min <= 0) || (max <= 0)) {
            throw new IllegalArgumentException("max and min should be > 0, but max is " + max + " and min is " + min);
        }

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
