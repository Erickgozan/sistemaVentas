
package com.tianguisweb.config;

import javax.sql.DataSource;

public class Conexion {
      
    private DataSource origenDatos;
   
    public Conexion(DataSource origenDatos){
     
            this.origenDatos = origenDatos;
        
    }
        
}
