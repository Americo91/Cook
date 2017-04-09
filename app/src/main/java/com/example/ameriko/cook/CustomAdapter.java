package com.example.ameriko.cook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ameriko on 09/04/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>  {


    private ArrayList<Ricetta> ricette;
    private Context context;

    public CustomAdapter(Context context, ArrayList<Ricetta> ricette) {
        this.context=context;
        this.ricette=ricette;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        ViewHolder v = new ViewHolder(itemView);
        return v;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.nome.setText(ricette.get(position).getNome());

        String imageUrl = "http://amerchri.altervista.org/Image/"+ricette.get(position).getImg();

        Picasso.with(context).load(imageUrl).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RicettaActivity.class);
                intent.putExtra("ricetta", ricette.get(position).getNome());
                intent.putExtra("img",ricette.get(position).getImg());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ricette.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public TextView nome;

        public ViewHolder(View itemView) {
            super(itemView);
            nome = (TextView) itemView.findViewById(R.id.nomeRicetta);
            image = (ImageView) itemView.findViewById(R.id.imgRicetta);
        }
    }
}
