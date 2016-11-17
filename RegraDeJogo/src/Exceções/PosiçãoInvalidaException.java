/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceções;

/**
 *
 * @author Max
 */
public class PosiçãoInvalidaException extends IllegalArgumentException {
    
    public PosiçãoInvalidaException(String msg){
        super(msg);
    }
}
