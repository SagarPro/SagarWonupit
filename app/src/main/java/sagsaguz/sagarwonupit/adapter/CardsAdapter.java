package sagsaguz.sagarwonupit.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sagsaguz.sagarwonupit.R;
import sagsaguz.sagarwonupit.model.Card;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {

    private List<Card> cardsList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, time;
        public ImageView clock;
        public LinearLayout llAction1, llAction2;
        public ConstraintLayout clCard;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            time = view.findViewById(R.id.time);
            clock = view.findViewById(R.id.clock);
            llAction1 = view.findViewById(R.id.llAction1);
            llAction2 = view.findViewById(R.id.llAction2);
            clCard = view.findViewById(R.id.clCard);
        }
    }


    public CardsAdapter(Context context, List<Card> cardsList) {
        this.context = context;
        this.cardsList = cardsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Card card = cardsList.get(position);
        holder.title.setText(card.getTitle());
        holder.description.setText(card.getDescription());
        holder.time.setText(card.getTime());

        if (card.getAction().equals("Action Taken")){
            holder.llAction1.setVisibility(View.GONE);
            holder.llAction2.setVisibility(View.VISIBLE);
        } else {
            holder.llAction1.setVisibility(View.VISIBLE);
            holder.llAction2.setVisibility(View.GONE);
        }

        if (card.getTime().contains("IST")){
            holder.time.setTextColor(context.getResources().getColor(R.color.red));
            holder.clock.setColorFilter(ContextCompat.getColor(context, R.color.red));
        } else if(card.getTime().contains("EST")){
            holder.time.setTextColor(context.getResources().getColor(R.color.white));
            holder.clock.setColorFilter(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.time.setTextColor(context.getResources().getColor(R.color.silver));
            holder.clock.setColorFilter(ContextCompat.getColor(context, R.color.silver));
        }

        LayerDrawable layerDrawable = (LayerDrawable) context.getResources()
                .getDrawable(R.drawable.gradient);
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable
                .findDrawableByLayerId(R.id.bg_gradient);
        gradientDrawable.setColor(context.getResources().getColor(card.getColor()));

        holder.clCard.setBackground(context.getResources().getDrawable(R.drawable.gradient));

    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

}
