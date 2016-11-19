package regradejogo;

import java.util.List;

/**
 * Classe que guarda todas as informações de uma jogada.
 * @author Max
 */
public class Jogada {
    private Peça peçaCapturada;
    private Peça peçaMovida;
    private Posição posInicial;
    private Posição posFinal;

    public Jogada(Peça peçaCapturada, Peça peçaMovida, Posição posInicial, Posição posFinal) {
        this.peçaCapturada = peçaCapturada;
        this.peçaMovida = peçaMovida;
        this.posInicial = posInicial;
        this.posFinal = posFinal;
    }

    public Jogada() {
         
    }
    
    public boolean houveCaptura(){
        return peçaCapturada != null;
    }

    public Peça getPeçaCapturada() {
        return peçaCapturada;
    }

    public void setPeçaCapturada(Peça peçaCapturada) {
        this.peçaCapturada = peçaCapturada;
    }

    public Peça getPeçaMovida() {
        return peçaMovida;
    }

    public void setPeçaMovida(Peça peçaMovida) {
        this.peçaMovida = peçaMovida;
    }

    public Posição getPosInicial() {
        return posInicial;
    }

    public void setPosInicial(Posição posInicial) {
        this.posInicial = posInicial;
    }

    public Posição getPosFinal() {
        return posFinal;
    }

    public void setPosFinal(Posição posFinal) {
        this.posFinal = posFinal;
    }

    @Override
    public String toString() {
        String posCapturada = peçaCapturada == null? "null" : peçaCapturada.toString();
        String posMovida = peçaMovida == null? "null" : peçaMovida.toString();
        return "Jogada{" + "peçaCapturada=" + posCapturada + ", peçaMovida=" + posMovida + ", posInicial=" + posInicial.toString() + ", posFinal=" + posFinal.toString() + '}';
    }
    
    
    
    
}
