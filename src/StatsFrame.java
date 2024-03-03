import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class StatsFrame extends JFrame {
    MainFrame parent;
    private int numOngoing, numInvited, numRejected;
    private int whichChart;
    private JButton btnNext;

    ArrayList<JPanel> charts;

    public StatsFrame(MainFrame mainFrame) {
        this.parent = mainFrame;
        init();
    }

    private void init() {
        whichChart = 0;
        charts = new ArrayList<>();
        getApplicationCounts();
        generateCharts();
        createFrame();

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateChart();
            }
        });

    }
    
    private void createFrame() {
        btnNext = new JButton("Next");

        setContentPane(charts.get(whichChart));
        add(btnNext);
        setTitle("Statistics");
        setSize(550, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }

    private void getApplicationCounts() {
        int[] nums = parent.model.getApplicationCounts();
        numOngoing = nums[0];
        numInvited = nums[1];
        numRejected = nums[2];
    }

    private void generateCharts() {
        charts.add(new PieChart("Overall applications: ", new int[] {numOngoing, numInvited, numRejected}));
        charts.add(new BarChart("Applied Companies", generateDatasetForBarChart(1)));
        charts.add(new BarChart("Applied Dates", generateDatasetForBarChart(0)));
    }

    private void nextChart() {
        whichChart = (whichChart + 1)%charts.size();
    }

    private void updateChart() {
        nextChart();
        setContentPane(charts.get(whichChart));
        add(btnNext);
        invalidate();
        validate();
    }

    private Map<String, Integer> generateDatasetForBarChart(int colIdx) {
        int n = parent.model.getRowCount();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add((String) parent.model.getValueAt(i, colIdx));
        }
        Set<String> set = new HashSet<String>(list);

        Map<String, Integer> resMap = new HashMap<>();
        for (String key : set) {
            resMap.put(key, Collections.frequency(list, key));
        }

        return resMap;
    }
}

class PieChart extends JPanel {
    public PieChart(String chartTitle, int[] counts) {
        JPanel pieChart = new ChartPanel(createChart(chartTitle, createDataset(counts)));
        add(pieChart);
        setSize(500, 500);
        setVisible(true);
    }

    private static PieDataset createDataset(int[] counts) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Ongoing", counts[0]);
        dataset.setValue("Invited", counts[1]);
        dataset.setValue("Rejected", counts[2]);
        return dataset;
    }

    private static JFreeChart createChart(String chartTitle, PieDataset dataset) {
        Color lightRed = new Color(255, 180, 180);
        Color lightGray = new Color(190, 190, 190);
        Color lightGreen = new Color(150, 255, 190);
        int sum = dataset.getValue(0).intValue()
                + dataset.getValue(1).intValue()
                + dataset.getValue(2).intValue();
        JFreeChart chart = ChartFactory.createPieChart(
            chartTitle + sum,
            dataset, 
            true, 
            true, 
            false);

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint(0, lightGray);
        plot.setSectionPaint(1, lightGreen);
        plot.setSectionPaint(2, lightRed);
        return chart;
    }
}


class BarChart extends JPanel {
   
    public BarChart(String chartTitle, Map<String, Integer> map) {
       JFreeChart barChart = ChartFactory.createBarChart(
          chartTitle,           
          "Company",            
          "Num",            
          createDataset(map),          
          PlotOrientation.VERTICAL,           
          true, true, false);
          
       ChartPanel chartPanel = new ChartPanel( barChart );        
       chartPanel.setPreferredSize(new java.awt.Dimension(500, 450)); 
       add(chartPanel);
       setSize(500, 500);
       setVisible(true);
   }
    

    private CategoryDataset createDataset(Map<String, Integer> map) {
        final DefaultCategoryDataset dataset = 
        new DefaultCategoryDataset();
        final String cat = "";
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            dataset.addValue(entry.getValue(), entry.getKey(), cat);
        } 
       return dataset; 
    }

 }