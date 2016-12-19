package com.example.max.trabalhoes2.Interface.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.CardView;
import android.widget.Button;

import com.example.max.trabalhoes2.Interface.Activity.EscolherTimeActivity;
import com.example.max.trabalhoes2.Interface.Activity.PartidaActivity;
import com.example.max.trabalhoes2.Interface.Adapter.SwipeMain;
import com.example.max.trabalhoes2.R;

import java.util.ArrayList;
import java.util.List;

public class TelaInicialFragment extends Fragment {

    public TelaInicialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tela_inicial, container, false);

        setViewPager(view);

        Button button = (Button) view.findViewById(R.id.jogar_button);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerMain);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EscolherTimeActivity.class);
                ModoDeJogoFragment frag1 = (ModoDeJogoFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());
                intent.putExtra("modo", frag1.getModoDeJogo());
                startActivity(intent);
            }
        });

        return view;
    }

    private void setViewPager(View view){
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerMain);
        final List<Fragment> fragments = getFragments();

        SwipeMain adapter = new SwipeMain(getActivity().getSupportFragmentManager(), fragments, fragments.size());
        viewPager.setAdapter(adapter);
    }

    private List<Fragment> getFragments(){
        List<Fragment> fragments = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putInt("modo", ModoDeJogoFragment.JOGADOR_VS_JOGADOR);

        Bundle bundle1 = new Bundle();
        bundle1.putInt("modo", ModoDeJogoFragment.JOGADOR_VS_IA);

        Bundle bundle2 = new Bundle();
        bundle2.putInt("modo", ModoDeJogoFragment.IA_VS_IA);

        ModoDeJogoFragment jogadorVsJogador = new ModoDeJogoFragment();
        jogadorVsJogador.setArguments(bundle);

        ModoDeJogoFragment jogadorVsIA = new ModoDeJogoFragment();
        jogadorVsIA.setArguments(bundle1);

        ModoDeJogoFragment iAVsIA = new ModoDeJogoFragment();
        iAVsIA.setArguments(bundle2);

        fragments.add(jogadorVsJogador);
        fragments.add(jogadorVsIA);
        fragments.add(iAVsIA);

        return fragments;
    }

}
