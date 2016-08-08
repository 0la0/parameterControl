package etc.a0la0.osccontroller.app.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements BaseView {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        super.onPostCreate(savedInstanceState);
        //force portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }

    @Override
    public void handleGlobalError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    public void setHomeAsUpEnabled(boolean isEnabled) {
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(isEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void launch(Activity fromActivity, Class launchType) {
        fromActivity.startActivity(new Intent(fromActivity, launchType));
    }

}
