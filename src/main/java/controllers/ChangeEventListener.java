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
public interface ChangeEventListener extends EventListener {
    public abstract void onFileNameChange(ChangeEvent ev,String fileName);
    
}
