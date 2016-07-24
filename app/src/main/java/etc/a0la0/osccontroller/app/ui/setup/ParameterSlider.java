package etc.a0la0.osccontroller.app.ui.setup;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import rx.Observable;

public class ParameterSlider extends FrameLayout {

    @BindView(R.id.parameterAddressLabel) TextView label;
    @BindView(R.id.parameterSlider) AppCompatSeekBar slider;

    private Parameter parameter;

    public ParameterSlider(Context context, Parameter parameter) {
        super(context);
        this.parameter = parameter;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.parameter_slider, this);
        ButterKnife.bind(this);
        label.setText(parameter.getAddress());
    }

    public String getUniqueId() {
        return parameter.getUniqueId();
    }

    public Observable<Integer> getObservable() {
        return RxSeekBar.userChanges(slider);
    }

    public int getSliderValue() {
        return slider.getProgress();
    }

    public void setSliderValue(int progress) {
        slider.setProgress(progress);
    }

    public String getAddress() {
        return parameter.getAddress();
    }

}
