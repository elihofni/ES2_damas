package regradejogo;

import java.util.List;

/**
 * Classe que guarda todas as informacões de uma jogada.
 *
 * @author Max
 */
public class Jogada {

    private Peca pecaCapturada;
    private Peca pecaMovida;
    private Posicao posInicial;
    private Posicao posFinal;

    public Jogada(Peca pecaCapturada, Peca pecaMovida, Posicao posInicial, Posicao posFinal) {
        this.pecaCapturada = pecaCapturada;
        this.pecaMovida = pecaMovida;
        this.posInicial = posInicial;
        this.posFinal = posFinal;
    }

    public Jogada() {

    }

    public boolean houveCaptura() {
        return pecaCapturada != null;
    }

    public Peca getPecaCapturada() {
        return pecaCapturada;
    }

    public void setPecaCapturada(Peca pecaCapturada) {
        this.pecaCapturada = pecaCapturada;
    }

    public Peca getPecaMovida() {
        return pecaMovida;
    }

    public void setPecaMovida(Peca pecaMovida) {
        this.pecaMovida = pecaMovida;
    }

    public Posicao getPosInicial() {
        return posInicial;
    }

    public void setPosInicial(Posicao posInicial) {
        this.posInicial = posInicial;
    }

    public Posicao getPosFinal() {
        return posFinal;
    }

    public void setPosFinal(Posicao posFinal) {
        this.posFinal = posFinal;
    }

    @Override
    public String toString() {
        String posCapturada = pecaCapturada == null ? "null" : pecaCapturada.toString();
        String posMovida = pecaMovida == null ? "null" : pecaMovida.toString();
        return "Jogada{" + "pecaCapturada=" + posCapturada + ", pecaMovida=" + posMovida + ", posInicial=" + posInicial.toString() + ", posFinal=" + posFinal.toString() + '}';
    }

}
