package calebowusuyianoma.sortalgovisualiser;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MergeSortVisualiser implements SortVisualiser {
    private static final String LOW = "low";
    private static final String MIDDLE = "middle";
    private static final String HIGH = "high";

    private final MergeSort mergeSort = new MergeSort();

    private boolean sorted, running, shouldCalculateMiddleIndex;
    private int spaceBetweenBars, currentTreeNode;
    private Map<Integer, Integer> parentNodes = new HashMap<>();
    private Map<String, Integer> currentPointerMap = new HashMap<>();
    private Map<Integer, Integer> numberOfSortedChildrenMap = new HashMap<>();
    private Map<Integer, Boolean> sortedTreeNodes = new HashMap<>();
    private Map<Integer, Map<String, Integer>> treeNodePointerMaps = new HashMap<>();

    public MergeSortVisualiser() {}

    public MergeSortVisualiser(int spaceBetweenBars) {
        this.spaceBetweenBars = spaceBetweenBars;
    }

    @Override
    public void moveToNextStep(ArrayList<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("The data should not be null");
        } else if (data.size() <= 1) {
            sorted = true;
        } else if (!running) {
            running = true;
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
                mergeSort.merge(data, currentPointerMap.get(LOW), currentPointerMap.get(MIDDLE),
                        currentPointerMap.get(HIGH));
                sortedTreeNodes.put(currentTreeNode, true);
                if (currentTreeNodeIsRoot(data)) {
                    sorted = true;
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

    @Override
    public void paint(Graphics g, int maxArrayValue, int maxBarHeight,
                                           int xCoordinate, int barWidth, ArrayList<Integer> data) {

        for (int i = 0; i < data.size(); i++) {
            if (sorted) {
                g.setColor(Color.MAGENTA);
            } else if (sortedTreeNodes.containsKey(currentTreeNode)) {
                if (currentElementIsInSubArrayThatWasJustSorted(i)) {
                    g.setColor(Color.MAGENTA);
                } else {
                    g.setColor(Color.BLACK);
                }
            } else if (currentPointerMap.containsValue(i)) {
                if (currentElementCorrespondsToMiddleAndOuterPointers(i)) {
                    g.setColor(Color.GREEN);
                } else if (currentElementOnlyCorrespondsToMiddlePointer(i)) {
                    g.setColor(Color.YELLOW);
                } else {
                    g.setColor(Color.CYAN);
                }
            } else {
                g.setColor(Color.BLACK);
            }

            int height = (int) (((double) data.get(i) / maxArrayValue) * maxBarHeight);
            g.fillRect(xCoordinate, 0, barWidth, height);
            xCoordinate += (barWidth + spaceBetweenBars);
        }
    }

    private boolean currentElementIsInSubArrayThatWasJustSorted(int index) {
        return (currentPointerMap.get(LOW) <= index) &&
                (currentPointerMap.get(HIGH) >= index);
    }

    private boolean currentElementCorrespondsToMiddleAndOuterPointers(int index) {
        return (currentPointerMap.get(LOW) == index || currentPointerMap.get(HIGH) == index) &&
                (currentPointerMap.get(MIDDLE) == index);
    }

    private boolean currentElementOnlyCorrespondsToMiddlePointer(int index) {
        return currentPointerMap.get(MIDDLE) == index;
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

    public Map<String, Integer> getCurrentPointerMap() {
        return currentPointerMap;
    }

    public Map<Integer, Map<String, Integer>> getTreeNodePointerMaps() {
        return treeNodePointerMaps;
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

    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isSorted() {
        return sorted;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public void setCurrentTreeNode(int value) {
        currentTreeNode = value;
    }

    public void setCurrentPointerMap(Map<String, Integer> map) {
        if (map == null) {
            throw new IllegalArgumentException("The map should not be null");
        }
        currentPointerMap = map;
    }

    public void setParentNodes(Map<Integer, Integer> parentNodes) {
        if (parentNodes == null) {
            throw new IllegalArgumentException("The map should not be null");
        }
        this.parentNodes = parentNodes;
    }

    public void setSortedTreeNodes(Map<Integer, Boolean> map) {
        if (map == null) {
            throw new IllegalArgumentException("The map should not be null");
        }
        sortedTreeNodes = map;
    }

    public void setNumberOfSortedChildrenMap(Map<Integer, Integer> map) {
        if (map == null) {
            throw new IllegalArgumentException("The map should not be null");
        }
        numberOfSortedChildrenMap = map;
    }

    public void setTreeNodePointerMaps(Map<Integer, Map<String, Integer>> map) {
        if (map == null) {
            throw new IllegalArgumentException("The map should not be null");
        }
        treeNodePointerMaps = map;
    }

    public void setShouldCalculateMiddleIndex(boolean shouldCalculate) {
        shouldCalculateMiddleIndex = shouldCalculate;
    }
}