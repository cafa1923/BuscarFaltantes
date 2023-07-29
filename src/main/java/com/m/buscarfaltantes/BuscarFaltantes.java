/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.m.buscarfaltantes;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

import javax.swing.JOptionPane;

import licencia.Licencia;
import licencia.Validate;
import vistas.Busqueda;

/**
 *
 * @author dcastro
 */
public class BuscarFaltantes {
	static Licencia licencia;
	private final static String idApp ="1";

    public static void main(String[] args) {
        //System.out.println("Hello World!");
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	licencia=new Licencia();
				licencia.confirmarXml(idApp);
				if(!licencia.validarLicencia(idApp)) {
		    		//btnProcesar.setVisible(false);
		    		//JOptionPane.showMessageDialog(null, "Se caduco su licencia", "Activar Licencia", 0);
		    		JOptionPane.showMessageDialog(null, "Se caduco su licencia","Mensaje de error",ERROR_MESSAGE);
		    		Validate validar=new Validate();
		    		Validate.showActivarLicencia();
		    		
		    		}else {
		    			new Busqueda().setVisible(true);
		    		}
                
            }
        });
    }
}
