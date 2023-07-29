/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
/**
 *
 * @author dcastro
 */
import java.io.File;
import java.util.ArrayList;
import java.util.ListIterator;

public class Archivos {
    private String fileName;
    private ArrayList<File> files;
    private static ArrayList listeners ;   
    
    public Archivos(String rutaArchivos){
        this.rutaArchivos = rutaArchivos;
        files = new ArrayList<>();
        listeners = new ArrayList();        
    }
    
    public void addEventListener(ChangeEventListener listener){
        listeners.add(listener);
    }
    private void tiggerFileNameEvent(String fileName){
        ListIterator li = listeners.listIterator();
        while(li.hasNext()){
            ChangeEventListener listener = (ChangeEventListener)li.next();
            ChangeEvent readerEvObj = new ChangeEvent(this,this);
            (listener).onFileNameChange(readerEvObj, fileName);
        }
        
    }
    
    public void listarArchivos(File directorios){
        File[] ficheros = directorios.listFiles();               
        if(ficheros!=null){
            for (File fichero : ficheros) {
                if (fichero.isDirectory()) {
                    listarArchivos(fichero);
                    
                }else{
                    getFiles().add(fichero);
                    this.setFileName(fichero.getName());
                }
            }
        }
    }
    
   
    
    private String rutaArchivos;

    /**
     * @return the rutaArchivos
     */
    public String getRutaArchivos() {
        return rutaArchivos;
    }

    /**
     * @param rutaArchivos the rutaArchivos to set
     */
    public void setRutaArchivos(String rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.tiggerFileNameEvent(fileName);
    }

    /**

  

    /**
     * @return the files
     */
    public ArrayList<File> getFiles() {
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }
    
    
    
}
