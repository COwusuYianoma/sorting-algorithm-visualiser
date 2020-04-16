import java.util.ArrayList;

public class ArrayGenerator {
    public ArrayList<Integer> generateRandomArray(int size, int max) {
        int min = 1;
        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(getRandomNumberInRange(min, max));
        }

        return data;
    }

    private int getRandomNumberInRange(int min, int max) {
        return (int)((Math.random() * ((max - min) + 1)) + min);
    }
}
