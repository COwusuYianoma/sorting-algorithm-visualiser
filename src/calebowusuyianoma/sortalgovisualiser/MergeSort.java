package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MergeSort extends Sort {
    private static final String NAME = "Merge sort";
    private static final String LOW = "low";
    private static final String MIDDLE = "middle";
    private static final String HIGH = "high";

    private boolean justCalculatedMiddle;
    private int currentTreeNode;
    private Map<Integer, Map<String, Integer>> treeNodePointerMaps;
    private Map<Integer, Integer> parentNodes, numberOfTimesChildrenHaveBeenMerged;
    private Map<Integer, Boolean> mergedTreeNodes;

    public void sort(ArrayList<Integer> data) {
        sort(data, 0, data.size() - 1);
    }

    private void sort(ArrayList<Integer> data, int lowIndex, int highIndex) {
        if (lowIndex < highIndex) {
            int middleIndex = (int) Math.floor((double) (lowIndex + highIndex) / 2);
            sort(data, lowIndex, middleIndex);
            sort(data, middleIndex + 1, highIndex);
            merge(data, lowIndex, middleIndex, highIndex);
        }
    }

    public void merge(ArrayList<Integer> data, int lowIndex, int middleIndex, int highIndex) {
        if (middleIndex + 1 > data.size() || highIndex + 1 > data.size()) {
            return;
        }

        ArrayList<Integer> leftSubArray = new ArrayList<>(data.subList(lowIndex, middleIndex + 1));
        ArrayList<Integer> rightSubArray = new ArrayList<>(data.subList(middleIndex + 1, highIndex + 1));
        leftSubArray.add(Integer.MAX_VALUE);
        rightSubArray.add(Integer.MAX_VALUE);

        int i = 0, j = 0;
        for (int k = lowIndex; k < highIndex + 1; k++) {
            if (leftSubArray.get(i) < rightSubArray.get(j)) {
                data.set(k, leftSubArray.get(i));
                i++;
            } else {
                data.set(k, rightSubArray.get(j));
                j++;
            }
        }
    }

    public void moveToNextStepInVisualisation(ArrayList<Integer> data) {
        if (!running() && data.size() > 1) {
            initialiseMapsBeforeSorting(data);
            setRunning(true);
        } else {
            Map<String, Integer> currentPointerMap = treeNodePointerMaps.get(currentTreeNode);
            if (correspondsToUnmergedSubArrayContainingMultipleElements(currentPointerMap, currentTreeNode)) {
                if (!justCalculatedMiddle) {
                    currentPointerMap.put(MIDDLE, calculateMiddleIndex(currentPointerMap));
                    justCalculatedMiddle = true;

                    return;
                }

                int previousTreeNode = currentTreeNode;
                int currentLow = currentPointerMap.get(LOW);
                int newHigh = currentPointerMap.get(MIDDLE);
                currentTreeNode = calculateUniqueNodeValue(currentLow, newHigh);

                Map<String, Integer> pointerMap = new HashMap<>();
                pointerMap.put(LOW, currentLow);
                pointerMap.put(MIDDLE, Integer.MIN_VALUE);
                pointerMap.put(HIGH, newHigh);
                treeNodePointerMaps.put(currentTreeNode, pointerMap);

                parentNodes.put(currentTreeNode, previousTreeNode);
                numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, 0);

                justCalculatedMiddle = false;
            } else {
                mergedTreeNodes.put(currentTreeNode, true);
                currentTreeNode = parentNodes.get(currentTreeNode);
                int numberOfTimesChildrenMerged = numberOfTimesChildrenHaveBeenMerged.get(currentTreeNode);

                if(numberOfTimesChildrenMerged == 0) {
                    numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, ++numberOfTimesChildrenMerged);
                    int previousTreeNode = currentTreeNode;

                    currentPointerMap = treeNodePointerMaps.get(currentTreeNode);
                    int currentMiddle = currentPointerMap.get(MIDDLE);
                    int currentHigh = currentPointerMap.get(HIGH);
                    int newLow = currentMiddle + 1;
                    currentTreeNode = calculateUniqueNodeValue(newLow, currentHigh);

                    Map<String, Integer> pointerMap = new HashMap<>();
                    pointerMap.put(LOW, newLow);
                    pointerMap.put(MIDDLE, Integer.MIN_VALUE);
                    pointerMap.put(HIGH, currentHigh);
                    treeNodePointerMaps.put(currentTreeNode, pointerMap);

                    parentNodes.put(currentTreeNode, previousTreeNode);
                    numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, 0);
                } else {
                    numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, ++numberOfTimesChildrenMerged);
                    currentPointerMap = treeNodePointerMaps.get(currentTreeNode);
                    merge(data, currentPointerMap.get(LOW), currentPointerMap.get(MIDDLE), currentPointerMap.get(HIGH));
                    mergedTreeNodes.put(currentTreeNode, true);
                    if(currentTreeNode == calculateUniqueNodeValue(0, data.size() - 1)) {
                        setSorted(true);
                    }
                }
            }
        }
    }

    private void initialiseMapsBeforeSorting(ArrayList<Integer> data) {
        Map<String, Integer> pointerMap = new HashMap<>();
        pointerMap.put(LOW, 0);
        pointerMap.put(MIDDLE, Integer.MIN_VALUE);
        pointerMap.put(HIGH, data.size() - 1);

        currentTreeNode = calculateUniqueNodeValue(0, data.size() - 1);
        treeNodePointerMaps = new HashMap<>();
        treeNodePointerMaps.put(currentTreeNode, pointerMap);

        parentNodes = new HashMap<>();
        parentNodes.put(currentTreeNode, 0);

        numberOfTimesChildrenHaveBeenMerged = new HashMap<>();
        numberOfTimesChildrenHaveBeenMerged.put(currentTreeNode, 0);

        mergedTreeNodes = new HashMap<>();
    }

    private boolean correspondsToUnmergedSubArrayContainingMultipleElements(Map<String, Integer> currentPointerMap,
                                                                            int currentTreeNode) {

        return (currentPointerMap.get(LOW) < currentPointerMap.get(HIGH) &&
                !mergedTreeNodes.containsKey(currentTreeNode));
    }

    private int calculateMiddleIndex(Map<String, Integer> currentPointerMap) {
        return (int) Math.floor((double) (currentPointerMap.get(LOW) + currentPointerMap.get(HIGH)) / 2);
    }

    private int calculateUniqueNodeValue(int low, int high) {
        return ((Math.max(low, high) * (Math.max(low, high) + 1)) / 2) + Math.min(low, high);
    }

    public static String getName() {
        return NAME;
    }

    public static String getLow() {
        return LOW;
    }

    public static String getMiddle() {
        return MIDDLE;
    }

    public static String getHigh() {
        return HIGH;
    }

    public int getCurrentTreeNode() {
        return currentTreeNode;
    }

    public Map<Integer, Map<String, Integer>> getTreeNodePointerMaps() {
        return treeNodePointerMaps;
    }

    public Map<Integer, Boolean> getMergedTreeNodes() {
        return mergedTreeNodes;
    }
}
