package etc.a0la0.osccontroller.app.ui.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Parameter;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;

/**
 * Created by lukeanderson on 7/9/16.
 */
public class EditActivity extends BaseActivity implements EditPresenter.EditView{

    private EditPresenter presenter = new EditPresenter();
    private ParameterListAdapter parameterListAdapter;

    @BindView(R.id.titleTextField) TextInputEditText titleTextField;
    @BindView(R.id.ipAddressTextField) TextInputEditText ipAddressTextField;
    @BindView(R.id.portTextField) TextInputEditText portTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
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

        titleTextField.setText(presenter.getOption().getTitle());
        ipAddressTextField.setText(presenter.getOption().getIpAddress());
        portTextField.setText(presenter.getOption().getPort() + "");

        RecyclerView optionListView = (RecyclerView) findViewById(R.id.parameterListView);
        optionListView.setLayoutManager(new LinearLayoutManager(this));
        parameterListAdapter = new ParameterListAdapter(presenter.getParameterList(), new ParameterClickDelegates());
        optionListView.setAdapter(parameterListAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String title = titleTextField.getText().toString();
        String ipAddress = ipAddressTextField.getText().toString();
        int port = Integer.parseInt(portTextField.getText().toString());

        presenter.updateOption(title, ipAddress, port);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @OnClick(R.id.addParameter)
    public void onAddOptionClick() {
        presenter.addParameter();
        parameterListAdapter.notifyDataSetChanged();
    }

    private class ParameterClickDelegates implements ParameterListAdapter.ClickDelegates {

        @Override
        public void onAddressBlur(int position, String address) {
            Parameter parameter = presenter.getParameterList().get(position);
            parameter.setAddress(address);
            presenter.setParameter(position, parameter);
        }

        @Override
        public void onRemoveClick(int position) {
            presenter.removeParameter(position);
            parameterListAdapter.notifyDataSetChanged();
        }

    }

}
