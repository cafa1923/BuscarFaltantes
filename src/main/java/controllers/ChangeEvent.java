/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.EventObject;

/**
 *
 * @author dcastro
 */
public class ChangeEvent extends EventObject {
    Archivos archivos;

    public ChangeEvent(Object source,Archivos _archivos) {
        super(source);
        archivos = _archivos;
    }
    
    
}

