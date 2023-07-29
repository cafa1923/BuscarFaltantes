/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.JOptionPane;
/**
 *
 * @author dcastro
 */
public class Controller {
    private Archivos archivo;
    private Conexion cn;
    private String carpetaArchivos;
    private String listaBuscar;
    private String modeloBusqueda;
    private static ArrayList listeners ;
    private int countBuscar;
    private int countEncontrada;
    private String message;
    private String messageError;
    public Controller(Archivos archivo,String lista,String modelo){
        this.archivo = archivo;
        this.listaBuscar = lista;
        this.modeloBusqueda = modelo;
        listeners = new ArrayList(); 
        this.cn=new Conexion();
    }
    
    public void addEventListener(ChangeEventListenerController listener){
        listeners.add(listener);
    }
    private void tiggerCountBuscarEvent(int count){
        ListIterator li = listeners.listIterator();
        while(li.hasNext()){
            ChangeEventListenerController listener = (ChangeEventListenerController)li.next();
            ChangeEventController readerEvObj = new ChangeEventController(this,this);
            (listener).onCountBuscarChange(readerEvObj, count);
        }
        
    }
    private void tiggerCountEncontradaEvent(int count){
        ListIterator li = listeners.listIterator();
        while(li.hasNext()){
            ChangeEventListenerController listener = (ChangeEventListenerController)li.next();
            ChangeEventController readerEvObj = new ChangeEventController(this,this);
            (listener).onCountEncontradoChange(readerEvObj, count);
        }
        
    }
    private void tiggerMesaggeEvent(String message){
        ListIterator li = listeners.listIterator();
        while(li.hasNext()){
            ChangeEventListenerController listener = (ChangeEventListenerController)li.next();
            ChangeEventController readerEvObj = new ChangeEventController(this,this);
            (listener).onMenssageChange(readerEvObj, message);
        }
        
    }
    private void tiggerMesaggeErrorEvent(String message){
        ListIterator li = listeners.listIterator();
        while(li.hasNext()){
            ChangeEventListenerController listener = (ChangeEventListenerController)li.next();
            ChangeEventController readerEvObj = new ChangeEventController(this,this);
            (listener).onMenssageErrorChange(readerEvObj, message);
        }
        
    }

    public void procesar(){
        this.setMessage("Iniciando Proceso.....");
        eliminarLista();
        eliminarArchivos();
        crearCarpetaSalida();
        leerListaBusqueda();
        File directorio= new File(archivo.getRutaArchivos());        
        archivo.listarArchivos(directorio);       
        escribirArchivoRutas();
        readCsvUsingLoad();       
        consultaArchivos();
        eliminarArchivoRutas();
        /*  subirRutasBase();*/
        JOptionPane.showMessageDialog(null, "Proceso Terminado");
        
        
    }
    public void leerListaBusqueda(){
        this.setMessage("Subiendo cuentas a DB.....");
        //Connection conn=cn.conn();
        BufferedReader br = null;
        try 
        {
            //String separador = System.getProperty("file.separator");
           // String fichero = new File(this.getListaBuscar()).getParentFile()+separador+"rutas.csv";
            Connection conn=cn.conn();
 
            String loadQuery = "LOAD DATA LOCAL INFILE '" +obtenerRutaCorregidaWindows(getListaBuscar())  + "' INTO TABLE faltantecorreosindex FIELDS TERMINATED BY '\t'" + " LINES TERMINATED BY '\r\n' (cta)";
            //String loadQuery = "LOAD DATA LOCAL INFILE '" +obtenerRutaCorregidaWindows(getListaBuscar())  + "' INTO TABLE faltantecorreosindex FIELDS TERMINATED BY ';'" + " LINES TERMINATED BY '\n' (cta)";            
//System.out.println(loadQuery);
            Statement stmt = conn.createStatement();
            stmt.execute(loadQuery);
            br = new BufferedReader(new FileReader(getListaBuscar()));
            String texto = br.readLine();
            while(texto != null) {
                texto = br.readLine();
                this.setCountBuscar(this.getCountBuscar()+ 1);                
            }
        }
        catch (SQLException ex)
        {
            this.setMessageError(ex.getMessage());
            //System.out.println("error al subir archivo"+ex.getMessage());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    
    }
    public void subirRutasBase(){ 
        this.setMessage("Listando archivos.....");
        Connection conn=cn.conn();
        
        
        for(File rutaArchivo:archivo.getFiles()){
            try {
                Statement s = conn.createStatement();
                String nuevaRuta =obtenerRutaCorregidaWindows(rutaArchivo.getPath());
                //System.out.print("nueva ruta:"+nuevaRuta);
                String idArchivo=extraeIdArchivo(rutaArchivo,modeloBusqueda);
                String query = "insert into faltantescorreos (cta,ubicacion) value ("+idArchivo+",'"+nuevaRuta+"')";
                s.execute(query);
                //System.out.print("cuenta: "+idArchivo);
            } catch (SQLException ex) {
                this.setMessageError(ex.getMessage());
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    
    }
    public void escribirArchivoRutas(){ 
        this.setMessage("Subiendo Rutas.....");
        String separador = System.getProperty("file.separator");
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(new File(this.getListaBuscar()).getParentFile()+separador+"rutas.csv");
            pw = new PrintWriter(fichero);
            for(File rutaArchivo:archivo.getFiles()){
                System.out.println("escribir: "+ rutaArchivo+"/n");
                String nuevaRuta =obtenerRutaCorregidaWindows(rutaArchivo.getPath());
                //String nuevaRuta =rutaArchivo.getPath();
                String idArchivo=extraeIdArchivo(rutaArchivo,modeloBusqueda);
                System.out.println("id_archivo: "+ idArchivo+"/n");
                pw.println(idArchivo+";"+ nuevaRuta);
            }

        } catch (IOException ex) {
            this.setMessageError(ex.getMessage());
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (IOException ex) {
               this.setMessageError(ex.getMessage());
           }
        }
    
    }
    public  void readCsvUsingLoad()
    {
        try 
        {
            String separador = System.getProperty("file.separator");
            String fichero = new File(this.getListaBuscar()).getParentFile()+separador+"rutas.csv";
            Connection conn=cn.conn();
 
            String loadQuery = "LOAD DATA LOCAL INFILE '" +obtenerRutaCorregidaWindows(fichero)  + "' INTO TABLE faltantescorreos FIELDS TERMINATED BY ';'" + " LINES TERMINATED BY '\r\n' (cta, ubicacion)";
            //System.out.println(loadQuery);
            Statement stmt = conn.createStatement();
            stmt.execute(loadQuery);
        }
        catch (SQLException ex)
        {
            this.setMessageError(ex.getMessage());
            //System.out.println("error al subir archivo"+ex.getMessage());
        }
    }
    
    public String obtenerRutaCorregidaWindows(String ruta)
    {
        String separador="\\";
        String nuevoSeparador="\\\\";
         StringTokenizer tokens=new StringTokenizer(ruta, separador);
         //Para guardar la ruta corregida
         String rutaCorregida = new String();
         //Contar los tokens (en este caso las carpetas, contado tambien
         // el nombre del archivo seleccionado).
         int noTokens = tokens.countTokens();
         int i = 1;
         do
         {      //Agregar el nuevo separador
            rutaCorregida += tokens.nextToken()+nuevoSeparador;
            i++;
         }while(i<noTokens);
         //Agregar a la ruta corregida el ultimo token, (nombre del archivo)
         rutaCorregida += tokens.nextToken();       
         //Mostrar la ruta corregida en la consola
        // System.out.println(rutaCorregida+"\n"+noTokens+ " tokens");
         return rutaCorregida;
    }
    
     public String extraeIdArchivo(File archivo,String modelo){
         String id="" ;
        if(!"credi".equals(modelo)){         
        String nombreArchivo[]=archivo.getName().replace(".txt", "").split("_");        
        System.out.println("largo nombre: "+archivo.getName());
        switch (modelo){
          case "bg" -> {
              if(nombreArchivo.length==3){
                  System.out.println("entre a cc: "+ nombreArchivo[1]);
                  id = nombreArchivo[2];
              }
              if(nombreArchivo.length==4){
                  System.out.println("entre a Tc: "+ nombreArchivo[3]);
                  id= nombreArchivo[3];
              }
            }
            case "bbp" -> id=nombreArchivo[1];
            case "adp" -> id=nombreArchivo[1];
            case "intr" -> id=nombreArchivo[0];
            case "mupi" -> id=nombreArchivo[1];
            default -> id="";               
             
        }
       /* id = switch (modelo) {
            case "bg" -> nombreArchivo[3].replace(".txt", "");
            case "bbp" -> nombreArchivo[1];
            case "adp" -> nombreArchivo[1];
            case "intr" -> nombreArchivo[0];
            case "mupi" -> nombreArchivo[1];
            default -> "";
        };*/
       System.out.println("Salida nombre: "+id);
        //return id;
        }
         else{
            id=archivo.getName().replace(".txt", "");
         }
        return id;
    }
     public void consultaArchivos(){
         this.setMessage("Match archivos.....");
        try {
            Connection conn=cn.conn();
            String ubicacion;
            PreparedStatement pst=conn.prepareStatement("SELECT a.cta,b.ubicacion FROM faltantecorreosindex a left join faltantescorreos b on a.cta=b.cta");
            ResultSet rs=pst.executeQuery();
            while(rs.next())
            {
                
                 ubicacion=rs.getString("ubicacion") ;
                 //System.out.print("si existe:" +rs.getString("ubicacion"));
                if(ubicacion !=null){
                    //System.out.print("si existe:" +ubicacion);
                    copiarArchivos(ubicacion);
                    this.setCountEncontrada(this.getConuntEncontrada()+1);
                }else{
                    escribirNoEncontradas(rs.getString("cta"));
                }
                
                
            }
        } catch (SQLException ex) {
            this.setMessageError(ex.getMessage());
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

     }
    public void copiarArchivos(String ruta){
        try {
            String separador = System.getProperty("file.separator");
            Path origenPath = Paths.get(ruta.trim());
            Path destinoPath = Paths.get(new File(getListaBuscar()).getParentFile()+separador+"Salida"+separador+origenPath.getFileName());
            Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
            
        } catch (IOException ex) {
            this.setMessageError(ex.getMessage());
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public void crearCarpetaSalida(){
        this.setMessage("Creando Carpeta Salida.....");
        String separador = System.getProperty("file.separator");
        File directorio =new File(new File(getListaBuscar()).getParent() +separador+"Salida");
        System.out.print("carpeta salida1: "+directorio.getPath());
        if(!directorio.exists() ){
                if(directorio.mkdirs()){
                    System.out.print("carpeta creada");
                }            
            }else{
            
            //borrarSalida(directorio);
            //crearCarpetaSalida();
        }    
    }
    
    public void borrarSalida(File directorio){
        this.setMessage("limpiar Carpeta Salida.....");
        File[] ficheros = directorio.listFiles();
        for(File fichero:ficheros){
            if (fichero.isDirectory()) {
                borrarSalida(fichero);
            }
            fichero.delete();
        
        }
    }
    public void eliminarLista(){
        this.setMessage("borrando DB.....");
         Connection conn=cn.conn();
            try {
                Statement s = conn.createStatement();
                String query = "truncate faltantecorreosindex";
                s.execute(query);
                //System.out.print("cuenta: "+idArchivo);
            } catch (SQLException ex) {
                this.setMessageError(ex.getMessage());
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
     }
    public void eliminarArchivos(){
        this.setMessage("borrando DB.....");
         Connection conn=cn.conn();
            try {
                Statement s = conn.createStatement();
                String query = "truncate faltantescorreos";
                s.execute(query);
                //System.out.print("cuenta: "+idArchivo);
            } catch (SQLException ex) {
                System.out.print("cuenta: errrrrrrrororororor");
                this.setMessageError(ex.getMessage());
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
     }
    
    public void escribirNoEncontradas(String cuenta){
        this.setMessage("Escribiendo no encontrada.....");
        FileWriter fichero = null;
        PrintWriter pw = null;
        String salida = getListaBuscar()+"_Repo.txt";
        try
        {
            fichero = new FileWriter(salida,true);
            pw = new PrintWriter(fichero);
            pw.println(cuenta);               

        } catch (IOException e) {
            this.setMessageError(e.getMessage());
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (IOException e2) {
           }
        }   
    
    }
    private void eliminarArchivoRutas(){
        File fichero =new File(new File(this.getListaBuscar()).getParentFile()+"\\"+"rutas.csv") ;
        if (fichero.exists()){
        fichero.delete();
        }   
    
    }

    /**
     * @return the carpetaArchivos
     */
    public String getCarpetaArchivos() {
        return carpetaArchivos;
    }

    /**
     * @param carpetaArchivos the carpetaArchivos to set
     */
    public void setCarpetaArchivos(String carpetaArchivos) {
        this.carpetaArchivos = carpetaArchivos;
    }

    /**
     * @return the listaBuscar
     */
    public String getListaBuscar() {
        return listaBuscar;
    }

    /**
     * @param listaBuscar the listaBuscar to set
     */
    public void setListaBuscar(String listaBuscar) {
        this.listaBuscar = listaBuscar;
    }

    /**
     * @return the modeloBusqueda
     */
    public String getModeloBusqueda() {
        return modeloBusqueda;
    }

    /**
     * @param modeloBusqueda the modeloBusqueda to set
     */
    public void setModeloBusqueda(String modeloBusqueda) {
        this.modeloBusqueda = modeloBusqueda;
    }

    /**
     * @return the countBuscar
     */
    public int getCountBuscar() {
        return countBuscar;
    }

    /**
     * @param countBuscar the countBuscar to set
     */
    public void setCountBuscar(int countBuscar) {
        //System.out.println("Se cambia contador "+ countBuscar);        
        this.countBuscar = countBuscar;
        this.tiggerCountBuscarEvent(countBuscar);
    }

    /**
     * @return the conuntEncontrada
     */
    public int getConuntEncontrada() {
        return countEncontrada;
    }

    /**
     * @param countEncontrada the conuntEncontrada to set
     */
    public void setCountEncontrada(int countEncontrada) {
        this.tiggerCountEncontradaEvent(countEncontrada);
        this.countEncontrada = countEncontrada;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
        this.tiggerMesaggeEvent(message);
    }

    /**
     * @return the messageError
     */
    public String getMessageError() {
        return messageError;
    }

    /**
     * @param messageError the messageError to set
     */
    public void setMessageError(String messageError) {
        this.messageError = messageError;
        this.tiggerMesaggeErrorEvent(messageError);
    }
    
}
