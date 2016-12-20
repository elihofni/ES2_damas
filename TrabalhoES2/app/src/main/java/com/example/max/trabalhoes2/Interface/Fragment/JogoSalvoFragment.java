package com.example.max.trabalhoes2.Interface.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.max.trabalhoes2.Interface.Activity.PartidaActivity;
import com.example.max.trabalhoes2.Interface.Adapter.JogoSalvoAdapter;
import com.example.max.trabalhoes2.Interface.Util.SalvarCarregarUtil;
import com.example.max.trabalhoes2.R;

import java.io.IOException;
import java.util.List;

public class JogoSalvoFragment extends Fragment {

    List<SalvarCarregarUtil.JogoSalvo> saves;


    public JogoSalvoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_jogo_salvo, container, false);

        SalvarCarregarUtil salvarCarregarUtil = new SalvarCarregarUtil(getContext());

        try {
            saves = salvarCarregarUtil.getAllSaves();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpRecyclerView(view);

        return view;
    }

    private void setUpRecyclerView(View view){
        RecyclerView recyclerViewDisciplinas = (RecyclerView) view.findViewById(R.id.recyclerView_saves);
        final LinearLayoutManager ll = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        JogoSalvoAdapter jogoSalvoAdapter = new JogoSalvoAdapter(getContext(), saves);
        jogoSalvoAdapter.setJogoSalvoOnClickListener(new JogoSalvoAdapter.JogoSalvoOnClickListener() {
            @Override
            public void onClickJogoSalvo(View view, int idx) {
                SalvarCarregarUtil.JogoSalvo save = saves.get(idx);
                Intent intent = new Intent(getContext(), PartidaActivity.class);
                intent.putExtras(toBundle(save));
                startActivity(intent);
            }
        });
        recyclerViewDisciplinas.setAdapter(jogoSalvoAdapter);
        recyclerViewDisciplinas.setLayoutManager(ll);
        recyclerViewDisciplinas.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDisciplinas.setHasFixedSize(true);
        recyclerViewDisciplinas.setNestedScrollingEnabled(true);
    }

    private Bundle toBundle(SalvarCarregarUtil.JogoSalvo jogoSalvo){
        Bundle bundle = new Bundle();

        bundle.putInt("peca1", jogoSalvo.getPeca1());
        bundle.putInt("peca2", jogoSalvo.getPeca2());
        bundle.putInt("modo", jogoSalvo.getModo());
        bundle.putInt("bot1", jogoSalvo.getBot1());
        bundle.putInt("bot2", jogoSalvo.getBot2());
        bundle.putString("arquivoJogoSalvo", jogoSalvo.getNome());

        return bundle;
    }

}
