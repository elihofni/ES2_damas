package regradejogo;

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
    
    @Override
    public String toString(){
        return "[" + String.valueOf(time) + ";" + String.valueOf(dama) + "]";
    }
}
