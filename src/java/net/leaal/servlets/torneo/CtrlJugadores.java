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
import net.leaal.jee.torneo.entidad.Jugador;
import static net.leaal.jee.UtilJee.getFormatoFecha;
import static net.leaal.jee.UtilJee.getFormatoHora;
import static net.leaal.jee.UtilJee.isNullOrEmpty;
import static net.leaal.jee.UtilJee.parseFechaWeb;
import static net.leaal.jee.UtilJee.parseHoraWeb;
import static net.leaal.servlets.torneo.CtrlInicio.JUGADOR;
import net.leaal.jee.torneo.entidad.Usuario;
import net.leaal.jee.torneo.entidad.Venta;
import net.leaal.servlets.CtrlAbc;
import net.leaal.servlets.ModeloForm;
import static net.leaal.servlets.UtilServlets.getId;
import static net.leaal.servlets.UtilServlets.getOpciones;
import net.leaal.servlets.torneo.form.ModeloFormJugadores;
import static net.leaal.servlets.UtilServlets.buscaId;

/**
 *
 * @author Soluciones LEAAL
 */
@WebServlet(name = "CtrlJugadores", urlPatterns = "ctrlJugadores")
public class CtrlJugadores extends CtrlAbc<Jugador, String, ModeloFormJugadores> {
  public CtrlJugadores() {
    super(Jugador.FILTRO, Jugador.class, String.class,
        ModeloFormJugadores.class, "Jugadores", "Jugador Nuevo");
  }
  @Override
  protected Renglon getRenglon(HttpServletRequest req, Jugador modelo) {
    return new Renglon(modelo.getId(), modelo.getId(),
        modelo.getUsuario().getNombre());
  }
  @Override protected String getTituloDeModelo(Jugador modelo) {
    return modelo.getId();
  }
  @Override protected void llenaModelo(EntityManager em,
      ModeloForm<ModeloFormJugadores> modeloForm, Jugador modelo) {
    final ModeloFormJugadores valores = modeloForm.getValores();
    final String usuario = valores.getUsuario();
    if (isNuevo(modeloForm) && isNullOrEmpty(usuario)) {
      throw new RuntimeException("Falta seleccionar el usuario.");
    }
    if (isNuevo(modeloForm)) {
//    final Venta venta = new Venta();
//    venta.setJugador(modelo);
//    modelo.setVentaActual(venta);
    modelo.setUsuario(buscaId(em, Usuario.class, String.class, usuario));
    }
    modelo.setApePat(valores.getApePat());
    modelo.setApeMat(valores.getApeMat());
    /*modelo.setNacimiento(parseFechaWeb(valores.getNacimiento(),
    "Formato incorrecto para Nacimiento."));
    modelo.setHoraFavorita(parseHoraWeb(valores.getHoraFavorita(),
    "Formato incorrecto para Hora Favorita."));*/
  }
  @Override protected void muestraModelo(EntityManager em,
      HttpServletRequest req, ModeloForm<ModeloFormJugadores> modeloForm,
      Jugador modelo) {
    final ModeloFormJugadores valores = modeloForm.getValores();
    valores.setApePat(modelo.getApePat());
    valores.setApeMat(modelo.getApeMat());
    /*valores.setNacimiento(format(getFormatoFecha(), modelo.getNacimiento()));
    valores.
    setHoraFavorita(format(getFormatoHora(), modelo.getHoraFavorita()));
    valores.setUsuario(getId(modelo.getUsuario()));
    */
  }
  @Override protected void muestraOpciones(EntityManager em,
      HttpServletRequest req, ModeloForm<ModeloFormJugadores> modeloForm) {
    modeloForm.getOpciones().put("usuario", getOpciones(
        "-- Selecciona Usuario --",
        em.createNamedQuery("Usuario.PARA_ROL", Usuario.class).
            setParameter("rol", JUGADOR).getResultList(),
        u -> new Opcion(u.getId(), u.getId() + ": " + u.getNombre())));
  }
}
