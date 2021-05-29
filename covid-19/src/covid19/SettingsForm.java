package covid19;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.time.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SettingsForm implements ValuesListener {
    private static TimeSeries healthyPeople;  // hastli olmayan kisi saysi
    private static TimeSeries infectedPeople;  // infect olun saysi
    private static TimeSeries recoveredPeople;  // iyilesmis kis saysi
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;
    private JSlider slider4;
    private JFreeChart chart;
    public JPanel contentspanel;
    private JLabel infected_count_label;
    private JLabel recovery_count_label;
    private JLabel healty_count_label;
    private SimulationWindow window;
    private int peopleCount = 10;
    private int peopleSpeed = 70;
    private int infectionProbabilty = 0;
    private int recoveryTime = 10;
    private long startTime = System.currentTimeMillis();

    public SettingsForm(SimulationWindow window) {
        this.window = window;
        initChart();

        slider1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                if (!slider.getValueIsAdjusting()) {
                    peopleCount = slider.getValue();
                    updateCity();
                }
            }
        });
        slider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                if (!slider.getValueIsAdjusting()) {
                    peopleSpeed = slider.getValue();
                    updateCity();
                }
            }
        });
        slider3.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                if (!slider.getValueIsAdjusting()) {
                    infectionProbabilty = slider.getValue();
                    updateCity();
                }
            }
        });
        slider4.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                JSlider slider = (JSlider) evt.getSource();
                if (!slider.getValueIsAdjusting()) {
                    recoveryTime = slider.getValue();
                    updateCity();
                }
            }
        });
    }

    private void initChart() {
        chart = ChartFactory.createTimeSeriesChart("healthy people chart", "Time (s)", "People Count", createDataset(), true, true, true);

        XYItemRenderer renderer = chart.getXYPlot().getRenderer();
        renderer.setSeriesPaint(0, Color.GREEN);
        renderer.setSeriesPaint(2, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);

//        chart.getXYPlot().getRangeAxis(0).setAutoRange(false);
//        chart.getXYPlot().getRangeAxis(1).setAutoRange(false);
//        chart.getXYPlot().getRangeAxis(2).setAutoRange(false);
        ChartPanel s = new ChartPanel(chart);
        contentspanel.add(s, new GridConstraints(7, 0, 1, 2,
                GridConstraints.ANCHOR_CENTER,
                GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK,
                GridConstraints.SIZEPOLICY_CAN_SHRINK, null, null, null, 0, false));
    }

    private static TimeSeriesCollection createDataset() {
        healthyPeople = new TimeSeries("Healthy People");
        infectedPeople = new TimeSeries("Infected People");
        recoveredPeople = new TimeSeries("Recovered People");
        TimeSeriesCollection dataset = new TimeSeriesCollection();

        dataset.addSeries(healthyPeople);
        dataset.addSeries(infectedPeople);
        dataset.addSeries(recoveredPeople);

//        dataset.addSeries(s2);

        return dataset;

    }


    public void updateCity() {
        healthyPeople.clear();
        recoveredPeople.clear();
        infectedPeople.clear();
        startTime = System.currentTimeMillis();
        chart.getXYPlot().getDomainAxis().setAutoRange(false);
        chart.getXYPlot().getRangeAxis().setAutoRange(false);
        chart.getXYPlot().getDomainAxis().setRange(new Range(0, 60 * 1000));
        chart.getXYPlot().getRangeAxis().setRange(new Range(0, peopleCount));
        window.newCity(peopleCount, peopleSpeed, infectionProbabilty, recoveryTime, this);
    }

    @Override
    public void onValuesChanged(int healthy, int recovered, int infected) {
        healty_count_label.setText(String.valueOf(healthy));
        recovery_count_label.setText(String.valueOf(recovered));
        infected_count_label.setText(String.valueOf(infected));
        updateChart(healthy, recovered, infected);
    }

    private void updateChart(int healthy, int recovered, int infected) {
        healthyPeople.addOrUpdate(new FixedMillisecond(getCurrentTime()), healthy);
        recoveredPeople.addOrUpdate(new FixedMillisecond(getCurrentTime()), recovered);
        infectedPeople.addOrUpdate(new FixedMillisecond(getCurrentTime()), infected);
    }

    private long getCurrentTime() {
        return System.currentTimeMillis() - startTime;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentspanel = new JPanel();
        contentspanel.setLayout(new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        slider1 = new JSlider();
        slider1.setInverted(false);
        slider1.setMajorTickSpacing(100);
        slider1.setMaximum(1000);
        slider1.setMinimum(10);
        slider1.setMinorTickSpacing(50);
        slider1.setPaintLabels(true);
        slider1.setPaintTicks(true);
        slider1.setSnapToTicks(true);
        slider1.setValue(10);
        contentspanel.add(slider1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentspanel.add(spacer1, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("People Count");
        contentspanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(75, 16), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("People Speed(%)");
        contentspanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(89, 52), null, 0, false));
        slider2 = new JSlider();
        slider2.setMajorTickSpacing(10);
        slider2.setMaximum(300);
        slider2.setMinimum(70);
        slider2.setMinorTickSpacing(5);
        slider2.setPaintLabels(true);
        slider2.setPaintTicks(true);
        slider2.setPaintTrack(true);
        slider2.setSnapToTicks(true);
        slider2.setValue(70);
        contentspanel.add(slider2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, 52), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Infection Probabilty(%)");
        contentspanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slider3 = new JSlider();
        slider3.setMajorTickSpacing(10);
        slider3.setMinorTickSpacing(5);
        slider3.setPaintLabels(true);
        slider3.setPaintTicks(true);
        slider3.setValue(0);
        contentspanel.add(slider3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Recovery time(s)");
        contentspanel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slider4 = new JSlider();
        slider4.setMajorTickSpacing(10);
        slider4.setMinimum(10);
        slider4.setMinorTickSpacing(5);
        slider4.setPaintLabels(true);
        slider4.setPaintTicks(true);
        slider4.setValue(10);
        contentspanel.add(slider4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setForeground(new Color(-65531));
        label5.setText("Infected");
        contentspanel.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        infected_count_label = new JLabel();
        infected_count_label.setForeground(new Color(-65531));
        infected_count_label.setText("0");
        contentspanel.add(infected_count_label, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setForeground(new Color(-16318209));
        label6.setText("Recovered");
        contentspanel.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setForeground(new Color(-15746304));
        label7.setText("Healthy");
        contentspanel.add(label7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        recovery_count_label = new JLabel();
        recovery_count_label.setForeground(new Color(-16318209));
        recovery_count_label.setText("0");
        contentspanel.add(recovery_count_label, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        healty_count_label = new JLabel();
        healty_count_label.setForeground(new Color(-15746304));
        healty_count_label.setText("0");
        contentspanel.add(healty_count_label, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentspanel;
    }

}
