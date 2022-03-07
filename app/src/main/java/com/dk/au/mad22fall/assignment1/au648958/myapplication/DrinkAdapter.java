package com.dk.au.mad22fall.assignment1.au648958.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkViewHolder> {

    //The main structure of this Adapter is copied/inspired from/by Kasper Løvborg lecture L3 Android UI - Recycler Code

    public interface IDrinkItemClickedListener {
        void onDrinkClicked(int index);
    }

    private IDrinkItemClickedListener listener;

    private ArrayList<Drink> drinkList;

    public DrinkAdapter(IDrinkItemClickedListener listener) {
        this.listener = listener;
    }

    public void updateDrinkList(ArrayList<Drink> lists) {
        drinkList = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drinks_list_item, parent, false);
        DrinkViewHolder vh = new DrinkViewHolder(v, listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {
        holder.txtName.setText(drinkList.get(position).name);
        holder.txtCategory.setText(drinkList.get(position).category);
        holder.txtRating.setText(""+drinkList.get(position).rating);

        //toLowerCase: https://www.javatpoint.com/java-string-tolowercase
        //replace: https://www.javatpoint.com/java-string-replace
        String name = drinkList.get(position).name.toLowerCase().replace(' ','_');

        //Context inspired https://stackoverflow.com/questions/32136973/how-to-get-a-context-in-a-recycler-view-adapter
        Context context = holder.imgDrink.getContext();

        //Following inspired by https://stackoverflow.com/questions/56560897/how-to-get-drawable-from-string/56561415
        holder.imgDrink.setImageResource(context.getResources().getIdentifier(name,"drawable", context.getPackageName()));
    }

    @Override
    public int getItemCount(){
        return drinkList.size();
    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgDrink;
        TextView txtName, txtCategory, txtRating;

        IDrinkItemClickedListener listener;

        public DrinkViewHolder(@NonNull View itemView, IDrinkItemClickedListener drinkItemClickedListener){
            super(itemView);

            imgDrink = itemView.findViewById(R.id.imgDrink);
            txtName = itemView.findViewById(R.id.txtName);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtRating = itemView.findViewById(R.id.txtRating);

            listener = drinkItemClickedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            listener.onDrinkClicked(getAdapterPosition());
        }
    }
}
