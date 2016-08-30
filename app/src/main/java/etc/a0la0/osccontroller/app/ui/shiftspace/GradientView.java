package etc.a0la0.osccontroller.app.ui.shiftspace;

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

public class GradientView extends FrameLayout {

    @BindView(R.id.gradient) ImageView gradientView;
    @BindView(R.id.outline) ImageView outlineView;
    @BindView(R.id.center) View center;

    private final int MOVING_THRESHOLD = 20;

    private float startX, startY;
    private float centerX, centerY;
    private EventDelegate eventDelegate;
    private boolean isMoving = false;
    private int radius = (int) getResources().getDimension(R.dimen.shift_space_gradient_radius);

    public interface EventDelegate {
        void onChange();
    }

    public GradientView(Context context) {
        super(context);
        init();
    }
    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public GradientView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GradientView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.shift_space_gradient, this);
        ButterKnife.bind(this);
    }

    public void setEventDelegate(EventDelegate eventDelegate) {
        this.eventDelegate = eventDelegate;
    }

    public void setColor(int color) {
        gradientView.setColorFilter(color);
    }

    @OnTouch(R.id.gradient)
    public boolean onTouch(View view, MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN) {
            startX = view.getX() - event.getRawX();
            startY = view.getY() - event.getRawY();
        }
        else if (eventAction == MotionEvent.ACTION_MOVE) {
            double moveDistance = Math.sqrt(
                Math.pow(event.getRawX() - startX, 2) +
                Math.pow(event.getRawY() - startY, 2)
            );
            isMoving = moveDistance > MOVING_THRESHOLD;
            float x = event.getRawX() + startX;
            float y = event.getRawY() + startY;
            if (eventDelegate != null) {
                eventDelegate.onChange();
            }
            moveTo(x, y);
        }
        else if (eventAction == MotionEvent.ACTION_UP) {
            if (isMoving) {
                isMoving = false;
            }
        }

        return false;
    }

    @OnLongClick(R.id.gradient)
    public boolean onGradientLongClick() {
        if (isMoving) {
            return false;
        }
        outlineView.setVisibility(View.VISIBLE);
        return true;
    }

    @OnTouch(R.id.outline)
    public boolean onOutlineTouch(View view, MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_MOVE) {
            float x = event.getRawX();
            float y = event.getRawY();
            radius = (int) getDistanceFromCenter(x, y);
            if (eventDelegate != null) {
                eventDelegate.onChange();
            }
            updateViewSize();
        }
        else if (eventAction == MotionEvent.ACTION_UP) {
            outlineView.setVisibility(View.GONE);
        }

        return true;
    }

    public void moveTo(float x, float y) {
        gradientView.setX(x);
        gradientView.setY(y);
        outlineView.setX(x);
        outlineView.setY(y);

        centerX = x + radius;
        centerY = y + radius;

        center.setX(centerX);
        center.setY(centerY);
    }

    private double getDistanceFromCenter(float x, float y) {
        return Math.sqrt(
                Math.pow(x - centerX, 2) +
                Math.pow(y - centerY, 2)
        );
    }

    private void updateViewSize() {
        float x = centerX - radius;
        float y = centerY - radius;
        int diameter = radius * 2;

        gradientView.setX(x);
        gradientView.setY(y);
        gradientView.getLayoutParams().width = diameter;
        gradientView.getLayoutParams().height = diameter;

        outlineView.setX(x);
        outlineView.setY(y);
        outlineView.getLayoutParams().width = diameter;
        outlineView.getLayoutParams().height = diameter;

        gradientView.requestLayout();
        outlineView.requestLayout();
    }

    public float getWeight(int canvasCenterX, int canvasCenterY) {
        double distance = getDistance(centerX, centerY, canvasCenterX, canvasCenterY);
        if (distance > radius) {
            return 0f;
        }
        double weight = (radius - distance) / (radius * 1.0);
        return (float) weight;
    }

    private double getDistance(float x1, float y1, int x2, int y2) {
        return Math.sqrt(
                Math.pow(x1 - x2, 2) +
                Math.pow(y1 - y2, 2)
        );
    }

}
