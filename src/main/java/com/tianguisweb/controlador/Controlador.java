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
    private DataSource pool;
    private ProductoDAO productoDao;

    @Override
    public void init() throws ServletException {
        try {
            productoDao = new ProductoDAO(pool);
        } catch (Exception e) {
            System.out.println("Eroror: " + e.getLocalizedMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String metodo = request.getParameter("agregar");

        switch (metodo) {

            case "insertar":
                agregarProducto(request, response);
                break;

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

            p = new Producto(nombreProducto, isIMG, descripcion, precio, stock);

            productoDao.agregarProducto(p);

            out.print("El producto se agrego correctamente");

        } catch (IOException | ServletException ex) {
            System.out.println("Error al agregar el producto " + ex.getLocalizedMessage());
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
