package domain;

import regradejogo.*;

public class Jogador {
    private final Regras regras;
    
    public Jogador(Regras regras){
        this.regras = regras;
    }
    
    public void realizarJogada(Posição posInicial, Posição posFinal){
        Peça peça = regras.getPeça(posInicial);
        regras.moverPeça(peça, posFinal);
    }
    
}
