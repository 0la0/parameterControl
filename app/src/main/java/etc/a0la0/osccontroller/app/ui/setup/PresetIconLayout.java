package etc.a0la0.osccontroller.app.ui.setup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import etc.a0la0.osccontroller.R;

public class PresetIconLayout extends FrameLayout {

    @BindView(R.id.presetLabel) TextView presetLabel;

    private ClickDelegate clickDelegate;

    interface ClickDelegate {
        void onClick();
        void onLongClick();
        void onUpdate();
        void onRemove();
    }

    public PresetIconLayout(Context context, ClickDelegate clickDelegate) {
        super(context);
        init();
        this.clickDelegate = clickDelegate;
    }

    private void init() {
        inflate(getContext(), R.layout.preset_icon, this);
        ButterKnife.bind(this);
    }

    public void setLabel(String label) {
        presetLabel.setText(label);
    }

    @OnClick(R.id.presetIcon)
    public void onClick() {
        clickDelegate.onClick();
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.preset_selected));
    }

    @OnLongClick(R.id.presetIcon)
    public boolean onLongClick() {
        new AlertDialog.Builder(getContext())
                .setPositiveButton("Update Preset", (DialogInterface dialog,int id) -> {
                    clickDelegate.onUpdate();
                })
                .setNeutralButton("Remove Preset", (DialogInterface dialog,int id) -> {
                    clickDelegate.onRemove();
                })
                .create()
                .show();
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.preset_selected));
        clickDelegate.onLongClick();
        return true;
    }

    public void onUnselect() {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.preset_unselected));
    }

}
