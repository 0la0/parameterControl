package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;

import java.util.Collections;
import java.util.List;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.Option;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
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
    private List<Preset> presetList;
    private List<Parameter> parameterList;

    interface View extends BaseView {
        void onAccelerometerChange(float[] accelerometerData);
    }

    public void init(Context context, int position) {
        this.position = position;
        dataModel = ModelProvider.getModel(context);

        Option option = dataModel.getOptionList().get(position);
        oscClient = new OscClient(option.getIpAddress(), option.getPort());
        presetList = dataModel.getOptionList().get(position).getPresetList();
        parameterList = dataModel.getOptionList().get(position).getParameterList();
    }

    public void onResume(SensorManager sensorManager) {
        accelerometerProvider = new AccelerometerProvider(sensorManager);
        oscClient.start();

        accelerometerProvider.getObservable()
                .subscribe(
                        accelerometerData -> view.onAccelerometerChange(accelerometerData),
                        error -> Log.i("AccelerometerError", error.toString())
                );
    }

    public void onPause() {
        accelerometerProvider.unregister();
        oscClient.stop();
        subscriptions.clear();
    }

    public int getNumberOfPresets() {
        return dataModel.getOptionList().get(position).getPresetList().size();
    }

    public void onUpdateWeightList(List<Double> weightList) {
        String weightString = Stream.of(weightList)
                .map(weight -> weight + "")
                .collect(Collectors.joining(", "));

        OSCBundle oscBundle = createOscBundle(weightList, parameterList, presetList);
        oscClient.send(oscBundle);
    }

    public void onUpdateWeightList(int classificationIndex) {
        Preset activePreset = presetList.get(classificationIndex);

        List<OSCPacket> messageList = Stream.of(parameterList)
                .map(parameter -> {
                    String parameterKey = parameter.getUniqueId();
                    String address = parameter.getAddress();
                    float value = activePreset.get(parameterKey);
                    return new OSCMessage(address, Collections.singletonList(value));
                })
                .collect(Collectors.toList());

        OSCBundle oscBundle = new OSCBundle(messageList);
        oscClient.send(oscBundle);
    }

    private OSCBundle createOscBundle(List<Double> weightList, List<Parameter> parameterList, List<Preset> presetValueList) {

        double weightSum = Stream.of(weightList)
                .reduce(0.0, (sum, weight) -> sum + weight);

        List<OSCPacket> messageList = Stream.of(parameterList)
                .map(parameter -> {
                    String parameterKey = parameter.getUniqueId();
                    String address = parameter.getAddress();
                    float presetValue = 0;
                    for (int i = 0; i < weightList.size(); i++) {
                        Preset preset = presetValueList.get(i);
                        double weight = weightList.get(i);
                        presetValue += preset.get(parameterKey) * weight;
                    }
                    presetValue /= weightSum;
                    return new OSCMessage(address, Collections.singletonList(presetValue));
                })
                .collect(Collectors.toList());

        return new OSCBundle(messageList);
    }


}
