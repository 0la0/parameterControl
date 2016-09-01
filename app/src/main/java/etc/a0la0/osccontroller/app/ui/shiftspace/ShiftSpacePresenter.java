package etc.a0la0.osccontroller.app.ui.shiftspace;

import android.content.Context;

import com.illposed.osc.OSCBundle;

import java.util.List;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.Option;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.osc.OscClient;
import etc.a0la0.osccontroller.app.ui.base.BasePresenter;
import etc.a0la0.osccontroller.app.ui.base.BaseView;
import etc.a0la0.osccontroller.app.ui.parameterspace.util.LocationOscPacketHelper;

public class ShiftSpacePresenter extends BasePresenter<ShiftSpacePresenter.View> {

    private int position;
    private Model dataModel;
    private OscClient oscClient;
    private List<Preset> presetList;
    private List<Parameter> parameterList;

    interface View extends BaseView {}

    public void init(Context context, int position) {
        this.position = position;
        dataModel = ModelProvider.getModel(context);

        Option option = dataModel.getOptionList().get(position);
        oscClient = new OscClient(option.getIpAddress(), option.getPort());

        presetList = dataModel.getOptionList().get(position).getPresetList();
        parameterList = dataModel.getOptionList().get(position).getParameterList();
    }

    public void onResume() {
        oscClient.start();
    }

    public void onPause() {
        oscClient.stop();
    }

    public int getNumberOfPresets() {
        return dataModel.getOptionList().get(position).getPresetList().size();
    }

    public void onGradientChange(List<Float> weightList) {
        OSCBundle oscBundle = LocationOscPacketHelper.getPacketFromWeights(weightList, parameterList, presetList);
        oscClient.send(oscBundle);
    }

}