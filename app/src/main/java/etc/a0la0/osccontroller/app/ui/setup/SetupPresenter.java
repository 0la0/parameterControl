package etc.a0la0.osccontroller.app.ui.setup;

import android.content.Context;

import com.illposed.osc.OSCMessage;

import java.util.List;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.Option;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.osc.OscClient;
import etc.a0la0.osccontroller.app.ui.base.BasePresenter;
import etc.a0la0.osccontroller.app.ui.base.BaseView;

public class SetupPresenter extends BasePresenter<SetupPresenter.View> {

    private Model dataModel;
    private int position;
    private OscClient oscClient;

    interface View extends BaseView {}

    public void init(Context context, int position) {
        dataModel = ModelProvider.getModel(context);
        if (position >= dataModel.getOptionList().size()) {
            dataModel.addOption();
        }
        this.position = position;

        Option option = dataModel.getOptionList().get(position);
        oscClient = new OscClient(option.getIpAddress(), option.getPort());
    }

    public void startOscThread() {
        oscClient.start();
    }

    public void stopOscThread() {
        oscClient.stop();
    }

    public List<Parameter> getParameterList() {
        return dataModel.getOptionList().get(position).getParameterList();
    }

    public List<Preset> getPresetList() {
        return dataModel.getOptionList().get(position).getPresetList();
    }

    public void addPreset(Preset preset) {
        dataModel.getOptionList().get(position).addPreset(preset);
        dataModel.persist();
    }

    public void setPreset(int presetIndex, Preset preset) {
        dataModel.getOptionList().get(position).getPresetList().set(presetIndex, preset);
        dataModel.persist();
    }

    public void removePreset(Preset preset) {
        dataModel.getOptionList().get(position).getPresetList().remove(preset);
        dataModel.persist();
    }

    public void sendOscMessage(OSCMessage message) {
        oscClient.send(message);
    }

    public void sendOscMessage(List<OSCMessage> messageList) {
        oscClient.send(messageList);
    }

}
