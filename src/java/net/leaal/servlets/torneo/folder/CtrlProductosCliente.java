/*
 * Copyright 2016 Soluciones LEAAL.
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

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import static net.leaal.jee.UtilJee.format;
import static net.leaal.jee.UtilJee.getFormatoPrecio;
import net.leaal.jee.torneo.entidad.Cliente;
import net.leaal.jee.torneo.entidad.DetalleDeVenta;
import net.leaal.jee.torneo.entidad.Producto;
import net.leaal.jee.torneo.entidad.Venta;
import net.leaal.servlets.CtrlAbc;
import net.leaal.servlets.ModeloForm;
import net.leaal.servlets.Renglon;
import static net.leaal.servlets.UtilServlets.getImagenUrl;
import net.leaal.servlets.torneo.form.ModeloFormProductosCliente;

/**
 *
 * @author Soluciones LEAAL
 */
@WebServlet(name = "CtrlProductosCliente", urlPatterns = "ctrlProductosCliente")
public class CtrlProductosCliente extends
    CtrlAbc<Producto, Long, ModeloFormProductosCliente> {
  public CtrlProductosCliente() {
    super(Producto.FILTRO, Producto.class, Long.class,
        ModeloFormProductosCliente.class, "Productos", "Selecciona un Producto");
  }
  @Override protected Renglon getRenglon(HttpServletRequest req, Producto modelo) {
    return new Renglon(modelo.getId(),
        getImagenUrl(req, modelo.getImagen().getId()),
        modelo.getNombre(), format(getFormatoPrecio(), modelo.getPrecio()));
  }
  @Override protected void procesaAccion(HttpServletRequest req,
      ModeloForm<ModeloFormProductosCliente> modeloForm) {
    switch (modeloForm.getValores().getAccion()) {
    case "action_comprar":
      compra(req, modeloForm);
      break;
    }
  }
  @Override protected String getTituloDeModelo(Producto modelo) {
    return modelo.getNombre();
  }
  @Override protected void llenaModelo(EntityManager em,
      ModeloForm<ModeloFormProductosCliente> modeloForm, Producto modelo) {
  }
  @Override protected void muestraModelo(EntityManager em,
      HttpServletRequest req, ModeloForm<ModeloFormProductosCliente> modeloForm,
      Producto modelo) {
    final ModeloFormProductosCliente valores = modeloForm.getValores();
    valores.setImg(getImagenUrl(req, modelo.getImagen().getId()));
    valores.setNombre(modelo.getNombre());
    valores.setPrecio(format(getFormatoPrecio(), modelo.getPrecio()));
    valores.setCategoria(modelo.getCategoria().getNombre());
  }
  public void compra(HttpServletRequest req,
      ModeloForm<ModeloFormProductosCliente> modeloForm) {
    EntityManager em = null;
    try {
      final String usuarioId = getUsuarioId(req);
      em = iniciaTransaccion();
      final Producto modelo = buscaModelo(modeloForm, em);
      final Cliente cliente = em.find(Cliente.class, usuarioId);
      final Venta venta = cliente.getVentaActual();
      final Optional<DetalleDeVenta> detalle = venta.busca(modelo);
      if (!detalle.isPresent()) {
        venta.getDetalles().add(new DetalleDeVenta(venta, modelo));
      }
      commit(em);
      if (!detalle.isPresent()) {
        modeloForm.setSiguienteForm("form-carrito");
      }
    } catch (Exception e) {
      procesaError(modeloForm, "Error comprando producto.", e);
    } finally {
      cierra(em);
    }
  }
}
