package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import etc.a0la0.osccontroller.R;

public class PagerClassifyView extends FrameLayout {

    @BindView(R.id.classificationOutput) TextView classificationOutput;

    private List<TrainingInstance> trainingData;
    private KnnClassifier knnClassifier;
    private final int KNN = 5;
    private int currentClassification = 0;
    private RotationSpaceActivity.MessageDelegate messageDelegate;

    public PagerClassifyView(Context context) {
        super(context);
    }
    public PagerClassifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PagerClassifyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public static PagerClassifyView inflate(ViewGroup group, boolean attachToRoot){
        return (PagerClassifyView) LayoutInflater.from(group.getContext()).inflate(R.layout.rotation_classify, group, attachToRoot);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            return;
        }
        ButterKnife.bind(this);
    }

    public void onAccelerometerChange(float[] accelerometerData) {
        if (trainingData != null) {
            int classification = knnClassifier.classifyInstance(new TrainingInstance(
                    accelerometerData[0],
                    accelerometerData[1],
                    accelerometerData[2],
                    0
            ));
            if (classification != currentClassification) {
                currentClassification = classification;
                if (messageDelegate != null) {
                    messageDelegate.onUpdateWeightList(currentClassification);
                }
                classificationOutput.setText("Classification: " + classification);
            }
        }
    }

    public void setTrainingSet(List<TrainingInstance> trainingData) {
        this.trainingData = trainingData;
        knnClassifier = new KnnClassifier(KNN, trainingData);
        knnClassifier.createModel();
    }

    public void setMessageDelegate(RotationSpaceActivity.MessageDelegate messageDelegate) {
        this.messageDelegate = messageDelegate;
    }

}
