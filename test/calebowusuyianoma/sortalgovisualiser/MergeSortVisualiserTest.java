package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MergeSortVisualiserTest {
    private final MergeSortVisualiser mergeSortVisualiser = new MergeSortVisualiser();

    private ArrayList<Integer> data;

    @Mock private Map<String, Integer> currentPointerMap;
    @Mock private Map<Integer, Integer> parentNodes;
    @Mock private Map<Integer, Integer> numberOfSortedChildrenMap;
    @Mock private Map<Integer, Boolean> sortedTreeNodes;
    @Mock private Map<Integer, Map<String, Integer>> treeNodePointerMaps;
    @Mock private Graphics g;

    @Test
    public void moveToNextStepThrowsExceptionWhenDataIsNull() {
        // Arrange
        data = null;
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                mergeSortVisualiser.moveToNextStep(data));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataIsEmpty() {
        // Arrange
        data = new ArrayList<>();

        // Act
        mergeSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(mergeSortVisualiser.isSorted());
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenDataContainsSingleElement() {
        // Arrange
        data = new ArrayList<>(Collections.singletonList(6));

        // Act
        mergeSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(mergeSortVisualiser.isSorted());
    }

    @Test
    public void moveToNextStepSetsRunningToTrueAndInitialisesMapsWhenSortNotRunning() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSortVisualiser.setRunning(false);

        // Act
        mergeSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(mergeSortVisualiser.isRunning());
        assertFalse(mergeSortVisualiser.getTreeNodePointerMaps().isEmpty());
        assertFalse(mergeSortVisualiser.getParentNodes().isEmpty());
        assertFalse(mergeSortVisualiser.getNumberOfSortedChildrenMap().isEmpty());
    }

    @Test
    public void moveToNextStepCalculatesMiddleIndexIfNotCalculatedAndCurrentSubArrayContainsMultipleUnsortedElements() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSortVisualiser.setRunning(true);
        mergeSortVisualiser.setShouldCalculateMiddleIndex(true);

        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(data.size() - 1);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        // Act
        mergeSortVisualiser.moveToNextStep(data);

        // Assert
        verify(currentPointerMap).put(eq(MergeSortVisualiser.getMiddle()), anyInt());
        assertFalse(mergeSortVisualiser.getShouldCalculateMiddleIndex());
    }

    @Test
    public void moveToNextStepMovesToLeftChildIfMiddleCalculatedAndCurrentSubArrayContainsMultipleUnsortedElements() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSortVisualiser.setRunning(true);
        mergeSortVisualiser.setShouldCalculateMiddleIndex(false);

        int middleIndex = 1;
        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSortVisualiser.getMiddle())).thenReturn(middleIndex);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(data.size() - 1);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        // Act
        mergeSortVisualiser.moveToNextStep(data);

        // Assert
        Map<String, Integer> currentPointerMap = mergeSortVisualiser.getCurrentPointerMap();
        assertEquals(middleIndex, currentPointerMap.get(MergeSortVisualiser.getHigh()));
    }

    @Test
    public void moveToNextStepMovesToRightChildWhenOnlyLeftChildSorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSortVisualiser.setRunning(true);

        int initialTreeNode = 0;
        mergeSortVisualiser.setCurrentTreeNode(initialTreeNode);

        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(0);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        int parentNode = 1;
        setParentNodes(initialTreeNode, parentNode);
        setNumberOfSortedChildrenMap(parentNode, 0);

        int middleIndex = 0;
        setTreeNodePointerMaps(parentNode, middleIndex, 1);

        // Act
        mergeSortVisualiser.moveToNextStep(data);

        // Assert
        Map<String, Integer> currentPointerMap = mergeSortVisualiser.getCurrentPointerMap();
        assertEquals(middleIndex + 1, currentPointerMap.get(MergeSortVisualiser.getLow()));
    }

    @Test
    public void moveToNextStepMergesWhenBothChildrenOfCurrentNodeAreSorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(9, 7, 5, 3));
        mergeSortVisualiser.setRunning(true);

        int initialTreeNode = 2;
        mergeSortVisualiser.setCurrentTreeNode(initialTreeNode);

        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(1);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(1);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        int parentNode = 1;
        setParentNodes(initialTreeNode, parentNode);
        setNumberOfSortedChildrenMap(parentNode, 1);

        setTreeNodePointerMaps(parentNode, 0, 1);

        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(7, 9, 5, 3));

        // Act
        mergeSortVisualiser.moveToNextStep(data);

        // Assert
        assertEquals(expected, data);
    }

    @Test
    public void moveToNextStepSetsSortedToTrueWhenChildrenOfRootNodeAreMerged() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(7, 9, 3, 5));
        mergeSortVisualiser.setRunning(true);

        int initialTreeNode = 8;
        mergeSortVisualiser.setCurrentTreeNode(initialTreeNode);

        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(2);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(3);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        when(sortedTreeNodes.containsKey(initialTreeNode)).thenReturn(true);
        mergeSortVisualiser.setSortedTreeNodes(sortedTreeNodes);

        int parentNode = 6;
        setParentNodes(initialTreeNode, parentNode);
        setNumberOfSortedChildrenMap(parentNode, 1);

        setTreeNodePointerMaps(parentNode, 1, data.size() - 1);

        // Act
        mergeSortVisualiser.moveToNextStep(data);

        // Assert
        assertTrue(mergeSortVisualiser.isSorted());
    }

    @Test
    public void paintPaintsOneBarForEachDataElement() {
        // Arrange
        int size = ThreadLocalRandom.current().nextInt(1, 1001);
        data = ArrayGenerator.generateRandomPositiveIntegerArray(size, 1, 200);

        // Act
        mergeSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(data.size())).fillRect(anyInt(), eq(0), anyInt(), anyInt());
    }

    @Test
    public void paintPaintsAllBarsMagentaWhenSortedIsTrue() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        mergeSortVisualiser.setSorted(true);

        // Act
        mergeSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(data.size())).setColor(Color.MAGENTA);
    }

    @Test
    public void paintPaintsBarsMagentaWhenTheyBelongToSubArrayThatWasJustSorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(3, 4, 2, 1));

        int currentTreeNode = 1;
        mergeSortVisualiser.setCurrentTreeNode(currentTreeNode);
        when(sortedTreeNodes.containsKey(currentTreeNode)).thenReturn(true);
        mergeSortVisualiser.setSortedTreeNodes(sortedTreeNodes);
        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(1);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        // Act
        mergeSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(2)).setColor(Color.MAGENTA);
    }

    @Test
    public void paintPaintsBarsBlackWhenTheyDoNotBelongToSubArrayThatWasJustSorted() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(3, 4, 2, 1));

        int currentTreeNode = 1;
        mergeSortVisualiser.setCurrentTreeNode(currentTreeNode);
        when(sortedTreeNodes.containsKey(currentTreeNode)).thenReturn(true);
        mergeSortVisualiser.setSortedTreeNodes(sortedTreeNodes);
        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(1);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        // Act
        mergeSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(2)).setColor(Color.BLACK);
    }

    @Test
    public void paintPaintsBarGreenIfElementCorrespondsToMiddleAndOuterPointers() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 3, 2, 1));

        when(currentPointerMap.containsValue(0)).thenReturn(true);
        when(currentPointerMap.containsValue(1)).thenReturn(true);
        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSortVisualiser.getMiddle())).thenReturn(0);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(1);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        // Act
        mergeSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(1)).setColor(Color.GREEN);
    }

    @Test
    public void paintPaintsBarYellowIfElementOnlyCorrespondsToMiddlePointer() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 3, 2, 1));

        when(currentPointerMap.containsValue(0)).thenReturn(true);
        when(currentPointerMap.containsValue(1)).thenReturn(true);
        when(currentPointerMap.containsValue(2)).thenReturn(false);
        when(currentPointerMap.containsValue(3)).thenReturn(true);
        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSortVisualiser.getMiddle())).thenReturn(1);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(3);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        // Act
        mergeSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(1)).setColor(Color.YELLOW);
    }

    @Test
    public void paintPaintsBarCyanIfElementOnlyCorrespondsToOuterPointer() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(4, 3, 2, 1));

        when(currentPointerMap.containsValue(0)).thenReturn(true);
        when(currentPointerMap.containsValue(1)).thenReturn(true);
        when(currentPointerMap.containsValue(2)).thenReturn(false);
        when(currentPointerMap.containsValue(3)).thenReturn(true);
        when(currentPointerMap.get(MergeSortVisualiser.getLow())).thenReturn(0);
        when(currentPointerMap.get(MergeSortVisualiser.getMiddle())).thenReturn(1);
        when(currentPointerMap.get(MergeSortVisualiser.getHigh())).thenReturn(3);
        mergeSortVisualiser.setCurrentPointerMap(currentPointerMap);

        // Act
        mergeSortVisualiser.paint(g, 1, 1, 1, 1, data);

        // Assert
        verify(g, times(2)).setColor(Color.CYAN);
    }

    // todo: consider extracting these common methods to a separate test class
    @Test
    public void fillRectangleThrowsExceptionWhenGraphicsObjectIsNull() {
        // Arrange
        String expected = "The Graphics object should not be null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                mergeSortVisualiser.fillRectangle(null, 1, 1, 1, 1, 1,
                        data));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void fillRectangleThrowsExceptionWhenDataIsNull() {
        // Arrange
        String expected = "The data should contain at least one element, but it is null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                mergeSortVisualiser.fillRectangle(g, 1, 1, 1, 1, 1,
                        null));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void fillRectangleCallsFillRectWithCorrectParameters() {
        // Arrange
        data = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        int xCoordinate = 1;
        int width = 1;

        // Act
        mergeSortVisualiser.fillRectangle(g, 1, 1, 1, xCoordinate, width, data);

        // Assert
        verify(g, times(1)).fillRect(eq(xCoordinate), eq(0), eq(width), anyInt());
    }

    private void setParentNodes(int childNode, int parentNode) {
        when(parentNodes.get(childNode)).thenReturn(parentNode);
        mergeSortVisualiser.setParentNodes(parentNodes);
    }

    private void setNumberOfSortedChildrenMap(int node, int numberOfSortedChildren) {
        when(numberOfSortedChildrenMap.get(node)).thenReturn(numberOfSortedChildren);
        mergeSortVisualiser.setNumberOfSortedChildrenMap(numberOfSortedChildrenMap);
    }

    private void setTreeNodePointerMaps(int node, int middle, int high) {
        Map<String, Integer> pointerMap = new HashMap<>();
        pointerMap.put(MergeSortVisualiser.getLow(), 0);
        pointerMap.put(MergeSortVisualiser.getMiddle(), middle);
        pointerMap.put(MergeSortVisualiser.getHigh(), high);
        when(treeNodePointerMaps.get(node)).thenReturn(pointerMap);
        mergeSortVisualiser.setTreeNodePointerMaps(treeNodePointerMaps);
    }
}