package regradejogo;

/**
 * 
 */
public class Posição {
    private int i;
    private int j;
    
    public void setI(int i){
        this.i = i;
    }
        
    public void setJ(int j){
        this.j = j;
    }
            
    public int getI(){
        return i;
    }
                
    public int getJ(){
        return j;
    }
    
    @Override
    public String toString(){
        return "[" + String.valueOf(i) + ";" + String.valueOf(j) + "]";
    }
}
