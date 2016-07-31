package etc.a0la0.osccontroller.app.ui.parameterspace;

import android.content.Context;

import java.util.List;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.Preset;
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

    public List<Preset> getPresetList() {
        return dataModel.getOptionList().get(position).getPresetList();
    }

}
