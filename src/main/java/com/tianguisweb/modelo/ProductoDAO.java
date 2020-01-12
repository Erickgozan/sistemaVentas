
package com.tianguisweb.modelo;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;


public class ProductoDAO {
    

    private DataSource origenDatos;
    private Connection conexion;
    
    public ProductoDAO(DataSource origenDatos){
        this.origenDatos = origenDatos;
    }
    
    public void agregarProducto(Producto p){
        PreparedStatement ps;
        
        try {    
            String sql = "INSERT INTO producto(Nombres,Foto,Descripcion,Precio,Stock) VALUES(?,?,?,?,?)";
            conexion = origenDatos.getConnection();
            ps=conexion.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setBlob(2, p.getImagen());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getStock());
            ps.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println("Error en el metodo agregarProducto " + ex.getLocalizedMessage());
        }
    }
    
}
