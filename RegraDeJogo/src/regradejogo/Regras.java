
package regradejogo;

import Exceções.JogadaInvalidaException;
import Exceções.PosiçãoInvalidaException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import regradejogo.Tabuleiro.Inclinacao;

/**
 * Classe responsável por controlar as regras do jogo.
 */
public class Regras {
    private Tabuleiro tabuleiro;
    private int turnoAtual;
    private BoardChangedListener boardChangedListener;
    private static final int JOGADOR_UM = 1;
    private static final int JOGADOR_DOIS = 2;
    private int jogadorAtual;
    private int nPecasJogador1;
    private int nPecasJogador2;
    
    public Regras(){
        turnoAtual = 0;
        tabuleiro = new Tabuleiro();
        jogadorAtual = 1;
        nPecasJogador1 = 8;
        nPecasJogador2 = 8;
    }
    
    public Regras(String nomeArquivo) throws IOException{
        turnoAtual = 0;
        tabuleiro = new Tabuleiro(nomeArquivo);
        jogadorAtual = 1;
    }
    
    /**
     * Move uma peça do tabuleiro a partir das regras.
     * @param posInicial posição que a peça a ser movida está.
     * @param posFinal posição destino.
     */
    public void moverPeça(Posição posInicial, Posição posFinal){
        Peça peça = getPeça(posInicial);
        
        if(tabuleiro.posValida(posInicial)){
            throw new PosiçãoInvalidaException("Posição inválida, fora da dimensão do tabuleiro, Index:" + posInicial.toString() + ".");
        }
        
        if(tabuleiro.posValida(posFinal)){
            throw new PosiçãoInvalidaException("Posição inválida, fora da dimensão do tabuleiro, Index:" + posFinal.toString() + ".");
        }
        
        if(peça == null){
            throw new PosiçãoInvalidaException("Não existe nenhuma peça na posição " + posInicial.toString() + ".");
        }
        
        List<Jogada> jogadas = jogadasPossiveis(peça);
        
        Jogada jogada = getJogada(jogadas, posFinal);
        
        if(jogada == null){
            throw new JogadaInvalidaException("A posição " + posFinal.toString() + " não é uma jogada válida para esta peça.");
        }
        
        Peça peçaCapturada = jogada.getPeçaCapturada();
        
        tabuleiro.movePeça(peça, posFinal);
        
        if(peçaCapturada != null){
            removerPeça(peçaCapturada);
        }
        
        //Caso tenha chegado na borda.
        int borda = tabuleiro.bordaSupInf(jogada.getPosFinal());
        if(borda == peça.getTime()){
            viraDama(peça);
        }
        
        //Sempre que um movimento for bem sucedido, acionar o callback.
        if(boardChangedListener != null){
            boardChangedListener.onPieceMoved(posFinal, posFinal);
        }
    }
    
    /**
     * Dada uma lista de jogada, retorna a jogada que possui tal posição final.
     * @param jogadas
     * @return 
     */
    private Jogada getJogada(List<Jogada> jogadas, Posição posFinal){
        for(Jogada jogada : jogadas){
            if(jogada.getPosFinal().equals(posFinal)){
                return jogada;
            }
        }
        
        return null;
    }
    
    /**
     * Verifica se existem jogadas.
     * @param list lista com posições.
     * @return retorna true se existe(m) jogada(s), false caso contrário.
     */
    public boolean existemJogadas(List<Jogada> list){
        return list.isEmpty();
    }
    
    /**
     * Analisa todo o campo da peça(normal) e retorna as posições válidas.
     * @param peça peça a ter as jogadas analisadas.
     * @return retorna uma lista com todas as posições válidas para jogada.
     */
    public List<Jogada> jogadasPossiveis(Peça peça){
        //Se a peça não for do jogador atual.
        /*if(peça.getTime() != jogadorAtual){
            return null;
        }*/
        
        //Pega a posição da peça.
        Posição poisçãoPeça = tabuleiro.getPosição(peça);
        
        List<Posição> posicoes = new ArrayList<>();
        List<Jogada> jogadas = new ArrayList<>();
        
        /* Váriavel que ajuda a decidir se vai descer o subir na matriz.
         * Jogador 1 sempre fica em baixo.
         */
        int varJogador = (peça.getTime() == 1)? -1 : 1;

        //Analisa as 2 posições possíveis.
        Posição pos1 = new Posição(poisçãoPeça.getI() + varJogador, poisçãoPeça.getJ() - 1);
        Posição pos2 = new Posição(poisçãoPeça.getI() + varJogador, poisçãoPeça.getJ() + 1);

        //Verifica se as posições são válidas.
        if(jogadaValida(pos1, peça)){
            posicoes.add(pos1);
            jogadas.add(new Jogada(null, peça, poisçãoPeça, pos1));
        }
        
        if(jogadaValida(pos2, peça)){
            posicoes.add(pos2);
            jogadas.add(new Jogada(null, peça, poisçãoPeça, pos2));
        }
        
        //Pega todas as peças que a peça atual pode capturar.
        List<Jogada> capturas = capturasPossiveis(posicoes, peça);
        
        if(!capturas.isEmpty()){
            return capturas;
        }
        
        return jogadas;
    }
    
    /**
     * Dada uma peça e uma posição futura, verifica se forma uma jogada válida.
     * @param pos posição futura.
     * @param peca peça a ser movida.
     * @return true caso seja valida, false caso não.
     */
    private boolean jogadaValida(Posição pos, Peça peca){
        if(!tabuleiro.posValida(pos)){
            return false;
        }
        
        Peça peca2 = tabuleiro.getPeça(pos);
        
        //Se não tem nenhuma peça na posição, jogada é válida.
        if(peca2 == null){
            return true;
        }
        
        //Se a peça na posição for do seu time, jogada inválida.
        if(peca2.getTime() == peca.getTime()){
            return false;
        }
        
        //Caso a peça esteja na borda do tabuleiro.
        if((pos.getI() == tabuleiro.DIMEN - 1) || (pos.getI() == 0)){
            return false;
        }
        
        if((pos.getJ() == tabuleiro.DIMEN - 1) || (pos.getJ() == 0)){
            return false;
        }
        
        return true;
    }
    
    /**
     * Dada uma peça, retorna todas as peças que podem ser capturadas por ela.
     * @param peça peça a ter os movimentos analisado.
     * @return peças que podem ser capturadas.
     */
    private List<Peça> capturasPossiveis(Peça peça){
        //TODO
        return null;
    }
    
    /**
     * Dada uma lista de posições, retorna todas as capturas possíveis.
     * @param pos lista de posições a serem analisadas.
     * @param peca time da peça que quer caputrar.
     * @return jogadas.
     */
    public List<Jogada> capturasPossiveis(List<Posição> pos, Peça peca){
        List<Jogada> jogadas = new ArrayList<>();
        
        for(Posição posicao : pos){
            Peça p = tabuleiro.getPeça(posicao);
            
            //Se existe alguma peça nessa posição.
            if(p != null){
                //Se não for do time da peça que quer capturar, então é do time inimigo.
                if(p.getTime() != peca.getTime()){
                    Posição posFinal = podeComer(peca, p);
                    if(posFinal != null){
                        jogadas.add(new Jogada(p, peca, tabuleiro.getPosição(peca), posFinal));
                    }
                }
            }
        }
        
        return jogadas;
    }
    
    /**
     * Verifica se a peca1 pode comer a peca2.
     * @param peca1 Peça que irá tentar capturar.
     * @param peca2 Peça a ser capturada.
     * @return true caso possa, false caso contrário.
     */
    public Posição podeComer(Peça peca1, Peça peca2){
        //Pego a inclinação relativa entre duas pecas.
        int inclinacao = tabuleiro.inclinacaoRelativa(peca1, peca2);
        
        Posição posFinal;
        
        if(peca1.getTime() == JOGADOR_UM){
            Posição pos = tabuleiro.getPosição(peca2);
            if(inclinacao == Inclinacao.ESQUERDA){
                posFinal = new Posição(pos.getI() - 1, pos.getJ() - 1);
            }else{
                posFinal = new Posição(pos.getI() - 1, pos.getJ() + 1);
            }
            
            //Verifico se a posição é válida.
            if(!tabuleiro.posValida(posFinal)){
                return null;
            }
            
            if(tabuleiro.getPeça(posFinal) != null){
                return null;
            } 
        }else{
            Posição pos = tabuleiro.getPosição(peca2);
            if(inclinacao == Inclinacao.ESQUERDA){
                posFinal = new Posição(pos.getI() + 1, pos.getJ() - 1);
            }else{
                posFinal = new Posição(pos.getI() + 1, pos.getJ() + 1);
            }
            
            //Verifico se a posição é válida.
            if(!tabuleiro.posValida(posFinal)){
                return null;
            }
            
            if(tabuleiro.getPeça(posFinal) != null){
                return null;
            }
        }
        
        return posFinal;
    }
    
    /**
     * Função que verifica se o jogo terminou.
     * @return true se o jogo acabou, false caso não. 
     */
    private boolean verificaFimDeJogo(){
        //TODO
        boardChangedListener.onGameFinished(0);
        return false;
    }
    
    /**
     * Função que trata o incremento de turno. Verifica possível numero máximo
     * de turnos e etc.
     */
    private void incrementaTurno(){
        turnoAtual++;
    }
    
    /**
     * Transforma uma peça em dama.
     * @param peça peça que vai se transformar em dama
     */
    private void viraDama(Peça peça){
        peça.setDama(true);
    }
    
    /**
     * Remove peça do tabuleiro.
     * @param peça peça a ser removida.
     */
    private void removerPeça(Peça peça){
        if(peça == null){
            return;
        }
        
        tabuleiro.removePeça(peça);
        
        if(peça.getTime() == JOGADOR_UM){
            nPecasJogador1--;
        }else{
            nPecasJogador2--;
        }
        
        if(boardChangedListener != null){
            boardChangedListener.onPieceRemoved(tabuleiro.getPosição(peça));
        }
    }
    
    /**
     * retorna uma peça dada a posição.
     * @param pos
     * @return retorna uma peça caso exista nessa posição, null caso contrário.
     */
    public Peça getPeça(Posição pos){
        return tabuleiro.getPeça(pos);
    }
    
    public Tabuleiro getTabuleiro(){
        return tabuleiro;
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
