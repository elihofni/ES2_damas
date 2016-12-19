package com.example.max.trabalhoes2.Interface.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.max.trabalhoes2.Interface.Activity.PartidaActivity;
import com.example.max.trabalhoes2.Interface.Layout.TabuleiroView;
import com.example.max.trabalhoes2.R;

import java.util.List;

import regradejogo.Bot;
import regradejogo.Jogada;
import regradejogo.Jogador;
import regradejogo.Posicao;
import regradejogo.Regras;
import regradejogo.Tabuleiro;

/**
 * A simple {@link Fragment} subclass.
 */
public class BotVsBotFragment extends Fragment {

    private Bot jogador2;
    private Bot jogador;
    private Regras regras;
    private Button jogarBot;
    private TabuleiroView tabuleiroView;
    private  Bot.Dificuldade[] vet = {Bot.Dificuldade.FACIL, Bot.Dificuldade.MEDIO, Bot.Dificuldade.DIFICIL};

    public BotVsBotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tabuleiro, container, false);

        tabuleiroView = (TabuleiroView) view.findViewById(R.id.tabuleiro);
        //tabuleiroView.setClickEnabled(false);

        Bundle bundle = getArguments();

        int peca1 = bundle.getInt("peca1");
        int peca2 = bundle.getInt("peca2");
        int dificuldadeBot1 = bundle.getInt("bot1");
        int dificuldadeBot2 = bundle.getInt("bot2");

        tabuleiroView.setJogadorUmPeao(peca1);
        tabuleiroView.setJogadorDoisPeao(peca2);

        tabuleiroView.setClickEnabled(false);

        regras = new Regras();
        jogador = new Bot(regras, vet[dificuldadeBot1], Regras.JOGADOR_UM);
        jogador2 = new Bot(regras, vet[dificuldadeBot2], Regras.JOGADOR_DOIS);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_jogadorAtual);

        imageView.setImageResource(peca1);

        jogador.setJogadorListener(new Jogador.JogadorListener() {
            @Override
            public void jogadaFinalizada() {
                ((PartidaActivity) getActivity()).trocarTituloToolbar("");
                imageView.setImageResource(peca2);
            }
        });

        jogador2.setJogadorListener(new Jogador.JogadorListener() {
            @Override
            public void jogadaFinalizada() {
                ((PartidaActivity) getActivity()).trocarTituloToolbar("");
                imageView.setImageResource(peca1);
            }
        });

        carregaTabuleiro(tabuleiroView);

        jogarBot = (Button) view.findViewById(R.id.realizarJogadaBot);
        jogarBot.setVisibility(View.VISIBLE);

        jogarBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(500, 500) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        ((PartidaActivity) getActivity()).trocarTituloToolbar("Bot " + String.valueOf(regras.getJogadorAtual()) + " Pensando...");
                        DataBaseTask dataBaseTask = new DataBaseTask();
                        dataBaseTask.execute();
                    }
                }.start();
            }
        });

        regras.setOnBoardChangedListener(new Regras.BoardChangedListener() {
            @Override
            public void onPieceMoved(Posicao posicao, Posicao posicao1) {
                TabuleiroView.Pos pos = new TabuleiroView.Pos(posicao1.getI(), posicao1.getJ());
                TabuleiroView.Pos pos2 = new TabuleiroView.Pos(posicao.getI(), posicao.getJ());
                tabuleiroView.movePeca(pos);
                /*int img = regras.getJogadorAtual() == 1? peca1 : peca2;
                imageView.setImageResource(img);
                ((PartidaActivity) getActivity()).trocarTituloToolbar("");*/
            }

            @Override
            public void onGameFinished(int i, int i1) {
                //TODO
            }

            @Override
            public void onPieceRemoved(Posicao posicao) {
                tabuleiroView.removerPeca(posicao.getI(), posicao.getJ());
            }

            @Override
            public void virouDama(int i, int i1, int i2) {
                TabuleiroView.Pos pos = new TabuleiroView.Pos(i, i1);
                tabuleiroView.trocaImagemPeca(pos, i2);
            }
        });

        tabuleiroView.setOnClickTabuleiro(new TabuleiroView.OnClickTabuleiro() {
            @Override
            public void onClickPeca(TabuleiroView.Pos pos) {
                List<Posicao> posicoes = jogador.getPosPossiveis(new Posicao(pos.getI(), pos.getJ()));

                for(Posicao posicao : posicoes){
                    tabuleiroView.marcaPosicao(posicao.getI(), posicao.getJ());
                }
            }

            @Override
            public void onClickCasa(TabuleiroView.Pos posPeca, TabuleiroView.Pos posCasa) {
                try{
                    jogador.realizarJogada(posPeca.getI(), posPeca.getJ(), posCasa.getI(), posCasa.getJ());
                    //((PartidaActivity) getActivity()).trocarTituloToolbar("");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public void carregaTabuleiro(TabuleiroView tabuleiroView){
        for(int i = 0; i < TabuleiroView.DIMENSAO_TABULEIRO; i++){
            for(int j = 0; j < TabuleiroView.DIMENSAO_TABULEIRO; j++){
                int pos = jogador.consultarPosicao(i, j);

                if(pos == Tabuleiro.DAMA_TIME1){
                    tabuleiroView.setPeca(i, j, 1, true);
                }

                if(pos == Tabuleiro.DAMA_TIME2){
                    tabuleiroView.setPeca(i, j, 2, true);
                }

                if(pos == Tabuleiro.PECA_TIME1){
                    tabuleiroView.setPeca(i, j, 1, false);
                }

                if(pos == Tabuleiro.PECA_TIME2){
                    tabuleiroView.setPeca(i, j, 2, false);
                }
            }
        }
    }

    private class DataBaseTask extends AsyncTask<Integer, Void, Jogada> {

        @Override
        protected Jogada doInBackground(Integer... params) {
            Jogada jogada = regras.getJogadorAtual() == 1? jogador.Jogar2() : jogador2.Jogar2();

            return jogada;
        }

        @Override
        protected void onPostExecute(Jogada jogada) {
            super.onPostExecute(jogada);
            if (jogada != null) {
                tabuleiroView.setClickEnabled(true);
                TabuleiroView.Pos posInicial = new TabuleiroView.Pos(jogada.getPosInicial().getI(), jogada.getPosInicial().getJ());
                TabuleiroView.Pos posFinal = new TabuleiroView.Pos(jogada.getPosFinal().getI(), jogada.getPosFinal().getJ());
                tabuleiroView.getPeca(posInicial).performClick();
                tabuleiroView.getCasa(posFinal).performClick();
                tabuleiroView.setClickEnabled(false);
            }
        }
    }

}
