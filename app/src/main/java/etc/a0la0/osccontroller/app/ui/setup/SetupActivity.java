package etc.a0la0.osccontroller.app.ui.setup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.illposed.osc.OSCMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class SetupActivity extends BaseActivity implements SetupPresenter.View{

    @BindView(R.id.seekBarContainer) LinearLayout sliderContainer;
    @BindView(R.id.presetContainer) LinearLayout presetContainer;

    private SetupPresenter presenter = new SetupPresenter();
    private List<Parameter> parameterList;
    private List<ParameterSlider> parameterSliderList = new ArrayList<>();
    private List<PresetIconLayout> presetIconList;
    private final float MAX_VALUE = 100.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setHomeAsUpEnabled(false);
        presenter.attachView(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int position = intent.getIntExtra(getString(R.string.option_id), 0);
        presenter.init(this, position);

        parameterList = presenter.getParameterList();
        for (Parameter parameter : parameterList) {
            ParameterSlider parameterSlider = createSlider(parameter);
            parameterSliderList.add(parameterSlider);
            sliderContainer.addView(parameterSlider);
        }

        updatePresetView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startOscThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopOscThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @OnClick(R.id.addPreset)
    public void onAddPreset() {
        Preset preset = takeSnapshot();
        presenter.addPreset(preset);
        updatePresetView();
    }

    private ParameterSlider createSlider(Parameter parameter) {
        ParameterSlider slider = new ParameterSlider(this, parameter);

        Subscription subscription = slider.getObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(progress -> {
                    List<Object> oscData = Collections.singletonList(progress / MAX_VALUE);
                    presenter.sendOscMessage(new OSCMessage(parameter.getAddress(), oscData));
                });

        presenter.addSubscription(subscription);
        return slider;
    }

    private PresetIconLayout createPresetIcon(Preset preset, int presetIndex) {
        PresetIconLayout presetIcon = new PresetIconLayout(this, new PresetIconLayout.ClickDelegate() {
            @Override
            public void onClick() {
                setParameters(preset);

                List<OSCMessage> sliderValues = Stream.of(parameterSliderList)
                        .map(parameterSlider -> {
                            List<Object> oscData = Collections.singletonList(parameterSlider.getSliderValue() / MAX_VALUE);
                            return new OSCMessage(parameterSlider.getAddress(), oscData);
                        })
                        .collect(Collectors.toList());
                presenter.sendOscMessage(sliderValues);

                unSelectInactivePresets(presetIndex);
            }

            public void onLongClick() {
                unSelectInactivePresets(presetIndex);
            }

            @Override
            public void onUpdate() {
                presenter.setPreset(presetIndex, takeSnapshot());
                updatePresetView();
            }

            @Override
            public void onRemove() {
                presenter.removePreset(preset);
                updatePresetView();
            }
        });
        presetIcon.setLabel(presetIndex + "");
        return presetIcon;
    }

    private void updatePresetView() {
        presetContainer.removeAllViews();
        List<Preset> presetList = presenter.getPresetList();
        presetIconList = new ArrayList<>();
        for (int i = 0; i < presetList.size(); i++) {
            PresetIconLayout presetIcon = createPresetIcon(presetList.get(i), i);
            presetContainer.addView(presetIcon);
            presetIconList.add(presetIcon);
        }
    }

    public Preset takeSnapshot() {
        Map<String, Float> sliderValues = new HashMap<>();
        for (ParameterSlider parameterSlider : parameterSliderList) {
            float value = parameterSlider.getSliderValue() / MAX_VALUE;
            sliderValues.put(parameterSlider.getUniqueId(), value);
        }
        return new Preset(sliderValues);
    }

    private void setParameters(Preset preset) {
        for (int i = 0; i < parameterList.size(); i++) {
            String parameterId = parameterList.get(i).getUniqueId();
            if (preset.has(parameterId)) {
                int parameterValue = (int) (preset.get(parameterId) * MAX_VALUE);
                parameterSliderList.get(i).setSliderValue(parameterValue);
            }
        }
    }

    private void unSelectInactivePresets(int activeindex) {
        for (int i = 0; i < presetIconList.size(); i++) {
            if (i != activeindex) {
                presetIconList.get(i).onUnselect();
            }
        }
    }

}
