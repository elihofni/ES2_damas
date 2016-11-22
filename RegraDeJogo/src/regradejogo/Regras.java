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
    private Tabuleiro tabuleiro; //Instancia do tabuleiro que vai ser regido pelas regras.
    private int turnoAtual; //Turno atual do jogo.
    private BoardChangedListener boardChangedListener; //Interface de callback.
    private static final int JOGADOR_UM = 1; //Constance do jogador 1.
    private static final int JOGADOR_DOIS = 2; //Constante do jogador 2.
    private int jogadorAtual;//TODO
    private int nPecasJogador1; //Quantidade de peças do jogador 1;
    private int nPecasJogador2; //Quantidade de peças do jogador 2;
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
     * Dada uma lista de jogadas, retorna a jogada que possui tal posição final.
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
    
    public List<Jogada> getPosicoesPossiveisDama(Peça peça){
        Posição posDama = tabuleiro.getPosição(peça);
        
        List<Posição> posicoes = new ArrayList<>();
        List<Posição> diagonalEsquerda = new ArrayList<>();
        List<Posição> diagonalDireita = new ArrayList<>();
        
        Posição pos = new Posição(posDama.getI()-1, posDama.getJ()-1);
        while(posicaoValida(pos, peça.getTime())){
            diagonalEsquerda.add(pos);
            pos = new Posição(pos.getI()-1, pos.getJ()-1);
        }
        
        Posição pos2 = new Posição(posDama.getI()+1, posDama.getJ()+1);
        while(posicaoValida(pos2, peça.getTime())){
            diagonalEsquerda.add(pos2);
            pos2 = new Posição(pos2.getI()+1, pos2.getJ()+1);
        }
        
        Posição pos3 = new Posição(posDama.getI()-1, posDama.getJ()+1);
        while(posicaoValida(pos3, peça.getTime())){
            diagonalDireita.add(pos3);
            pos3 = new Posição(pos3.getI()-1, pos3.getJ()+1);
        }
        
        Posição pos4 = new Posição(posDama.getI()+1, posDama.getJ()-1);
        while(posicaoValida(pos4, peça.getTime())){
            diagonalDireita.add(pos4);
            pos4 = new Posição(pos4.getI()+1, pos4.getJ()-1);
        }
        
        List<Jogada> capturasDireita = capturasPossiveis(diagonalDireita, peça);
        List<Jogada> capturasEsquerda = capturasPossiveis(diagonalEsquerda, peça);
        
        List<Jogada> listAux = new ArrayList<>();
        
        for(Jogada jogada : capturasDireita){
            listAux.add(jogada);
            boolean aux = jogada.getPosInicial().getI() > jogada.getPosFinal().getI();
            List<Posição> p;
            if(aux){
                p = tabuleiro.getDiagonal(jogada.getPosFinal(), 1, -1);
            }else{
                p = tabuleiro.getDiagonal(jogada.getPosFinal(), -1, 1);
            }
            
            for(Posição pAux : p){
                listAux.add(new Jogada(jogada.getPeçaCapturada(), jogada.getPeçaMovida(), jogada.getPosInicial(), pAux));
            }
        }
        
        for(Jogada jogada : capturasEsquerda){
            listAux.add(jogada);
            boolean aux = jogada.getPosInicial().getI() > jogada.getPosFinal().getI();
            List<Posição> p;
            if(aux){
                p = tabuleiro.getDiagonal(jogada.getPosFinal(), -1, -1);
            }else{
                p = tabuleiro.getDiagonal(jogada.getPosFinal(), 1, 1);
            }
            
            for(Posição pAux : p){
                listAux.add(new Jogada(jogada.getPeçaCapturada(), jogada.getPeçaMovida(), jogada.getPosInicial(), pAux));
            }
        }

        List<Jogada> jogadas = new ArrayList<>();
        posicoes.addAll(diagonalDireita);
        posicoes.addAll(diagonalEsquerda);
        if(listAux.isEmpty()){
            for(Posição p : posicoes){
                jogadas.add(new Jogada(null, peça, posDama, p));
            }
            return jogadas;
        }
        
        return listAux;
    }
    
    /**
     * Essa função simula uma peça em uma posição qualquer e retorna as jogadas possiveis.
     * @param posição posição a ser analisada
     * @return lista com as posições possiveis.
     */
    private List<Posição> getPosicoesPossiveisPos(Posição posição, int time){
        /**
         * A lista posições guarda todas as posições na quais são oriundas de jogadas "normais",
         * apenas andar pra frente.
         */
        List<Posição> posicoes = new ArrayList<>();
        
        /**
         * Váriavel que ajuda a decidir se vai descer o subir na matriz.
         * Jogador 1 sempre fica em baixo.
         */
        int varJogador = (time == 1)? -1 : 1;
        
        Posição pos1 = new Posição(posição.getI() + varJogador, posição.getJ() - 1);
        Posição pos2 = new Posição(posição.getI() + varJogador, posição.getJ() + 1);
        
        //Posições que podem conter inimigos e que estão no sentido contrário ao "normal" da peça.
        Posição pos3 = new Posição(posição.getI() - varJogador, posição.getJ() - 1);
        Posição pos4 = new Posição(posição.getI() - varJogador, posição.getJ() + 1);

        //Verifica se as posições são válidas.
        if(posicaoValida(pos1, time)){
            posicoes.add(pos1);
        }
        
        if(posicaoValida(pos2, time)){
            posicoes.add(pos2);
        }
        
        if(posicaoValida(pos3, time)){
            posicoes.add(pos3);
        }
        
        if(posicaoValida(pos4, time)){
            posicoes.add(pos4);
        }
        
        return posicoes;
    }
    
    /**
     * Dada uma peça, retorna todo seu entorno.
     * @param peça
     * @return 
     */
    public List<Posição> getPosicoesPossiveisPeça(Peça peça){
        //Pega a posição da peça.
        Posição poisçãoPeça = tabuleiro.getPosição(peça);
        
        /**
         * A lista posições guarda todas as posições na quais são oriundas de jogadas "normais",
         * apenas andar pra frente.
         */
        List<Posição> posicoes = new ArrayList<>();
        
        /**
         * Váriavel que ajuda a decidir se vai descer o subir na matriz.
         * Jogador 1 sempre fica em baixo.
         */
        int varJogador = (peça.getTime() == 1)? -1 : 1;
        
        Posição pos1 = new Posição(poisçãoPeça.getI() + varJogador, poisçãoPeça.getJ() - 1);
        Posição pos2 = new Posição(poisçãoPeça.getI() + varJogador, poisçãoPeça.getJ() + 1);
        
        //Posições que podem conter inimigos e que estão no sentido contrário ao "normal" da peça.
        Posição pos3 = new Posição(poisçãoPeça.getI() - varJogador, poisçãoPeça.getJ() - 1);
        Posição pos4 = new Posição(poisçãoPeça.getI() - varJogador, poisçãoPeça.getJ() + 1);

        //Verifica se as posições são válidas.
        if(posicaoValida(pos1, peça.getTime())){
            posicoes.add(pos1);
        }
        
        if(posicaoValida(pos2, peça.getTime())){
            posicoes.add(pos2);
        }
        
        /**
         * Jogadas não-normais. Captura para "trás". No mínimo vai ser uma captura, logo não posso adiciona-la
         * como uma candidata.
         */
        if(posicaoValida(pos3, peça.getTime())){
            posicoes.add(pos3);
        }
        
        if(posicaoValida(pos4, peça.getTime())){
            posicoes.add(pos4);
        }
        
        return posicoes;
    }
    
    /**
     * Analisa todo o campo da peça(sem ser dama) e retorna as posições válidas.
     * @param peça peça a ter as jogadas analisadas.
     * @return retorna uma lista com todas as posições válidas para jogada.
     */
    public List<Jogada> jogadasPossiveis(Peça peça){
        //Se a peça não for do jogador atual.
        /*if(peça.getTime() != jogadorAtual){
            return null;
        }*/
        
        //Se a peça não estiver apta para o turno, retorna uma lista de jogada vazia.
        /*if(getPeçasAptas().indexOf(peça) == -1){
            return new ArrayList<>();
        }*/
        
        //Pega o entorno da peça.
        List<Posição> posicoes = getPosicoesPossiveisPeça(peça);
        /**
         * A lista jogadas é fruto da analise das posições e interpretação do que pode acontecer das jogadas
         * da lista posições.
         */
        List<Jogada> jogadas = new ArrayList<>();
        
        /**
         * Nesse jogo de damas, captura é prioridade. Se existe, pelo menos uma, jogada na qual seja uma captura
         * ela será retornada.
         * Aquela jogada ali em cima que era candidata a ser uma jogada normal, pode cair aqui dentro de capturas
         * mas não tem problema.
         */
        List<Jogada> capturas = capturasPossiveis(posicoes, peça);
        
        //Caso exista alguma jogada de captura.
        if(!capturas.isEmpty()){
            return capturas;
        }
        
        //Caso não tenha havido nenhuma captura, adicionar as jogadas normais.
        for(Posição posicao : posicoes){
            boolean teste2 = peça.getTime() == 1? posicao.getI() < tabuleiro.getPosição(peça).getI() : posicao.getI() > tabuleiro.getPosição(peça).getI();
            if(!tabuleiro.existePecaPos(posicao) && teste2){
                jogadas.add(new Jogada(null, peça, tabuleiro.getPosição(peça), posicao));
            }
        }
        
        /**
         * Se chegou até aqui quer dizer que as duas jogadas candidatas ali em cima eram efetivamente
         * jogadas normais.
         */
        return jogadas;
    }
    
    /**
     * Dada uma peça e uma posição futura, verifica se é uma posição válida.
     * Uma posição é dita válida caso esteja vazia ou tenha alguma peça inimiga.
     * @param pos posição a ser analisada.
     * @param peca peça a ser movida.
     * @return true caso seja valida, false caso não.
     */
    private boolean posicaoValida(Posição pos, int time){
        if(!tabuleiro.posValida(pos)){
            return false;
        }
        
        Peça peca2 = tabuleiro.getPeça(pos);
        
        //Se não tem nenhuma peça na posição, jogada é válida.
        if(peca2 == null){
            return true;
        }
        
        //Se a peça na posição for do seu time, jogada inválida.
        if(peca2.getTime() == time){
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
     * Dada uma lista de posições, retorna todas as capturas possíveis.
     * @param pos lista de posições a serem analisadas.
     * @param peca time da peça que quer caputrar.
     * @return jogadas.
     */
    public List<Jogada> capturasPossiveis(List<Posição> posicoes, Posição pos, int time){
        List<Jogada> jogadas = new ArrayList<>();
        
        for(Posição posicao : posicoes){
            Peça p = tabuleiro.getPeça(posicao);
            
            //Se existe alguma peça nessa posição.
            if(p != null){
                //Se não for do time da peça que quer capturar, então é do time inimigo.
                if(p.getTime() != time){
                    Posição posFinal = podeComer(pos, tabuleiro.getPosição(p));
                    if(posFinal != null){
                        //TODO: Função que verifica capturas seguidas.
                        jogadas.add(new Jogada(p, null, pos, posFinal));
                    }
                }
            }
        }
        
        return jogadas;
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
                        //TODO: Função que verifica capturas seguidas.
                        jogadas.add(new Jogada(p, peca, tabuleiro.getPosição(peca), posFinal));
                    }
                }
            }
        }
        
        return jogadas;
    }
    
    /**
     * Função recursiva que retorna todas as capturas em sequencia.
     * @param jogadas
     * @param jogada
     * @param time
     * @param rastro
     * @return 
     */
    public List<Jogada> capturasSeguidas(List<Jogada> jogadas, Jogada jogada, int time, List<Posição> rastro){
        List<Posição> posicoes = getPosicoesPossiveisPos(jogada.getPosFinal(), time);
        List<Jogada> capturas = capturasPossiveis(posicoes, jogada.getPosFinal(), time);
        
        if(!possuiCapturaValida(capturas, rastro)){
            return jogadas;
        }
        
        for(Jogada captura : capturas){
            if(!inPosList(rastro, captura.getPosFinal())){
                jogadas.add(jogada);
                rastro.add(jogada.getPosInicial());
                capturasSeguidas(jogadas, captura, time, rastro);
            }
        }
        
        return jogadas;
        
        //rastro.add(jogada.getPosInicial());
        
        /*if(capturas.size() > 1){
            Jogada jogada1 = capturas.get(0);
            Jogada jogada2 = capturas.get(1);
            if(!inPosList(rastro, jogada1.getPosFinal())){
                jogadas.add(jogada);
                capturasSeguidas(jogadas, jogada1, time, rastro);
            }
            
            if(!inPosList(rastro, jogada2.getPosFinal())){
                jogadas.add(jogada);
                rastro.add(jogada.getPosFinal());
                capturasSeguidas(jogadas, jogada2, time, rastro);
            }
        }
        
        if(capturas.size() == 1){
            Jogada jogada1 = capturas.get(0);
            if(!inPosList(rastro, jogada1.getPosFinal())){
                rastro.add(jogada.getPosFinal());
                jogadas.add(jogada);
                capturasSeguidas(jogadas, jogada1, time, rastro);
            }
        }
        
        return jogadas;*/
    }
    
    private boolean possuiCapturaValida(List<Jogada> capturas, List<Posição> rastros){
        if(rastros.isEmpty()){
            return true;
        }
        
        for(Posição rastro : rastros){
            for(Jogada captura : capturas){
                Posição pos = captura.getPosFinal();
                if(!pos.equals(rastro)){
                    return true;
                }   
            }
        }
        
        return false;
    }
    
    private boolean inPosList(List<Posição> list, Posição pos){
        for(Posição posição : list){
            if(posição.equals(pos)){
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Verifica se a peca1 pode comer a peca2.
     * @param peca1 Peça que irá tentar capturar.
     * @param peca2 Peça a ser capturada.
     * @return caso seja possível a captura, retorna a posição final.
     */
    public Posição podeComer(Peça peca1, Peça peca2){
        Posição pos1 = tabuleiro.getPosição(peca1);
        Posição pos2 = tabuleiro.getPosição(peca2);
        //Pego a inclinação relativa entre duas pecas.
        int inclinacao = tabuleiro.inclinacaoRelativa(pos1, pos2);
        //Variável que auxilia no cálculo da posição final.
        int altura = tabuleiro.ehMaisAlta(pos1, pos2)? -1 : 1;
        
        Posição posFinal;
        
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
        //Verifico se existe alguma peça nessa aposição.
        if(tabuleiro.getPeça(posFinal) != null){
            return null;
        }
        
        return posFinal;
    }
    
    /**
     * Dada duas posições verifica se uma hipotética peça pode comer a outra.
     * @param pos1 posição da peça que quer capturar.
     * @param pos2 posição da peça a ser capturada.
     * @return retorna a posição final caso possa comer, null caso não possa.
     */
    public Posição podeComer(Posição pos1, Posição pos2){
        //Pego a inclinação relativa entre duas pecas.
        int inclinacao = tabuleiro.inclinacaoRelativa(pos1, pos2);
        //Variável que auxilia no cálculo da posição final.
        int altura = tabuleiro.ehMaisAlta(pos1, pos2)? -1 : 1;
        
        Posição posFinal;
        
        //Posição pos = tabuleiro.getPosição(peca2);
        if(inclinacao == Inclinacao.ESQUERDA){
            posFinal = new Posição(pos2.getI() + altura, pos2.getJ() - 1);
        }else{
            posFinal = new Posição(pos2.getI() + altura, pos2.getJ() + 1);
        }

        //Verifico se a posição é válida.
        if(!tabuleiro.posValida(posFinal)){
            return null;
        }
        //Verifico se existe alguma peça nessa aposição.
        if(tabuleiro.getPeça(posFinal) != null){
            return null;
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
     * Verifica todas as peças que estão aptas a se moverem no turno.
     * Peças que podem capturar são prioredade.
     * @return Lista de peças válidas para o turno.
     */
    public List<Peça> getPeçasAptas(){
        List<Peça> peçasAptasCaptura = new ArrayList<>();
        List<Peça> peçasJogadaNormal = new ArrayList<>();
        
        //Pega todas as peças que estão no tabuleiro.
        List<Peça> peçasTabuleiro = tabuleiro.getPeças();
        
        for(Peça peça : peçasTabuleiro){
            List<Jogada> jogadas = jogadasPossiveis(peça);
            if(possuiCaptura(jogadas)){
                peçasAptasCaptura.add(peça);
            }else if(!jogadas.isEmpty()){
                peçasJogadaNormal.add(peça);
            }
        }
        
        return peçasAptasCaptura.isEmpty()? peçasJogadaNormal : peçasAptasCaptura;
    }
    
    /**
     * Verifica se uma lista de jogadas possui uma jogada com captura.
     * @param jogadas lista de jogadas a ser verificada.
     * @return tre caso exista jogada de captura, false caso contrário.
     */
    public boolean possuiCaptura(List<Jogada> jogadas){
        for(Jogada jogada : jogadas){
            if(jogada.houveCaptura()){
                return true;
            }
        }
        
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
        
        /**
         * Sempre que uma peça virar dama essa função será chamada.
         * @param i posição i da peça na matriz.
         * @param j posição j da peça na matriz.
         */
        public void virouDama(int i, int j);
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
