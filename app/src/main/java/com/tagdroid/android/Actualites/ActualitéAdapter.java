package com.tagdroid.android.Actualites;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.android.ImageLoader.ImageLoader;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.Actualites.Actualité;

import java.util.List;

public class ActualitéAdapter extends RecyclerView.Adapter<ActualitéAdapter.ActualitéViewHolder> {
    OnItemClickListener onItemClickListener;
    private List<Actualité> actualitéList;
    private ImageLoader imageLoader;

    public ActualitéAdapter(List<Actualité> actualitéList, Context context) {
        this.actualitéList = actualitéList;
        imageLoader = new ImageLoader(context);
    }
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ActualitéViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_actualites_card, viewGroup, false);
        return new ActualitéViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActualitéViewHolder actualitéViewHolder, int i) {
        Actualité actualité = actualitéList.get(i);
        actualitéViewHolder.vTitre.setText(actualité.titre);
        actualitéViewHolder.vDescr.setText(actualité.description);
        imageLoader.DisplayImage(actualité.image, actualitéViewHolder.vImage);
    }

    @Override
    public int getItemCount() {
        return actualitéList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ActualitéViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView vTitre;
        private TextView vDescr;
        private ImageView vImage;

        public ActualitéViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            vTitre = (TextView) view.findViewById(R.id.titre);
            vDescr = (TextView) view.findViewById(R.id.description);
            vImage = (ImageView)view.findViewById(R.id.image);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(view, getPosition());
        }
    }
}
