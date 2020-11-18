package com.calebkoy.sortingalgovisualiser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class MainPanelTest {
    private final ActionEvent e = new ActionEvent(new Object(), 1, "Sort");

    private MainPanel mainPanel;

    @Mock private OptionPane optionPane;
    @Mock private Graphics g;

    @Test
    public void actionPerformedThrowsExceptionWhenActionEventIsNull() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        String expected = "ActionEvent should not be null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mainPanel.actionPerformed(null));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void actionPerformedStopsTheSortingWhenKnownSortHasJustFinishedRunning() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        mainPanel.setSortingAlgorithmRunning(BubbleSort.getName());
        mainPanel.setSorting(true);
        SortVisualiser sortVisualiser = new BubbleSortVisualiser();
        sortVisualiser.setSorted(true);
        mainPanel.setSortVisualiser(sortVisualiser);

        // Act
        mainPanel.actionPerformed(e);

        // Assert
        assertFalse(mainPanel.isSorting());
    }

    @Test
    public void actionPerformedRunsSelectedSortingAlgorithmWhenItIsRecognised() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        mainPanel.setSortingAlgorithmSelected(BubbleSort.getName());

        // Act
        mainPanel.actionPerformed(e);

        // Assert
        assertTrue(mainPanel.isSorting());
        assertEquals(BubbleSort.getName(), mainPanel.getSortingAlgorithmRunning());
    }

    @Test
    public void actionPerformedPromptsUserToSelectSortingAlgorithmWhenSortingAlgorithmSelectedIsNotValid() {
        // Arrange
        Mockito.doNothing().when(optionPane).showMessageDialog(ArgumentMatchers.any(Component.class), ArgumentMatchers.any());
        mainPanel = new MainPanel(optionPane, 10, 1, 100);

        // Act
        mainPanel.actionPerformed(e);

        // Assert
        Mockito.verify(optionPane, Mockito.times(1)).showMessageDialog(ArgumentMatchers.any(Component.class), ArgumentMatchers.any());
    }

    @Test
    public void paintComponentThrowsExceptionWhenGraphicsObjectIsNull() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        String expected = "Graphics object should not be null";

        // Act
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> mainPanel.paintComponent(null));

        // Assert
        Assertions.assertEquals(expected, exception.getMessage());
    }

    @Test
    public void paintComponentPaintsComponentForRunningSortingAlgorithmWhenSortingIsTrue() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        mainPanel.setSorting(true);
        mainPanel.setSortingAlgorithmRunning(BubbleSort.getName());

        Mockito.when(g.create()).thenReturn(g);

        SortVisualiser sortVisualiser = new BubbleSortVisualiser();
        SortVisualiser spy = Mockito.spy(sortVisualiser);
        mainPanel.setSortVisualiser(spy);

        // Act
        mainPanel.paintComponent(g);

        // Assert
        Mockito.verify(spy, Mockito.times(1)).paint(ArgumentMatchers.eq(g), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt(), ArgumentMatchers.any());
    }

    @Test
    public void paintComponentPaintsAllBarsMagentaWhenNotSortingAndSortingAlgorithmJustRan() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        mainPanel.setSortingAlgorithmJustRan(true);
        Mockito.when(g.create()).thenReturn(g);

        // Act
        mainPanel.paintComponent(g);

        // Assert
        Mockito.verify(g, Mockito.times(1)).setColor(Color.MAGENTA);

        int expected = mainPanel.getData().size() + 1; // The 'plus 1' accounts for the super.paintComponent() call
        Mockito.verify(g, Mockito.times(expected)).fillRect(ArgumentMatchers.anyInt(), ArgumentMatchers.eq(0), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
    }

    @Test
    public void paintComponentPaintsAllBarsBlackWhenNotSortingAndSortingAlgorithmDidNotJustRun() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        Mockito.when(g.create()).thenReturn(g);

        // Act
        mainPanel.paintComponent(g);

        // Assert
        Mockito.verify(g, Mockito.times(1)).setColor(Color.BLACK);

        int expected = mainPanel.getData().size() + 1; // The 'plus 1' accounts for the super.paintComponent() call
        Mockito.verify(g, Mockito.times(expected)).fillRect(ArgumentMatchers.anyInt(), ArgumentMatchers.eq(0), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
    }
}