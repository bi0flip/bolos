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

import java.util.Objects;
import javax.persistence.EntityManager;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import static net.leaal.jee.UtilJee.encripta;
import static net.leaal.jee.UtilJee.isNullOrEmpty;
import net.leaal.jee.torneo.entidad.Rol;
import net.leaal.jee.torneo.entidad.Usuario;
import net.leaal.servlets.CtrlAbc;
import net.leaal.servlets.ModeloForm;
import net.leaal.servlets.Opcion;
import net.leaal.servlets.Renglon;
import static net.leaal.servlets.UtilServlets.getIds;
import net.leaal.servlets.torneo.form.ModeloFormUsuarios;
import static net.leaal.servlets.UtilServlets.buscaIds;
import static net.leaal.servlets.UtilServlets.getOpciones;
import static net.leaal.servlets.UtilServlets.texto;

/**
 *
 * @author Soluciones LEAAL
 */
@MultipartConfig
@WebServlet(name = "CtrlUsuarios", urlPatterns = "ctrlUsuarios")
public class CtrlUsuarios extends CtrlAbc<Usuario, String, ModeloFormUsuarios> {
  public CtrlUsuarios() {
    super(Usuario.FILTRO, Usuario.class, String.class,
        ModeloFormUsuarios.class, "Usuarios", "Usuario Nuevo");
  }
  @Override protected Renglon getRenglon(HttpServletRequest req, Usuario modelo) {
    return new Renglon(modelo.getId(), modelo.getId(), modelo.getNombre());
  }
  @Override protected String getTituloDeModelo(Usuario modelo) {
    return modelo.getId();
  }
  @Override protected void llenaModelo(EntityManager em,
      ModeloForm<ModeloFormUsuarios> modeloForm, Usuario modelo) {
    final ModeloFormUsuarios valores = modeloForm.getValores();
    final String contrasena = valores.getContrasena();
    final String id = valores.getId();
    if (isNuevo(modeloForm) && isNullOrEmpty(id)) {
      throw new RuntimeException("Falta el identificador.");
    } else if (isNuevo(modeloForm) && isNullOrEmpty(contrasena)) {
      throw new RuntimeException("Falta la Contraseña.");
    } else if ("true".equals(valores.getDebeConfirmar())
        && !Objects.equals(contrasena, valores.getConfirma())) {
      throw new RuntimeException("Las Contraseñas no coinciden.");
    } else if (contrasena != null
        && (contrasena.length() < 5 || contrasena.length() > 25)) {
      throw new RuntimeException(
          "La Contraseña debe tener de 5 a 25 caracteres.");
    } else {
      if (isNuevo(modeloForm)) {
        modelo.setId(valores.getId());
        modelo.setContrasena(encripta(contrasena));
      } else if (!isNullOrEmpty(contrasena)) {
        modelo.setContrasena(encripta(contrasena));
      }
      modelo.setNombre(valores.getNombre());
      modelo.setRoles(buscaIds(em, Rol.class, String.class, valores.getRoles()));
    }
  }
  @Override protected void muestraModelo(EntityManager em,
      HttpServletRequest req, ModeloForm<ModeloFormUsuarios> modeloForm,
      Usuario modelo) {
    final ModeloFormUsuarios valores = modeloForm.getValores();
    valores.setId(texto(modelo.getId()));
    valores.setContrasena("");
    valores.setConfirma("");
    valores.setNombre(texto(modelo.getNombre()));
    valores.setRoles(getIds(modelo.getRoles()));
  }
  @Override
  protected void muestraOpciones(EntityManager em, HttpServletRequest req,
      ModeloForm<ModeloFormUsuarios> modeloForm) {
    modeloForm.getOpciones().put("roles", getOpciones(null,
        em.createNamedQuery(Rol.TODOS, Rol.class).getResultList(),
        r -> new Opcion(r.getId(), r.getId() + ": " + r.getDescripcion())));
  }
}
