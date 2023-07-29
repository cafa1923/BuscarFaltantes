/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controllers;

import java.util.EventListener;

/**
 *
 * @author dcastro
 */
public interface ChangeEventListenerController  extends EventListener {
    public abstract void onCountBuscarChange(ChangeEventController ev,int count);
    public abstract void onCountEncontradoChange(ChangeEventController ev,int count);
    public abstract void onMenssageChange(ChangeEventController ev,String message);
    public abstract void onMenssageErrorChange(ChangeEventController ev,String message);

    
}
