package com.example.max.trabalhoes2.Interface.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.max.trabalhoes2.Interface.Activity.PartidaActivity;
import com.example.max.trabalhoes2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EscolherTimeFragment extends Fragment {

    private GridLayout gridJogador1;
    private GridLayout gridJogador2;
    private boolean carregouGrid1 = false;
    private boolean carregouGrid2 = false;
    private final int maximoTimes = 5;
    private int index1 = -1;
    private int index2 = -1;
    private int dificuldadeBot1 = 1;
    private int dificuldadeBot2 = 1;

    private int pecas[] = {R.drawable.pen_peao, R.drawable.pp_peao, R.drawable.democratas_peao,
    R.drawable.psb_peao, R.drawable.pt_peao_2};

    public EscolherTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_escolher_time, container, false);

        gridJogador1 = (GridLayout) view.findViewById(R.id.gridTime1);
        gridJogador2 = (GridLayout) view.findViewById(R.id.gridTime2);

        Button buttonJogar = (Button) view.findViewById(R.id.button_jogador_escolherTime);

        Bundle bundle = getArguments();

        int modo = bundle.getInt("modo");

        EditText editText = (EditText) view.findViewById(R.id.nomeSave);

        setTextViews(modo, view);

        gridJogador1.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(!carregouGrid1) {
                    for (int i = 0; i < maximoTimes; i++) {
                        gridJogador1.addView(criaPeca(pecas[i], gridJogador1, 1));
                    }
                    carregouGrid1 = true;
                }
            }
        });

        gridJogador1.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(!carregouGrid2) {
                    for (int i = 0; i < maximoTimes; i++) {
                        gridJogador2.addView(criaPeca(pecas[i], gridJogador2, 2));
                    }
                    carregouGrid2 = true;
                }
            }
        });

        buttonJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index2 == -1 || index1 == -1){
                    return;
                }

                if(editText.getText().toString().equals("")){
                    return;
                }

                Intent intent = new Intent(getContext(), PartidaActivity.class);
                intent.putExtra("peca1", pecas[index1]);
                intent.putExtra("peca2", pecas[index2]);
                intent.putExtra("modo", getArguments().getInt("modo"));
                intent.putExtra("bot1", dificuldadeBot1);
                intent.putExtra("bot2", dificuldadeBot2);
                intent.putExtra("save", editText.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }

    private FrameLayout criaPeca(int imageId, GridLayout gridLayout, int time){
        int tamanho = larguraCasa();

        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(tamanho, tamanho, Gravity.CENTER);
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(params2);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(tamanho - 10, tamanho - 10, Gravity.CENTER);
        params.setMargins(1, 1, 1, 1);

        ImageView piece = new ImageView(getContext());
        piece.setLayoutParams(params);
        piece.setImageResource(imageId);
        piece.setScaleType(ImageView.ScaleType.CENTER_CROP);
        piece.setAlpha((float) 0.85);

        frameLayout.setOnClickListener(onClickPeca(gridLayout, time));

        frameLayout.addView(piece);

        return frameLayout;
    }

    private int larguraCasa(){
        return gridJogador1.getMeasuredWidth() / 3;
    }

    private View.OnClickListener onClickPeca(GridLayout gridLayout, int time){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desmarcadaTodas(gridLayout);
                marcaPosicao(gridLayout.indexOfChild(v), gridLayout, time);
            }
        };
    }

    private void marcaPosicao(int pos, GridLayout gridLayout, int time){
        FrameLayout view = (FrameLayout) gridLayout.getChildAt(pos);
        if(view != null) {
            view.setBackgroundResource(R.color.colorPrimary);
            if(time == 1){
                index1 = pos;
            }else{
                index2 = pos;
            }
        }
    }

    private void desmarcaPosicao(View view){
        view.setBackgroundColor(Color.TRANSPARENT);
    }

    private void desmarcadaTodas(GridLayout gridLayout){
        for(int i = 0; i < maximoTimes; i++) {
            View view = gridLayout.getChildAt(i);
            if(view != null) {
                desmarcaPosicao(gridLayout.getChildAt(i));
            }
        }
    }

    private void setTextViews(int modo, View view){
        TextView textView1 = (TextView) view.findViewById(R.id.jogador1);
        TextView textView2 = (TextView) view.findViewById(R.id.jogador2);

        String vetor[] = {"FACIL", "MEDIO", "DIFICIL"};

        //Gambiarra, se der tempo melhoro.
        EscolherDificuldadeDialog escolherDificuldadeDialog = new EscolherDificuldadeDialog();
        EscolherDificuldadeDialog escolherDificuldadeDialog2 = new EscolherDificuldadeDialog();

        escolherDificuldadeDialog.setOnDificuldadeSelected(new EscolherDificuldadeDialog.OnDificuldadeSelected() {
            @Override
            public void dificuldadeSelected(int dificuldade) {
                textView2.setText("BOT " + vetor[dificuldade]);
                dificuldadeBot2 = dificuldade;
            }
        });

        escolherDificuldadeDialog2.setOnDificuldadeSelected(new EscolherDificuldadeDialog.OnDificuldadeSelected() {
            @Override
            public void dificuldadeSelected(int dificuldade) {
                textView1.setText("BOT " + vetor[dificuldade]);
                dificuldadeBot1 = dificuldade;
            }
        });


        if(modo == ModoDeJogoFragment.JOGADOR_VS_IA){
            textView2.setText("BOT MEDIO");

            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    escolherDificuldadeDialog.show(getFragmentManager(), "teste");
                }
            });
        }else if(modo == ModoDeJogoFragment.IA_VS_IA){
            textView2.setText("BOT MEDIO");
            textView1.setText("BOT MEDIO");

            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    escolherDificuldadeDialog.show(getFragmentManager(), "teste");
                }
            });

            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    escolherDificuldadeDialog2.show(getFragmentManager(), "teste");
                }
            });
        }
    }

}
