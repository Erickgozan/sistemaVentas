package com.tianguisweb.modelo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class ProductoDAO {

    private DataSource origenDatos;
    private Connection conexion;
    private PreparedStatement ps;
    private ResultSet rs;

    public ProductoDAO(DataSource origenDatos) {
        this.origenDatos = origenDatos;
    }

    public void agregarProducto(Producto p) {

        try {
            String sql = "INSERT INTO producto(Nombres,Foto,Descripcion,Precio,Stock) VALUES(?,?,?,?,?)";
            conexion = origenDatos.getConnection();
            ps = conexion.prepareStatement(sql);
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

    public List listar() throws SQLException {

        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM producto";
        Producto p;
        try {
            conexion = origenDatos.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                p = new Producto();
                p.setIdProducto(rs.getInt("idProducto"));
                p.setNombre(rs.getString("Nombres"));
                p.setImagen(rs.getBinaryStream("Foto"));
                p.setDescripcion(rs.getString("Descripcion"));
                p.setPrecio(rs.getDouble("Precio"));
                p.setStock(rs.getInt("Stock"));

                productos.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Erroe en el metodo listar" + e.getLocalizedMessage());
        } finally {
            conexion.close();
            ps.close();
        }

        return productos;
    }

    public void listarImg(int id, HttpServletResponse response) throws SQLException {

        String sql = "SELECT * FROM producto WHERE idProducto = " + id;
        InputStream inputStream = null;
        OutputStream outputStream;
        BufferedInputStream bufInputStream;
        BufferedOutputStream bufOutputStream;

        try {
            outputStream = response.getOutputStream();
            conexion = origenDatos.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                inputStream = rs.getBinaryStream("Foto");
            }
            bufInputStream = new BufferedInputStream(inputStream);
            bufOutputStream = new BufferedOutputStream(outputStream);

            int i;
            while ((i = bufInputStream.read()) != -1) {
                bufOutputStream.write(i);
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error listarImg " + e.getLocalizedMessage());
        } finally {
            conexion.close();
            ps.close();
        }
    }

    public Producto listarId(int id) throws SQLException {
        String sql = "SELECT * FROM producto WHERE idProducto=" + id;
        Producto p = new Producto();

        try {
            conexion = origenDatos.getConnection();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {

                p.setIdProducto(rs.getInt("idProducto"));
                p.setNombre(rs.getString("Nombres"));
                p.setImagen(rs.getBinaryStream("Foto"));
                p.setDescripcion(rs.getString("Descripcion"));
                p.setPrecio(rs.getDouble("Precio"));
                p.setStock(rs.getInt("Stock"));

            }
        } catch (SQLException e) {

        } finally {
            conexion.close();
            ps.close();
        }

        return p;
    }

}
