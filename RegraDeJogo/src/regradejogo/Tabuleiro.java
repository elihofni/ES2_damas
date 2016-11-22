package regradejogo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o tabuleiro do jogo.
 * Realiza, efetivamente, a remoção e alteração de uma peça do tabuleiro.
 */
public class Tabuleiro {
    public static final int DIMEN = 8;
    private Peça[][] tabuleiro;
    
    public Tabuleiro(){
        inicializaTabuleiro();
    }
    
    /**
     * Construtor que carrega o jogo a partir de um arquivo.
     * @param nomeArquivo arquivo com o jogo salvo.
     * @throws FileNotFoundException Caso o arquivo não seja encontrado.
     * @throws IOException 
     */
    public Tabuleiro(String nomeArquivo) throws FileNotFoundException, IOException{
        tabuleiro = new Peça[DIMEN][DIMEN];
        
        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
        
        String line = br.readLine();
        
        int i = 0;
        int j = 0;
        while (line != null) {
            String[] strVetor = line.split(" ");
            j = 0;
            for(String string : strVetor){
                if(string.equals("0")){
                    tabuleiro[i][j] = null;
                }else if(string.equals("1")){
                    tabuleiro[i][j] = new Peça(1, false);
                }else if(string.equals("2")){
                    tabuleiro[i][j] = new Peça(2, false);
                }else if(string.equals("3")){
                    tabuleiro[i][j] = new Peça(1, true);
                }else if(string.equals("4")){
                    tabuleiro[i][j] = new Peça(2, true);
                }
                j++;
            }
            
            i++;
            
            line = br.readLine();
        }
        
        br.close();
    }
    
    /** Inicializa tabuleiro com as peças de cada jogador no leyout clássico do jogo */
    private void inicializaTabuleiro(){
        tabuleiro = new Peça[DIMEN][DIMEN];
        
        //Inicializa peças do jogador 1.
        for(int i = 0; i < (DIMEN / 2) - 1; i++){
            for(int j = 0; j < DIMEN; j++){
                if(i%2 == 0){
                    if(j%2 != 0){
                        tabuleiro[i][j] = new Peça(2, false);
                    }
                }else if(i%2 != 0){
                    if(j%2 == 0){
                        tabuleiro[i][j] = new Peça(2, false);
                    }
                }
            }
        }
        
        //Inicializa peças do segundo jogador.
        for(int i = 5; i < 8; i++){
            for(int j = 0; j < DIMEN; j++){
                if(i%2 == 0){
                    if(j%2 != 0){
                        tabuleiro[i][j] = new Peça(1, false);
                    }
                }else if(i%2 != 0){
                    if(j%2 == 0){
                        tabuleiro[i][j] = new Peça(1, false);
                    }
                }
            }
        }
    }
    
    /** Retorna uma determinada peça a partir de uma posição na matriz, retorna
    * null se não houver nenhuma da posição.
    * @param posição linha
    * @return instancia de peça caso exista, null caso não.*/
    public Peça getPeça(Posição posição){
        return tabuleiro[posição.getI()][posição.getJ()];
    }
    
    /**
     * Retorna a posição de uma peça no tabuleiro.
     * @param peça peça a ter a posição retornada.
     * @return posição da peça;
     */
    public Posição getPosição(Peça peça){
        for(int i = 0; i < DIMEN; i++){
            for(int j = 0; j < DIMEN; j++){
                if(tabuleiro[i][j] != null){
                    if(tabuleiro[i][j].equals(peça)){
                        return new Posição(i, j);
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Muda uma peça de posição.
     * @param peça peça a ser movida.
     * @param posFinal posição final.
     */
    //Simplesmente move, sem regras. Quem vai cuidar das regras é a classe Regras.
    public void movePeça(Peça peça, Posição posFinal){
        Posição posAtual = getPosição(peça);
        
        tabuleiro[posAtual.getI()][posAtual.getJ()] = null;
        
        tabuleiro[posFinal.getI()][posFinal.getJ()] = peça;
    }
    
    /**
     * Dada uma posição retorna todas as posições na diagonal.
     * (1, 1) = esquerda descendo
     * (-1, -1) = esquerda subindo.
     * (1, -1) = direita subindo.
     * (-1, 1) = direita descendo.
     * @param posReferencia posição de referencia
     * @param inclinacao inclinação da diagonal
     * @param sentido sentido
     * @return 
     */
    public List<Posição> getDiagonal(Posição posReferencia, int inclinacao, int sentido){
        List<Posição> posicoes = new ArrayList<>();
        
        Posição pos = new Posição(posReferencia.getI() + sentido, posReferencia.getJ() + inclinacao);
        while(posValida(pos) && !existePecaPos(pos)){
            posicoes.add(pos);
            pos = new Posição(pos.getI() + sentido, pos.getJ() + inclinacao);
        }
        
        return posicoes;
    }
    
    /**
     * Remove uma peça do tabuleiro.
     * @param peça peça a ser removida.
     */
    public void removePeça(Peça peça){
        Posição posAtual = getPosição(peça);
        
        tabuleiro[posAtual.getI()][posAtual.getJ()] = null;
    }
    
    /**
     * Dada uma posição, diz se é válida ou não.
     * Uma posição é dita válida se para i e j pertencerem ao intervalo [0, 7]
     * @param pos posição a ser analisada.
     * @return true se for válida, false caso contrário.
     */
    public boolean posValida(Posição pos){
        if(pos.getI() >= 8 || pos.getJ() >= 8){
            return false;
        }
        
        if(pos.getI() < 0 || pos.getJ() < 0){
            return false;
        }
        
        return true;
    }
    
    /**
     * Dada uma posição, verifica se existe uma peça.
     * @param pos posição a ser verificada.
     * @return retorna true caso exista, false caso contrário.
     */
    public boolean existePecaPos(Posição pos){
        return getPeça(pos) != null;
    }
    
    /**
     * Dada duas peças, retorna a inclinação relativa da peca1 sobre a peca2.
     * @param peca1 peca de referencia.
     * @param peca2 peca secundária.
     * @return 0 para inclinação a esquerda, 1 para direita.
     */
    public int inclinacaoRelativa(Posição posPeca1, Posição posPeca2){
        if(posPeca1.getJ() > posPeca2.getJ()){
            return 0;
        }
        
        return 1;
    }
    
    /**
     * Dada uma posição, diz se está na borda inferior ou superior.
     * @param pos posição a ser analisada.
     * @return 0 caso não esteja na borda, 1 caso esteja na borda inferior e 2 na superior.
     */
    public int bordaSupInf(Posição pos){
        if((pos.getI() == DIMEN - 1)){
            return 2;
        }
        
        if((pos.getI() == 0)){
            return 1;
        }
        
        return 0;
    }
    
    /**
     * Verifica se uma peça está numa linha mais alta que a outra.
     * @param peca1 peca referencia.
     * @param peca2 peca secundaria.
     * @return retorna true caso a peca1 esteja acima da peca1, false caso contrário.
     */
    public boolean ehMaisAlta(Posição pos1, Posição pos2){
        return pos1.getI() > pos2.getI();
    }
    
    /**
     * Retorna todas as peças que estão no tabuleiro.
     * @return Lista com todas as peças do tabuleiro.
     */
    public List<Peça> getPeças(){
        List<Peça> peças = new ArrayList<>();
        
        for(int i = 0; i < DIMEN; i++){
            for(int j = 0; j < DIMEN; j++){
                Peça peça = getPeça(new Posição(i, j));
                if(peça != null){
                    peças.add(peça);
                }
            }
        }
        
        return peças;
    }
    
    /**
     * Aquele toString de lei pros testes.
     * @return 
     */
    @Override
    public String toString(){
        String str = new String();
        for(int i = 0; i < DIMEN; i++){
            for(int j = 0; j < DIMEN; j++){
                if(tabuleiro[i][j] == null){
                    str += "0 ";
                }else if(tabuleiro[i][j].isDama()){
                    str += (tabuleiro[i][j].getTime() == 1)? "3 " : "4 ";
                }else{
                    str += tabuleiro[i][j].getTime() + " ";
                }
            }
            str += '\n';
        }
        return str;
    }
    
    /**
     * Classe pra agrupar constantes e facilitar a identificação e manutenção.
     */
    public class Inclinacao{
        public static final int ESQUERDA = 0;
        public static final int DIREITA = 1;
    }
}
