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
    private Map<Integer, Integer> parentNodes = new HashMap<>();
    private Map<String, Integer> currentPointerMap = new HashMap<>();
    private Map<Integer, Integer> numberOfSortedChildrenMap = new HashMap<>();
    private Map<Integer, Boolean> sortedTreeNodes = new HashMap<>();
    private Map<Integer, Map<String, Integer>> treeNodePointerMaps = new HashMap<>();

    public void moveToNextStep(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        } else if (data.size() <= 1) {
            setSorted(true);
        } else if (!isRunning()) {
            setRunning(true);
            initialiseMapsBeforeSorting(data);
            currentPointerMap = treeNodePointerMaps.get(currentTreeNode);
            shouldCalculateMiddleIndex = true;
        } else if (currentNodeCorrespondsToUnsortedSubArrayContainingMultipleElements()) {
            if (shouldCalculateMiddleIndex) {
                currentPointerMap.put(MIDDLE, calculateMiddleIndex(currentPointerMap));
                shouldCalculateMiddleIndex = false;
            } else {
                int previousTreeNode = currentTreeNode;
                moveToLeftChild();
                setParentAndChildrenData(previousTreeNode);
                shouldCalculateMiddleIndex = true;
            }
        } else {
            sortedTreeNodes.put(currentTreeNode, true);
            currentTreeNode = parentNodes.get(currentTreeNode);
            int sortedChildren = numberOfSortedChildrenMap.get(currentTreeNode);
            numberOfSortedChildrenMap.put(currentTreeNode, ++sortedChildren);
            currentPointerMap = treeNodePointerMaps.get(currentTreeNode);

            if (onlyLeftChildSorted(sortedChildren)) {
                int previousTreeNode = currentTreeNode;
                moveToRightChild();
                setParentAndChildrenData(previousTreeNode);
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
        treeNodePointerMaps.put(currentTreeNode, pointerMap);
        parentNodes.put(currentTreeNode, 0);
        numberOfSortedChildrenMap.put(currentTreeNode, 0);
    }

    private boolean currentNodeCorrespondsToUnsortedSubArrayContainingMultipleElements() {
        return (currentPointerMap.get(LOW) < currentPointerMap.get(HIGH)) &&
                (!sortedTreeNodes.containsKey(currentTreeNode));
    }

    private int calculateMiddleIndex(Map<String, Integer> currentPointerMap) {
        return (int) Math.floor((double) (currentPointerMap.get(LOW) + currentPointerMap.get(HIGH)) / 2);
    }

    private int calculateUniqueNodeId(int low, int high) {
        return ((Math.max(low, high) * (Math.max(low, high) + 1)) / 2) + Math.min(low, high);
    }

    private void moveToLeftChild() {
        int currentLow = currentPointerMap.get(LOW);
        int newHigh = currentPointerMap.get(MIDDLE);
        currentTreeNode = calculateUniqueNodeId(currentLow, newHigh);

        Map<String, Integer> pointerMap = new HashMap<>();
        pointerMap.put(LOW, currentLow);
        pointerMap.put(MIDDLE, Integer.MIN_VALUE);
        pointerMap.put(HIGH, newHigh);
        treeNodePointerMaps.put(currentTreeNode, pointerMap);
        currentPointerMap = pointerMap;
    }

    private void setParentAndChildrenData(int previousTreeNode) {
        parentNodes.put(currentTreeNode, previousTreeNode);
        numberOfSortedChildrenMap.put(currentTreeNode, 0);
    }

    private boolean onlyLeftChildSorted(int sortedChildren) {
        return sortedChildren == 1;
    }

    private void moveToRightChild() {
        int currentMiddle = currentPointerMap.get(MIDDLE);
        int currentHigh = currentPointerMap.get(HIGH);
        int newLow = currentMiddle + 1;
        currentTreeNode = calculateUniqueNodeId(newLow, currentHigh);

        Map<String, Integer> pointerMap = new HashMap<>();
        pointerMap.put(LOW, newLow);
        pointerMap.put(MIDDLE, Integer.MIN_VALUE);
        pointerMap.put(HIGH, currentHigh);
        treeNodePointerMaps.put(currentTreeNode, pointerMap);
        currentPointerMap = pointerMap;
    }

    private boolean currentTreeNodeIsRoot(ArrayList<Integer> data) {
        return currentTreeNode == calculateUniqueNodeId(0, data.size() - 1);
    }

    public void sort(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        }

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
        if (data == null) {
            throw new IllegalArgumentException("The data should contain at least one element, but it is null");
        }

        if ((data.isEmpty()) || (data.size() == 1)) {
            return;
        }

        validateIndices(data, lowIndex, middleIndex, highIndex);

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

    private void validateIndices(ArrayList<Integer> data, int lowIndex, int middleIndex, int highIndex) {
        if (lowIndex < 0) {
            throw new IllegalArgumentException("lowIndex must be >= 0 but is " + lowIndex);
        }

        if (middleIndex > data.size() - 1) {
            throw new IllegalArgumentException("middleIndex must be <= data.size() - 1, but middleIndex is " +
                    middleIndex + " and (data.size() - 1) equals " + (data.size() - 1));
        }

        if (lowIndex > middleIndex) {
            throw new IllegalArgumentException("lowIndex must be <= middleIndex, but lowIndex is " + lowIndex +
                    " and middleIndex is " + middleIndex);
        }

        if (highIndex > data.size() - 1) {
            throw new IllegalArgumentException("highIndex must be <= data.size() - 1, but highIndex is " +
                    highIndex + " and (data.size() - 1) equals " + (data.size() - 1));
        }

        if (middleIndex > highIndex) {
            throw new IllegalArgumentException("middleIndex must be <= highIndex, but middleIndex is " + middleIndex +
                    " and highIndex is " + highIndex);
        }
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

    public Map<String, Integer> getCurrentPointerMap() {
        return currentPointerMap;
    }

    public Map<Integer, Map<String, Integer>> getTreeNodePointerMaps() {
        return treeNodePointerMaps;
    }

    public Map<Integer, Boolean> getSortedTreeNodes() {
        return sortedTreeNodes;
    }

    public Map<Integer, Integer> getParentNodes() {
        return parentNodes;
    }

    public Map<Integer, Integer> getNumberOfSortedChildrenMap() {
        return numberOfSortedChildrenMap;
    }

    public boolean getShouldCalculateMiddleIndex() {
        return shouldCalculateMiddleIndex;
    }

    public void setCurrentTreeNode(int value) {
        currentTreeNode = value;
    }

    public void setCurrentPointerMap(Map<String, Integer> map) {
        if (map == null) {
            throw new IllegalArgumentException("Cannot assign null to map");
        }
        currentPointerMap = map;
    }

    public void setParentNodes(Map<Integer, Integer> parentNodes) {
        if (parentNodes == null) {
            throw new IllegalArgumentException("Cannot assign null to map");
        }
        this.parentNodes = parentNodes;
    }

    public void setSortedTreeNodes(Map<Integer, Boolean> map) {
        if (map == null) {
            throw new IllegalArgumentException("Cannot assign null to map");
        }
        sortedTreeNodes = map;
    }

    public void setNumberOfSortedChildrenMap(Map<Integer, Integer> map) {
        if (map == null) {
            throw new IllegalArgumentException("Cannot assign null to map");
        }
        numberOfSortedChildrenMap = map;
    }

    public void setTreeNodePointerMaps(Map<Integer, Map<String, Integer>> map) {
        if (map == null) {
            throw new IllegalArgumentException("Cannot assign null to map");
        }
        treeNodePointerMaps = map;
    }

    public void setShouldCalculateMiddleIndex(boolean value) {
        shouldCalculateMiddleIndex = value;
    }
}
