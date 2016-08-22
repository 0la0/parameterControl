package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import etc.a0la0.osccontroller.R;

public class PagerTrainView extends FrameLayout {

    @BindView(R.id.presetContainer) LinearLayout presetContainer;

    private int numberOfPresets;
    private List<PresetElement> presetElementList;
    private List<TrainingInstance> trainingSet;
    private int[] presetTrainingInstanceCount;

    private int selectedIndex = 0;
    private boolean isCollectingTrainingInstances = false;
    private final int TRAINING_INSTACE_THRESHOLD = 6;
    private RotationSpaceActivity.MessageDelegate messageDelegate;

    public PagerTrainView(Context context) {
        super(context);
    }
    public PagerTrainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PagerTrainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public static PagerTrainView inflate(ViewGroup group, boolean attachToRoot){
        return (PagerTrainView) LayoutInflater.from(group.getContext()).inflate(R.layout.rotation_train, group, attachToRoot);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (isInEditMode()) {
            return;
        }
        ButterKnife.bind(this);
    }

    public void setMessageDelegate(RotationSpaceActivity.MessageDelegate messageDelegate) {
        this.messageDelegate = messageDelegate;
    }

    public void setNumberOfPresets(int numberOfPresets) {
        this.numberOfPresets = numberOfPresets;
        selectedIndex = 0;

        presetContainer.removeAllViews();
        presetElementList = new ArrayList<>();
        presetTrainingInstanceCount = new int[numberOfPresets];
        trainingSet = new ArrayList<>();

        clearTrainingSet();

        PresetElement.ClickDelegate clickDelegate = new PresetElement.ClickDelegate() {
            @Override
            public void onClick(int position) {
                setSelectedIndex(position);
            }
        };

        for (int i = 0; i < numberOfPresets; i++) {
            PresetElement presetElement = new PresetElement(getContext(), i, clickDelegate);
            presetElementList.add(presetElement);
            presetContainer.addView(presetElement);
        }
        presetElementList.get(selectedIndex).setActive(true);
    }

    @OnClick(R.id.buttonClear)
    public void clearTrainingSet() {
        trainingSet = new ArrayList<>();
    }

    @OnCheckedChanged(R.id.buttonAddInstance)
    public void onAddInstanceClick(boolean isChecked) {
        isCollectingTrainingInstances = isChecked;
        if (!isChecked) {
            int nextIndex = Math.min(selectedIndex + 1, numberOfPresets -1);
            setSelectedIndex(nextIndex);
        }
    }

    private void validatePresets() {
        boolean allPresetsAreValid = true;
        for (int i = 0; i < presetElementList.size(); i++) {

            boolean isValid = presetTrainingInstanceCount[i] > TRAINING_INSTACE_THRESHOLD;
            presetElementList.get(i).setValid(isValid);
            if (!isValid) {
                allPresetsAreValid = false;
            }
        }
        if (allPresetsAreValid && messageDelegate != null) {
            messageDelegate.setTrainingSet(trainingSet);
        }
    }

    private void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        for (int i = 0; i < presetElementList.size(); i++) {
            PresetElement presetElement = presetElementList.get(i);
            presetElement.setActive(i == selectedIndex);
        }
        validatePresets();
    }

    public void onAccelerometerChange(float[] accelerometerData) {
        if (isCollectingTrainingInstances) {
            TrainingInstance trainingInstance = new TrainingInstance(
                    accelerometerData[0],
                    accelerometerData[1],
                    accelerometerData[2],
                    selectedIndex
            );
            trainingSet.add(trainingInstance);
            presetTrainingInstanceCount[selectedIndex]++;
            Log.i("Added instace", selectedIndex + "::: " + trainingInstance.toString());
        }
    }

}
