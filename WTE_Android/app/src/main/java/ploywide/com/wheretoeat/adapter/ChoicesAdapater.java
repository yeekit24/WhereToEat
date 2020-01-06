package ploywide.com.wheretoeat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ploywide.com.wheretoeat.API.APIHelper;
import ploywide.com.wheretoeat.API.Callback;
import ploywide.com.wheretoeat.R;
import ploywide.com.wheretoeat.app.AppController;
import ploywide.com.wheretoeat.model.ChoicesItem;

public class ChoicesAdapater extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<ChoicesItem> choiceItems;

    private static final int CHOICE_VIEW_TYPE = 111;

    public ChoicesAdapater(Context mContext, List<ChoicesItem> choiceItem) {
        this.mContext = mContext;
        this.choiceItems = choiceItem;
    }

    private class ChoiceViewHolder extends RecyclerView.ViewHolder{
        ImageButton clearButton;
        TextView choiceText;

        ChoiceViewHolder(final View itemView) {
            super(itemView);

            clearButton = itemView.findViewById(R.id.clearButton);
            choiceText = itemView.findViewById(R.id.choiceText);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType){
            case CHOICE_VIEW_TYPE:
            default:
                View choiceItemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_choices, parent, false);
                return new ChoiceViewHolder(choiceItemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ChoiceViewHolder choiceHolder = (ChoiceViewHolder)holder;
        final ChoicesItem choicesItem = choiceItems.get(position);

        choiceHolder.choiceText.setText(choicesItem.getChoices());
        choiceHolder.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (choicesItem.server_choice_id != -1) {
                    APIHelper.delete_choice(mContext, choicesItem.server_choice_id, AppController.user_id, new Callback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            choiceItems.remove(position);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFail(String error) {
                        }
                    });
                }else{
                    choiceItems.remove(position);
                    notifyDataSetChanged();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return choiceItems.size();
    }
}
