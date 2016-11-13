
package regradejogo;

import java.util.List;

/**
 * Classe responsável por controlar as regras do jogo.
 */
public class Regras {
    private Tabuleiro tabuleiro;
    private int turnoAtual;
    private BoardChangedListener boardChangedListener;
    private final int JOGADOR_UM = 1;
    private final int JOGADOR_DOIS = 2;
    
    public Regras(){
        turnoAtual = 0;
        tabuleiro = new Tabuleiro();
    }
    
    /**
     * Move uma peça do tabuleiro a partir das regras.
     * @param peça peça a ser movida.
     * @param posFinal posição destino
     */
    public void moverPeça(Peça peça, Posição posFinal){
        //Fazer lógica, verificar se virou dama e etc...
        
        List<Posição> posições = jogadasPossiveis(peça);
        
        if(!existemPosições(posições)){
            //levantar exceção.
        }
        
        tabuleiro.movePeça(peça, posFinal);
        //Sempre que um movimento for bem sucedido, acionar o callback.
        boardChangedListener.onPieceMoved(posFinal, posFinal);
    }
    
    /**
     * Verifica se existem jogadas.
     * @param list lista com posições.
     * @return retorna true se existe(m) jogada(s), false caso contrário.
     */
    public boolean existemPosições(List<Posição> list){
        return list.isEmpty();
    }
    
    /**
     * Analisa todo o campo da peça e retorna as posições válidas.
     * @param peça peça a ter as jogadas analisadas.
     * @return retorna uma lista com todas as posições válidas para jogada.
     */
    private List<Posição> jogadasPossiveis(Peça peça){
        //TODO
        return null;
    }
    
    /**
     * Dada uma peça, retorna todas as peças que podem ser capturadas por ela.
     * @param peça peça a ter os movimentos analisado.
     * @return peças que podem ser capturadas.
     */
    private List<Peça> capturasPossíveis(Peça peça){
        //TODO
        return null;
    }
    
    /**
     * Função que verifica se o jogo terminou.
     * @return true se o jogo acabou, false caso não. 
     */
    private boolean verificaFimDeJogo(){
        //TODO
        boardChangedListener.onGameFinished(JOGADOR_UM);
        return false;
    }
    
    /**
     * Função que trata o incremento de turno. Verifica possível numero máximo
     * de turnos e etc.
     */
    private void incrementaTurno(){
        //TODO
    }
    
    /**
     * Transforma uma peça em dama.
     * @param peça peça que vai se transformar em dama
     */
    private void viraDama(Peça peça){
        //TODO
    }
    
    /**
     * Remove peça do tabuleiro.
     * @param peça peça a ser removida.
     */
    private void removerPeça(Peça peça){
        //TODO
        tabuleiro.removePeça(peça);
        boardChangedListener.onPieceRemoved(tabuleiro.getPosição(peça));
    }
    
    /**
     * retorna uma peça dada a posição.
     * @param pos
     * @return retorna uma peça caso exista nessa posição, null caso contrário.
     */
    public Peça getPeça(Posição pos){
        return tabuleiro.getPeça(pos);
    }
    
    /**
     * Interface de callback.
     */
    public interface BoardChangedListener{
        public void onPieceMoved(Posição posInicial, Posição posFinal);
        public void onGameFinished(int vencedor);
        public void onPieceRemoved(Posição posição);
    }
    
    public void setOnBoardChangedListener(BoardChangedListener boardChangedListener){
        this.boardChangedListener = boardChangedListener;
    }
}
