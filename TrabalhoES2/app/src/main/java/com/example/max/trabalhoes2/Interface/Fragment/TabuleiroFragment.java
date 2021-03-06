package com.example.max.trabalhoes2.Interface.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.max.trabalhoes2.Interface.Activity.PartidaActivity;
import com.example.max.trabalhoes2.Interface.Layout.TabuleiroView;
import com.example.max.trabalhoes2.Interface.Util.SalvarCarregarUtil;
import com.example.max.trabalhoes2.Interface.Util.SalvarCarregarUtil.*;
import com.example.max.trabalhoes2.R;

import com.example.max.trabalhoes2.Interface.Layout.TabuleiroView.*;

import java.io.IOException;
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
    private SalvarCarregarUtil salvarCarregarUtil;

    public TabuleiroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tabuleiro, container, false);

        tabuleiroView = (TabuleiroView) view.findViewById(R.id.tabuleiro);

        salvarCarregarUtil = new SalvarCarregarUtil(getContext());

        imageView = (ImageView) view.findViewById(R.id.imageView_jogadorAtual);

        Bundle bundle = getArguments();
        int modoDeJogo = bundle.getInt("modo");
        int peca1 = bundle.getInt("peca1");
        int peca2 = bundle.getInt("peca2");
        int dificuldade2 = bundle.getInt("bot2");
        String nomeArquivo = bundle.getString("arquivoJogoSalvo");
        String nomeSave = bundle.getString("save");

        if(nomeArquivo != null){
            try {
                SalvarCarregarUtil.JogoSalvo salvo = salvarCarregarUtil.carregarJogo(nomeArquivo);
                regras = new Regras(salvo.getInputStream());
                regras.setJogadorAtual(salvo.getJogadorAtual());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            regras = new Regras();
        }

        imageView.setImageResource(peca1);

        tabuleiroView.setJogadorUmPeao(peca1);
        tabuleiroView.setJogadorDoisPeao(peca2);

        /*try {
            JogoSalvo jogoSalvo = salvarCarregarUtil.carregarJogo("jogo1");
            regras = new Regras(jogoSalvo.getInputStream());
            System.out.println(jogoSalvo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        if(modoDeJogo == ModoDeJogoFragment.JOGADOR_VS_JOGADOR){
            jogador = new Humano(regras, Regras.JOGADOR_UM);
            jogador.setJogadorListener(new Jogador.JogadorListener() {
                @Override
                public void jogadaFinalizada() {
                    try {
                        salvarCarregarUtil.salvarJogo(nomeSave, jogador.getTurno(), regras.getJogadorAtual(), peca1,
                                peca2, jogador.getStringTabuleiro(), modoDeJogo, 0, dificuldade2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else if(modoDeJogo == ModoDeJogoFragment.JOGADOR_VS_IA){
            jogador = new Humano(regras, Regras.JOGADOR_UM);
            if(dificuldade2 == 2) {
                bot = new Bot(regras, Dificuldade.DIFICIL, Regras.JOGADOR_DOIS);
            }else if(dificuldade2 == 1){
                bot = new Bot(regras, Dificuldade.MEDIO, Regras.JOGADOR_DOIS);
            }else{
                bot = new Bot(regras, Dificuldade.FACIL, Regras.JOGADOR_DOIS);
            }

            /**
             * Timer pro bot jogar.
             * Pra não ficar logo depois que o jogador joga.
             */
            jogador.setJogadorListener(new Jogador.JogadorListener() {
                @Override
                public void jogadaFinalizada() {
                    DataBaseTask dataBaseTask = new DataBaseTask();

                    try {
                        salvarCarregarUtil.salvarJogo(nomeSave, jogador.getTurno(), regras.getJogadorAtual(), peca1,
                                peca2, jogador.getStringTabuleiro(), modoDeJogo, 0, dificuldade2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
                int img = regras.getJogadorAtual() == 1? peca1 : peca2;
                imageView.setImageResource(img);
                ((PartidaActivity) getActivity()).trocarTituloToolbar("");
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