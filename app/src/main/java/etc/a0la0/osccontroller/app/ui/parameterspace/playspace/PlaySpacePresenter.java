package etc.a0la0.osccontroller.app.ui.parameterspace.playspace;

import android.content.Context;

import com.illposed.osc.OSCPacket;

import java.util.List;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.Option;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;
import etc.a0la0.osccontroller.app.osc.OscClient;
import etc.a0la0.osccontroller.app.ui.base.BasePresenter;
import etc.a0la0.osccontroller.app.ui.base.BaseView;

public class PlaySpacePresenter extends BasePresenter<PlaySpacePresenter.View> {

    private int position;
    private Model dataModel;
    private OscClient oscClient;

    interface View extends BaseView {}

    public void init(Context context, int position) {
        this.position = position;
        dataModel = ModelProvider.getModel(context);

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

    public List<SpacePreset> getSpacePresetList() {
        List<SpacePreset> presetList = dataModel.getOptionList().get(position).getSpacePresetList();
        if (presetList == null) {
            //TODO: catch error
        }
        return presetList;
    }

    public void sendOscPacket(OSCPacket oscPacket) {
        oscClient.send(oscPacket);
    }
}
