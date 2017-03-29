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

import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import net.leaal.servlets.CtrlBase;
import net.leaal.servlets.ModeloForm;
import net.leaal.servlets.torneo.form.ModeloFormSesion;

/**
 *
 * @author Soluciones LEAAL
 */
@WebServlet(name = "CtrlSesion", urlPatterns = "ctrlSesion")
public class CtrlSesion extends CtrlBase<ModeloFormSesion> {
  public CtrlSesion() {
    super(ModeloFormSesion.class);
  }
  @Override protected void inicia(HttpServletRequest req,
      ModeloForm<ModeloFormSesion> modeloForm) {
    final ModeloFormSesion valores = modeloForm.getValores();
    final Principal userPrincipal = req.getUserPrincipal();
    if (userPrincipal == null) {
      valores.setFormUsuarioId("");
      valores.setSesionIniciada("false");
      valores.setRolAdministrador("false");
      valores.setRolJugador("false");
    } else {
      valores.setFormUsuarioId(userPrincipal.getName());
      valores.setSesionIniciada("true");
      valores.setRolAdministrador(
          req.isUserInRole("Administrador") ? "true" : "false");
      valores.setRolJugador(req.isUserInRole("Jugador") ? "true" : "false");
    }
  }
  @Override protected void procesa(HttpServletRequest req,
      ModeloForm<ModeloFormSesion> modeloForm) {
    final ModeloFormSesion valores = modeloForm.getValores();
    if ("terminarSesion".equals(valores.getAccion())) {
      try {
        req.logout();
        valores.setFormUsuarioId("");
        valores.setSesionIniciada("false");
        valores.setRolAdministrador("false");
        valores.setRolJugador("false");
        modeloForm.setSiguienteForm("torneo-inicio");
      } catch (ServletException e) {
        procesaError(modeloForm, "Error cerrando sesión", e);
      }
    }
  }
}
