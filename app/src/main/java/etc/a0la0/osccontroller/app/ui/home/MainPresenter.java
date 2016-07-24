package etc.a0la0.osccontroller.app.ui.home;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

import etc.a0la0.osccontroller.app.data.Model;
import etc.a0la0.osccontroller.app.data.ModelProvider;
import etc.a0la0.osccontroller.app.ui.base.BasePresenter;
import etc.a0la0.osccontroller.app.ui.base.BaseView;

/**
 * Created by lukeanderson on 7/9/16.
 */
public class MainPresenter extends BasePresenter<MainPresenter.MainActivityView> {

    private Model dataModel;

    public interface MainActivityView extends BaseView {
        void setOptionCardList(List<String> optionCardList);
    }

    public MainPresenter() {}

    public void init(Context context) {
        dataModel = ModelProvider.getModel(context);
    }

    public void removeOption(int position) {
        dataModel.removeOption(position);
        view.setOptionCardList(getOptionList());
    }

    public void createOption() {}

    public List<String> getOptionList() {
        return Stream.of(dataModel.getOptionList())
                .map(option -> option.getTitle())
                .collect(Collectors.toList());
    }

}
