package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;

public class TestUtilities {
    public boolean isSorted(ArrayList<Integer> data) {
        for(int i = 0; i < data.size() - 1; i++) {
            if(data.get(i) > data.get(i + 1)) {
                return false;
            }
        }

        return true;
    }
}
