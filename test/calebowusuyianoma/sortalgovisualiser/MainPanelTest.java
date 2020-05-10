package calebowusuyianoma.sortalgovisualiser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Exception exception = assertThrows(IllegalArgumentException.class, () -> mainPanel.actionPerformed(null));

        // Assert
        assertEquals(expected, exception.getMessage());
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
        doNothing().when(optionPane).showMessageDialog(any(Component.class), any());
        mainPanel = new MainPanel(optionPane, 10, 1, 100);

        // Act
        mainPanel.actionPerformed(e);

        // Assert
        verify(optionPane, times(1)).showMessageDialog(any(Component.class), any());
    }

    @Test
    public void paintComponentThrowsExceptionWhenGraphicsObjectIsNull() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        String expected = "Graphics object should not be null";

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> mainPanel.paintComponent(null));

        // Assert
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void paintComponentPaintsComponentForRunningSortingAlgorithmWhenSortingIsTrue() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        mainPanel.setSorting(true);
        mainPanel.setSortingAlgorithmRunning(BubbleSort.getName());

        when(g.create()).thenReturn(g);

        SortVisualiser sortVisualiser = new BubbleSortVisualiser();
        SortVisualiser spy = spy(sortVisualiser);
        mainPanel.setSortVisualiser(spy);

        // Act
        mainPanel.paintComponent(g);

        // Assert
        verify(spy, times(1)).paint(eq(g), anyInt(), anyInt(), anyInt(), anyInt(), any());
    }

    @Test
    public void paintComponentPaintsAllBarsMagentaWhenNotSortingAndSortingAlgorithmJustRan() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        mainPanel.setSortingAlgorithmJustRan(true);
        when(g.create()).thenReturn(g);

        // Act
        mainPanel.paintComponent(g);

        // Assert
        verify(g, times(1)).setColor(Color.MAGENTA);

        int expected = mainPanel.getData().size() + 1; // The 'plus 1' accounts for the super.paintComponent() call
        verify(g, times(expected)).fillRect(anyInt(), eq(0), anyInt(), anyInt());
    }

    @Test
    public void paintComponentPaintsAllBarsBlackWhenNotSortingAndSortingAlgorithmDidNotJustRun() {
        // Arrange
        mainPanel = new MainPanel(optionPane, 10, 1, 100);
        when(g.create()).thenReturn(g);

        // Act
        mainPanel.paintComponent(g);

        // Assert
        verify(g, times(1)).setColor(Color.BLACK);

        int expected = mainPanel.getData().size() + 1; // The 'plus 1' accounts for the super.paintComponent() call
        verify(g, times(expected)).fillRect(anyInt(), eq(0), anyInt(), anyInt());
    }
}