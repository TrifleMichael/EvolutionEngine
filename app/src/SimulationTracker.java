import java.util.ArrayList;
import java.util.HashMap;

public class SimulationTracker {


    public class DataWrapper {
        public String dataTag;
        public ArrayList<Float> xValues;
        public ArrayList<Float> yValues;
        public DataWrapper(String dataTag) {
            this.dataTag = dataTag;
            this.xValues = new ArrayList<>();
            this.yValues = new ArrayList<>();
        }
    }

    HashMap<String, DataWrapper> data = new HashMap<>();

    public void addData(float xValue, float yValue, String dataTag) {
        if (!data.containsKey(dataTag)) {
            data.put(dataTag, new DataWrapper(dataTag));
        }
        data.get(dataTag).xValues.add(xValue);
        data.get(dataTag).yValues.add(yValue);
    }

    public void plot(String dataTag, String title, String xLabel, String yLabel, String path) {
        DataWrapper dataWrapper = data.get(dataTag);
        float[] xValues = PlotTool.toFloatArray(dataWrapper.xValues);
        float[] yValues = PlotTool.toFloatArray(dataWrapper.yValues);
        PlotTool.createAndSave2DPlot(xValues, yValues, title, xLabel, yLabel, path);
    }
}
