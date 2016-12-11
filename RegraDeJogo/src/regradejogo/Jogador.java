package regradejogo;

import java.util.ArrayList;
import java.util.List;
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
    
    public int consultarPosicao(int i, int j){
        Peca peca = regras.getTabuleiro().getPeca(new Posicao(i, j));
        
        if(peca == null){
            return Tabuleiro.VAZIO;
        }
        
        if(peca.getTime() == 1 && !peca.isDama()){
            return Tabuleiro.PECA_TIME1;
        }
        
        if(peca.getTime() == 2 && !peca.isDama()){
            return Tabuleiro.PECA_TIME2;
        }
        
        if(peca.getTime() == 1 && peca.isDama()){
            return Tabuleiro.DAMA_TIME1;
        }
        
        return Tabuleiro.DAMA_TIME2;
    }
    
    public List<Jogada> getJogadasPossiveis(Posicao pos){
        Peca peca = regras.getPeca(pos);
        
        if(peca != null){
            if(regras.getPecasAptasDoJogadorAtual().indexOf(peca) == -1){
                return new ArrayList<>();
            }
            
            return peca.isDama()? regras.jogadasPossiveisDama(peca) : regras.jogadasPossiveis(peca);
        }
        
        return new ArrayList<>();
    }
    
    public int getTurno(){
        return regras.getTurno();
    }

    public int getTime() {
        return time;
    }
    
    
    
}
