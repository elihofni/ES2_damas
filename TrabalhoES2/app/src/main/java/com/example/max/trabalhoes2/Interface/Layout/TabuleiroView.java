package com.example.max.trabalhoes2.Interface.Layout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.max.trabalhoes2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * View personalizada que representa um tabuleiro de jogo de damas.
 * IMPORTANTE
 * Essa classe não sabe nada de regras. Ela somente move e desenha, peças e tabuleiro.
 */
public class TabuleiroView extends GridLayout {
    /**
     * Matriz que vai controlar as peças no tabuleiro da interface.
     * null corresponde a casa sem nenhuma pecça.
     */
    private View[][] tabuleiroPecas;

    /**
     * Fundo das casas do tabuleiro.
     */
    private int casa_transparente = R.drawable.casa_transparente;
    private int casa = R.drawable.casa;

    /**
     * Imagem das peças dos jogadores.
     */
    private int jogadorUmPeao = R.drawable.pt_peao_2;
    private int jogadorDoisPeao = R.drawable.psdb_peao;
    private int damaJogadorUm = R.drawable.pt_dama_2;
    private int damaJogadorDois = R.drawable.psdb_dama;

    /**
     * Constantes estáticas representando os jogadores.
     */
    public static final int JOGADOR_UM = 1;
    public static final int JOGADOR_DOIS = 2;

    /**
     * Variáveis que auxiliam no controle da movimentação das peças nas peças.
     */
    private View ultimaPecaSelecionada;
    private List<View> casasMarcadas;

    /**
     * Dimensão do tabuleiro.
     * Padrão 8x8.
     */
    public static final int DIMENSAO_TABULEIRO = 8;

    /**
     * Animação de movimento das peças.
     */
    private int duracaoAnimMovimento = 500;
    private Animation removerPeca = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
    private Animation virarDama = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

    /**
     * Interface de Callback.
     */
    private OnClickTabuleiro onClickTabuleiro;

    /**
     * Construtor padrão quando a instancia da classe é feita via código.
     * @param context contexto na qual a classe é instanciada.
     */
    public TabuleiroView(Context context) {
        super(context);
        tabuleiroPecas = new View[DIMENSAO_TABULEIRO][DIMENSAO_TABULEIRO];
        casasMarcadas = new ArrayList<>();
        criaTabuleiro();
    }

    /**
     * Construtor padrão quando a classe é instancia via XML.
     * @param context contexto na qual é view pertence.
     * @param attrs atributos do XML.
     */
    public TabuleiroView(Context context, AttributeSet attrs) {
        super(context, attrs);
        tabuleiroPecas = new View[DIMENSAO_TABULEIRO][DIMENSAO_TABULEIRO];
        casasMarcadas = new ArrayList<>();
        criaTabuleiro();

    }

    public TabuleiroView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tabuleiroPecas = new View[DIMENSAO_TABULEIRO][DIMENSAO_TABULEIRO];
        casasMarcadas = new ArrayList<>();
        criaTabuleiro();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    /**
     * Construtor para versões Android acima da 5.0. Permite definir estilos personalizados para a view.
     */
    public TabuleiroView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        tabuleiroPecas = new View[DIMENSAO_TABULEIRO][DIMENSAO_TABULEIRO];
        casasMarcadas = new ArrayList<>();
        criaTabuleiro();
    }

    @Override
    public void onMeasure(int width, int height){
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY));
    }

    public void setCasaTransparente(int casa_transparente) {
        this.casa_transparente = casa_transparente;
    }

    public void setCasa(int casa) {
        this.casa = casa;
    }

    public void setJogadorUmPeao(int jogadorUmPeao) {
        this.jogadorUmPeao = jogadorUmPeao;
    }

    public void setJogadorDoisPeao(int jogadorDoisPeao) {
        this.jogadorDoisPeao = jogadorDoisPeao;
    }

    public void setDamaJogadorUm(int damaJogadorUm) {
        this.damaJogadorUm = damaJogadorUm;
    }

    public void setDamaJogadorDois(int damaJogadorDois) {
        this.damaJogadorDois = damaJogadorDois;
    }

    protected int larguraTabuleiro() {
        return LayoutUtil.getDisplay((Activity) getContext()).widthPixels;
    }

    protected int larguraCasa() {
        return larguraTabuleiro() / DIMENSAO_TABULEIRO;
    }

    public void setDuracaoAnimMovimento(int duracaoAnimMovimento) {
        this.duracaoAnimMovimento = duracaoAnimMovimento;
    }

    public void setRemoverPeca(Animation removerPeca) {
        this.removerPeca = removerPeca;
    }

    private void criaTabuleiro(){
        int tableSize = larguraTabuleiro();
        //Parametros do gridLayout.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (tableSize, tableSize);
        //Seta os parametros do gridLayout.
        this.setLayoutParams(params);
        this.setBackgroundResource(R.drawable.base_g);
        this.setRowCount(DIMENSAO_TABULEIRO);
        this.setColumnCount(DIMENSAO_TABULEIRO);

        setaFundoTabuleiro();
    }

    /**
     * Remove uma peca do tabuleiro.
     * @param i coordenada i.
     * @param j coordenada j.
     */
    public void removerPeca(int i, int j){
        if(!posValida(i, j)){
            throw new IllegalArgumentException("Posicao [" + String.valueOf(i) + ";" + String.valueOf(j) + "] inválida.");
        }

        View view = tabuleiroPecas[i][j];

        removerPeca.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                tabuleiroPecas[i][j] = null;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(removerPeca);
    }

    /**
     * Cria o fundo do tabuleiro.
     */
    private void setaFundoTabuleiro(){
        //Para cada coordenada do tabuleiro, cria uma view.
        for(int i = 0; i < DIMENSAO_TABULEIRO; i++){
            for(int j = 0; j < DIMENSAO_TABULEIRO; j++){
                View tile = criaCasa(escolheCorLadrilho(i, j), larguraCasa());
                addViewToGrid(tile, i, j);
            }
        }
    }

    /**
     * Coloca uma peca em um dada posicao.
     * @param i coordenada i da posicao.
     * @param j coordenada j da posicao.
     * @param jogador jogador na qual a peca pertence.
     */
    public void setPeca(int i, int j, int jogador, boolean dama){
        View peca = criaPeca(jogador, dama);

        if(!posValida(i, j)){
            throw new IllegalArgumentException("Posicao [" + String.valueOf(i) + ";" + String.valueOf(j) + "] inválida.");
        }

        tabuleiroPecas[i][j] = peca;

        addViewToGrid(peca, i, j);
    }

    /**
     * Escolhe o fundo do ladrilho de acordo com a posiçao.
     * @param i coordenada i do ladrilho.
     * @param j coordenada j do ladrilho.
     * @return id da imagem.
     */
    private int escolheCorLadrilho(int i, int j){
        if(i%2 == 0){
            if(j%2 == 0) {
                return casa_transparente;
            }else{
                return casa;
            }
        }else if(i%2 != 0){
            if(j%2 == 0){
                return casa;
            }else{
                return casa_transparente;
            }
        }

        return R.drawable.base_p;
    }

    /**
     * Dado um ladrilho, retorna sua posição na matriz.
     * @param ladrilho ladrilho a ser analisado.
     * @return instancia de Posicao.
     */
    private Pos getPosicaoLadrilho(View ladrilho){
        int index = this.indexOfChild(ladrilho);

        int linha = index / 8;
        int coluna = index % 8;

        return new Pos(linha, coluna);
    }

    /**
     * Busca posicao de uma peca no tabuleiro.
     * @param pecaView view, representando uma peca, a ser procurada.
     * @return instancia de posicao caso haja uma, null caso contrário.
     */
    private Pos getPosicaoPeca(View pecaView){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                View aux = tabuleiroPecas[i][j];
                if(aux != null){
                    if(aux.equals(pecaView)){
                        return new Pos(i, j);
                    }
                }
            }
        }

        return null;
    }

    /**
     * Adiciona uma instancia de view no tabuleiro.
     * @param view view a ser adicionada.
     * @param i coordenada i da posicao a ser adicionada.
     * @param j coordenada j da posicao a ser adicionada.
     */
    private void addViewToGrid(View view, int i, int j){
        GridLayout.Spec indexI = GridLayout.spec(i);
        GridLayout.Spec indexJ = GridLayout.spec(j);

        GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(indexI, indexJ);

        this.addView(view, gridParam);
    }

    /**
     * Marca uma casa.
     * @param i coordenada i da posicao.
     * @param j coordenada j da posicao.
     */
    public void marcaPosicao(int i, int j){
        int pos = (i * 8) + j;
        FrameLayout view = (FrameLayout) this.getChildAt(pos);
        ImageView image = (ImageView) view.getChildAt(0);
        image.setImageResource(R.drawable.teste);

        casasMarcadas.add(view);
    }

    /**
     * Desmarca uma casa.
     * @param i coordenada i da posicao.
     * @param j coordenada j da posicao.
     */
    public void desmarcaPosicao(int i, int j){
        int pos = (i * 8) + j;
        FrameLayout view = (FrameLayout) this.getChildAt(pos);
        ImageView image = (ImageView) view.getChildAt(0);
        image.setImageResource(escolheCorLadrilho(i, j));
    }

    public void desmarcaTodasCasas(){
        for(View view : casasMarcadas){
            Pos posViewTabuleiro = getPosicaoLadrilho(view);
            desmarcaPosicao(posViewTabuleiro.getI(), posViewTabuleiro.getJ());
        }

        casasMarcadas.clear();
    }

    /**
     * Cria uma casa, fundo do tabuleiro.
     * @param imageId imagem que vai ficar no fundo.
     * @param largura largura da casa.
     * @return retorna uma instancia de FrameLayout representando uma casa.
     */
    private FrameLayout criaCasa(int imageId, int largura){
        FrameLayout tile = new FrameLayout(getContext());
        ImageView imageView = new ImageView(getContext());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(largura, largura);

        tile.setLayoutParams(params);
        imageView.setLayoutParams(params);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(imageId);
        imageView.setAlpha((float) 0.8);
        tile.addView(imageView);

        tile.setOnClickListener(onClickCasa());

        return tile;
    }

    /**
     * Cria uma peça de jogo.
     * @param time time da peça.
     * @param dama se é dama ou não.
     * @return retorna uma instancia de FrameLayout representando a peça.
     */
    private FrameLayout criaPeca(int time, boolean dama){
        int imageId = 0;
        if(time == 1){
            imageId = (dama)? damaJogadorUm : jogadorUmPeao;
        }

        if(time == 2){
            imageId = (dama)? damaJogadorDois : jogadorDoisPeao;
        }

        int tamanho = larguraCasa();

        FrameLayout frameLayout = new FrameLayout(getContext());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(tamanho - 20, tamanho - 20, Gravity.CENTER);
        params.setMargins(10, 10, 10, 10);

        ImageView piece = new ImageView(getContext());
        piece.setLayoutParams(params);
        piece.setImageResource(imageId);
        piece.setScaleType(ImageView.ScaleType.CENTER_CROP);
        piece.setAlpha((float) 0.85);

        frameLayout.setOnClickListener(onClickPeca());

        frameLayout.addView(piece);

        return frameLayout;
    }

    /**
     * Lida com o click em uma peca.
     * @return
     */
    private View.OnClickListener onClickPeca(){
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                ultimaPecaSelecionada = v;

                desmarcaTodasCasas();

                if(onClickTabuleiro != null){
                    Pos pos = getPosicaoPeca(v);
                    onClickTabuleiro.onClickPeca(pos);
                }
            }
        };
    }

    /**
     * Lida com o click em uma casa.
     * Um click só é considerado válido quando a casa está marcada como uma possível jogada de alguma peça.
     * @return
     */
    private View.OnClickListener onClickCasa(){
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(casasMarcadas.indexOf(v) == -1){
                    return;
                }

                if(ultimaPecaSelecionada == null){
                    return;
                }

                if(onClickTabuleiro != null){
                    Pos pos = getPosicaoLadrilho(v);
                    Pos posPeca = getPosicaoPeca(ultimaPecaSelecionada);
                    onClickTabuleiro.onClickCasa(posPeca, pos);
                }

                ultimaPecaSelecionada = null;
                desmarcaTodasCasas();
            }
        };
    }

    /**
     * Move uma peca para um determinada posição.
     * @param posFim posicao de destino da peca.
     */
    public void movePeca(Pos posFim){
        if(ultimaPecaSelecionada == null){
            throw new NullPointerException("Nenhuma peca selecionada.");
        }

        Pos posInicial = getPosicaoPeca(ultimaPecaSelecionada);

        if(posInicial == null){
            throw new IllegalArgumentException("Peca " + ultimaPecaSelecionada.toString() + " fornecida nao está no tabuleiro.");
        }

        tabuleiroPecas[posFim.getI()][posFim.getJ()] = tabuleiroPecas[posInicial.getI()][posInicial.getJ()];
        tabuleiroPecas[posInicial.getI()][posInicial.getJ()] = null;

        View casa = getChildAt((posFim.getI()*8) + posFim.getJ());

        animMovimentoPeca(ultimaPecaSelecionada, casa);

        ultimaPecaSelecionada = null;
    }

    public void trocaImagemPeca(Pos pos, int time){
        FrameLayout view = (FrameLayout) tabuleiroPecas[pos.getI()][pos.getJ()];
        ImageView imageView = (ImageView) view.getChildAt(0);

        int img = (time == JOGADOR_UM)? damaJogadorUm : damaJogadorDois;

        virarDama.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setImageResource(img);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(virarDama);
    }

    /**
     * Animação do movimento da peça.
     * @param peca peça a ser movida.
     * @param casa casa de destino da peça.
     */
    private void animMovimentoPeca(View peca, View casa){
        float finalX = casa.getX();
        float finalY = casa.getY();

        ObjectAnimator animX = ObjectAnimator.ofFloat(peca, "x", finalX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(peca, "y", finalY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animX, animY);
        animatorSet.setDuration(duracaoAnimMovimento);
        animatorSet.start();
    }

    private boolean posValida(int i, int j){
        int max  = DIMENSAO_TABULEIRO - 1;
        return !((i < 0 || i > max) || (j < 0 || j > max));
    }

    public void setOnClickTabuleiro(OnClickTabuleiro onClickTabuleiro) {
        this.onClickTabuleiro = onClickTabuleiro;
    }

    public interface OnClickTabuleiro{
        /**
         * Click numa peça.
         * @param pos posicao da peça selecionada.
         */
        void onClickPeca(Pos pos);

        /**
         * Click numa casa.
         * Função só é chamada quando pode haver uma jogada, ou seja, existe uma peça selecionada e uma casa foi selecionada.
         * @param posPeca peça a ser movida.
         * @param posCasa casa de destino.
         */
        void onClickCasa(Pos posPeca, Pos posCasa);
    }

    /**
     * Claase que encapsula uma coordenada na matriz de peças.
     */
    public static class Pos{
        private int i;
        private int j;

        public Pos(){

        }

        public Pos(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public int getJ() {
            return j;
        }

        public void setJ(int j) {
            this.j = j;
        }
    }
}
