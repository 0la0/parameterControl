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

    private List<String> optionTitleList;
    private ClickDelegates clickDelegates;

    public interface ClickDelegates {
        void onEditClick(int position);
        void onSetupClick(int position);
        void onRemoveClick(int position);
        void onParamSpaceClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.optionName) TextView name;
        @BindView(R.id.optionEdit) TextView optionEdit;
        @BindView(R.id.optionSetup) TextView optionSetup;
        @BindView(R.id.optionParamSpace) TextView optionParamSpace;
        @BindView(R.id.optionDelete) ImageView optionDelete;
        @BindView(R.id.optionCardToggle) ImageView cardToggle;
        @BindView(R.id.optionCardHeader) RelativeLayout header;
        @BindView(R.id.optionCardExpand) RelativeLayout expandable;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public OptionTitleListAdapter(List<String> optionTitleList, ClickDelegates clickDelegates) {
        this.optionTitleList = optionTitleList;
        this.clickDelegates = clickDelegates;
    }

    @Override
    public OptionTitleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.option_card, parent, false);
        return new ViewHolder(cardView);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.name.setText(optionTitleList.get(position));

        viewHolder.header.setOnClickListener((View view) -> {
            if (viewHolder.expandable.getVisibility() == View.GONE) {
                viewHolder.expandable.setVisibility(View.VISIBLE);
                viewHolder.cardToggle.setImageResource(R.drawable.ic_expand_less_black_24dp);
            }
            else if (viewHolder.expandable.getVisibility() == View.VISIBLE){
                viewHolder.expandable.setVisibility(View.GONE);
                viewHolder.cardToggle.setImageResource(R.drawable.ic_expand_more_black_24dp);
            }
        });

        viewHolder.optionDelete.setOnClickListener(view -> clickDelegates.onRemoveClick(position));
        viewHolder.optionEdit.setOnClickListener(view -> clickDelegates.onEditClick(position));
        viewHolder.optionSetup.setOnClickListener(view -> clickDelegates.onSetupClick(position));
        viewHolder.optionParamSpace.setOnClickListener(view -> clickDelegates.onParamSpaceClick(position));
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

