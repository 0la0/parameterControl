package etc.a0la0.osccontroller.app.ui.parameterspace.playspace;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.illposed.osc.OSCPacket;

import java.util.List;

import butterknife.BindView;
import butterknife.OnTouch;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;
import etc.a0la0.osccontroller.app.ui.parameterspace.util.LocationOscPacketHelper;
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

        OSCPacket oscPacket = LocationOscPacketHelper.getPacketForLocation(x, y, spacePresetList, parameterList, presetValueList);
        presenter.sendOscPacket(oscPacket);
        return true;
    }

}
