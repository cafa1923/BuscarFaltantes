
package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 *
 * @author dcastro
 */
public class Conexion {
    String url = "jdbc:mysql://192.168.11.213:3306/impdig?autoReconnect=true&allowLoadLocalInfile=true";
    String user = "root";
    String pass = "ags1921";
    String driver;
    Connection connection;

    public Conexion() {
        this.driver = "com.mysql.jdbc.Driver";
    }
public Connection conn(){
    try{
        Class.forName(driver);
     //System.out.println("Conectado!!");
         connection = DriverManager.getConnection(url, user,pass);
      /// Resto del codigo aqui
    }catch(SQLException e){
        JOptionPane.showMessageDialog(null, "Error Conexion: "+ e.getMessage()+"/n"+e.getErrorCode()+"/n" +e.getSQLState(),"Mensaje de error",ERROR_MESSAGE);
     System.out.println("mensaje de error " +e.getMessage());
    }   catch (ClassNotFoundException ex) {         
               JOptionPane.showMessageDialog(null, "Error Conexion2: "+ ex.getMessage()+"/n" +ex.getException(),"Mensaje de error",ERROR_MESSAGE);

            //Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    
}
    
}
