package etc.a0la0.osccontroller.app.ui.parameterspace;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;
import etc.a0la0.osccontroller.app.ui.base.BasePresenter;
import etc.a0la0.osccontroller.app.ui.base.BaseView;

public class SpacePresenter extends BasePresenter<SpacePresenter.View>{

    private int position;
    private Model dataModel;

    interface View extends BaseView {}

    public void init(Context context, int position) {
        this.position = position;
        dataModel = ModelProvider.getModel(context);
    }

    public List<SpacePreset> getSpacePresetList() {
        List<SpacePreset> presetList = dataModel.getOptionList().get(position).getSpacePresetList();
        if (presetList == null) {
            presetList = createSpacePresetList(
                    dataModel.getOptionList().get(position).getPresetList().size()
            );
            dataModel.getOptionList().get(position).setSpacePresetList(presetList);
        }
        return presetList;
    }

    public void persistState() {
        for(SpacePreset spacePreset : getSpacePresetList()) {
            spacePreset.setPixelValue(null);
            spacePreset.setMatrix(null);
        }
        dataModel.persist();
    }

    private List<SpacePreset> createSpacePresetList(int size) {
        Log.i("-----", "creating space preset list");
        List<SpacePreset> spacePresetList = new ArrayList<>();
        spacePresetList.add(new SpacePreset(0, 0, 0, 0, 0, 0, 0));
        for (int i = 1; i < size; i++) {
            spacePresetList.add(new SpacePreset(
                    (int) (500 * Math.random()),
                    (int) (500 * Math.random()),
                    500, 500,
                    (int) (255 * Math.random()),
                    (int) (255 * Math.random()),
                    (int) (255 * Math.random())
            ));
        }
        return spacePresetList;
    }

}
