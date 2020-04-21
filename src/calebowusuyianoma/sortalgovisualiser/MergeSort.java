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
    private int low, middle = Integer.MIN_VALUE, high, level, numberOfTimesLevelBelowZeroHasBeenMerged,
            currentTreeNode, numberOfTreeNodes;
    // private ArrayList<Map<String, Integer>> pointerMaps;
    private Map<Integer, Map<String, Integer>> pointerMaps;
    private Map<String, Integer> currentPointerMap;
    //private ArrayList<Integer> parentNodes, numberOfTimesChildrenHaveBeenMerged;
    private Map<Integer, Integer> parentNodes, numberOfTimesChildrenHaveBeenMerged;
    private Map<Integer, Boolean> merged;
    //private boolean[] merged;

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

    public int getNumberOfTreeNodes() {
        return numberOfTreeNodes;
    }

    public int getCurrentTreeNode() {
        return currentTreeNode;
    }

    public Map<Integer, Map<String, Integer>> getPointerMaps() {
        return pointerMaps;
    }

    public int[] getPointers() {
        return new int[] {low, middle, high};
    }

    public int getMiddlePointer() {
        return middle;
    }

    public Map<Integer, Boolean> getMerged() {
        return merged;
    }

    public void adjustPointers(ArrayList<Integer> data) {
        if(!isRunning) {
            level = 0; // TODO: get rid of the concept of a level if it's not necessary

            numberOfTreeNodes = (2 * data.size()) - 1;

            // currentTreeNode = data.size() - 1;
            currentTreeNode = calculateNodeValue(0, data.size() - 1);

//            pointerMaps = new ArrayList<>(numberOfTreeNodes);
//            parentNodes = new ArrayList<>(numberOfTreeNodes);
//            numberOfTimesChildrenHaveBeenMerged = new ArrayList<>(numberOfTreeNodes);
            pointerMaps = new HashMap<>(numberOfTreeNodes);
            parentNodes = new HashMap<>(numberOfTreeNodes);
            numberOfTimesChildrenHaveBeenMerged = new HashMap<>(numberOfTreeNodes);

//            for(int i = 0; i < numberOfTreeNodes; i++) { // Extract into a method
//                pointerMaps.add(null);
//                parentNodes.add(null);
//                numberOfTimesChildrenHaveBeenMerged.add(null);
//            }
//            System.out.println("Just initialised pointerMaps with size: " + pointerMaps.size());
//            System.out.println("Just initialised parentNodes with size: " + parentNodes.size());
//            System.out.println("Just initialised numberOfTimesChildrenHaveBeenMerged with size: "
//                    + numberOfTimesChildrenHaveBeenMerged.size());

            //return;

            System.out.println("numberOfTreeNodes: " + numberOfTreeNodes);
            System.out.println();

            Map<String, Integer> pointerMap = new HashMap<>();
            pointerMap.put(LOW, 0);
            pointerMap.put(MIDDLE, Integer.MIN_VALUE);
            pointerMap.put(HIGH, data.size() - 1);
            System.out.println("Just set the LOW, MIDDLE and HIGH values for pointerMap");
            pointerMaps.put(currentTreeNode, pointerMap);
            System.out.println("Just set the pointerMap for the currentTreeNode: " + currentTreeNode);

            parentNodes.put(currentTreeNode, 0);
            System.out.println("Just set the parentNode for the currentTreeNode: " + currentTreeNode);

            numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, 0);
            System.out.println("Just set the numberOfTimesChildrenHaveBeenMerged for the currentTreeNode: "
                    + currentTreeNode);
            System.out.println();

            merged = new HashMap<>(numberOfTreeNodes);

            isRunning = true;
            justCalledSortLeft = true;
        }
//        else if (justCalculatedMiddle) {
//            System.out.println("In justCalculatedMiddle branch");
//            System.out.println();
//
//            // int currentLow = pointerMaps.get(level).get(LOW);
//            // int currentMiddle = pointerMaps.get(level).get(MIDDLE);
//            int currentLow = pointerMaps.get(currentTreeNode).get(LOW);
//            int currentMiddle = pointerMaps.get(currentTreeNode).get(MIDDLE);
//
//            // Check if a pointerMap exists at the level above. If one doesn't add it; o/w, update it.
////            if(pointerMaps.size() >= level + 2) {
////                level++;
////                pointerMaps.get(level).put(LOW, currentLow);
////                pointerMaps.get(level).put(MIDDLE, Integer.MIN_VALUE);
////                pointerMaps.get(level).put(HIGH, currentMiddle);
////            } else {
////                Map<String, Integer> pointerMap = new HashMap<>();
////                pointerMap.put(LOW, currentLow);
////                pointerMap.put(MIDDLE, Integer.MIN_VALUE);
////                pointerMap.put(HIGH, currentMiddle);
////                pointerMaps.add(pointerMap);
////                level++;
////                currentTreeNode++;
////            }
//
//            // If
//
//            justCalculatedMiddle = false;
//            justCalledSortLeft = true;
        else if (justCalledSortLeft) { // When things are working, you could change this to justCalledSort or else
            System.out.println("In justCalledSortLeft branch");
            System.out.println();

            // If merged?

            // currentPointerMap = pointerMaps.get(level);
            currentPointerMap = pointerMaps.get(currentTreeNode);
            if((currentPointerMap.get(LOW) < currentPointerMap.get(HIGH)) && !merged.containsKey(currentTreeNode)){
                // middle = (int)Math.floor((double)(low + high) / 2);
                currentPointerMap.put(MIDDLE, (int)Math.floor((double)(currentPointerMap.get(LOW)
                        + currentPointerMap.get(HIGH)) / 2));

                int previousTreeNode = currentTreeNode;
                int currentLow = currentPointerMap.get(LOW);
                int newHigh = currentPointerMap.get(MIDDLE);
                // currentTreeNode = currentLow + newHigh;
                currentTreeNode = calculateNodeValue(currentLow, newHigh);

                Map<String, Integer> pointerMap = new HashMap<>();
                pointerMap.put(LOW, currentLow);
                pointerMap.put(MIDDLE, Integer.MIN_VALUE);
                pointerMap.put(HIGH, newHigh);
                pointerMaps.put(currentTreeNode, pointerMap);

                parentNodes.put(currentTreeNode, previousTreeNode);
                numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, 0);

//                justCalledSortLeft = false;
//                justCalledSortRight = false;
//                justCalculatedMiddle = true;
            } else {
                // level--;
                System.out.println("In else branch of justCalledSortLeft");

                // Set the current node to merged
                merged.put(currentTreeNode, true);

                // Navigate to the parent node
                currentTreeNode = parentNodes.get(currentTreeNode);
                System.out.println("Just navigated back to parentNode: " + currentTreeNode);

                // Get no. of times children have been merged
                int numberOfTimesChildrenMerged = numberOfTimesChildrenHaveBeenMerged.get(currentTreeNode);
                System.out.println("Number of times children have been merged for this node: "
                        + numberOfTimesChildrenMerged);
                System.out.println();

                // If children have been merged zero times, then we've just come from the left branch.
                // So we'll go down the right branch.
                if(numberOfTimesChildrenMerged == 0) {
                    numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, ++numberOfTimesChildrenMerged);

                    int previousTreeNode = currentTreeNode;

                    currentPointerMap = pointerMaps.get(currentTreeNode);

                    int currentMiddle = currentPointerMap.get(MIDDLE);
                    int currentHigh = currentPointerMap.get(HIGH);
                    int newLow = currentMiddle + 1;
                    // currentTreeNode = newLow + currentHigh;
                    currentTreeNode = calculateNodeValue(newLow, currentHigh);

                    Map<String, Integer> pointerMap = new HashMap<>();
                    pointerMap.put(LOW, newLow);
                    pointerMap.put(MIDDLE, Integer.MIN_VALUE);
                    pointerMap.put(HIGH, currentHigh);
                    pointerMaps.put(currentTreeNode, pointerMap);

                    parentNodes.put(currentTreeNode, previousTreeNode);
                    numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, 0);
                } else { // The only other case is that numberOfTimesChildrenMerged == 1
                    numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, ++numberOfTimesChildrenMerged);

                    // Merge the sorted sub-arrays for this node
                    currentPointerMap = pointerMaps.get(currentTreeNode);
                    merge(data, currentPointerMap.get(LOW), currentPointerMap.get(MIDDLE), currentPointerMap.get(HIGH));
                    merged.put(currentTreeNode, true);
                    if(currentTreeNode == calculateNodeValue(0, data.size() - 1)) {
                        isSorted = true;
                    }
                }


                // Set the pointers and other data for the right child

                // Move right

                // -----------------------------------------
                // OLD CODE

                // currentPointerMap = pointerMaps.get(level);
                // currentTimesChildrenHaveBeenMerged
                // numberOfTimesChildrenHaveBeenMerged.set(level, );

//                level++;
//                pointerMaps.get(level).put(LOW, currentPointerMap.get(MIDDLE) + 1);
//                pointerMaps.get(level).put(MIDDLE, Integer.MIN_VALUE);
//                pointerMaps.get(level).put(HIGH, currentPointerMap.get(HIGH));
//
//                justCalledSortLeft = false;
//                justCalledSortRight = true;
            }
        }
//        else if(justCalledSortRight) {
//            System.out.println("In justCalledSortRight branch");
//            System.out.println();
//
//            currentPointerMap = pointerMaps.get(level);
//            if(currentPointerMap.get(LOW) < currentPointerMap.get(HIGH)) {
//                // middle = (int)Math.floor((double)(low + high) / 2);
//                currentPointerMap.put(MIDDLE, (int)Math.floor((double)(currentPointerMap.get(LOW)
//                        + currentPointerMap.get(HIGH)) / 2));
//                justCalledSortLeft = false;
//                justCalledSortRight = false;
//                justCalculatedMiddle = true;
//            } else {
//                level--;
//                currentPointerMap = pointerMaps.get(level);
//                merge(data, currentPointerMap.get(LOW), currentPointerMap.get(MIDDLE), currentPointerMap.get(HIGH));
//                justCalledSortRight = false;
//                justCalledMerge = true;
//            }
//        }
//        else if(justCalledMerge) {
//            System.out.println("In justCalledMerge branch");
//            System.out.println();
//
//            level--;
//            currentPointerMap = pointerMaps.get(level);
//            int timesLevelMerged = 0;
//            if(currentPointerMap.containsKey(TIMES_LEVEL_MERGED)) {
//                timesLevelMerged = currentPointerMap.get(TIMES_LEVEL_MERGED);
//                if(timesLevelMerged == Math.pow(2, level) - 1) {
//                    merge(data, currentPointerMap.get(LOW), currentPointerMap.get(MIDDLE), currentPointerMap.get(HIGH));
//                    currentPointerMap.put(TIMES_LEVEL_MERGED, timesLevelMerged++);
//
//                    if(level == 0) {
//                        isSorted = true;
//                    }
//
//                    return;
//                }
//                currentPointerMap.put(TIMES_LEVEL_MERGED, timesLevelMerged++);
//            } else {
//                currentPointerMap.put(TIMES_LEVEL_MERGED, timesLevelMerged);
//                level++;
//                pointerMaps.get(level).put(LOW, currentPointerMap.get(MIDDLE) + 1);
//                pointerMaps.get(level).put(MIDDLE, Integer.MIN_VALUE);
//                pointerMaps.get(level).put(HIGH, currentPointerMap.get(HIGH));
//                justCalledMerge = false;
//                justCalledSortRight = true;
//            }
//        }
    }

    private int calculateNodeValue(int low, int high) {
        return ((Math.max(low, high) * (Math.max(low, high) + 1)) / 2) + Math.min(low, high);
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
