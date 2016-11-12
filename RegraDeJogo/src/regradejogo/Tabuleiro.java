package regradejogo;

public class Tabuleiro {
    private final int dimenção = 8;
    private Peça[][] tabuleiro;
    
    public Tabuleiro(){
        inicializaTabuleiro();
    }
    
    /** Inicializa tabuleiro com as peças de cada jogador no leyout clássico do jogo */
    private void inicializaTabuleiro(){
        tabuleiro = new Peça[dimenção][dimenção];
        
        //...
    }
    
    /** Retorna uma determinada peça a partir de uma posição na matriz, retorna
    * null se não houver nenhuma da posição.
    * @param posição linha
    * @return instancia de peça caso exista, null caso não.*/
    public Peça getPeça(Posição posição){
        //TODO;
        return null;
    }
    
    /**
     * Retorna a posição de uma peça no tabuleiro.
     * @param peça peça a ter a posição retornada.
     * @return posição da peça;
     */
    public Posição getPosição(Peça peça){
        return null;
    }
    
    /**
     * Muda uma peça de posição.
     * @param peça peça a ser movida.
     * @param posFinal posição final.
     */
    //Simplesmente move, sem regras. Quem vai cuidar das regras é a classe Regras.
    public void movePeça(Peça peça, Posição posFinal){
        //TODO
    }
    
    /**
     * Remove uma peça do tabuleiro.
     * @param peça peça a ser removida.
     */
    //Simplesmente remove, sem regras. Quem vai cuidar das regras é a classe Regras.
    public void removePeça(Peça peça){
        //TODO
    }
    
    @Override
    public String toString(){
        //TODO
        return null;
    }
}
