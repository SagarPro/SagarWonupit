package sagsaguz.sagarwonupit.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sagsaguz.sagarwonupit.R;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder> {

    private Context context;
    private List<Integer> colorsList;
    private List<Float> opacityList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView thumbnailView;

        public MyViewHolder(View view) {
            super(view);
            thumbnailView = view.findViewById(R.id.thumbnailView);
        }
    }

    public ThumbnailAdapter(Context context, List<Integer> colorsList, List<Float> opacityList) {
        this.context = context;
        this.colorsList = colorsList;
        this.opacityList = opacityList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnail_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.thumbnailView.setAlpha(opacityList.get(position));
        holder.thumbnailView.setBackgroundColor(context.getResources().getColor(colorsList.get(position)));
    }

    @Override
    public int getItemCount() {
        return colorsList.size();
    }
}
