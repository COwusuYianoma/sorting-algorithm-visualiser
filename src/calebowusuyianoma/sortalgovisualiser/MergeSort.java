package calebowusuyianoma.sortalgovisualiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MergeSort extends Sort {
    private static final String NAME = "Merge sort";
    private static final String LOW = "low";
    private static final String MIDDLE = "middle";
    private static final String HIGH = "high";

    private boolean shouldCalculateMiddleIndex;
    private int currentTreeNode;
    private Map<Integer, Map<String, Integer>> treeNodePointerMaps;
    private Map<Integer, Integer> parentNodes, numberOfSortedChildren;
    private Map<Integer, Boolean> sortedTreeNodes;
    private Map<String, Integer> currentPointerMap;

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

        int i = 0;
        int j = 0;
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

    public void moveToNextStep(ArrayList<Integer> data) {
        if (!running() && data.size() > 1) {
            setRunning(true);
            initialiseMapsBeforeSorting(data);
            currentPointerMap = treeNodePointerMaps.get(currentTreeNode);
            shouldCalculateMiddleIndex = true;
        } else if (unsortedSubArrayContainingMultipleElements(currentPointerMap, currentTreeNode)) {
            if (shouldCalculateMiddleIndex) {
                currentPointerMap.put(MIDDLE, calculateMiddleIndex(currentPointerMap));
                shouldCalculateMiddleIndex = false;
            } else {
                int previousTreeNode = currentTreeNode;
                moveToLeftChild(currentPointerMap);
                initialiseParentAndChildrenData(previousTreeNode);
                shouldCalculateMiddleIndex = true;
                currentPointerMap = treeNodePointerMaps.get(currentTreeNode);
            }
        } else {
            sortedTreeNodes.put(currentTreeNode, true);
            currentTreeNode = parentNodes.get(currentTreeNode);
            int sortedChildren = numberOfSortedChildren.get(currentTreeNode);
            numberOfSortedChildren.put(currentTreeNode, ++sortedChildren);
            currentPointerMap = treeNodePointerMaps.get(currentTreeNode);

            if (onlyLeftChildSorted(sortedChildren)) {
                int previousTreeNode = currentTreeNode;
                moveToRightChild(currentPointerMap);
                currentPointerMap = treeNodePointerMaps.get(currentTreeNode);
                initialiseParentAndChildrenData(previousTreeNode);
            } else {
                merge(data, currentPointerMap.get(LOW), currentPointerMap.get(MIDDLE), currentPointerMap.get(HIGH));
                sortedTreeNodes.put(currentTreeNode, true);
                if (currentTreeNodeIsRoot(data)) {
                    setSorted(true);
                }
            }
        }
    }

    private void initialiseMapsBeforeSorting(ArrayList<Integer> data) {
        Map<String, Integer> pointerMap = new HashMap<>();
        pointerMap.put(LOW, 0);
        pointerMap.put(MIDDLE, Integer.MIN_VALUE);
        pointerMap.put(HIGH, data.size() - 1);

        currentTreeNode = calculateUniqueNodeId(0, data.size() - 1);
        treeNodePointerMaps = new HashMap<>();
        treeNodePointerMaps.put(currentTreeNode, pointerMap);
        parentNodes = new HashMap<>();
        parentNodes.put(currentTreeNode, 0);
        numberOfSortedChildren = new HashMap<>();
        numberOfSortedChildren.put(currentTreeNode, 0);
        sortedTreeNodes = new HashMap<>();
    }

    private boolean unsortedSubArrayContainingMultipleElements(Map<String, Integer> currentPointerMap,
                                                               int currentTreeNode) {

        return (currentPointerMap.get(LOW) < currentPointerMap.get(HIGH)) &&
                !sortedTreeNodes.containsKey(currentTreeNode);
    }

    private int calculateMiddleIndex(Map<String, Integer> currentPointerMap) {
        return (int) Math.floor((double) (currentPointerMap.get(LOW) + currentPointerMap.get(HIGH)) / 2);
    }

    private int calculateUniqueNodeId(int low, int high) {
        return ((Math.max(low, high) * (Math.max(low, high) + 1)) / 2) + Math.min(low, high);
    }

    private void moveToLeftChild(Map<String, Integer> currentPointerMap) {
        int currentLow = currentPointerMap.get(LOW);
        int newHigh = currentPointerMap.get(MIDDLE);
        currentTreeNode = calculateUniqueNodeId(currentLow, newHigh);

        Map<String, Integer> pointerMap = new HashMap<>();
        pointerMap.put(LOW, currentLow);
        pointerMap.put(MIDDLE, Integer.MIN_VALUE);
        pointerMap.put(HIGH, newHigh);
        treeNodePointerMaps.put(currentTreeNode, pointerMap);
    }

    private void initialiseParentAndChildrenData(int previousTreeNode) {
        parentNodes.put(currentTreeNode, previousTreeNode);
        numberOfSortedChildren.put(currentTreeNode, 0);
    }

    private boolean onlyLeftChildSorted(int sortedChildren) {
        return sortedChildren == 1;
    }

    private void moveToRightChild(Map<String, Integer> currentPointerMap) {
        int currentMiddle = currentPointerMap.get(MIDDLE);
        int currentHigh = currentPointerMap.get(HIGH);
        int newLow = currentMiddle + 1;
        currentTreeNode = calculateUniqueNodeId(newLow, currentHigh);

        Map<String, Integer> pointerMap = new HashMap<>();
        pointerMap.put(LOW, newLow);
        pointerMap.put(MIDDLE, Integer.MIN_VALUE);
        pointerMap.put(HIGH, currentHigh);
        treeNodePointerMaps.put(currentTreeNode, pointerMap);
    }

    private boolean currentTreeNodeIsRoot(ArrayList<Integer> data) {
        return currentTreeNode == calculateUniqueNodeId(0, data.size() - 1);
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

    public Map<Integer, Boolean> getSortedTreeNodes() {
        return sortedTreeNodes;
    }
}
