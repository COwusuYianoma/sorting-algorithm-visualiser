package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MergeSort extends Sort {
    public static final String name = "Merge sort";
    public static final String LOW = "low";
    public static final String MIDDLE = "middle";
    public static final String HIGH = "high";

    private boolean justCalculatedMiddle;
    private int currentTreeNode;
    private Map<Integer, Map<String, Integer>> pointerMaps;
    private Map<String, Integer> currentPointerMap;
    private Map<Integer, Integer> parentNodes, numberOfTimesChildrenHaveBeenMerged;
    private Map<Integer, Boolean> merged;

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
        if(!running()) {
            if(data.size() == 1) {
                setSorted(true);

                return;
            }

            Map<String, Integer> pointerMap = new HashMap<>();
            pointerMap.put(LOW, 0);
            pointerMap.put(MIDDLE, Integer.MIN_VALUE);
            pointerMap.put(HIGH, data.size() - 1);

            currentTreeNode = calculateNodeValue(0, data.size() - 1);
            pointerMaps = new HashMap<>();
            pointerMaps.put(currentTreeNode, pointerMap);

            parentNodes = new HashMap<>();
            parentNodes.put(currentTreeNode, 0);

            numberOfTimesChildrenHaveBeenMerged = new HashMap<>();
            numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, 0);

            merged = new HashMap<>();

            setRunning(true);
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
                currentTreeNode = parentNodes.get(currentTreeNode);
                int numberOfTimesChildrenMerged = numberOfTimesChildrenHaveBeenMerged.get(currentTreeNode);

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
                } else {
                    numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, ++numberOfTimesChildrenMerged);
                    currentPointerMap = pointerMaps.get(currentTreeNode);
                    merge(data, currentPointerMap.get(LOW), currentPointerMap.get(MIDDLE), currentPointerMap.get(HIGH));
                    merged.put(currentTreeNode, true);
                    if(currentTreeNode == calculateNodeValue(0, data.size() - 1)) {
                        setSorted(true);
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
