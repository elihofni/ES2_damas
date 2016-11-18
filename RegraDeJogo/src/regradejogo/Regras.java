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
    private int jogadorAtual;//TODO
    private int nPecasJogador1;
    private int nPecasJogador2;
    private boolean jogoFinalizado;
    
    public Regras(){
        turnoAtual = 0;
        tabuleiro = new Tabuleiro();
        jogadorAtual = JOGADOR_UM;
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
        
        //Teste de sanidade. Impossível chegar uma posição inválida a partir da interface.
        if(!tabuleiro.posValida(posInicial)){
            throw new PosiçãoInvalidaException("Posição inválida, fora da dimensão do tabuleiro, Index:" + posInicial.toString() + ".");
        }
        
        //Teste de sanidade. É impossível(quase) chegar uma possição final inválida.
        if(!tabuleiro.posValida(posFinal)){
            throw new PosiçãoInvalidaException("Posição inválida, fora da dimensão do tabuleiro, Index:" + posFinal.toString() + ".");
        }
        
        //Teste de sanidade.
        if(peça == null){
            throw new PosiçãoInvalidaException("Não existe nenhuma peça na posição " + posInicial.toString() + ".");
        }
        
        //Se for dama, a função que lida com a jogada é diferente.
        //TODO: Função que retorna as jogadas da dama.
        List<Jogada> jogadas = peça.isDama()? jogadasPossiveis(peça) : jogadasPossiveis(peça);
        
        //Verifica se a posição final da jogada está contida nas jogadas possíveis.
        Jogada jogada = getJogada(jogadas, posFinal);
        
        //Se não tiver contida, jogada é inválida.
        if(jogada == null){
            throw new JogadaInvalidaException("A posição " + posFinal.toString() + " não é uma jogada válida para esta peça " + tabuleiro.getPosição(peça) + ".");
        }
        
        tabuleiro.movePeça(peça, posFinal);
        
        //Verifica se houve captura na jogada.
        if(jogada.houveCaptura()){
            removerPeça(jogada.getPeçaCapturada());
        }
        
        //Caso tenha chegado na borda.
        int borda = tabuleiro.bordaSupInf(jogada.getPosFinal());
        if(borda == peça.getTime()){
            viraDama(peça);
        }
        
        incrementaTurno();
        verificaFimDeJogo();
        
        //Sempre que um movimento for bem sucedido, acionar o callback.
        if(boardChangedListener != null){
            boardChangedListener.onPieceMoved(posFinal, posFinal);
        }
    }
    
    /**
     * Dada uma lista de jogada, retorna a jogada que possui tal posição final.
     * @param jogadas Lista de jogadas a serem analisadas.
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
        
        /**
         * A lista posições guarda todas as posições na quais são oriundas de jogadas "normais",
         * apenas andar pra frente.
         */
        List<Posição> posicoes = new ArrayList<>();
        /**
         * A lista jogadas é fruto da analise das posições e interpretação do que pode acontecer das jogadas
         * da lista posições.
         */
        List<Jogada> jogadas = new ArrayList<>();
        
        /**
         * Váriavel que ajuda a decidir se vai descer o subir na matriz.
         * Jogador 1 sempre fica em baixo.
         */
        int varJogador = (peça.getTime() == 1)? -1 : 1;

        //Analisa as 2 posições de jogadas "normais" possíveis.
        Posição pos1 = new Posição(poisçãoPeça.getI() + varJogador, poisçãoPeça.getJ() - 1);
        Posição pos2 = new Posição(poisçãoPeça.getI() + varJogador, poisçãoPeça.getJ() + 1);
        
        //Posições que podem conter inimigos e que estão no sentido contrário ao "normal" da peça.
        Posição pos3 = new Posição(poisçãoPeça.getI() - varJogador, poisçãoPeça.getJ() - 1);
        Posição pos4 = new Posição(poisçãoPeça.getI() - varJogador, poisçãoPeça.getJ() + 1);

        //Verifica se as posições são válidas.
        if(jogadaValida(pos1, peça)){
            posicoes.add(pos1);
            //Candidata a ser uma jogada "normal". Pode não ser.
            jogadas.add(new Jogada(null, peça, poisçãoPeça, pos1));
        }
        
        if(jogadaValida(pos2, peça)){
            posicoes.add(pos2);
            //Candidata a ser uma jogada "normal". Pode não ser.
            jogadas.add(new Jogada(null, peça, poisçãoPeça, pos2));
        }
        
        /**
         * Jogadas não-normais. Captura para "trás". No mínimo vai ser uma captura, logo não posso adiciona-la
         * como uma candidata.
         */
        if(jogadaValida(pos3, peça)){
            posicoes.add(pos3);
        }
        
        if(jogadaValida(pos4, peça)){
            posicoes.add(pos4);
        }
        
        /**
         * Nesse jogo de damas, captura é prioridade. Se existe, pelo menos uma, jogada na qual seja uma captura
         * ela será retornada.
         * Aquela jogada ali em cima que era condidata a ser uma jogada normal, pode cair aqui dentro de capturas
         * mas não tem problema.
         */
        List<Jogada> capturas = capturasPossiveis(posicoes, peça);
        
        //Caso exista alguma jogada de captura.
        if(!capturas.isEmpty()){
            return capturas;
        }
        
        /**
         * Se chegou até aqui quer dizer que as duas jogadas candidatas ali em cima eram efetivamente
         * jogadas normais.
         */
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
        if((pos.getI() == Tabuleiro.DIMEN - 1) || (pos.getI() == 0)){
            return false;
        }
        
        if((pos.getJ() == Tabuleiro.DIMEN - 1) || (pos.getJ() == 0)){
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
        //Variável que auxilia no cálculo da posição final.
        int altura = tabuleiro.ehMaisAlta(peca1, peca2)? -1 : 1;
        
        Posição posFinal;
        
        if(peca1.getTime() == JOGADOR_UM){
            Posição pos = tabuleiro.getPosição(peca2);
            if(inclinacao == Inclinacao.ESQUERDA){
                posFinal = new Posição(pos.getI() + altura, pos.getJ() - 1);
            }else{
                posFinal = new Posição(pos.getI() + altura, pos.getJ() + 1);
            }
            
            //Verifico se a posição é válida.
            if(!tabuleiro.posValida(posFinal)){
                return null;
            }
            //Verifico se existe alguma peça ness aposição.
            if(tabuleiro.getPeça(posFinal) != null){
                return null;
            } 
        }else{
            Posição pos = tabuleiro.getPosição(peca2);
            if(inclinacao == Inclinacao.ESQUERDA){
                posFinal = new Posição(pos.getI() + altura, pos.getJ() - 1);
            }else{
                posFinal = new Posição(pos.getI() + altura, pos.getJ() + 1);
            }
            
            //Verifico se a posição é válida.
            if(!tabuleiro.posValida(posFinal)){
                return null;
            }
            //Verifico se existe alguma peça ness aposição.
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
    private void verificaFimDeJogo(){
        //TODO: Verifica se ambos jogadores ainda tem peças com jogadas possíveis.
        
        //Checa fim de jogo por término de peças.
        if(nPecasJogador1 == 0){
            if(boardChangedListener != null){
                boardChangedListener.onGameFinished(JOGADOR_DOIS, FimDeJogo.TERMINO_DE_PEÇAS);
                jogoFinalizado = true;
                return;
            }
        }
        
        if(nPecasJogador2 == 0){
            if(boardChangedListener != null){
                boardChangedListener.onGameFinished(JOGADOR_UM, FimDeJogo.TERMINO_DE_PEÇAS);
                jogoFinalizado = true;
                return;
            }
        }
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
        /**
         * Sempre que uma peça realizar um movimento essa função vai ser executada.
         * @param posInicial posição do inicio do movimento.
         * @param posFinal posição de término do movimento.
         */
        public void onPieceMoved(Posição posInicial, Posição posFinal);
        
        /**
         * Sempre que o jogo terminar essa função será chamada.
         * @param vencedor Jogador que ganhou.
         * @param causa Causa do término do jogo.
         */
        public void onGameFinished(int vencedor, int causa);
        
        /**
         * Sempre que uma peça for removida essa função será chamada.
         * @param posição posição na qual a peça foi removida.
         */
        public void onPieceRemoved(Posição posição);
    }
    
    public void setOnBoardChangedListener(BoardChangedListener boardChangedListener){
        this.boardChangedListener = boardChangedListener;
    }
    
    public class FimDeJogo{
        public static final int TERMINO_DE_PEÇAS = 0;
        public static final int MAXIMO_DE_TURNOS = 1;
        public static final int TRAVADO = 2;
    }
}
