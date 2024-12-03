package userInterfaceTest;

import userInterface.DialPanel;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DialPanelTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dial Panel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        DialPanel dialPanel = new DialPanel("XP Adherence");
        frame.add(dialPanel, BorderLayout.CENTER);

        JSlider slider = new JSlider(0, 100, 0);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider.getValue();
                double normalizedScore = value / 100.0;
                dialPanel.setScore(normalizedScore);
            }
        });


        frame.add(slider, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
