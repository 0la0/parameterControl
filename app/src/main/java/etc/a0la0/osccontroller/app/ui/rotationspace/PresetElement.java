package etc.a0la0.osccontroller.app.ui.rotationspace;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import etc.a0la0.osccontroller.R;

public class PresetElement extends FrameLayout {

    @BindView(R.id.presetLabel) TextView presetLabel;
    @BindView(R.id.presetElement) RelativeLayout presetElement;
    @BindView(R.id.validationIcon) ImageView validationIcon;

    private int presetIndex;
    private ClickDelegate clickDelegate;

    interface ClickDelegate {
        void onClick(int position);
    }

    public PresetElement(Context context, int presetIndex, ClickDelegate clickDelegate) {
        super(context);
        this.presetIndex = presetIndex;
        this.clickDelegate = clickDelegate;
        inflate(getContext(), R.layout.rotation_preset, this);
        ButterKnife.bind(this);
        presetLabel.setText("Preset " + presetIndex);
    }

    @OnClick(R.id.presetElement)
    public void onClick() {
        clickDelegate.onClick(presetIndex);
    }

    public void setActive(boolean isActive) {
        presetElement.setBackgroundColor(isActive ? getResources().getColor(R.color.grey) : getResources().getColor(R.color.white));
    }

    public void setValid(boolean isValid) {
        validationIcon.setVisibility(isValid ? View.VISIBLE : View.INVISIBLE);
    }

}
