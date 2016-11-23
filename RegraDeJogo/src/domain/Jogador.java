package domain;

import regradejogo.*;

public class Jogador {
    //Instancia de Regra na qual o jogador está respeitando.
    private final Regras regras;
    
    public Jogador(Regras regras){
        this.regras = regras;
    }
    
    public void realizarJogada(int iIni, int jIni, int iFim, int jFim){
        Posicao posIni = new Posicao(iIni, jIni);
        Posicao posFim = new Posicao(iFim, jFim);
        regras.moverPeca(posIni, posFim);
    }
    
}
