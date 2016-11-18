package domain;

import regradejogo.*;

public class Jogador {
    //Instancia de Regra na qual o jogador está respeitando.
    private final Regras regras;
    
    public Jogador(Regras regras){
        this.regras = regras;
    }
    
    public void realizarJogada(int iIni, int jIni, int iFim, int jFim){
        Posição posIni = new Posição(iIni, jIni);
        Posição posFim = new Posição(iFim, jFim);
        regras.moverPeça(posIni, posFim);
    }
    
}
