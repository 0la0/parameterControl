package etc.a0la0.osccontroller.app.ui.parameterspace.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import butterknife.OnTouch;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.SpacePreset;

public class PresetLocationView extends FrameLayout {

    @BindView(R.id.presetIcon) ImageView iconView;

    private final int MOVING_THRESHOLD = 20;
    private final int ICON_HALF_SIZE = (int) getResources().getDimension(R.dimen.space_preset_icon_half_size);
    private boolean isMoving = false;
    private float dX, dY, rawX, rawY;
    private SpacePreset spacePreset;
    private EventDelegate eventDelegate;

    public interface EventDelegate {
        void onNewPosition();
        void onOpenEdit();
    }

    public PresetLocationView(Context context) {
        super(context);
        init();
    }
    public PresetLocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PresetLocationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PresetLocationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.preset_location, this);
        ButterKnife.bind(this);
    }

    public void setEventDelegate(EventDelegate eventDelegate) {
        this.eventDelegate = eventDelegate;
    }

    public void setSpacePreset(SpacePreset spacePreset) {
        this.spacePreset = spacePreset;
        moveTo(spacePreset.getCenterX(), spacePreset.getCenterY());
    }

    @OnLongClick(R.id.presetIcon)
    public boolean onLongClick() {
        if (isMoving || eventDelegate == null) {
            return false;
        }
        eventDelegate.onOpenEdit();
        return true;
    }

    @OnTouch(R.id.presetIcon)
    public boolean onTouch(View view, MotionEvent event) {
        int eventAction = event.getAction();

        if (eventAction == MotionEvent.ACTION_DOWN) {
            dX = view.getX() - event.getRawX();
            dY = view.getY() - event.getRawY();
            rawX = event.getRawX();
            rawY = event.getRawY();
        }
        else if (eventAction == MotionEvent.ACTION_MOVE) {
            double moveDistance = Math.sqrt(
                    Math.pow(event.getRawX() - rawX, 2) +
                    Math.pow(event.getRawY() - rawY, 2)
            );
            isMoving = moveDistance > MOVING_THRESHOLD;

            view.animate()
                    .x(event.getRawX() + dX)
                    .y(event.getRawY() + dY)
                    .setDuration(0)
                    .start();
        }
        else if (eventAction == MotionEvent.ACTION_UP) {
            if (isMoving) {
                int x = (int) (event.getRawX() + dX + ICON_HALF_SIZE);
                int y = (int) (event.getRawY() + dY + ICON_HALF_SIZE);

                spacePreset.setCenterX(x);
                spacePreset.setCenterY(y);

                if (eventDelegate != null) {
                    eventDelegate.onNewPosition();
                }
                isMoving = false;
            }
        }

        return false;
    }

    public void moveTo(int x, int y) {
        iconView.setX(x - ICON_HALF_SIZE);
        iconView.setY(y - ICON_HALF_SIZE);
    }

}
