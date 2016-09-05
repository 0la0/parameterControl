package etc.a0la0.osccontroller.app.ui.home;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import etc.a0la0.osccontroller.R;

public class OptionTitleListAdapter extends RecyclerView.Adapter<OptionTitleListAdapter.ViewHolder> {

    private int selectedPosition = -1;
    private List<String> optionTitleList;
    private ClickDelegate clickDelegate;

    public interface ClickDelegate {
        void onEditClick(int position);
        void onSetupClick(int position);
        void onRemoveClick(int position);
        void onParamSpaceEditClick(int position);
        void onParamSpacePlayClick(int position);
        void onParamSpaceTiltClick(int position);
        void onRotationSpaceClick(int position);
        void onShiftSpaceClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.optionName) TextView name;
        @BindView(R.id.optionEdit) TextView optionEdit;
        @BindView(R.id.optionSetup) TextView optionSetup;
        @BindView(R.id.optionParamSpaceEdit) TextView optionParamSpaceEdit;
        @BindView(R.id.optionParamSpacePlay) TextView optionParamSpacePlay;
        @BindView(R.id.optionParamSpaceTilt) TextView optionParamSpaceTilt;
        @BindView(R.id.optionRotationSpace) TextView optionRotationSpace;
        @BindView(R.id.optionShiftSpace) TextView optionShiftSpace;
        @BindView(R.id.optionDelete) ImageView optionDelete;
        @BindView(R.id.optionCardToggle) ImageView cardToggle;
        @BindView(R.id.optionCardHeader) RelativeLayout header;
        @BindView(R.id.optionCardExpand) RelativeLayout expandable;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public OptionTitleListAdapter(List<String> optionTitleList, ClickDelegate clickDelegate) {
        this.optionTitleList = optionTitleList;
        this.clickDelegate = clickDelegate;
    }

    @Override
    public OptionTitleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.option_card, parent, false);
        return new ViewHolder(cardView);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        boolean isExpanded = position == selectedPosition;
        viewHolder.expandable.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        viewHolder.name.setText(optionTitleList.get(position));

        viewHolder.header.setOnClickListener((View view) -> {
            if (selectedPosition >= 0) {
                notifyItemChanged(selectedPosition);
            }
            selectedPosition = position;
            notifyItemChanged(selectedPosition);
        });

        viewHolder.optionDelete.setOnClickListener(view -> clickDelegate.onRemoveClick(position));
        viewHolder.optionEdit.setOnClickListener(view -> clickDelegate.onEditClick(position));
        viewHolder.optionSetup.setOnClickListener(view -> clickDelegate.onSetupClick(position));
        viewHolder.optionParamSpaceEdit.setOnClickListener(view -> clickDelegate.onParamSpaceEditClick(position));
        viewHolder.optionParamSpacePlay.setOnClickListener(view -> clickDelegate.onParamSpacePlayClick(position));
        viewHolder.optionParamSpaceTilt.setOnClickListener(view -> clickDelegate.onParamSpaceTiltClick(position));
        viewHolder.optionRotationSpace.setOnClickListener(view -> clickDelegate.onRotationSpaceClick(position));
        viewHolder.optionShiftSpace.setOnClickListener(view -> clickDelegate.onShiftSpaceClick(position));
    }

    @Override
    public int getItemCount() {
        return optionTitleList.size();
    }

    public void setOptionCardList(List<String> optionTitleList) {
        this.optionTitleList = optionTitleList;
        notifyDataSetChanged();
    }


}

