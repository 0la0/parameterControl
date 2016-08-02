package etc.a0la0.osccontroller.app.ui.parameterspace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;

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
        List<SpacePreset> spacePresetList = presenter.getSpacePresetList();

        editSpaceView.init(position, presetList, spacePresetList);
        for (SpacePreset spacePreset : spacePresetList) {
            iconContainer.addView(createPresetLocationView(spacePreset));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.persistState();
        presenter.detachView();
    }

    private PresetLocationView createPresetLocationView(SpacePreset spacePreset) {
        PresetLocationView view = new PresetLocationView(this, null);
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setSpacePreset(spacePreset);
        view.setEventDelegate(new PresetLocationView.EventDelegate(){
            @Override
            public void onNewPosition() {
                editSpaceView.onPresetChange(spacePreset);
            }
            @Override
            public void onOpenEdit() {
                openEditDialog(spacePreset);
            }
        });
        return view;
    }

    public void openEditDialog(SpacePreset spacePreset) {
        PresetEditDialogView presetEditDialogView = new PresetEditDialogView(this);
        presetEditDialogView.setPresetViewModel(spacePreset);

        new AlertDialog.Builder(this)
                .setView(presetEditDialogView)
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.ok), (DialogInterface dialog, int id) -> {

                    float std = presetEditDialogView.getStandardDeviation();
                    float amplitude = presetEditDialogView.getAmplitude();
                    int red = presetEditDialogView.getRed();
                    int green = presetEditDialogView.getGreen();
                    int blue = presetEditDialogView.getBlue();

                    spacePreset.setStandardDeviation(std);
                    spacePreset.setAmplitude(amplitude);
                    spacePreset.setR(red);
                    spacePreset.setG(green);
                    spacePreset.setB(blue);
                    editSpaceView.onPresetChange(spacePreset);
                })
                .create()
                .show();
    }

}
