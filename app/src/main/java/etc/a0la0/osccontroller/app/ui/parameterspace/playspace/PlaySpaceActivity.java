package etc.a0la0.osccontroller.app.ui.parameterspace.playspace;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

import butterknife.BindView;
import butterknife.OnTouch;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;
import etc.a0la0.osccontroller.app.ui.parameterspace.views.EditSpaceView;

public class PlaySpaceActivity extends BaseActivity implements PlaySpacePresenter.View {

    @BindView(R.id.editSpaceView) EditSpaceView editSpaceView;

    private PlaySpacePresenter presenter = new PlaySpacePresenter();
    private List<SpacePreset> spacePresetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.space_play_activity);
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

        spacePresetList = presenter.getSpacePresetList();
        editSpaceView.init(position, spacePresetList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startOscThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stopOscThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @OnTouch(R.id.editSpaceView)
    public boolean onTouch(View view, MotionEvent event) {
        int touchCount = event.getPointerCount();
        if (touchCount == 1 && event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_MOVE) {
            return true;
        }

        if (touchCount == 1) {
            presenter.onLocationChange((int) event.getX(0), (int) event.getY(0));
        }
        else {
            List<Point> touchList = Stream.ofRange(0, touchCount)
                    .map(index -> new Point((int) event.getX(index), (int) event.getY(index)))
                    .collect(Collectors.toList());
            presenter.onLocationChange(touchList);
        }

        return true;
    }

}
