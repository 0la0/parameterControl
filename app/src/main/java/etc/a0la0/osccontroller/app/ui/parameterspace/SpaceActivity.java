package etc.a0la0.osccontroller.app.ui.parameterspace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;
import etc.a0la0.osccontroller.app.ui.parameterspace.entities.PresetViewModel;

public class SpaceActivity extends BaseActivity implements SpacePresenter.View {

    @BindView(R.id.editSpaceView) EditSpaceView editSpaceView;
    @BindView(R.id.iconContainer) RelativeLayout iconContainer;

    private SpacePresenter presenter = new SpacePresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.space_activity);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setHomeAsUpEnabled(false);
        presenter.attachView(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int position = intent.getIntExtra(getString(R.string.option_id), 0);
        presenter.init(this, position);

        List<Preset> presetList = presenter.getPresetList();

        List<PresetViewModel> viewModelList = new ArrayList<>();

        for (int i = 0; i < presetList.size(); i++) {
            PresetViewModel viewModel = new PresetViewModel(
                    (int) (500 * Math.random()),
                    (int) (600 * Math.random()),
                    500, 600,
                    (int) (255 * Math.random()),
                    (int) (255 * Math.random()),
                    (int) (255 * Math.random())
            );
            viewModelList.add(viewModel);
        }

        editSpaceView.init(position, presetList, viewModelList);
        Stream.of(viewModelList).forEach(presetViewModel -> {
            iconContainer.addView(createPresetLocationView(presetViewModel));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private PresetLocationView createPresetLocationView(PresetViewModel presetViewModel) {
        PresetLocationView view = new PresetLocationView(this, null);
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setPresetViewModel(presetViewModel);
        view.setEventDelegate(new PresetLocationView.EventDelegate(){
            @Override
            public void onNewPosition() {
                Log.i("Preset", "OnNewPosition");
                editSpaceView.onPresetChange(presetViewModel);
            }
        });
        return view;
    }

}
