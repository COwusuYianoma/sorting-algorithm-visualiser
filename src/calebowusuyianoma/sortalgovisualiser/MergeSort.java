package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MergeSort {
    public static final String LOW = "low";
    public static final String MIDDLE = "middle";
    public static final String HIGH = "high";

    private boolean isRunning, isSorted, justCalculatedMiddle;
    private int currentTreeNode, numberOfTreeNodes;
    private Map<Integer, Map<String, Integer>> pointerMaps;
    private Map<String, Integer> currentPointerMap;
    private Map<Integer, Integer> parentNodes, numberOfTimesChildrenHaveBeenMerged;
    private Map<Integer, Boolean> merged;

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public boolean isSorted() {
        return isSorted;
    }

    public int getCurrentTreeNode() {
        return currentTreeNode;
    }

    public Map<Integer, Map<String, Integer>> getPointerMaps() {
        return pointerMaps;
    }

    public Map<Integer, Boolean> getMerged() {
        return merged;
    }

    public void adjustPointers(ArrayList<Integer> data) {
        if(!isRunning) {
            if(data.size() == 1) {
                isSorted = true;

                return;
            }

            numberOfTreeNodes = (2 * data.size()) - 1;

            currentTreeNode = calculateNodeValue(0, data.size() - 1);

            pointerMaps = new HashMap<>(numberOfTreeNodes);
            parentNodes = new HashMap<>(numberOfTreeNodes);
            numberOfTimesChildrenHaveBeenMerged = new HashMap<>(numberOfTreeNodes);

            Map<String, Integer> pointerMap = new HashMap<>();
            pointerMap.put(LOW, 0);
            pointerMap.put(MIDDLE, Integer.MIN_VALUE);
            pointerMap.put(HIGH, data.size() - 1);
            pointerMaps.put(currentTreeNode, pointerMap);

            parentNodes.put(currentTreeNode, 0);

            numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, 0);

            merged = new HashMap<>(numberOfTreeNodes);

            isRunning = true;
        } else {
            currentPointerMap = pointerMaps.get(currentTreeNode);
            if((currentPointerMap.get(LOW) < currentPointerMap.get(HIGH)) && !merged.containsKey(currentTreeNode)){
                if(!justCalculatedMiddle) {
                    currentPointerMap.put(MIDDLE, (int)Math.floor((double)(currentPointerMap.get(LOW)
                            + currentPointerMap.get(HIGH)) / 2));
                    justCalculatedMiddle = true;

                    return;
                }

                int previousTreeNode = currentTreeNode;
                int currentLow = currentPointerMap.get(LOW);
                int newHigh = currentPointerMap.get(MIDDLE);
                currentTreeNode = calculateNodeValue(currentLow, newHigh);

                Map<String, Integer> pointerMap = new HashMap<>();
                pointerMap.put(LOW, currentLow);
                pointerMap.put(MIDDLE, Integer.MIN_VALUE);
                pointerMap.put(HIGH, newHigh);
                pointerMaps.put(currentTreeNode, pointerMap);

                parentNodes.put(currentTreeNode, previousTreeNode);
                numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, 0);

                justCalculatedMiddle = false;
            } else {
                merged.put(currentTreeNode, true);

                // Navigate to the parent node
                currentTreeNode = parentNodes.get(currentTreeNode);
                System.out.println("Just navigated back to parentNode: " + currentTreeNode);

                int numberOfTimesChildrenMerged = numberOfTimesChildrenHaveBeenMerged.get(currentTreeNode);

                // If children have been merged zero times, then we've just come from the left branch.
                // So we'll go down the right branch.
                if(numberOfTimesChildrenMerged == 0) {
                    numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, ++numberOfTimesChildrenMerged);

                    int previousTreeNode = currentTreeNode;

                    currentPointerMap = pointerMaps.get(currentTreeNode);

                    int currentMiddle = currentPointerMap.get(MIDDLE);
                    int currentHigh = currentPointerMap.get(HIGH);
                    int newLow = currentMiddle + 1;
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
            }
        }
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
