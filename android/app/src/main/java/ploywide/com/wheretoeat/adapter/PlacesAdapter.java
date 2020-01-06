package ploywide.com.wheretoeat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ploywide.com.wheretoeat.R;
import ploywide.com.wheretoeat.model.PlacesItem;

public class PlacesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PlacesItem> placesItems;
    private OnPlaceAddedListener mListener;

    private static final int PLACES_VIEW_TYPE = 333;


    public PlacesAdapter(Context mContext, List<PlacesItem> placesItems,OnPlaceAddedListener listener) {
        this.mContext = mContext;
        this.placesItems = placesItems;
        this.mListener = listener;
    }

    public PlacesAdapter(Context mContext, List<PlacesItem> placesItems) {
        this.mContext = mContext;
        this.placesItems = placesItems;
    }

    private class PlacesViewHolder extends RecyclerView.ViewHolder{
        TextView placeName,placeAddress;
        ImageButton addPlaceButton;

        PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            placeAddress = itemView.findViewById(R.id.placeAddress);
            addPlaceButton = itemView.findViewById(R.id.addPlaceButton);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch(viewType){
            case PLACES_VIEW_TYPE:
            default:
                View placeItemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_places, parent, false);
                return new PlacesViewHolder(placeItemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlacesViewHolder placesViewHolder = (PlacesViewHolder)holder;
        final PlacesItem placesItem = placesItems.get(position);

        placesViewHolder.placeName.setText(placesItem.getPlaceName());
        placesViewHolder.placeAddress.setText(placesItem.getPlaceAddress());
        placesViewHolder.addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPlaceAdded(placesItem.getPlaceName());
                Toast.makeText(mContext,"Added",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnPlaceAddedListener {
        void onPlaceAdded( String placeName );
    }

    @Override
    public int getItemCount() {
        return placesItems.size();
    }


}
