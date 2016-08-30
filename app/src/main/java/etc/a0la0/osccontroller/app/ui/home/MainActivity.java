package etc.a0la0.osccontroller.app.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.OnClick;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;
import etc.a0la0.osccontroller.app.ui.edit.EditActivity;
import etc.a0la0.osccontroller.app.ui.parameterspace.editspace.EditSpaceActivity;
import etc.a0la0.osccontroller.app.ui.parameterspace.playspace.PlaySpaceActivity;
import etc.a0la0.osccontroller.app.ui.parameterspace.tiltspace.TiltSpaceActivity;
import etc.a0la0.osccontroller.app.ui.rotationspace.RotationSpaceActivity;
import etc.a0la0.osccontroller.app.ui.setup.SetupActivity;
import etc.a0la0.osccontroller.app.ui.shiftspace.ShiftSpaceActivity;

public class MainActivity extends BaseActivity implements MainPresenter.MainActivityView{

    private MainPresenter presenter = new MainPresenter();
    private OptionTitleListAdapter optionTitleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setHomeAsUpEnabled(false);
        presenter.attachView(this);
        presenter.init(this);

        RecyclerView optionListView = (RecyclerView) findViewById(R.id.optionListView);
        optionListView.setLayoutManager(new LinearLayoutManager(this));
        optionTitleListAdapter = new OptionTitleListAdapter(presenter.getOptionList(), new OptionClickDelegates(this));
        optionListView.setAdapter(optionTitleListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOptionCardList(presenter.getOptionList());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @OnClick(R.id.addOption)
    public void onAddOptionClick() {
        startActivity(presenter.getOptionList().size(), EditActivity.class);
    }

    public void setOptionCardList(List<String> optionTitleList) {
        optionTitleListAdapter.setOptionCardList(optionTitleList);
    }

    private void startActivity(int position, Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(getString(R.string.option_id), position);
        startActivity(intent);
    }

    private class OptionClickDelegates implements OptionTitleListAdapter.ClickDelegates {

        private Context context;

        public OptionClickDelegates(Context context) {
            this.context = context;
        }

        @Override
        public void onEditClick(int position) {
            startActivity(position, EditActivity.class);
        }

        @Override
        public void onRemoveClick(int position) {
            new AlertDialog.Builder(context)
                    .setTitle(getString(R.string.remove_option))
                    .setPositiveButton(getString(R.string.ok), (DialogInterface dialog, int id) -> {
                        presenter.removeOption(position);
                    })
                    .create()
                    .show();
        }

        @Override
        public void onSetupClick(int position) {
            startActivity(position, SetupActivity.class);
        }

        @Override
        public void onParamSpaceEditClick(int position) {
            startActivity(position, EditSpaceActivity.class);
        }

        @Override
        public void onParamSpacePlayClick(int position) {
            startActivity(position, PlaySpaceActivity.class);
        }

        @Override
        public void onParamSpaceTiltClick(int position) {
            startActivity(position, TiltSpaceActivity.class);
        }

        @Override
        public void onRotationSpaceClick(int position) {
            startActivity(position, RotationSpaceActivity.class);
        }

        @Override
        public void onShiftSpaceClick(int position) {
            startActivity(position, ShiftSpaceActivity.class);
        }

    }

}
