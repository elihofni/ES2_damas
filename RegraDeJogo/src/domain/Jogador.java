package domain;

import regradejogo.*;

public class Jogador {
    //Instancia de Regra na qual o jogador está respeitando.
    protected final Regras regras;
    protected final int time;
    
    public Jogador(Regras regras,int time){
        this.regras = regras;
        this.time = time;
        
    }
    
    public void realizarJogada(int iIni, int jIni, int iFim, int jFim){
        Posicao posIni = new Posicao(iIni, jIni);
        Posicao posFim = new Posicao(iFim, jFim);
        regras.moverPeca(posIni, posFim);
    }

    public int getTime() {
        return time;
    }
    
    
    
}
