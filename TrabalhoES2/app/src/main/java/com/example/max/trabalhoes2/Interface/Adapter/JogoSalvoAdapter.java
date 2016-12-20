package com.example.max.trabalhoes2.Interface.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.max.trabalhoes2.Interface.Fragment.ModoDeJogoFragment;
import com.example.max.trabalhoes2.Interface.Util.SalvarCarregarUtil.*;
import com.example.max.trabalhoes2.R;

import java.util.List;

/**
 * Created by Max on 20/12/2016.
 */

public class JogoSalvoAdapter extends RecyclerView.Adapter<JogoSalvoAdapter.JogoSalvoViewHolder>{
    private List<JogoSalvo> saves;
    private Context context;
    private JogoSalvoOnClickListener jogoSalvoOnClickListener;

    public JogoSalvoAdapter(Context context, List<JogoSalvo> saves){
        this.context = context;
        this.saves = saves;
    }

    @Override
    public JogoSalvoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_jogo_salvo, parent, false);
        JogoSalvoViewHolder holder = new JogoSalvoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final JogoSalvoViewHolder holder, final int position) {
        final JogoSalvo save = saves.get(position);
        String modo = "IA X IA";
        String turno = String.valueOf(save.getTurno());

        if(save.getModo() == ModoDeJogoFragment.JOGADOR_VS_JOGADOR){
            modo = "Jogador X Jogador";
        }else if(save.getModo() == ModoDeJogoFragment.JOGADOR_VS_IA){
            modo = "Jogador X IA";
        }

        holder.titulo.setText(save.getNome());
        holder.modo.setText(modo);
        holder.turno.setText(turno);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jogoSalvoOnClickListener != null){
                    jogoSalvoOnClickListener.onClickJogoSalvo(v, position);
                }
            }
        });
    }

    public void setJogoSalvoOnClickListener(JogoSalvoOnClickListener jogoSalvoOnClickListener) {
        this.jogoSalvoOnClickListener = jogoSalvoOnClickListener;
    }

    @Override
    public int getItemCount() {
        return this.saves != null? this.saves.size(): 0;
    }

    public interface JogoSalvoOnClickListener {
        public void onClickJogoSalvo(View view, int idx);
    }

    public static class JogoSalvoViewHolder extends RecyclerView.ViewHolder{
        public TextView titulo;
        public TextView modo;
        public TextView turno;

        public JogoSalvoViewHolder(View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.titulo_save);
            modo = (TextView) itemView.findViewById(R.id.modo_save);
            turno = (TextView) itemView.findViewById(R.id.turno_save);
        }
    }
}
