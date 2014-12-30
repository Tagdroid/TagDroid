package com.tagdroid.android.Pages.StationDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tagdroid.android.R;

import java.util.List;

public class StationCardAdapter extends RecyclerView.Adapter<StationCardAdapter.StationViewHolder> {
    OnItemClickListener onItemClickListener;
    private List<StationCard> StationCardList;

    public StationCardAdapter(List<StationCard> StationCardList, Context context) {
        this.StationCardList = StationCardList;
    }
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.station_card, viewGroup, false);
        return new StationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StationViewHolder ViewHolder, int i) {
        StationCard station = StationCardList.get(i);
        ViewHolder.vDirection.setText(station.direction);
        ViewHolder.vHoraire1.setText(station.horaire1);
        ViewHolder.vHoraire2.setText(station.horaire2);
    }

    @Override
    public int getItemCount() {
        return StationCardList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class StationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView vDirection;
        private TextView vHoraire1;
        private TextView vHoraire2;

        public StationViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            vDirection = (TextView) view.findViewById(R.id.direction);
            vHoraire1 = (TextView) view.findViewById(R.id.horaire1);
            vHoraire2 = (TextView) view.findViewById(R.id.horaire2);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(view, getPosition());
        }
    }
}
