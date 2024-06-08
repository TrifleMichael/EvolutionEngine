import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlotTool {
    public static void createAndSave2DPlot(float[] xValues, float[] yValues, String title, String xAxisLabel, String yAxisLabel, String filePath) {
        // Check if xValues and yValues are of the same length
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("xValues and yValues must have the same length");
        }

        // Create a dataset
        XYSeries series = new XYSeries("Series 1");
        for (int i = 0; i < xValues.length; i++) {
            series.add(xValues[i], yValues[i]);
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // Create the chart
        JFreeChart chart = ChartFactory.createScatterPlot(
                title, // Chart title
                xAxisLabel, // X-axis Label
                yAxisLabel, // Y-axis Label
                dataset, // Dataset for the Chart
                PlotOrientation.VERTICAL,
                true, // Show legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        // Save the chart as an image file
        try {
            ChartUtilities.saveChartAsPNG(new File(filePath), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float[] toFloatArray(ArrayList<Float> floatList) {
        // Create a float array of the same size as the ArrayList
        float[] floatArray = new float[floatList.size()];

        // Iterate over the ArrayList and assign each value to the float array
        for (int i = 0; i < floatList.size(); i++) {
            floatArray[i] = floatList.get(i);
        }

        return floatArray;
    }
}
