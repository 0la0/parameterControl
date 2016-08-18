package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Preset;

public class PagerInterpolateView extends FrameLayout {

    @BindView(R.id.tempContainer)
    LinearLayout tempContainer;
    private List<TextView> textViewList;

    private List<Preset> presetList;
    private List<TrainingInstance> trainingData;
    private List<TrainingInstance> centroids;

    public PagerInterpolateView(Context context) {
        super(context);
    }

    public PagerInterpolateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerInterpolateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static PagerInterpolateView inflate(ViewGroup group, boolean attachToRoot){
        return (PagerInterpolateView) LayoutInflater.from(group.getContext()).inflate(R.layout.rotation_interpolate, group, attachToRoot);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            return;
        }
        init();
    }

    private void init(){
        ButterKnife.bind(this);
        Log.i("Interpolate", "init");
        //presenter.attachView(this);
    }

    public void onDestroy() {
        Log.i("Interpolate", "onDestroy");
        //presenter.detachView();
    }

    public void onSelect() {
        Log.i("Interpolate", "onSelect");
    }

    public void onUnselect() {
        Log.i("Interpolate", "onUnselect");
    }

    public void setPresetList(List<Preset> presetList) {
        this.presetList = presetList;
    }

    public void onAccelerometerChange(float[] accelerometerData) {
        if (centroids == null) {
            Log.e("InterpolateView", "Centroids have not been calculated.");
            return;
        }

        TrainingInstance instance = new TrainingInstance(
                accelerometerData[0],
                accelerometerData[1],
                accelerometerData[2],
                0
        );

        //calculate distance between instance and each centroid
        List<Double> inverseDistances = Stream.of(centroids)
                .map(centroid -> calculateInverseDistance(centroid, instance, 2))
                .collect(Collectors.toList());

        double distanceSum = Stream.of(inverseDistances)
                .reduce(0.0, (sum, inverseDistance) -> sum += inverseDistance);

        // normalize distances ... this becomes the weights
        List<Double> normalizedWeights = Stream.of(inverseDistances)
                .map(inverseDistance -> inverseDistance / distanceSum)
                .collect(Collectors.toList());

        for (int i = 0; i < normalizedWeights.size(); i++) {
            TextView textView = textViewList.get(i);
            double weight = normalizedWeights.get(i);
            textView.setText(weight + "");
        }

    }

    public void setTrainingSet(List<TrainingInstance> trainingData) {
        this.trainingData = trainingData;

        Map<Integer, List<TrainingInstance>> groupedInstances = Stream.of(trainingData)
                .collect(Collectors.groupingBy(trainingInstance -> trainingInstance.classification));

        centroids = new ArrayList<>();

        //for each classification, find centroid
        Set<Integer> classSet = groupedInstances.keySet();

        for (int i = 0; i < classSet.size(); i++) {
            List<TrainingInstance> classPopulation = groupedInstances.get(i);
            float numberOfInstances = (float) classPopulation.size();

            TrainingInstance centroid = Stream.of(classPopulation)
                    .reduce(new TrainingInstance(0, 0, 0, 0), (sum, instance) -> sum.add(instance))
                    .divide(numberOfInstances);

            centroids.add(centroid);
        }

        createTempView();
    }

    private void createTempView() {
        tempContainer.removeAllViews();
        textViewList = new ArrayList<>();

        for (TrainingInstance centroid : centroids) {
            TextView textView = new TextView(getContext());
            textView.setText("");
            textViewList.add(textView);
            tempContainer.addView(textView);
        }
    }

    private double calculateInverseDistance(TrainingInstance u, TrainingInstance v, int power) {
        double distance =  Math.sqrt(
                Math.pow(u.alpha - v.alpha, 2) +
                Math.pow(u.beta - v.beta, 2) +
                Math.pow(u.gamma - v.gamma, 2)
        );
        return 1 / Math.pow(distance, power);
    }

}
