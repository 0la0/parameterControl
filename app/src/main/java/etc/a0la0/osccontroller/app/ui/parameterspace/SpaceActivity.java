package etc.a0la0.osccontroller.app.ui.parameterspace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Preset;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;

public class SpaceActivity extends BaseActivity implements SpacePresenter.View {

    @BindView(R.id.editSpaceView) EditSpaceView editSpaceView;

    private SpacePresenter presenter = new SpacePresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Log.i("Preset list", presetList.size() + "");
        editSpaceView.init(position, presetList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

}
