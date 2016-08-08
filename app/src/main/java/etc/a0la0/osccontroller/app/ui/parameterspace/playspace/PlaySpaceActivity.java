package etc.a0la0.osccontroller.app.ui.parameterspace.playspace;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnTouch;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;
import etc.a0la0.osccontroller.app.ui.parameterspace.views.EditSpaceView;

public class PlaySpaceActivity extends BaseActivity implements PlaySpacePresenter.View {

    @BindView(R.id.editSpaceView) EditSpaceView editSpaceView;

    private PlaySpacePresenter presenter = new PlaySpacePresenter();
    private List<Preset> presetValueList;
    private List<SpacePreset> spacePresetList;
    private List<Parameter> parameterList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_play_activity);
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

        presetValueList = presenter.getPresetList();
        spacePresetList = presenter.getSpacePresetList();
        parameterList = presenter.getParameterList();

        editSpaceView.init(position, spacePresetList);
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

    @OnTouch(R.id.editSpaceView)
    public boolean onTouch(View view, MotionEvent event) {
        int eventAction = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        if (eventAction != MotionEvent.ACTION_DOWN && eventAction != MotionEvent.ACTION_MOVE) {
            return true;
        }

        OSCPacket oscPacket = getMessageList(x, y);
        presenter.sendOscPacket(oscPacket);
        return true;
    }

    private OSCPacket getMessageList(int x, int y) {
        List<Float> weightList = Stream.of(spacePresetList)
                .map(spacePreset -> Math.min(1f, spacePreset.getValue(x, y))) //maybe cap at 1 another place?
                .collect(Collectors.toList());

        float weightSum = Stream.of(weightList)
                .reduce(0.0f, (sum, weight) -> sum + weight);

        float baseWeight = 1 - (Math.min(1, weightSum));
        weightList.set(0, baseWeight);
        float totalWeightSum = weightSum + baseWeight;

        List<OSCPacket> messageList = Stream.of(parameterList)
                .map(parameter -> {
                    String parameterKey = parameter.getUniqueId();
                    String address = parameter.getAddress();
                    float presetValue = 0;
                    for (int i = 0; i < weightList.size(); i++) {
                        Preset preset = presetValueList.get(i);
                        float weight = weightList.get(i);
                        presetValue += preset.get(parameterKey) * weight;
                    }
                    presetValue /= totalWeightSum;
                    return new OSCMessage(address, Collections.singletonList(presetValue));
                })
                .collect(Collectors.toList());

        return new OSCBundle(messageList);
    }

}
