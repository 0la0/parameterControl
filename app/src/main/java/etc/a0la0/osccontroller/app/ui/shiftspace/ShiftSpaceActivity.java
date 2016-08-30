package etc.a0la0.osccontroller.app.ui.shiftspace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.ui.base.BaseActivity;

public class ShiftSpaceActivity extends BaseActivity implements ShiftSpacePresenter.View {

    @BindView(R.id.gradientViewContainer) RelativeLayout gradientViewContainer;

    private int width, height, halfWidth, halfHeight;
    private ShiftSpacePresenter presenter = new ShiftSpacePresenter();
    private List<GradientView> gradientViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_space_activity);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setHomeAsUpEnabled(false);
        presenter.attachView(this);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        int position = intent.getIntExtra(getString(R.string.option_id), 0);
        presenter.init(this, position);

        calculateDimensions();
        gradientViewList = new ArrayList<>();

        int numberOfPresets = presenter.getNumberOfPresets();
        for (int i = 0; i < numberOfPresets; i++) {
            GradientView gradientView = createGradientView();
            gradientViewList.add(gradientView);
            gradientViewContainer.addView(gradientView);
            int x = getRandomPosition(width);
            int y = getRandomPosition(height);
            gradientView.moveTo(x, y);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private GradientView createGradientView() {
        GradientView gradientView = new GradientView(this);
        gradientView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gradientView.setEventDelegate(this::onGradientChange);
        gradientView.setColor(getRandomColor());
        return gradientView;
    }

    private void onGradientChange() {
        List<Float> weightList = Stream.of(gradientViewList)
                .map(gradientView -> gradientView.getWeight(halfWidth, halfHeight))
                .collect(Collectors.toList());
        Log.i("weightList", weightList.toString());
    }

    private void calculateDimensions() {
        int statusBarResourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int topBarHeight = getResources().getDimensionPixelSize(statusBarResourceId);

        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels - topBarHeight;
        halfWidth = width / 2;
        halfHeight = (height / 2);
    }

    private int getRandomPosition(int dimension) {
        int buffer = 100;
        return buffer + (int) ((dimension - buffer * 2) * Math.random());
    }

    private int getRandomColor() {
        int r = (int) (255 * Math.random());
        int g = (int) (255 * Math.random());
        int b = (int) (255 * Math.random());
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        return 0xFF000000 | r | g | b;
    }

}
