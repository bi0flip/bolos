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

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import net.leaal.servlets.Opcion;
import net.leaal.servlets.Renglon;
import static net.leaal.jee.UtilJee.format;
import net.leaal.jee.torneo.entidad.Cliente;
import static net.leaal.jee.UtilJee.getFormatoFecha;
import static net.leaal.jee.UtilJee.getFormatoHora;
import static net.leaal.jee.UtilJee.isNullOrEmpty;
import static net.leaal.jee.UtilJee.parseFechaWeb;
import static net.leaal.jee.UtilJee.parseHoraWeb;
//import static net.leaal.servlets.torneo.CtrlInicio.CLIENTE;
import net.leaal.jee.torneo.entidad.Usuario;
import net.leaal.jee.torneo.entidad.Venta;
import net.leaal.servlets.CtrlAbc;
import net.leaal.servlets.ModeloForm;
import static net.leaal.servlets.UtilServlets.getId;
import static net.leaal.servlets.UtilServlets.getOpciones;
import net.leaal.servlets.torneo.form.ModeloFormClientes;
import static net.leaal.servlets.UtilServlets.buscaId;

/**
 *
 * @author Soluciones LEAAL
 */
@WebServlet(name = "CtrlClientes", urlPatterns = "ctrlClientes")
public class CtrlClientes extends CtrlAbc<Cliente, String, ModeloFormClientes> {
  public CtrlClientes() {
    super(Cliente.FILTRO, Cliente.class, String.class,
        ModeloFormClientes.class, "Clientes", "Cliente Nuevo");
  }
  @Override
  protected Renglon getRenglon(HttpServletRequest req, Cliente modelo) {
    return new Renglon(modelo.getId(), modelo.getId(),
        modelo.getUsuario().getNombre());
  }
  @Override protected String getTituloDeModelo(Cliente modelo) {
    return modelo.getId();
  }
  @Override protected void llenaModelo(EntityManager em,
      ModeloForm<ModeloFormClientes> modeloForm, Cliente modelo) {
    final ModeloFormClientes valores = modeloForm.getValores();
    final String usuario = valores.getUsuario();
    if (isNuevo(modeloForm) && isNullOrEmpty(usuario)) {
      throw new RuntimeException("Falta seleccionar el usuario.");
    }
    if (isNuevo(modeloForm)) {
      final Venta venta = new Venta();
      venta.setCliente(modelo);
      modelo.setVentaActual(venta);
      modelo.setUsuario(buscaId(em, Usuario.class, String.class, usuario));
    }
    modelo.setNacimiento(parseFechaWeb(valores.getNacimiento(),
        "Formato incorrecto para Nacimiento."));
    modelo.setHoraFavorita(parseHoraWeb(valores.getHoraFavorita(),
        "Formato incorrecto para Hora Favorita."));
  }
  @Override protected void muestraModelo(EntityManager em,
      HttpServletRequest req, ModeloForm<ModeloFormClientes> modeloForm,
      Cliente modelo) {
    final ModeloFormClientes valores = modeloForm.getValores();
    valores.setNacimiento(format(getFormatoFecha(), modelo.getNacimiento()));
    valores.
        setHoraFavorita(format(getFormatoHora(), modelo.getHoraFavorita()));
    valores.setUsuario(getId(modelo.getUsuario()));
  }
   /*@Override protected void muestraOpciones(EntityManager em,
          @Override protected void muestraOpciones(EntityManager em,
          HttpServletRequest req, ModeloForm<ModeloFormClientes> modeloForm) {
          modeloForm.getOpciones().put("usuario", getOpciones(
          "-- Selecciona Usuario --",
          em.createNamedQuery("Usuario.PARA_ROL", Usuario.class).
          setParameter("rol", CLIENTE).getResultList(),
          u -> new Opcion(u.getId(), u.getId() + ": " + u.getNombre())));
          }   HttpServletRequest req, ModeloForm<ModeloFormClientes> modeloForm) {
    modeloForm.getOpciones().put("usuario", getOpciones(
        "-- Selecciona Usuario --",
        em.createNamedQuery("Usuario.PARA_ROL", Usuario.class).
            setParameter("rol", CLIENTE).getResultList(),
        u -> new Opcion(u.getId(), u.getId() + ": " + u.getNombre())));
  }*/
}
