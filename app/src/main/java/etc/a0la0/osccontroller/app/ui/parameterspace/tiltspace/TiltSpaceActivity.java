package etc.a0la0.osccontroller.app.ui.parameterspace.tiltspace;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnTouch;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;
import etc.a0la0.osccontroller.app.ui.parameterspace.views.EditSpaceView;
import etc.a0la0.osccontroller.app.ui.util.AccelerometerProvider;

public class TiltSpaceActivity extends BaseActivity implements TiltSpacePresenter.View {

    @BindView(R.id.editSpaceView) EditSpaceView editSpaceView;
    @BindView(R.id.iconContainer) RelativeLayout iconContainer;
    @BindView(R.id.tiltIcon) ImageView tiltIcon;

    private int ICON_HALF_SIZE;
    private TiltSpacePresenter presenter = new TiltSpacePresenter();
    private List<Preset> presetValueList;
    private List<SpacePreset> spacePresetList;
    private List<Parameter> parameterList;
    private int width = 0;
    private int height = 0;
    private int iconPositionX = 0;
    private int iconPositionY = 0;
    private AccelerometerProvider accelerometerProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_tilt_activity);
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

        ICON_HALF_SIZE = (int) getResources().getDimension(R.dimen.space_preset_icon_half_size);

        presetValueList = presenter.getPresetList();
        spacePresetList = presenter.getSpacePresetList();
        parameterList = presenter.getParameterList();

        editSpaceView.init(position, spacePresetList);
        editSpaceView.setEventDelegate((int width, int height) -> {
            presenter.setDimensions(width, height);
            Log.i("dimensions", width + ", " + height);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        presenter.onResume(sensorManager);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
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
        //float pressure = event.getPressure();

        if (eventAction != MotionEvent.ACTION_DOWN && eventAction != MotionEvent.ACTION_MOVE) {
            return true;
        }
        Log.i("Touch", x + ", " + y);
        presenter.setIconPosition(x, y);
        return true;
    }

    @Override
    public void setIconPosition(int x, int y) {
        //TODO: calculate preset values and send OSC bundle

        tiltIcon.setX(x - ICON_HALF_SIZE);
        tiltIcon.setY(y - ICON_HALF_SIZE);
    }

}
