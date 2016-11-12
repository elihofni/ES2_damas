package regradejogo;

public class Peça {
    private int time;
    private boolean dama;
    
    public Peça(int time, boolean dama){
        this.time = time;
        this.dama = dama;
    }
    
    public Peça(){
        
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
}
