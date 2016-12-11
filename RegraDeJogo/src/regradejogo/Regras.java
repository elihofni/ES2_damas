package regradejogo;

import Excecoes.JogadaInvalidaException;
import Excecoes.PosicaoInvalidaException;
import java.io.IOException;
import java.io.InputStream;
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
    public static final int JOGADOR_UM = 1; //Constance do jogador 1.
    public static final int JOGADOR_DOIS = 2; //Constante do jogador 2.
    private int jogadorAtual;//Define quem joga no turno atual
    private int nPecasJogador1; //Quantidade de peças do jogador 1;
    private int nPecasJogador2; //Quantidade de peças do jogador 2;
    private boolean jogoFinalizado;
    private List<Peca> pecasAptasCapturas;
    
    public Regras(){
        turnoAtual = 0;
        tabuleiro = new Tabuleiro();
        jogadorAtual = JOGADOR_UM;
        nPecasJogador1 = 8;
        nPecasJogador2 = 8;
        pecasAptasCapturas = getPecasAptasDoJogadorAtual();
    }
    
    public Regras(String nomeArquivo) throws IOException{
        turnoAtual = 0;
        tabuleiro = new Tabuleiro(nomeArquivo);
        jogadorAtual = 1;
        pecasAptasCapturas = getPecasAptasDoJogadorAtual();
    }
    

    public Regras(Tabuleiro tabuleiro, int turnoAtual, int jogadorAtual, int nPecasJogador1, int nPecasJogador2) {
        this.tabuleiro = tabuleiro;
        this.turnoAtual = turnoAtual;
        this.jogadorAtual = jogadorAtual;
        this.nPecasJogador1 = nPecasJogador1;
        this.nPecasJogador2 = nPecasJogador2;
        this.pecasAptasCapturas = getPecasAptasDoJogadorAtual();
    }
    
    /**
     * Move uma peça do tabuleiro a partir das regras.
     * @param posInicial posição que a peça a ser movida está.
     * @param posFinal posição destino.
     */
    protected void moverPeca(Posicao posInicial, Posicao posFinal){
        Peca peca = getPeca(posInicial);
        
        pecasAptasCapturas = getPecasAptasDoJogadorAtual();
        
        //Teste de sanidade. Impossível chegar uma posição inválida a partir da interface.
        if(!tabuleiro.posValida(posInicial)){
            throw new PosicaoInvalidaException("Posicao invalida, fora da dimensao do tabuleiro, Index:" + posInicial.toString() + ".");
        }
        
        //Teste de sanidade. É impossível(quase) chegar uma possição final inválida.
        if(!tabuleiro.posValida(posFinal)){
            throw new PosicaoInvalidaException("Posicao invalida, fora da dimensao do tabuleiro, Index:" + posFinal.toString() + ".");
        }
        
        //Teste de sanidade.
        if(peca == null){
            throw new PosicaoInvalidaException("Nao existe nenhuma peca na posicao " + posInicial.toString() + ".");
        }
        List<Jogada> jogadas;
        
        jogadas = peca.isDama()? jogadasPossiveisDama(peca) : jogadasPossiveis(peca);
        
        //Verifica se a posição final da jogada está contida nas jogadas possíveis.
        Jogada jogada = getJogada(jogadas, posFinal);
        
        //Se não tiver contida, jogada é inválida.
        if(jogada == null){
            throw new JogadaInvalidaException("A posicao " + posFinal.toString() + " nao e uma jogada valida para esta peca " + tabuleiro.getPosicao(peca) + ".");
        }
        
        tabuleiro.movePeca(peca, posFinal);
        
        //Caso tenha chegado na borda.
        int borda = tabuleiro.bordaSupInf(jogada.getPosFinal());
        if(borda == peca.getTime()){
            Posicao pos = tabuleiro.getPosicao(peca);
            boardChangedListener.virouDama(pos.getI(), pos.getJ());
        }
        
        //Verifica se houve captura na jogada.
        if (jogada.houveCaptura()) {
            removerPeca(jogada.getPecaCapturada());
            
            if(!peca.isDama()){
                List<Jogada> capturasPecaAtual = jogadasPossiveis(peca);
                if(!possuiCaptura(capturasPecaAtual)){
                    trocaJogadorAtual();
                }
            }else{
                 trocaJogadorAtual();
            }

        } else {
            trocaJogadorAtual();
        }
        
        //Caso tenha chegado na borda.
        borda = tabuleiro.bordaSupInf(jogada.getPosFinal());
        if(borda == peca.getTime()){
            viraDama(peca);
            Posicao pos = tabuleiro.getPosicao(peca);
            boardChangedListener.virouDama(pos.getI(), pos.getJ());
        }
        
        incrementaTurno();
        verificaFimDeJogo();
        
        //Sempre que um movimento for bem sucedido, acionar o callback.
        if(boardChangedListener != null){
            boardChangedListener.onPieceMoved(posInicial, posFinal);
        }
    }
    
    protected List<Peca> getPecassssss(){
        return this.pecasAptasCapturas;
    }
    
    /**
     * Dada uma lista de jogadas, retorna a jogada que possui tal posição final.
     * @param jogadas Lista de jogadas a serem analisadas.
     * @return 
     */
    protected Jogada getJogada(List<Jogada> jogadas, Posicao posFinal){
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
    
    protected List<Jogada> jogadasPossiveisDama(Peca peca){
        if(peca.getTime() != jogadorAtual){
            return new ArrayList<>();
        }
        
        Posicao posDama = tabuleiro.getPosicao(peca);
        
        List<Posicao> posicoes = new ArrayList<>();
        List<Posicao> diagonalEsquerda = new ArrayList<>();
        List<Posicao> diagonalDireita = new ArrayList<>();
        
        Posicao pos = new Posicao(posDama.getI()-1, posDama.getJ()-1);
        while(posicaoValida(pos, peca.getTime())){
            diagonalEsquerda.add(pos);
            pos = new Posicao(pos.getI()-1, pos.getJ()-1);
        }
        
        Posicao pos2 = new Posicao(posDama.getI()+1, posDama.getJ()+1);
        while(posicaoValida(pos2, peca.getTime())){
            diagonalEsquerda.add(pos2);
            pos2 = new Posicao(pos2.getI()+1, pos2.getJ()+1);
        }
        
        Posicao pos3 = new Posicao(posDama.getI()-1, posDama.getJ()+1);
        while(posicaoValida(pos3, peca.getTime())){
            diagonalDireita.add(pos3);
            pos3 = new Posicao(pos3.getI()-1, pos3.getJ()+1);
        }
        
        Posicao pos4 = new Posicao(posDama.getI()+1, posDama.getJ()-1);
        while(posicaoValida(pos4, peca.getTime())){
            diagonalDireita.add(pos4);
            pos4 = new Posicao(pos4.getI()+1, pos4.getJ()-1);
        }
        
        List<Jogada> capturasDireita = capturasPossiveis(diagonalDireita, peca);
        List<Jogada> capturasEsquerda = capturasPossiveis(diagonalEsquerda, peca);
        
        List<Jogada> listAux = new ArrayList<>();
        
        for(Jogada jogada : capturasDireita){
            listAux.add(jogada);
            boolean aux = jogada.getPosInicial().getI() > jogada.getPosFinal().getI();
            List<Posicao> p;
            if(aux){
                p = tabuleiro.getDiagonal(jogada.getPosFinal(), 1, -1);
            }else{
                p = tabuleiro.getDiagonal(jogada.getPosFinal(), -1, 1);
            }
            
            for(Posicao pAux : p){
                listAux.add(new Jogada(jogada.getPecaCapturada(), jogada.getPecaMovida(), jogada.getPosInicial(), pAux));
            }
            
            break;
        }
        
        for(Jogada jogada : capturasEsquerda){
            listAux.add(jogada);
            boolean aux = jogada.getPosInicial().getI() > jogada.getPosFinal().getI();
            List<Posicao> p;
            if(aux){
                p = tabuleiro.getDiagonal(jogada.getPosFinal(), -1, -1);
            }else{
                p = tabuleiro.getDiagonal(jogada.getPosFinal(), 1, 1);
            }
            
            for(Posicao pAux : p){
                listAux.add(new Jogada(jogada.getPecaCapturada(), jogada.getPecaMovida(), jogada.getPosInicial(), pAux));
            }
            
            break;
        }

        List<Jogada> jogadas = new ArrayList<>();
        posicoes.addAll(diagonalDireita);
        posicoes.addAll(diagonalEsquerda);
        if(listAux.isEmpty()){
            for(Posicao p : posicoes){
                jogadas.add(new Jogada(null, peca, posDama, p));
            }
            return jogadas;
        }
        
        return listAux;
    }
    
    /**
     * Essa função simula uma peça em uma posição qualquer e retorna as jogadas possiveis.
     * @param posicao posição a ser analisada
     * @return lista com as posições possiveis.
     */
    private List<Posicao> getPosicoesPossiveisPos(Posicao posicao, int time){
        /**
         * A lista posições guarda todas as posições na quais são oriundas de jogadas "normais",
         * apenas andar pra frente.
         */
        List<Posicao> posicoes = new ArrayList<>();
        
        /**
         * Váriavel que ajuda a decidir se vai descer o subir na matriz.
         * Jogador 1 sempre fica em baixo.
         */
        int varJogador = (time == 1)? -1 : 1;
        
        Posicao pos1 = new Posicao(posicao.getI() + varJogador, posicao.getJ() - 1);
        Posicao pos2 = new Posicao(posicao.getI() + varJogador, posicao.getJ() + 1);
        
        //Posições que podem conter inimigos e que estão no sentido contrário ao "normal" da peça.
        Posicao pos3 = new Posicao(posicao.getI() - varJogador, posicao.getJ() - 1);
        Posicao pos4 = new Posicao(posicao.getI() - varJogador, posicao.getJ() + 1);

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
     * @param peca
     * @return 
     */
    protected List<Posicao> getPosicoesPossiveisPeca(Peca peca){
        if(peca.getTime() != jogadorAtual){
            return new ArrayList<>();
        }
        
        //Pega a posição da peça.
        Posicao poiscaoPeca = tabuleiro.getPosicao(peca);
        
        /**
         * A lista posições guarda todas as posições na quais são oriundas de jogadas "normais",
         * apenas andar pra frente.
         */
        List<Posicao> posicoes = new ArrayList<>();
        
        /**
         * Váriavel que ajuda a decidir se vai descer o subir na matriz.
         * Jogador 1 sempre fica em baixo.
         */
        int varJogador = (peca.getTime() == 1)? -1 : 1;
        
        Posicao pos1 = new Posicao(poiscaoPeca.getI() + varJogador, poiscaoPeca.getJ() - 1);
        Posicao pos2 = new Posicao(poiscaoPeca.getI() + varJogador, poiscaoPeca.getJ() + 1);
        
        //Posições que podem conter inimigos e que estão no sentido contrário ao "normal" da peça.
        Posicao pos3 = new Posicao(poiscaoPeca.getI() - varJogador, poiscaoPeca.getJ() - 1);
        Posicao pos4 = new Posicao(poiscaoPeca.getI() - varJogador, poiscaoPeca.getJ() + 1);

        //Verifica se as posições são válidas.
        if(posicaoValida(pos1, peca.getTime())){
            posicoes.add(pos1);
        }
        
        if(posicaoValida(pos2, peca.getTime())){
            posicoes.add(pos2);
        }
        
        /**
         * Jogadas não-normais. Captura para "trás". No mínimo vai ser uma captura, logo não posso adiciona-la
         * como uma candidata.
         */
        if(posicaoValida(pos3, peca.getTime())){
            posicoes.add(pos3);
        }
        
        if(posicaoValida(pos4, peca.getTime())){
            posicoes.add(pos4);
        }
        
        return posicoes;
    }
    
    /**
     * Analisa todo o campo da peça(sem ser dama) e retorna as posições válidas.
     * @param peca peça a ter as jogadas analisadas.
     * @return retorna uma lista com todas as posições válidas para jogada.
     */
    protected List<Jogada> jogadasPossiveis(Peca peca){
        //Se a peça não for do jogador atual.
        /*if(peça.getTime() != jogadorAtual){
            return null;
        }*/
        
        //Se a peça não estiver apta para o turno, retorna uma lista de jogada vazia.
        /*if(getPeçasAptas().indexOf(peça) == -1){
            return new ArrayList<>();
        }*/
        
        /*if(!pecasAptasCapturas.isEmpty()){
            if(pecasAptasCapturas.indexOf(peca) == -1){
                return new ArrayList<>();
            }
        }*/
        
        //Pega o entorno da peça.
        List<Posicao> posicoes = getPosicoesPossiveisPeca(peca);
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
        List<Jogada> capturas = capturasPossiveis(posicoes, peca);
        
        //Caso exista alguma jogada de captura.
        if(!capturas.isEmpty()){
            return capturas;
        }
        
        //Caso não tenha havido nenhuma captura, adicionar as jogadas normais.
        for(Posicao posicao : posicoes){
            boolean teste2 = peca.getTime() == 1? posicao.getI() < tabuleiro.getPosicao(peca).getI() : posicao.getI() > tabuleiro.getPosicao(peca).getI();
            if(!tabuleiro.existePecaPos(posicao) && teste2){
                jogadas.add(new Jogada(null, peca, tabuleiro.getPosicao(peca), posicao));
            }
        }
        
        /**
         * Se chegou até aqui quer dizer que as duas jogadas candidatas ali em cima eram efetivamente
         * jogadas normais.
         */
        return jogadas;
    }
    
    /**
     * Retorna todas as peças que estão áptas a jogar nesse turno.
     * @return lista de todas as peças que possuem captura no turno
     */
    protected List<Peca> getPecasCaptura2(){
        List<Peca> pecasJogador = tabuleiro.getPecasJogador(jogadorAtual);
        List<Peca> pecasAptas = new ArrayList<>();
        
        for(Peca peca : pecasJogador){
            if(Peca.possuiCaptura(peca, this)){
                pecasAptas.add(peca);
            }
        }
        
        return pecasAptas;
    }
    
    /**
     * Dada uma peça e uma posição futura, verifica se é uma posição válida.
     * Uma posição é dita válida caso esteja vazia ou tenha alguma peça inimiga.
     * @param pos posição a ser analisada.
     * @param peca peça a ser movida.
     * @return true caso seja valida, false caso não.
     */
    protected boolean posicaoValida(Posicao pos, int time){
        if(!tabuleiro.posValida(pos)){
            return false;
        }
        
        Peca peca2 = tabuleiro.getPeca(pos);
        
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
    protected List<Jogada> capturasPossiveis(List<Posicao> posicoes, Posicao pos, int time){
        List<Jogada> jogadas = new ArrayList<>();
        
        for(Posicao posicao : posicoes){
            Peca p = tabuleiro.getPeca(posicao);
            
            //Se existe alguma peça nessa posição.
            if(p != null){
                //Se não for do time da peça que quer capturar, então é do time inimigo.
                if(p.getTime() != time){
                    Posicao posFinal = podeComer(pos, tabuleiro.getPosicao(p));
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
    protected List<Jogada> capturasPossiveis(List<Posicao> pos, Peca peca){
        List<Jogada> jogadas = new ArrayList<>();
        
        for(Posicao posicao : pos){
            Peca p = tabuleiro.getPeca(posicao);
            
            //Se existe alguma peça nessa posição.
            if(p != null){
                //Se não for do time da peça que quer capturar, então é do time inimigo.
                if(p.getTime() != peca.getTime()){
                    Posicao posFinal = podeComer(peca, p);
                    if(posFinal != null){
                        //TODO: Função que verifica capturas seguidas.
                        jogadas.add(new Jogada(p, peca, tabuleiro.getPosicao(peca), posFinal));
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
    protected List<Jogada> capturasSeguidas(List<Jogada> jogadas, Jogada jogada, int time, List<Posicao> rastro){
        List<Posicao> posicoes = getPosicoesPossiveisPos(jogada.getPosFinal(), time);
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
    }
    
    private boolean possuiCapturaValida(List<Jogada> capturas, List<Posicao> rastros){
        if(rastros.isEmpty()){
            return true;
        }
        
        for(Posicao rastro : rastros){
            for(Jogada captura : capturas){
                Posicao pos = captura.getPosFinal();
                if(!pos.equals(rastro)){
                    return true;
                }   
            }
        }
        
        return false;
    }
    
    private boolean inPosList(List<Posicao> list, Posicao pos){
        for(Posicao posicao : list){
            if(posicao.equals(pos)){
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
    protected Posicao podeComer(Peca peca1, Peca peca2){
        Posicao pos1 = tabuleiro.getPosicao(peca1);
        Posicao pos2 = tabuleiro.getPosicao(peca2);
        //Pego a inclinação relativa entre duas pecas.
        int inclinacao = tabuleiro.inclinacaoRelativa(pos1, pos2);
        //Variável que auxilia no cálculo da posição final.
        int altura = tabuleiro.ehMaisAlta(pos1, pos2)? -1 : 1;
        
        Posicao posFinal;
        
        Posicao pos = tabuleiro.getPosicao(peca2);
        if(inclinacao == Inclinacao.ESQUERDA){
            posFinal = new Posicao(pos.getI() + altura, pos.getJ() - 1);
        }else{
            posFinal = new Posicao(pos.getI() + altura, pos.getJ() + 1);
        }

        //Verifico se a posição é válida.
        if(!tabuleiro.posValida(posFinal)){
            return null;
        }
        //Verifico se existe alguma peça nessa aposição.
        if(tabuleiro.getPeca(posFinal) != null){
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
    protected Posicao podeComer(Posicao pos1, Posicao pos2){
        //Pego a inclinação relativa entre duas pecas.
        int inclinacao = tabuleiro.inclinacaoRelativa(pos1, pos2);
        //Variável que auxilia no cálculo da posição final.
        int altura = tabuleiro.ehMaisAlta(pos1, pos2)? -1 : 1;
        
        Posicao posFinal;
        
        //Posição pos = tabuleiro.getPosição(peca2);
        if(inclinacao == Inclinacao.ESQUERDA){
            posFinal = new Posicao(pos2.getI() + altura, pos2.getJ() - 1);
        }else{
            posFinal = new Posicao(pos2.getI() + altura, pos2.getJ() + 1);
        }

        //Verifico se a posição é válida.
        if(!tabuleiro.posValida(posFinal)){
            return null;
        }
        //Verifico se existe alguma peça nessa aposição.
        if(tabuleiro.getPeca(posFinal) != null){
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
                boardChangedListener.onGameFinished(JOGADOR_DOIS, FimDeJogo.TERMINO_DE_PECAS);
                jogoFinalizado = true;
                return;
            }
        }
        
        if(nPecasJogador2 == 0){
            if(boardChangedListener != null){
                boardChangedListener.onGameFinished(JOGADOR_UM, FimDeJogo.TERMINO_DE_PECAS);
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
    protected List<Peca> getPecasCaptura(){
        List<Peca> pecasAptasCaptura = new ArrayList<>();
        List<Peca> pecasJogadaNormal = new ArrayList<>();
        
        //Pega todas as peças que estão no tabuleiro.
        List<Peca> pecasTabuleiro = tabuleiro.getPecas();
        
        for(Peca peca : pecasTabuleiro){
            List<Jogada> jogadas = jogadasPossiveis(peca);
            if(possuiCaptura(jogadas)){
                pecasAptasCaptura.add(peca);
            }else if(!jogadas.isEmpty()){
                pecasJogadaNormal.add(peca);
            }
        }
        
        return pecasAptasCaptura.isEmpty()? pecasJogadaNormal : pecasAptasCaptura;
    }
    
    /**
     * Verifica se uma lista de jogadas possui uma jogada com captura.
     * @param jogadas lista de jogadas a ser verificada.
     * @return tre caso exista jogada de captura, false caso contrário.
     */
    protected boolean possuiCaptura(List<Jogada> jogadas){
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
     * @param peca peça que vai se transformar em dama
     */
    private void viraDama(Peca peca){
        peca.setDama(true);
    }
    
    protected int getTurno(){
        return turnoAtual;
    }
    
    /**
     * Remove peça do tabuleiro.
     * @param peca peça a ser removida.
     */
    private void removerPeca(Peca peca){
        if(peca == null){
            return;
        }
        
        if(boardChangedListener != null){
            boardChangedListener.onPieceRemoved(tabuleiro.getPosicao(peca));
        }
        
        tabuleiro.removePeca(peca);
        
        if(peca.getTime() == JOGADOR_UM){
            nPecasJogador1--;
        }else{
            nPecasJogador2--;
        }
    }
    
    /**
     * retorna uma peça dada a posição.
     * @param pos
     * @return retorna uma peça caso exista nessa posição, null caso contrário.
     */
    protected Peca getPeca(Posicao pos){
        return tabuleiro.getPeca(pos);
    }
    
    protected Tabuleiro getTabuleiro(){
        return tabuleiro;
    }
    
    protected int getJogadorAtual(){
        return jogadorAtual;
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
        public void onPieceMoved(Posicao posInicial, Posicao posFinal);
        
        /**
         * Sempre que o jogo terminar essa função será chamada.
         * @param vencedor Jogador que ganhou.
         * @param causa Causa do término do jogo.
         */
        public void onGameFinished(int vencedor, int causa);
        
        /**
         * Sempre que uma peça for removida essa função será chamada.
         * @param posicao posição na qual a peça foi removida.
         */
        public void onPieceRemoved(Posicao posicao);
        
        /**
         * Sempre que uma peça virar dama essa função será chamada.
         * Função é chamada antes do movimento da peca.
         * @param i posição i da peça na matriz.
         * @param j posição j da peça na matriz.
         */
        public void virouDama(int i, int j);
    }
    
    public void setOnBoardChangedListener(BoardChangedListener boardChangedListener){
        this.boardChangedListener = boardChangedListener;
    }
    
    public class FimDeJogo{
        public static final int TERMINO_DE_PECAS = 0;
        public static final int MAXIMO_DE_TURNOS = 1;
        public static final int TRAVADO = 2;
    }
    
    public int getnPecasJogador1() {
        return nPecasJogador1;
    }

    public int getnPecasJogador2() {
        return nPecasJogador2;
    }

    public boolean isJogoFinalizado() {
        return jogoFinalizado;
    }

    public Regras copia() {

        return new Regras(tabuleiro.copia(), turnoAtual, jogadorAtual, nPecasJogador1, nPecasJogador2);
    }

    public List<Peca> getPecasAptasDoJogadorAtual() {
        List<Peca> pecas = getPecasCaptura();
        List<Peca> returnList = new ArrayList<>();
        for (Peca peca : pecas) {
            if (peca.getTime() == jogadorAtual) {
                returnList.add(peca);
            }
        }
        return returnList;
    }

    public List<Peca> getPecasAptasParaCapturaDoJogadorAtual() {
        List<Peca> pecasAptasCaptura = new ArrayList<>();

        //Pega todas as peças que estão no tabuleiro.
        List<Peca> pecasTabuleiro = tabuleiro.getPecas();

        for (Peca peca : pecasTabuleiro) {
            List<Jogada> jogadas = jogadasPossiveis(peca);
            if (possuiCaptura(jogadas) && peca.getTime() == jogadorAtual) {
                pecasAptasCaptura.add(peca);
            }
        }

        return pecasAptasCaptura;
    }

    private void trocaJogadorAtual() {
        if (jogadorAtual == JOGADOR_UM) {
            jogadorAtual = JOGADOR_DOIS;
        } else {
            jogadorAtual = JOGADOR_UM;
        }
    }
}
