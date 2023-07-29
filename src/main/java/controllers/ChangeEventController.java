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
public class ChangeEventController extends EventObject{
    Controller controller;

    public ChangeEventController(Object source,Controller _controller) {
        super(source);
        controller = _controller;
    }    
    
}
