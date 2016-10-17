package com.example.max.trabalhoes2.Interface.Fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.example.max.trabalhoes2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabuleiroFragment extends Fragment {

    protected int pieceTeam1 = R.drawable.pt_peao;
    protected int pieceTeam2 = R.drawable.democratas_peao;

    protected View lastSelectedPiece;

    protected int boardSize = 8;


    public TabuleiroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tabuleiro, container, false);

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
        GridLayout board = new GridLayout(getContext());
        //Seta os parametros do gridLayout.
        board.setLayoutParams(params);
        //board.setBackgroundColor(getResources().getColor(R.color.fundo_tabuleiro_amarelo));
        board.setBackgroundResource(R.drawable.base_g);
        board.setRowCount(8);
        board.setColumnCount(8);

        //Para cada coordenada do tabuleiro, cria uma view.
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                View tile = createTile(chooseTileColor(i, j));
                addViewToGrid(board, tile, i, j);
            }
        }

        setPlayer1Pieces(board);
        setPlayer2Pieces(board);

        linearLayout.addView(board, 1);
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
    protected FrameLayout createPiece(int team){
        int imageId = (team == 1)? pieceTeam1 : pieceTeam2;

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

    //Posiciona as peças nas posições iniciais.
    protected void setPlayer1Pieces(GridLayout board){
        for(int i = 0; i < (boardSize / 2) - 1; i++){
            for(int j = 0; j < boardSize; j++){
                if(i%2 == 0){
                    if(j%2 != 0){
                        View view = createPiece(1);
                        addViewToGrid(board, view, i, j);
                    }
                }else if(i%2 != 0){
                    if(j%2 == 0){
                        View view = createPiece(1);
                        addViewToGrid(board, view, i, j);
                    }
                }
            }
        }
    }

    protected void setPlayer2Pieces(GridLayout board){
        for(int i = 5; i < 8; i++){
            for(int j = 0; j < boardSize; j++){
                if(i%2 == 0){
                    if(j%2 != 0){
                        View view = createPiece(2);
                        addViewToGrid(board, view, i, j);
                    }
                }else if(i%2 != 0){
                    if(j%2 == 0){
                        View view = createPiece(2);
                        addViewToGrid(board, view, i, j);
                    }
                }
            }
        }
    }

    //Click na peça.
    protected View.OnClickListener onClickPiece(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPiece = v;
            }
        };
    }

    //Click na tile.
    protected View.OnClickListener onClickTile(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastSelectedPiece != null){
                    animMovePiece(lastSelectedPiece, v);
                    lastSelectedPiece = null;
                }
            }
        };
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
}
