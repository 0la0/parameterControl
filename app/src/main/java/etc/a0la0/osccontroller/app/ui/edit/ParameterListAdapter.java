package etc.a0la0.osccontroller.app.ui.edit;

import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import etc.a0la0.osccontroller.R;
import etc.a0la0.osccontroller.app.data.entities.Parameter;


public class ParameterListAdapter extends RecyclerView.Adapter<ParameterListAdapter.ViewHolder> {

    private List<Parameter> parameterList;
    private ClickDelegates clickDelegates;

    public interface ClickDelegates {
        void onAddressBlur(int position, String address);
        void onRemoveClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.parameterAddressTextField) TextInputEditText parameterAddress;
        @BindView(R.id.parameterDelete) ImageView parameterDelete;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    public ParameterListAdapter(List<Parameter> parameterList, ClickDelegates clickDelegates) {
        this.parameterList = parameterList;
        this.clickDelegates = clickDelegates;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_parameter, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.parameterAddress.setText(parameterList.get(position).getAddress());

        viewHolder.parameterAddress.setOnFocusChangeListener((View view, boolean isFocused) -> {
            if (!isFocused) {
                clickDelegates.onAddressBlur(position, viewHolder.parameterAddress.getText().toString());
            }
        });

        viewHolder.parameterDelete.setOnClickListener(view -> clickDelegates.onRemoveClick(position));
    }

    @Override
    public int getItemCount() {
        return parameterList.size();
    }

    public void setPrameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
        notifyDataSetChanged();
    }

}
