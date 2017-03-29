/*
 * Copyright 2017 Soluciones LEAAL.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.leaal.servlets.torneo;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import static net.leaal.jee.UtilJee.format;
import net.leaal.jee.torneo.entidad.DetalleDeVenta;
import net.leaal.jee.torneo.entidad.DetalleDeVentaPK;
import net.leaal.jee.torneo.entidad.Producto;
import static net.leaal.jee.UtilJee.getFormatoEntero;
import static net.leaal.jee.UtilJee.getFormatoFechaHora;
import static net.leaal.jee.UtilJee.getFormatoPrecio;
import static net.leaal.jee.UtilJee.isNullOrEmpty;
import static net.leaal.jee.UtilJee.parseEntero;
import static net.leaal.jee.UtilJee.parseFechaHoraWeb;
import net.leaal.jee.torneo.entidad.Cliente;
import net.leaal.jee.torneo.entidad.Venta;
import net.leaal.servlets.CtrlAbc;
import net.leaal.servlets.ModeloForm;
import net.leaal.servlets.Renglon;
import static net.leaal.servlets.UtilServlets.getImagenUrl;
import net.leaal.servlets.torneo.form.ModeloFormCarrito;

/**
 *
 * @author Soluciones LEAAL
 */
@WebServlet(name = "CtrlCarrito", urlPatterns = "ctrlCarrito")
public class CtrlCarrito extends
    CtrlAbc<DetalleDeVenta, DetalleDeVentaPK, ModeloFormCarrito> {
  public CtrlCarrito() {
    super(null, DetalleDeVenta.class, DetalleDeVentaPK.class,
        ModeloFormCarrito.class, "Carrito de Compras", "Selecciona un Producto");
  }
  @Override protected void procesaAccion(HttpServletRequest req,
      ModeloForm<ModeloFormCarrito> modeloForm) {
    switch (modeloForm.getValores().getAccion()) {
    case "action_guardar_venta":
      guardaVenta(req, modeloForm);
      break;
    case "action_pagar":
      registra(req, modeloForm);
    }
  }
  @Override protected void elimina(HttpServletRequest req,
      ModeloForm<ModeloFormCarrito> modeloForm) {
    EntityManager em = null;
    try {
      final String usuarioId = getUsuarioId(req);
      em = iniciaTransaccion();
      final Cliente cliente = em.find(Cliente.class, usuarioId);
      final Venta venta = cliente.getVentaActual();
      final DetalleDeVenta modelo = buscaModelo(modeloForm, em);
      if (venta.elimina(modelo.getProducto())) {
        em.merge(venta);
        em.flush();
        inicia(em, req, modeloForm);
      }
      commit(em);
    } catch (Exception e) {
      procesaError(modeloForm, "Error guardado venta.", e);
    } finally {
      cierra(em);
    }
  }
  public void guardaVenta(HttpServletRequest req,
      ModeloForm<ModeloFormCarrito> modeloForm) {
    final ModeloFormCarrito valores = modeloForm.getValores();
    EntityManager em = null;
    try {
      final String usuarioId = getUsuarioId(req);
      em = iniciaTransaccion();
      final Cliente cliente = em.find(Cliente.class, usuarioId);
      final Venta venta = cliente.getVentaActual();
      venta.setDireccionDeEntrega(valores.getDireccionDeEntrega());
      venta.setFechaHoraDeEntrega(parseFechaHoraWeb(
          valores.getFechaHoraDeEntrega(), getTimeZone(),
          "Valor incorrecto para Fecha y Hora de Entrega."));
      commit(em);
      modeloForm.setSiguienteForm("torneo-inicio");
    } catch (Exception e) {
      procesaError(modeloForm, "Error guardado venta.", e);
    } finally {
      cierra(em);
    }
  }
  public void registra(HttpServletRequest req,
      ModeloForm<ModeloFormCarrito> modeloForm) {
    final ModeloFormCarrito valores = modeloForm.getValores();
    EntityManager em = null;
    try {
      if (isNullOrEmpty(valores.getFechaHoraDeEntrega())) {
        throw new RuntimeException("Falta la fecha de entrega.");
      } else if (isNullOrEmpty(valores.getDireccionDeEntrega())) {
        throw new RuntimeException("Falta la dirección de entrega.");
      }
      final String usuarioId = getUsuarioId(req);
      em = iniciaTransaccion();
      final Cliente cliente = em.find(Cliente.class, usuarioId);
      final Venta venta = cliente.getVentaActual();
      venta.setDireccionDeEntrega(valores.getDireccionDeEntrega());
      venta.setFechaHoraDeEntrega(parseFechaHoraWeb(
          valores.getFechaHoraDeEntrega(), getTimeZone(),
          "Valor incorrecto para Fecha y Hora de Entrega."));
      venta.setRegistro(new Date());
      venta.getDetalles().stream().forEach(d -> {
        d.setPrecio(d.getProducto().getPrecio());
        d.getProducto().decrementaExistencias(d.getCantidad());
      });
      final Venta ventaNueva = new Venta();
      cliente.setVentaActual(ventaNueva);
      ventaNueva.setCliente(cliente);
      commit(em);
      modeloForm.setSiguienteForm("torneo-inicio");
      CtrlNotificacion.
          notifica(usuarioId, "Venta en proceso de entrega: " + venta.getId());
    } catch (RuntimeException | IOException e) {
      procesaError(modeloForm, "Error guardado venta.", e);
    } finally {
      cierra(em);
    }
  }
  @Override protected String getTituloDeModelo(DetalleDeVenta modelo) {
    return modelo.getProducto().getNombre();
  }
  @Override protected List<Renglon> consulta(EntityManager em,
      HttpServletRequest req, ModeloForm<ModeloFormCarrito> modeloForm) {
    final ModeloFormCarrito valores = modeloForm.getValores();
    final String usuarioId = getUsuarioId(req);
    final Cliente cliente = em.find(Cliente.class, usuarioId);
    final Venta venta = cliente.getVentaActual();
    valores.setFechaHoraDeEntrega(format(getFormatoFechaHora(getTimeZone()),
        venta.getFechaHoraDeEntrega()));
    valores.setDireccionDeEntrega(venta.getDireccionDeEntrega());
    valores.setTotal(format(getFormatoPrecio(), venta.getTotal()));
    return venta.getDetalles().stream().map(c -> getRenglon(req, c)).
        collect(Collectors.toList());
  }
  @Override
  protected Renglon getRenglon(HttpServletRequest req, DetalleDeVenta modelo) {
    final Producto producto = modelo.getProducto();
    return new Renglon(modelo.getId(),
        getImagenUrl(req, producto.getImagen().getId()),
        modelo.getProducto().getNombre(),
        MessageFormat.format(
            "{0,number,#,##0.00} a ${1,number,#,##0.00} = ${2,number,#,##0.00}",
            modelo.getCantidad(), modelo.getPrecio(), modelo.getSubtotal()));
  }
  @Override protected void llenaModelo(EntityManager em,
      ModeloForm<ModeloFormCarrito> modeloForm, DetalleDeVenta modelo) {
    modelo.setCantidad(parseEntero(modeloForm.getValores().getCantidad(),
        "Formato incorrecto para cantidad."));
  }
  @Override protected void muestraModelo(EntityManager em,
      HttpServletRequest req, ModeloForm<ModeloFormCarrito> modeloForm,
      DetalleDeVenta modelo) {
    final ModeloFormCarrito valores = modeloForm.getValores();
    valores.setImg(getImagenUrl(req, modelo.getProducto().getImagen().getId()));
    valores.setNombre(modelo.getProducto().getNombre());
    valores.setCantidad(format(getFormatoEntero(), modelo.getCantidad()));
    valores.setPrecio(format(getFormatoPrecio(), modelo.getPrecio()));
    valores.setCategoria(modelo.getProducto().getCategoria().getNombre());
  }
  protected static TimeZone getTimeZone() {
    return TimeZone.getTimeZone("America/Mexico_City");
  }
}
