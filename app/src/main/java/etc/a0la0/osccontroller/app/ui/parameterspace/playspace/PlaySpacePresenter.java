package etc.a0la0.osccontroller.app.ui.parameterspace.playspace;

import android.content.Context;
import android.graphics.Point;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
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
import etc.a0la0.osccontroller.app.ui.parameterspace.util.LocationOscPacketHelper;

public class PlaySpacePresenter extends BasePresenter<PlaySpacePresenter.View> {

    private Model dataModel;
    private OscClient oscClient;

    private List<Preset> presetValueList;
    private List<SpacePreset> spacePresetList;
    private List<Parameter> parameterList;

    interface View extends BaseView {}

    public void init(Context context, int position) {
        dataModel = ModelProvider.getModel(context);

        Option option = dataModel.getOptionList().get(position);
        oscClient = new OscClient(option.getIpAddress(), option.getPort());

        presetValueList = dataModel.getOptionList().get(position).getPresetList();
        spacePresetList = dataModel.getOptionList().get(position).getSpacePresetList();
        parameterList = dataModel.getOptionList().get(position).getParameterList();
    }

    public void startOscThread() {
        oscClient.start();
    }

    public void stopOscThread() {
        oscClient.stop();
    }

    public List<SpacePreset> getSpacePresetList() {
        return spacePresetList;
    }

    public void onLocationChange(int x, int y) {
        List<Float> weightList = Stream.of(spacePresetList)
                .map(spacePreset -> Math.min(1f, spacePreset.getValue(x, y)))
                .collect(Collectors.toList());

        processWeightList(weightList);
    }

    public void onLocationChange(List<Point> touchList) {
        int touchCount = touchList.size();

        List<Float> weightList = Stream.of(spacePresetList)
                .map(spacePreset -> {
                    // calculate mean value of each preset at a given touch point
                    return Stream.of(touchList)
                            .map(touchPoint -> Math.min(1f, spacePreset.getValue(touchPoint.x, touchPoint.y)))
                            .reduce(0.0, (sum, weight) -> sum + weight)
                            .floatValue() / (touchCount * 1.0f);
                })
                .collect(Collectors.toList());

        processWeightList(weightList);
    }

    private void processWeightList(List<Float> weightList) {
        OSCPacket oscPacket = LocationOscPacketHelper.getPacketFromWeights(weightList, parameterList, presetValueList);
        oscClient.send(oscPacket);
    }

}
