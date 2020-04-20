package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MergeSort {
    public static final String LOW = "low";
    public static final String MIDDLE = "middle";
    public static final String HIGH = "high";
    public static final String TIMES_LEVEL_MERGED = "timesLevelMerged";

    private boolean isRunning = false, isSorted = false, justCalculatedMiddle = false, justCalledSortLeft = false,
    justCalledSortRight = false,  justCalledMerge = false;
    private int low, middle = Integer.MIN_VALUE, high, level, numberOfTimesLevelBelowZeroHasBeenMerged;
    private ArrayList<Map<String, Integer>> pointerMaps = new ArrayList<>();
    private Map<String, Integer> currentPointerMap;

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isSorted() {
        return isSorted;
    }

    public boolean justCalledSort() {
        return justCalledSortLeft;
    }

    public boolean justCalculatedMiddle() {
        return justCalculatedMiddle;
    }

    public boolean justCalledMerge() {
        return justCalledMerge;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Map<String, Integer>> getPointerMaps() {
        return pointerMaps;
    }

    public int[] getPointers() {
        return new int[] {low, middle, high};
    }

    public int getMiddlePointer() {
        return middle;
    }

    public void adjustPointers(ArrayList<Integer> data) {
        if(!isRunning) {
            level = 0;

            Map<String, Integer> pointerMap = new HashMap<>();
            pointerMap.put(LOW, 0);
            pointerMap.put(MIDDLE, Integer.MIN_VALUE);
            pointerMap.put(HIGH, data.size() - 1);
            pointerMaps.add(pointerMap);

            isRunning = true;
            justCalledSortLeft = true;
        } else if (justCalculatedMiddle) {
            System.out.println("In justCalculatedMiddle branch");
            System.out.println();

            int currentLow = pointerMaps.get(level).get(LOW);
            int currentMiddle = pointerMaps.get(level).get(MIDDLE);

            // Check if a pointerMap exists at the level above. If one doesn't add it; o/w, update it.
            if(pointerMaps.size() >= level + 2) {
                level++;
                pointerMaps.get(level).put(LOW, currentLow);
                pointerMaps.get(level).put(MIDDLE, Integer.MIN_VALUE);
                pointerMaps.get(level).put(HIGH, currentMiddle);
            } else {
                Map<String, Integer> pointerMap = new HashMap<>();
                pointerMap.put(LOW, currentLow);
                pointerMap.put(MIDDLE, Integer.MIN_VALUE);
                pointerMap.put(HIGH, currentMiddle);
                pointerMaps.add(pointerMap);
                level++;
            }

            justCalculatedMiddle = false;
            justCalledSortLeft = true;
        } else if (justCalledSortLeft) {
            System.out.println("In justCalledSortLeft branch");
            System.out.println();

            currentPointerMap = pointerMaps.get(level);
            if(currentPointerMap.get(LOW) < currentPointerMap.get(HIGH)) {
                // middle = (int)Math.floor((double)(low + high) / 2);
                currentPointerMap.put(MIDDLE, (int)Math.floor((double)(currentPointerMap.get(LOW)
                        + currentPointerMap.get(HIGH)) / 2));
                justCalledSortLeft = false;
                justCalledSortRight = false;
                justCalculatedMiddle = true;
            } else {
                level--;
                currentPointerMap = pointerMaps.get(level);
                level++;
                pointerMaps.get(level).put(LOW, currentPointerMap.get(MIDDLE) + 1);
                pointerMaps.get(level).put(MIDDLE, Integer.MIN_VALUE);
                pointerMaps.get(level).put(HIGH, currentPointerMap.get(HIGH));

                justCalledSortLeft = false;
                justCalledSortRight = true;
            }
        } else if(justCalledSortRight) {
            System.out.println("In justCalledSortRight branch");
            System.out.println();

            currentPointerMap = pointerMaps.get(level);
            if(currentPointerMap.get(LOW) < currentPointerMap.get(HIGH)) {
                // middle = (int)Math.floor((double)(low + high) / 2);
                currentPointerMap.put(MIDDLE, (int)Math.floor((double)(currentPointerMap.get(LOW)
                        + currentPointerMap.get(HIGH)) / 2));
                justCalledSortLeft = false;
                justCalledSortRight = false;
                justCalculatedMiddle = true;
            } else {
                level--;
                currentPointerMap = pointerMaps.get(level);
                merge(data, currentPointerMap.get(LOW), currentPointerMap.get(MIDDLE), currentPointerMap.get(HIGH));
                justCalledSortRight = false;
                justCalledMerge = true;
            }
        }
        else if(justCalledMerge) {
            System.out.println("In justCalledMerge branch");
            System.out.println();

            level--;
            currentPointerMap = pointerMaps.get(level);
            int timesLevelMerged = 0;
            if(currentPointerMap.containsKey(TIMES_LEVEL_MERGED)) {
                timesLevelMerged = currentPointerMap.get(TIMES_LEVEL_MERGED);
                if(timesLevelMerged == Math.pow(2, level) - 1) {
                    merge(data, currentPointerMap.get(LOW), currentPointerMap.get(MIDDLE), currentPointerMap.get(HIGH));
                    currentPointerMap.put(TIMES_LEVEL_MERGED, timesLevelMerged++);

                    if(level == 0) {
                        isSorted = true;
                    }

                    return;
                }
                currentPointerMap.put(TIMES_LEVEL_MERGED, timesLevelMerged++);
            } else {
                currentPointerMap.put(TIMES_LEVEL_MERGED, timesLevelMerged);
                level++;
                pointerMaps.get(level).put(LOW, currentPointerMap.get(MIDDLE) + 1);
                pointerMaps.get(level).put(MIDDLE, Integer.MIN_VALUE);
                pointerMaps.get(level).put(HIGH, currentPointerMap.get(HIGH));
                justCalledMerge = false;
                justCalledSortRight = true;
            }
        }
    }

    public void sort(ArrayList<Integer> data) {
        sort(data, 0, data.size() - 1);
    }

    private void sort(ArrayList<Integer> data, int lowIndex, int highIndex) {
        if(lowIndex < highIndex) {
            int middleIndex = (int)Math.floor((double)(lowIndex + highIndex) / 2);
            sort(data, lowIndex, middleIndex);
            sort(data, middleIndex + 1, highIndex);
            merge(data, lowIndex, middleIndex, highIndex);
        }
    }

    private void merge(ArrayList<Integer> data, int lowIndex, int middleIndex, int highIndex) {
        ArrayList<Integer> leftSubArray = new ArrayList<>(data.subList(lowIndex, middleIndex + 1));
        ArrayList<Integer> rightSubArray = new ArrayList<>(data.subList(middleIndex + 1, highIndex + 1));

        leftSubArray.add(Integer.MAX_VALUE);
        rightSubArray.add(Integer.MAX_VALUE);

        int i = 0, j = 0;
        for(int k = lowIndex; k < highIndex + 1; k++) {
            if(leftSubArray.get(i) < rightSubArray.get(j)) {
                data.set(k, leftSubArray.get(i));
                i++;
            } else {
                data.set(k, rightSubArray.get(j));
                j++;
            }
        }
    }
}
