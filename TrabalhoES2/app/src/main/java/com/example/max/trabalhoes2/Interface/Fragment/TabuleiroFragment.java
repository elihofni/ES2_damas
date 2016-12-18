package com.example.max.trabalhoes2.Interface.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.max.trabalhoes2.Interface.Layout.TabuleiroView;
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

    private Regras regras;
    private Jogador jogador;
    private Bot bot;

    public TabuleiroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tabuleiro, container, false);

        TabuleiroView tabuleiroView = (TabuleiroView) view.findViewById(R.id.tabuleiro);

        regras = new Regras();

        Bundle bundle = getArguments();
        int modoDeJogo = bundle.getInt("modo");
        int peca1 = bundle.getInt("peca1");
        int peca2 = bundle.getInt("peca2");

        tabuleiroView.setJogadorUmPeao(peca1);
        tabuleiroView.setJogadorDoisPeao(peca2);

        if(modoDeJogo == ModoDeJogoFragment.JOGADOR_VS_JOGADOR){
            jogador = new Humano(regras, Regras.JOGADOR_UM);
        }else if(modoDeJogo == ModoDeJogoFragment.JOGADOR_VS_IA){
            jogador = new Humano(regras, Regras.JOGADOR_UM);
            bot = new Bot(regras, Dificuldade.MEDIO, Regras.JOGADOR_DOIS);
        }


        carregaTabuleiro(tabuleiroView);

        jogador.setJogadorListener(new Jogador.JogadorListener() {
            @Override
            public void jogadaFinalizada() {
                Jogada jogada = bot.Jogar2();
                if(jogada != null) {
                    Pos posInicial = new Pos(jogada.getPosInicial().getI(), jogada.getPosInicial().getJ());
                    Pos posFinal = new Pos(jogada.getPosFinal().getI(), jogada.getPosFinal().getJ());
                    tabuleiroView.getPeca(posInicial).performClick();
                    tabuleiroView.getCasa(posFinal).performClick();
                }
            }
        });

        regras.setOnBoardChangedListener(new Regras.BoardChangedListener() {
            @Override
            public void onPieceMoved(Posicao posicao, Posicao posicao1) {
                Pos pos = new Pos(posicao1.getI(), posicao1.getJ());
                Pos pos2 = new Pos(posicao.getI(), posicao.getJ());
                tabuleiroView.movePeca(pos);
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void carregaTabuleiro(TabuleiroView tabuleiroView){
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
}