package com.example.max.trabalhoes2.Interface.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.trabalhoes2.Interface.Activity.PartidaActivity;
import com.example.max.trabalhoes2.Interface.Layout.TabuleiroView;
import com.example.max.trabalhoes2.Interface.Layout.LayoutUtil;
import com.example.max.trabalhoes2.R;

import com.example.max.trabalhoes2.Interface.Layout.TabuleiroView.*;

import java.util.List;

import regradejogo.Bot;
import regradejogo.Bot.*;
import regradejogo.Humano;
import regradejogo.Jogada;
import regradejogo.Jogador;
import regradejogo.Posicao;
import regradejogo.Regras;
import regradejogo.Tabuleiro;

public class TabuleiroFragment extends Fragment {

    protected Regras regras;
    protected Jogador jogador;
    private Bot bot;
    private TabuleiroView tabuleiroView;
    private ImageView imageView;
    private LayoutUtil layoutUtil = new LayoutUtil();

    public TabuleiroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tabuleiro, container, false);

        tabuleiroView = (TabuleiroView) view.findViewById(R.id.tabuleiro);

        regras = new Regras();

        imageView = (ImageView) view.findViewById(R.id.imageView_jogadorAtual);

        Bundle bundle = getArguments();
        int modoDeJogo = bundle.getInt("modo");
        int peca1 = bundle.getInt("peca1");
        int peca2 = bundle.getInt("peca2");
        int dificuldade2 = bundle.getInt("bot2");

        imageView.setImageResource(layoutUtil.peoes[peca1]);

        tabuleiroView.setJogadorUmPeao(layoutUtil.peoes[peca1]);
        tabuleiroView.setJogadorDoisPeao(layoutUtil.peoes[peca2]);

        if(modoDeJogo == ModoDeJogoFragment.JOGADOR_VS_JOGADOR){
            jogador = new Humano(regras, peca1);
        }else if(modoDeJogo == ModoDeJogoFragment.JOGADOR_VS_IA){
            jogador = new Humano(regras, peca2);
            if(dificuldade2 == 2) {
                bot = new Bot(regras, Dificuldade.DIFICIL, peca2);
            }else if(dificuldade2 == 1){
                bot = new Bot(regras, Dificuldade.MEDIO, peca2);
            }else{
                bot = new Bot(regras, Dificuldade.FACIL, peca2);
            }

            /**
             * Timer pro bot jogar.
             * Pra não ficar logo depois que o jogador joga.
             */
            jogador.setJogadorListener(new Jogador.JogadorListener() {
                @Override
                public void jogadaFinalizada() {
                    DataBaseTask dataBaseTask = new DataBaseTask();

                    new CountDownTimer(500, 500) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            if(regras.getJogadorAtual() != Regras.JOGADOR_UM) {
                                ((PartidaActivity) getActivity()).trocarTituloToolbar("Pensando...");
                            }
                            tabuleiroView.setClickEnabled(false);
                            dataBaseTask.execute();
                        }
                    }.start();
                }
            });
        }

        carregaTabuleiro(tabuleiroView);

        regras.setOnBoardChangedListener(new Regras.BoardChangedListener() {
            @Override
            public void onPieceMoved(Posicao posicao, Posicao posicao1) {
                Pos pos = new Pos(posicao1.getI(), posicao1.getJ());
                Pos pos2 = new Pos(posicao.getI(), posicao.getJ());
                tabuleiroView.movePeca(pos);
                int img = regras.getJogadorAtual() == 1? layoutUtil.peoes[peca1] : layoutUtil.peoes[peca2];
                imageView.setImageResource(img);
                ((PartidaActivity) getActivity()).trocarTituloToolbar("");
            }

            @Override
            public void onGameFinished(int i, int i1) { //vencedor, motivo
                FrameLayout telaFimJogo = (FrameLayout) view.findViewById(R.id.TabuleiroFrag_TelaFimJogo);
                FrameLayout bgColor = (FrameLayout) view.findViewById(R.id.TabuleiroFrag_BackgroundColorFimJogo);
                TextView mensagem = (TextView) view.findViewById(R.id.TabuleiroFrag_MensagemFim);
                ImageView bandeira = (ImageView) view.findViewById(R.id.TabuleiroFrag_BandeiraFimJogo);
                ImageView peca = (ImageView) view.findViewById(R.id.TabuleiroFrag_PecaFimJogo);
                ImageView imgFim = (ImageView) view.findViewById(R.id.TabuleiroFrag_ImagemFim);

                jogador.getTime();
                if(i == Regras.JOGADOR_UM){ //jogador1 venceu
                    bgColor.setBackgroundColor(getResources().getColor(R.color.colorPlay));
                    mensagem.setText("VITORIA");
                    bandeira.setImageResource(layoutUtil.bandeiras[jogador.getTime()]); //Ó pai
                    peca.setImageResource(layoutUtil.peoes[jogador.getTime()]);
                    imgFim.setImageResource(layoutUtil.imgFim[1]); //Imagem de vitoria
                }else { //jogador2 venceu
                    bgColor.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mensagem.setText("DERROTA");
                    bandeira.setImageResource(layoutUtil.bandeiras[bot.getTime()]);
                    peca.setImageResource(layoutUtil.peoes[bot.getTime()]);
                    imgFim.setImageResource(layoutUtil.imgFim[0]); //Imagem de derrota
                }
                telaFimJogo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPieceRemoved(Posicao posicao) {
                tabuleiroView.removerPeca(posicao.getI(), posicao.getJ());
            }

            @Override
            public void virouDama(int i, int i1, int i2) {
                Pos pos = new Pos(i, i1);
                tabuleiroView.trocaImagemPeca(pos, i2);
            }
        });

        tabuleiroView.setOnClickTabuleiro(new TabuleiroView.OnClickTabuleiro() {
            @Override
            public void onClickPeca(Pos pos) {
                List<Posicao> posicoes = jogador.getPosPossiveis(new Posicao(pos.getI(), pos.getJ()));

                for(Posicao posicao : posicoes){
                    tabuleiroView.marcaPosicao(posicao.getI(), posicao.getJ());
                }
            }

            @Override
            public void onClickCasa(Pos posPeca, Pos posCasa) {
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
            Jogada jogada = bot.Jogar2();

            return jogada;
        }

        @Override
        protected void onPostExecute(Jogada jogada) {
            super.onPostExecute(jogada);
            tabuleiroView.setClickEnabled(true);
            if (jogada != null) {
                Pos posInicial = new Pos(jogada.getPosInicial().getI(), jogada.getPosInicial().getJ());
                Pos posFinal = new Pos(jogada.getPosFinal().getI(), jogada.getPosFinal().getJ());
                tabuleiroView.getPeca(posInicial).performClick();
                tabuleiroView.getCasa(posFinal).performClick();
            }
        }
    }
}