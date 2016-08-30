package etc.a0la0.osccontroller.app.ui.shiftspace;

import android.content.Context;

import com.illposed.osc.OSCPacket;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.Option;
import etc.a0la0.osccontroller.app.osc.OscClient;
import etc.a0la0.osccontroller.app.ui.base.BasePresenter;
import etc.a0la0.osccontroller.app.ui.base.BaseView;

public class ShiftSpacePresenter extends BasePresenter<ShiftSpacePresenter.View> {

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

    public void onResume() {
        //oscClient.start();
    }

    public void onPause() {
        //oscClient.stop();
    }

    public int getNumberOfPresets() {
        return dataModel.getOptionList().get(position).getPresetList().size();
    }

    public void sendOscPacket(OSCPacket oscPacket) {
        oscClient.send(oscPacket);
    }

}