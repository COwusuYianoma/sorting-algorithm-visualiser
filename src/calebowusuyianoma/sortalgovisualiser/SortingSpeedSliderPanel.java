package calebowusuyianoma.sortalgovisualiser;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;

public class SortingSpeedSliderPanel extends JPanel implements ChangeListener {
    private final JSlider slider;
    private SliderPanelListener sliderPanelListener;

    public SortingSpeedSliderPanel(JSlider slider) {
        JLabel sliderLabel = new JLabel("Sorting speed");
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.slider = slider;
        setUpSlider();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        //add(new JLabel("Sorting speed"));
        add(sliderLabel);
        add(slider);
    }

    private void setUpSlider() {
        slider.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setVisible(true);
        slider.addChangeListener(this);
    }

    // TODO: test this method
    @Override
    public void stateChanged(ChangeEvent e) {
        if (sliderPanelListener != null) {
            sliderPanelListener.sliderPanelEventOccurred(e);
        }
    }

    public void setSliderPanelListener(SliderPanelListener sliderPanelListener) {
        this.sliderPanelListener = sliderPanelListener;
    }
}
