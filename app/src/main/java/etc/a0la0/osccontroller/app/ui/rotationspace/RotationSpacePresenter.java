package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.List;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.Option;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.osc.OscClient;
import etc.a0la0.osccontroller.app.ui.base.BasePresenter;
import etc.a0la0.osccontroller.app.ui.base.BaseView;
import etc.a0la0.osccontroller.app.ui.util.AccelerometerProvider;

public class RotationSpacePresenter extends BasePresenter<RotationSpacePresenter.View> {

    private int position;
    private Model dataModel;
    private OscClient oscClient;
    private AccelerometerProvider accelerometerProvider;

    interface View extends BaseView {
        void onAccelerometerChange(float[] accelerometerData);
    }

    public void init(Context context, int position) {
        this.position = position;
        dataModel = ModelProvider.getModel(context);

        Option option = dataModel.getOptionList().get(position);
        //oscClient = new OscClient(option.getIpAddress(), option.getPort());
    }

    public void onResume(SensorManager sensorManager) {
        accelerometerProvider = new AccelerometerProvider(sensorManager);
        //oscClient.start();

        accelerometerProvider.getObservable()
                .subscribe(
                        accelerometerData -> view.onAccelerometerChange(accelerometerData),
                        error -> Log.i("AccelerometerError", error.toString())
                );
    }

    public void onPause() {
        accelerometerProvider.unregister();
        //oscClient.stop();
        subscriptions.clear();
    }

    public List<Preset> getPresetList() {
        return dataModel.getOptionList().get(position).getPresetList();
    }


}
