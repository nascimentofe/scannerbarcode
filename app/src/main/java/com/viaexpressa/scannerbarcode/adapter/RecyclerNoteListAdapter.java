package com.viaexpressa.scannerbarcode.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.viaexpressa.scannerbarcode.R;
import com.viaexpressa.scannerbarcode.classes.Nota;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecyclerNoteListAdapter extends RecyclerView.Adapter<RecyclerNoteListAdapter.MyViewHolder>{

    private Context mContext;
    private List<Nota> mData;
    RequestOptions requestOptions;

    public RecyclerNoteListAdapter(Context mContext, List<Nota> mData){
        this.mContext = mContext;
        this.mData = mData;

        requestOptions = new RequestOptions().centerCrop().placeholder(android.R.drawable.stat_notify_sync).error(android.R.drawable.stat_notify_error);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.item_view_nota, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ao clicar no item
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtNumNota.setText(holder.txtNumNota.getText() + mData.get(position).getNumNota());
        holder.txtDataHora.setText(holder.txtDataHora.getText() + formatarData(mData.get(position).getDataHora()));
        holder.txtCnpj.setText(holder.txtCnpj.getText() + mData.get(position).getCnpj());
        Glide.with(mContext).load(mData.get(position).getCaminhoImagemNota()).apply(requestOptions).into(holder.imgItemNota);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String formatarData(String data){
        //2020-05-30 12:15:00
        String[] dt = data.split(" ");
        List<String> lista = Arrays.asList(dt[0].split("-"));
        Collections.reverse(lista);
        return TextUtils.join("/", lista) + " ás " + dt[1];
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtNumNota, txtCnpj, txtDataHora;
        ImageView imgItemNota;
        LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNumNota = (TextView) itemView.findViewById(R.id.txtItemNotaNumNota);
            txtCnpj = (TextView) itemView.findViewById(R.id.txtItemNotaCnpjNota);
            txtDataHora = (TextView) itemView.findViewById(R.id.txtItemNotaHoraData);
            imgItemNota = (ImageView) itemView.findViewById(R.id.imgItemViewNota);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linLayoutItemNota);

        }
    }

}
