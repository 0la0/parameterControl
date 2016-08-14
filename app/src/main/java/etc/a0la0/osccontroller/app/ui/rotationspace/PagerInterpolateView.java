package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import butterknife.ButterKnife;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Preset;

public class PagerInterpolateView extends FrameLayout {

    private List<Preset> presetList;
    private List<TrainingInstance> trainingData;

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
        //Log.i("Interpolate", accelerometerData[0] + "");
    }

    public void setTrainingSet(List<TrainingInstance> trainingData) {
        this.trainingData = trainingData;
    }

}
