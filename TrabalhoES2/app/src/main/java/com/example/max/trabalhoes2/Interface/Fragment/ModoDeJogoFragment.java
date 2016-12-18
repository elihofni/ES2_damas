package com.example.max.trabalhoes2.Interface.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.trabalhoes2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModoDeJogoFragment extends Fragment {
    public static final int JOGADOR_VS_JOGADOR = 0;
    public static final int JOGADOR_VS_IA = 1;
    public static final int IA_VS_IA = 2;
    private int modoDeJogo;


    public ModoDeJogoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_modo_de_jogo, container, false);

        Bundle bundle = this.getArguments();

        modoDeJogo = bundle.getInt("modo");

        ImageView imageView = (ImageView) view.findViewById(R.id.imagem_fundo_modo_de_jogo);
        TextView textView = (TextView) view.findViewById(R.id.titulo_modo_de_jogo);

        switch (modoDeJogo){
            case JOGADOR_VS_JOGADOR:
                setResourcers(imageView, textView, R.drawable.jog_vs_jog, "Jogador x Jogador");
                break;
            case JOGADOR_VS_IA:
                setResourcers(imageView, textView, R.drawable.jog_vs_ia, "Jogador x IA");
                break;
            case IA_VS_IA:
                setResourcers(imageView, textView, R.drawable.ia_vs_ia, "IA x IA");
                break;
        }

        return view;
    }

    private void setResourcers(ImageView imageView, TextView textView, int image, String text){
        imageView.setImageResource(image);
        textView.setText(text);
    }

    public int getModoDeJogo() {
        return modoDeJogo;
    }
}
