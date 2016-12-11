/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excecoes;

/**
 *
 * @author Max
 */
public class JogadaInvalidaException extends IllegalArgumentException {
    
    public JogadaInvalidaException(String msg){
        super(msg);
    }
}
