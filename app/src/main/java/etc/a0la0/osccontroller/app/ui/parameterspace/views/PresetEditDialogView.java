package etc.a0la0.osccontroller.app.ui.parameterspace.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;

public class PresetEditDialogView extends FrameLayout {


    @BindView(R.id.standardDeviationTextField) EditText standardDeviationTextField;
    @BindView(R.id.amplitudeTextField) EditText amplitudeTextField;
    @BindView(R.id.colorBar) View colorBar;
    @BindView(R.id.seekBarRedTextView) TextView redTextDisplay;
    @BindView(R.id.seekBarRed) SeekBar seekBarRed;
    @BindView(R.id.seekBarGreenTextView) TextView greenTextDisplay;
    @BindView(R.id.seekBarGreen) SeekBar seekBarGreen;
    @BindView(R.id.seekBarBlueTextView) TextView blueTextDisplay;
    @BindView(R.id.seekBarBlue) SeekBar seekBarBlue;

    public PresetEditDialogView(Context context) {
        super(context);
        init();
    }
    public PresetEditDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PresetEditDialogView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PresetEditDialogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.space_preset_dialog, this);
        ButterKnife.bind(this);

        seekBarRed.setOnSeekBarChangeListener(seekBarListener);
        seekBarGreen.setOnSeekBarChangeListener(seekBarListener);
        seekBarBlue.setOnSeekBarChangeListener(seekBarListener);
    }

    public void setPresetViewModel(SpacePreset spacePreset) {
        standardDeviationTextField.setText(spacePreset.getStandardDeviation() + "");
        amplitudeTextField.setText(spacePreset.getAmplitude() + "");

        redTextDisplay.setText(spacePreset.getR() + "");
        seekBarRed.setProgress(spacePreset.getR());

        greenTextDisplay.setText(spacePreset.getG() + "");
        seekBarGreen.setProgress(spacePreset.getG());

        blueTextDisplay.setText(spacePreset.getB() + "");
        seekBarBlue.setProgress(spacePreset.getB());

        colorBar.setBackgroundColor(Color.rgb(
                spacePreset.getR(),
                spacePreset.getG(),
                spacePreset.getB()
        ));
    }

    public float getStandardDeviation() {
        return Float.parseFloat(standardDeviationTextField.getText().toString());
    }

    public float getAmplitude() {
        return Float.parseFloat(amplitudeTextField.getText().toString());
    }

    public int getRed() {
        return seekBarRed.getProgress();
    }

    public int getGreen() {
        return seekBarGreen.getProgress();
    }

    public int getBlue() {
        return seekBarBlue.getProgress();
    }

    private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            int red = seekBarRed.getProgress();
            int green = seekBarGreen.getProgress();
            int blue = seekBarBlue.getProgress();
            colorBar.setBackgroundColor(Color.rgb(red, green, blue));
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

}
