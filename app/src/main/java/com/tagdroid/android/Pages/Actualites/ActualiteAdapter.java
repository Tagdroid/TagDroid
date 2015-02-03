package com.tagdroid.android.Pages.Actualites;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tagdroid.android.ImageLoader.ImageLoader;
import com.tagdroid.android.R;
import com.tagdroid.tagapi.Actualites.Actualite;

import java.util.List;

public class ActualiteAdapter extends RecyclerView.Adapter<ActualiteAdapter.ActualiteViewHolder> {
    OnItemClickListener onItemClickListener;
    private List<Actualite> actualiteList;
    private ImageLoader imageLoader;

    public ActualiteAdapter(List<Actualite> actualiteList, Context context) {
        this.actualiteList = actualiteList;
        imageLoader = new ImageLoader(context);
    }
    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ActualiteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.actualites_card, viewGroup, false);
        return new ActualiteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActualiteViewHolder actualiteViewHolder, int i) {
        Actualite actualite = actualiteList.get(i);
        actualiteViewHolder.vTitre.setText(actualite.titre);
        imageLoader.DisplayImage(actualite.image, actualiteViewHolder.vImage);
    }

    @Override
    public int getItemCount() {
        return actualiteList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ActualiteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView vTitre;
        private ImageView vImage;

        public ActualiteViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            vTitre = (TextView) view.findViewById(R.id.titre);
            vImage = (ImageView)view.findViewById(R.id.image);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(view, getPosition());
        }
    }
}
