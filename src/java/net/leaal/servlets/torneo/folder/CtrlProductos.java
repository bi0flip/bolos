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

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import static net.leaal.jee.UtilJee.format;
import static net.leaal.jee.UtilJee.getFormatoEntero;
import static net.leaal.jee.UtilJee.getFormatoPrecio;
import static net.leaal.jee.UtilJee.parseEntero;
import static net.leaal.jee.UtilJee.parsePrecio;
import net.leaal.jee.Archivo;
import net.leaal.jee.torneo.entidad.Categoria;
import net.leaal.jee.torneo.entidad.Producto;
import net.leaal.servlets.CtrlAbc;
import net.leaal.servlets.ModeloForm;
import net.leaal.servlets.Opcion;
import net.leaal.servlets.Renglon;
import static net.leaal.servlets.UtilServlets.buscaId;
import static net.leaal.servlets.UtilServlets.getId;
import static net.leaal.servlets.UtilServlets.getImagenUrl;
import static net.leaal.servlets.UtilServlets.getOpciones;
import static net.leaal.servlets.UtilServlets.texto;
import net.leaal.servlets.torneo.form.ModeloFormProductos;

/**
 *
 * @author Soluciones LEAAL
 */
@WebServlet(name = "CtrlProductos", urlPatterns = "ctrlProductos")
public class CtrlProductos extends CtrlAbc<Producto, Long, ModeloFormProductos> {
  public CtrlProductos() {
    super(Producto.FILTRO, Producto.class, Long.class, ModeloFormProductos.class,
        "Productos", "Producto Nuevo");
  }
  @Override protected Renglon getRenglon(HttpServletRequest req, Producto modelo) {
    return new Renglon(modelo.getId(),
        getImagenUrl(req, modelo.getImagen().getId()),
        modelo.getNombre(), format(getFormatoPrecio(), modelo.getPrecio()));
  }
  @Override protected String getTituloDeModelo(Producto modelo) {
    return modelo.getNombre();
  }
  @Override protected void llenaModelo(EntityManager em,
      ModeloForm<ModeloFormProductos> modeloForm, Producto modelo) {
    final boolean nuevo = isNuevo(modeloForm);
    final ModeloFormProductos valores = modeloForm.getValores();
    if (nuevo && valores.getImagen() == null) {
      throw new RuntimeException("Falta seleccionar la imagen.");
    } else {
      modelo.setCategoria(
          buscaId(em, Categoria.class, Long.class, valores.getCategoria()));
      if (nuevo || valores.getImagen() != null) {
        modelo.setImagen(new Archivo(valores.getImagen()));
      }
      modelo.setNombre(valores.getNombre());
      modelo.setExistencias(parseEntero(valores.getExistencias(),
          "Formato incorrecto para existencias."));
      modelo.setPrecio(parsePrecio(valores.getPrecio(),
          "Formato incorrecto para precio."));
    }
    valores.setImagen(null);
  }
  @Override protected void muestraModelo(EntityManager em,
      HttpServletRequest req, ModeloForm<ModeloFormProductos> modeloForm,
      Producto modelo) {
    final ModeloFormProductos valores = modeloForm.getValores();
    valores.setImg(isNuevo(modeloForm) ? ""
        : getImagenUrl(req, modelo.getImagen().getId()));
    valores.setImagen(new byte[0]);
    valores.setNombre(texto(modelo.getNombre()));
    valores.setExistencias(format(getFormatoEntero(), modelo.getExistencias()));
    valores.setPrecio(format(getFormatoPrecio(), modelo.getPrecio()));
    valores.setCategoria(getId(modelo.getCategoria()));
  }
  @Override
  protected void muestraOpciones(EntityManager em, HttpServletRequest req,
      ModeloForm<ModeloFormProductos> modeloForm) {
    modeloForm.getOpciones().put("categoria", getOpciones(
        "-- Selecciona Categoría --",
        em.createNamedQuery("Categoria.TODAS", Categoria.class).getResultList(),
        c -> new Opcion(c.getId().toString(), c.getNombre())));
  }
}
