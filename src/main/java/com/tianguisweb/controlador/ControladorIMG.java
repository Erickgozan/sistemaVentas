/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tianguisweb.controlador;

import com.tianguisweb.modelo.ProductoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class ControladorIMG extends HttpServlet {

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
       
         try {
             response.setContentType("text/html;charset=UTF-8");
             int idProducto = Integer.parseInt(request.getParameter("id"));
             productoDao.listarImg(idProducto, response); 
             //response.getWriter().print("funciona");
         } catch (SQLException ex) {
             Logger.getLogger(ControladorIMG.class.getName()).log(Level.SEVERE, null, ex);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
