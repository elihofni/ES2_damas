package regradejogo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class Posição {
    private int i;
    private int j;
    
    public Posição(int i, int j){
        this.i = i;
        this.j = j;
    }
    
    public Posição(){
        
    }
    
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
    
    public boolean equals(Posição pos){
        return (this.i == pos.i) && (this.j == pos.j);
    }
    
    @Override
    public String toString(){
        return "[" + String.valueOf(i) + ";" + String.valueOf(j) + "]";
    }
}
