/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tianguisweb.controlador;

import com.tianguisweb.modelo.Producto;
import com.tianguisweb.modelo.ProductoDAO;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

@MultipartConfig
public class Controlador extends HttpServlet {

    @Resource(name = "jdbc/tianguisWeb")
    private DataSource conexion;
    private ProductoDAO productoDao;

    @Override
    public void init() throws ServletException {
        try {
            productoDao = new ProductoDAO(conexion);
        } catch (Exception e) {
            System.out.println("Eroror: " + e.getLocalizedMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String accion = request.getParameter("accion");

        switch (accion) {

            case "agrgegar":
                agregarProducto(request, response);
                break;
            case "home":
              listarProductos(request,response);
              break;
            default:
            listarProductos(request,response);

        }
    }

    private void agregarProducto(HttpServletRequest request, HttpServletResponse response) {

        try {
            PrintWriter out = response.getWriter();
            Producto p;

            String nombreProducto = request.getParameter("nombreProducto");
            Part archivoImagen = request.getPart("archivoImagen");
            InputStream isIMG = archivoImagen.getInputStream();
            String descripcion = request.getParameter("descripcion");
            double precio = Double.parseDouble(request.getParameter("precio"));
            int stock = Integer.parseInt(request.getParameter("stock"));

            p = new Producto();
            p.setNombre(nombreProducto);
            p.setImagen(isIMG);
            p.setDescripcion(descripcion);
            p.setPrecio(precio);
            p.setStock(stock);

            productoDao.agregarProducto(p);

            request.getRequestDispatcher("Controlador?accion=home").forward(request, response);

        } catch (IOException | ServletException ex) {
            System.out.println("Error al agregar el producto " + ex.getLocalizedMessage());
        }

    }
    
      private void listarProductos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Producto> productos;
            productos = productoDao.listar();
            request.setAttribute("productos", productos);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

  

}
