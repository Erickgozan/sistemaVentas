/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tianguisweb.controlador;

import com.tianguisweb.modelo.Carrito;
import com.tianguisweb.modelo.Producto;
import com.tianguisweb.modelo.ProductoDAO;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    PrintWriter out;
    List<Carrito> listaCarrito = new ArrayList<>();
    int item;
    double totalPagar = 0.0;
    int cantidad = 1;

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
            case "comprar":
                comprar(request, response);
                break;
            case "agreagrCarrito":
                agregarCarrito(request, response);
                break;
            case "delete":
                eliminarCarrito(request, response);
            case "carrito":
                listarCarrito(request, response);
                break;
            case "home":
                listarProductos(request, response);
                break;
            default:
                listarProductos(request, response);

        }
    }

    private void agregarProducto(HttpServletRequest request, HttpServletResponse response) {

        try {

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
            out.println("Error al agregar el producto " + ex.getLocalizedMessage());
        }

    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Producto> productos;
            productos = productoDao.listar();
            request.setAttribute("productos", productos);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (SQLException ex) {
            out.print("Error en el metodo listarProductos " + ex.getLocalizedMessage());
        }
    }

    private void agregarCarrito(HttpServletRequest request, HttpServletResponse response) {
        try {
            int pos = 0;
            cantidad = 1;
            int idP = Integer.parseInt(request.getParameter("idProducto"));
            Producto p;
            p = productoDao.listarId(idP);
            if (listaCarrito.size() > 0) {
                for (int i = 0; i < listaCarrito.size(); i++) {
                    if (idP == listaCarrito.get(i).getIdProducto()) {
                        pos = i;
                    }
                }
                if (idP == listaCarrito.get(pos).getIdProducto()) {
                    cantidad = listaCarrito.get(pos).getCantidad() + cantidad;
                    double subtotal = listaCarrito.get(pos).getPrecioCompra() * cantidad;
                    listaCarrito.get(pos).setCantidad(cantidad);
                    listaCarrito.get(pos).setSubTotal(subtotal);
                } else {
                    item += 1;
                    Carrito car = new Carrito();
                    car.setItem(item);
                    car.setIdProducto(p.getIdProducto());
                    car.setNombre(p.getNombre());
                    car.setDescripcion(p.getDescripcion());
                    car.setPrecioCompra(p.getPrecio());
                    car.setCantidad(cantidad);
                    car.setSubTotal(cantidad * p.getPrecio());
                    listaCarrito.add(car);
                }
            } else {
                item += 1;
                Carrito car = new Carrito();
                car.setItem(item);
                car.setIdProducto(p.getIdProducto());
                car.setNombre(p.getNombre());
                car.setDescripcion(p.getDescripcion());
                car.setPrecioCompra(p.getPrecio());
                car.setCantidad(cantidad);
                car.setSubTotal(cantidad * p.getPrecio());
                listaCarrito.add(car);
            }

            request.setAttribute("contador", listaCarrito.size());
            request.getRequestDispatcher("Controlador?accion=home").forward(request, response);
        } catch (SQLException | IOException | ServletException ex) {
            out.print("Error en el metodo agregarCarrito " + ex.getLocalizedMessage());
        }
    }

    private void listarCarrito(HttpServletRequest request, HttpServletResponse response) {
        try {
            totalPagar = 0.0;
            request.setAttribute("carrito", listaCarrito);
            for (int i = 0; i < listaCarrito.size(); i++) {
                totalPagar = totalPagar + listaCarrito.get(i).getSubTotal();
            }
            request.setAttribute("totalApagar", totalPagar);
            request.getRequestDispatcher("carrito.jsp").forward(request, response);
        } catch (IOException | ServletException ex) {
            out.print("Error en el metodo listarCarriot " + ex.getLocalizedMessage());
        }
    }

    private void comprar(HttpServletRequest request, HttpServletResponse response) {

        try {
            totalPagar = 0.0;
            int idP = Integer.parseInt(request.getParameter("idProducto"));
            Producto p;
            p = productoDao.listarId(idP);
            item += 1;
            Carrito car = new Carrito();
            car.setItem(item);
            car.setIdProducto(p.getIdProducto());
            car.setNombre(p.getNombre());
            car.setDescripcion(p.getDescripcion());
            car.setPrecioCompra(p.getPrecio());
            car.setCantidad(cantidad);
            car.setSubTotal(cantidad * p.getPrecio());
            listaCarrito.add(car);
            for (int i = 0; i < listaCarrito.size(); i++) {
                totalPagar = totalPagar + listaCarrito.get(i).getSubTotal();
            }
            request.setAttribute("totalApagar", totalPagar);
            request.setAttribute("carrito", listaCarrito);
            request.setAttribute("contador", listaCarrito.size());
            request.getRequestDispatcher("carrito.jsp").forward(request, response);
        } catch (SQLException | IOException | ServletException ex) {
            out.print("Error en el metodo agregarCarrito " + ex.getLocalizedMessage());
        }
    }

    private void eliminarCarrito(HttpServletRequest request, HttpServletResponse response) {

        int idProducto = Integer.parseInt(request.getParameter("idp"));

        for (int i = 0; i < listaCarrito.size(); i++) {
            if (listaCarrito.get(i).getIdProducto() == idProducto) {
                listaCarrito.remove(i);
            }
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
