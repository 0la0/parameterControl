package etc.a0la0.osccontroller.app.ui.parameterspace.tiltspace;

import android.content.Context;
import android.hardware.SensorManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.Option;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;
import etc.a0la0.osccontroller.app.osc.OscClient;
import etc.a0la0.osccontroller.app.ui.base.BasePresenter;
import etc.a0la0.osccontroller.app.ui.base.BaseView;
import etc.a0la0.osccontroller.app.ui.util.AccelerometerProvider;

public class TiltSpacePresenter extends BasePresenter<TiltSpacePresenter.View> {

    private int position;
    private Model dataModel;
    private OscClient oscClient;
    private AccelerometerProvider accelerometerProvider;
    private int width = 0;
    private int height = 0;
    private int iconPositionX = 0;
    private int iconPositionY = 0;
    private long previousTime = System.nanoTime();
    private float inverseTimeMultiplier = 5000000.0f;

    interface View extends BaseView {
        void setIconPosition(int x, int y);
    }

    public void init(Context context, int position) {
        this.position = position;
        dataModel = ModelProvider.getModel(context);

        Option option = dataModel.getOptionList().get(position);
        oscClient = new OscClient(option.getIpAddress(), option.getPort());
    }

    public void onResume(SensorManager sensorManager) {
        accelerometerProvider = new AccelerometerProvider(sensorManager);
        oscClient.start();

        accelerometerProvider.getObservable()
                .debounce(16, TimeUnit.MILLISECONDS)
                .subscribe(accelerometerArray -> {
                    long currentTime = System.nanoTime();
                    long elapsedTime = currentTime - previousTime;
                    previousTime = currentTime;

                    float timeMultiplier = elapsedTime / inverseTimeMultiplier;
                    int dx = (int) (timeMultiplier * -accelerometerArray[0]);
                    int dy = (int) (timeMultiplier *  accelerometerArray[1]);
                    updateIconPosition(dx, dy);
                });
    }

    public void onPause() {
        accelerometerProvider.unregister();
        oscClient.stop();
    }

    public List<Parameter> getParameterList() {
        return dataModel.getOptionList().get(position).getParameterList();
    }

    public List<Preset> getPresetList() {
        return dataModel.getOptionList().get(position).getPresetList();
    }

    public List<SpacePreset> getSpacePresetList() {
        List<SpacePreset> presetList = dataModel.getOptionList().get(position).getSpacePresetList();
        if (presetList == null) {
            //TODO: catch error
        }
        return presetList;
    }

    public void setDimensions(int x, int y) {
        width = x;
        height = y;
        iconPositionX = x / 2;
        iconPositionY = y / 2;
        setIconPosition(iconPositionX, iconPositionY);
    }

    private void updateIconPosition(int dx, int dy) {
        iconPositionX += dx;
        iconPositionY += dy;
        setIconPosition(iconPositionX, iconPositionY);
    }

    public void setIconPosition(int x, int y) {
        iconPositionX = Math.max(0, Math.min(width, x));
        iconPositionY = Math.max(0, Math.min(height, y));
        view.setIconPosition(iconPositionX, iconPositionY);
    }

    public void setInverseTimeMultiplier(float inverseTimeMultiplier) {
        this.inverseTimeMultiplier = inverseTimeMultiplier;
    }

}