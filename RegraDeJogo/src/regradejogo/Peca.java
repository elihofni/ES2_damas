package regradejogo;

import java.util.List;

/**
 * Classe que representa uma peca de jogo.
 * @author Max
 */
public class Peca {
    private int time;
    private boolean dama;
    
    public Peca(int time, boolean dama){
        this.time = time;
        this.dama = dama;
    }
    
    public Peca(){
        
    }
    
    public void setTime(int time){
        this.time = time;
    }
    
    public int getTime(){
        return time;
    }
    
    public void setDama(boolean dama){
        this.dama = dama;
    }
    
    public boolean isDama(){
        return dama;
    }
    
    /**
     * Verifica se uma peça possui alguma jogada de captura.
     * @param peca instancia de peça a ser anaisada.
     * @param regras instancia de regra a qual a peca se aplica.
     * @return true caso haja, false caso contrário.
     */
    public static boolean possuiCaptura(Peca peca, Regras regras){
        List<Jogada> jogadas = regras.jogadasPossiveis(peca);
        
        for(Jogada jogada : jogadas){
            if(jogada.houveCaptura()){
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString(){
        return "[" + String.valueOf(time) + ";" + String.valueOf(dama) + "]";
    }
}
