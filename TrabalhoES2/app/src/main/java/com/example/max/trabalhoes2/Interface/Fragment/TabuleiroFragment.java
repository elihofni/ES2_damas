package com.example.max.trabalhoes2.Interface.Fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.max.trabalhoes2.R;

import java.io.IOException;
import java.util.List;

import Excecoes.JogadaInvalidaException;
import Excecoes.PosicaoInvalidaException;
import domain.Jogador;
import livroandroid.lib.utils.FileUtils;
import regradejogo.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabuleiroFragment extends Fragment {

    private int pieceTeam1 = R.drawable.pt_peao;
    private int pieceTeam2 = R.drawable.democratas_peao;
    private int damaTime1 = R.drawable.pt_dama;
    private int damaTime2 = R.drawable.democratas_dama;
    private View lastSelectedPiece;
    private int boardSize = 8;
    private Jogador jogador;

    private Regras regras;

    private GridLayout board;

    private View[][] tabuleiroPecas = new View[8][8];

    public TabuleiroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tabuleiro, container, false);

        try {
            //System.out.println(FileUtils.readRawFileString(getContext(), R.raw.testedama1, "UTF-8"));
            //regras = new Regras(FileUtils.readRawFile(getContext(), R.raw.testedama1));
            regras = new Regras();
            regras.setOnBoardChangedListener(new Regras.BoardChangedListener() {
                @Override
                public void onPieceMoved(Posicao posicao, Posicao posicao1) {

                }

                @Override
                public void onGameFinished(int i, int i1) {

                }

                @Override
                public void onPieceRemoved(Posicao posicao) {
                    int i = posicao.getI();
                    int j = posicao.getJ();

                    View view = tabuleiroPecas[i][j];

                    board.removeView(view);
                }

                @Override
                public void virouDama(int i, int i1) {

                }
            });

            jogador = new Jogador(regras);
        } catch (Exception e) {
            e.printStackTrace();
        }

        createTable(view);

        return view;
    }

    //Instancia classe que pega dimensão da tela.
    protected DisplayMetrics getDisplay(){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    //Pega a largura da tela.
    protected int getTableWidth(){
        return getDisplay().widthPixels;
    }

    //Cria o tabuleiro do jogo.
    protected void createTable(View view){
        int tableSize = getTableWidth();

        //Infla o layout que vai contar o tabuleiro.
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.lienarLayout_main);
        //Parametros do gridLayout.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (tableSize, tableSize);
        //Cria o gridLayout que vai representar o tabuleiro.
        board = new GridLayout(getContext());
        //Seta os parametros do gridLayout.
        board.setLayoutParams(params);
        //board.setBackgroundColor(getResources().getColor(R.color.fundo_tabuleiro_amarelo));
        board.setBackgroundResource(R.drawable.base_g);
        board.setRowCount(8);
        board.setColumnCount(8);

        setaFundoTabuleiro();

        setaPecasJogadores(board);

        linearLayout.addView(board, 1);
    }

    private void setaFundoTabuleiro(){
        //Para cada coordenada do tabuleiro, cria uma view.
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                View tile = createTile(chooseTileColor(i, j));
                addViewToGrid(board, tile, i, j);
            }
        }
    }

    private void setaPecasJogadores(GridLayout board){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Peca peca = regras.getPeca(new Posicao(i, j));
                if(peca != null){
                    View view = criaPeca(peca.getTime(), peca.isDama());
                    tabuleiroPecas[i][j] = view;
                    addViewToGrid(board, view, i, j);
                }
            }
        }
    }

    //Escolhe o fundo de cada tile do tabuleiro
    protected int chooseTileColor(int i, int j){
        if(i%2 == 0){
            if(j%2 == 0) {
                return R.drawable.casa_transparente;
            }else{
                return R.drawable.casa;
            }
        }else if(i%2 != 0){
            if(j%2 == 0){
                return R.drawable.casa;
            }else{
                return R.drawable.casa_transparente;
            }
        }

        return R.drawable.base_p;
    }

    protected int tileHeight(){
        return tileWidth();
    }

    protected int tileWidth(){
        return getTableWidth() / boardSize;
    }

    //Cria a tile, que representa uma coordenada do tabuleiro.
    protected FrameLayout createTile(int imageId){
        FrameLayout tile = new FrameLayout(getContext());
        ImageView imageView = new ImageView(getContext());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(tileWidth(), tileHeight());

        tile.setLayoutParams(params);
        imageView.setLayoutParams(params);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(imageId);
        imageView.setAlpha((float) 0.8);
        tile.addView(imageView);

        tile.setOnClickListener(onClickTile());

        return tile;
    }

    //Cria peça dos jogadores.
    protected FrameLayout criaPeca(int team, boolean isDama){
        int imageId = 0;
        if(team == 1){
            imageId = (isDama)? damaTime1 : pieceTeam1;
        }

        if(team == 2){
            imageId = (isDama)? damaTime2 : pieceTeam2;
        }

        FrameLayout frameLayout = new FrameLayout(getContext());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(tileWidth() - 20, tileHeight() - 20, Gravity.CENTER);
        params.setMargins(10, 10, 10, 10);

        ImageView piece = new ImageView(getContext());
        piece.setLayoutParams(params);
        piece.setImageResource(imageId);
        piece.setScaleType(ImageView.ScaleType.CENTER_CROP);
        piece.setAlpha((float) 0.85);

        frameLayout.setOnClickListener(onClickPiece());

        frameLayout.addView(piece);

        return frameLayout;
    }

    //Click na peça.
    protected View.OnClickListener onClickPiece(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastSelectedPiece != null) {
                    Peca ultimaPeca = regras.getPeca(getPosicaoView(lastSelectedPiece));
                    desmarcaJogadas(ultimaPeca.isDama() ? regras.jogadasPossiveisDama(ultimaPeca) : regras.jogadasPossiveis(ultimaPeca));
                }
                lastSelectedPiece = v;
                Peca peca = regras.getPeca(getPosicaoView(v));
                if(peca != null) {
                    List<Jogada> jogadas = peca.isDama() ? regras.jogadasPossiveisDama(peca) : regras.jogadasPossiveis(peca);
                    marcaJogadas(jogadas);
                }
            }
        };
    }

    //Click na tile.
    protected View.OnClickListener onClickTile(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FrameLayout view = (FrameLayout) board.getChildAt(10);
                //ImageView image = (ImageView) view.getChildAt(0);

                //image.setImageResource(R.drawable.casa_transparente);

                //System.out.println(board.indexOfChild(v));

                if(lastSelectedPiece != null){
                    Posicao posicao = getPosicaoTile(v);
                    Posicao posicao1 = getPosicaoView(lastSelectedPiece);

                    Peca ultimaPeca = regras.getPeca(getPosicaoView(lastSelectedPiece));
                    desmarcaJogadas(ultimaPeca.isDama() ? regras.jogadasPossiveisDama(ultimaPeca) : regras.jogadasPossiveis(ultimaPeca));
                    try {
                        jogador.realizarJogada(posicao1.getI(), posicao1.getJ(), posicao.getI(), posicao.getJ());

                        tabuleiroPecas[posicao.getI()][posicao.getJ()] = tabuleiroPecas[posicao1.getI()][posicao1.getJ()];
                        tabuleiroPecas[posicao1.getI()][posicao1.getJ()] = null;

                        animMovePiece(lastSelectedPiece, v);
                        lastSelectedPiece = null;
                    }catch(JogadaInvalidaException e){

                    }
                }
            }
        };
    }

    private void marcaJogadas(List<Jogada> jogadas){
        for(Jogada jogada : jogadas){
            Posicao posicao = jogada.getPosFinal();
            int pos = (posicao.getI()*8)+posicao.getJ();
            FrameLayout view = (FrameLayout) board.getChildAt(pos);
            ImageView image = (ImageView) view.getChildAt(0);
            image.setImageResource(R.drawable.teste);
        }
    }

    private void desmarcaJogadas(List<Jogada> jogadas){
        for(Jogada jogada : jogadas){
            Posicao posicao = jogada.getPosFinal();
            int pos = (posicao.getI()*8)+posicao.getJ();
            FrameLayout view = (FrameLayout) board.getChildAt(pos);
            ImageView image = (ImageView) view.getChildAt(0);
            image.setImageResource(chooseTileColor(posicao.getI(), posicao.getJ()));
        }
    }

    private Posicao getPosicaoTile(View tile){
        int index = board.indexOfChild(tile);

        int linha = index / 8;
        int coluna = index % 8;

        return new Posicao(linha, coluna);
    }

    //Animação de movimento.
    protected void animMovePiece(View piece, View tile){
        float finalX = tile.getX();
        float finalY = tile.getY();

        ObjectAnimator animX = ObjectAnimator.ofFloat(piece, "x", finalX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(piece, "y", finalY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animX, animY);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    //Adiciona view ao gridLayout.
    protected void addViewToGrid(GridLayout gridLayout, View view, int i, int j){
        GridLayout.Spec indexI = GridLayout.spec(i);
        GridLayout.Spec indexJ = GridLayout.spec(j);

        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(indexI, indexJ);

        gridLayout.addView(view, gridParam);
    }

    private Posicao getPosicaoView(View view){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                View aux = tabuleiroPecas[i][j];
                if(aux != null){
                    if(aux.equals(view)){
                        return new Posicao(i, j);
                    }
                }
            }
        }

        return null;
    }
}
