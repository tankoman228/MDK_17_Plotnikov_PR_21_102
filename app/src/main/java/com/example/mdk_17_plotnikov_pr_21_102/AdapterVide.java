package com.example.mdk_17_plotnikov_pr_21_102;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterVide extends RecyclerView.Adapter<AdapterVide.ViewHolder> {
    private List<VideoForAdapter> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, link, likes;

        public ViewHolder(View v) {
            super(v);
            //mTextView = (TextView) v.findViewById(R.id.text_view);

            name = (TextView) v.findViewById(R.id.tvItemName);
            link = (TextView) v.findViewById(R.id.tvItemLink);
            likes = (TextView) v.findViewById(R.id.tvLikes);
        }
    }

    public AdapterVide(List<VideoForAdapter> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public AdapterVide.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        VideoForAdapter vd = mDataset.get(position);

        holder.name.setText(vd.name); // первая часть - название
        holder.link.setText(vd.link); // вторая часть - ссылка
        holder.likes.setText("+" + vd.likes + "\t\t\t\t\t\t -" + vd.dislikes); // третья часть - количество лайков

        //holder.mTextView.setText(mDataset.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

