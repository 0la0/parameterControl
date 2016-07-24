package etc.a0la0.osccontroller.app.ui.edit;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.data.entities.Option;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.ui.base.BasePresenter;
import etc.a0la0.osccontroller.app.ui.base.BaseView;

/**
 * Created by lukeanderson on 7/9/16.
 */
public class EditPresenter extends BasePresenter<EditPresenter.EditView> {

    private Model dataModel;
    private int position;

    public interface EditView extends BaseView {}

    public EditPresenter() {}

    public void init(Context context, int position) {
        dataModel = ModelProvider.getModel(context);
        if (position >= dataModel.getOptionList().size()) {
            dataModel.addOption();
        }
        this.position = position;
    }

    public Option getOption() {
       return dataModel.getOptionList().get(position);
    }

    public void updateOption(String title, String ipAddress, int port) {
        dataModel.set(position, title, ipAddress, port, getLocalParameterList(position), getLocalPresetList(position));
    }

    public List<Parameter> getParameterList() {
        if (getLocalParameterList(position) == null) {
            createNewParameterList();
        }
        return getLocalParameterList(position);
    }

    public void removeParameter(int parameterIndex) {
        getLocalParameterList(position).remove(parameterIndex);
    }

    public void addParameter() {
        getLocalParameterList(position).add(new Parameter());
    }

    public void setParameter(int parameterIndex, Parameter parameter) {
        getLocalParameterList(position).set(parameterIndex, parameter);
        dataModel.persist();
    }

    private List<Parameter> getLocalParameterList(int index) {
        return dataModel.getOptionList().get(index).getParameterList();
    }

    private List<Preset> getLocalPresetList(int index) {
        return dataModel.getOptionList().get(index).getPresetList();
    }

    private void createNewParameterList() {
        dataModel.getOptionList().get(position).setParameterList(new ArrayList<>());
    }

}