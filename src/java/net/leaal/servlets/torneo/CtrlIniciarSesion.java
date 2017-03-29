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
import net.leaal.servlets.torneo.form.ModeloFormIniciarSesion;

/**
 *
 * @author Soluciones LEAAL
 */
@WebServlet(name = "CtrlIniciarSesion", urlPatterns = "ctrlIniciarSesion")
public class CtrlIniciarSesion extends CtrlBase<ModeloFormIniciarSesion> {
  public CtrlIniciarSesion() {
    super(ModeloFormIniciarSesion.class);
  }
  @Override protected void inicia(HttpServletRequest req,
      ModeloForm<ModeloFormIniciarSesion> modeloForm) {
    final ModeloFormIniciarSesion valores = modeloForm.getValores();
    final Principal userPrincipal = req.getUserPrincipal();
    if (userPrincipal == null) {
      valores.setContrasena("");
      valores.setId("");
    } else {
      modeloForm.setSiguienteForm("torneo-inicio");
    }
  }
  @Override protected void procesa(HttpServletRequest req,
      ModeloForm<ModeloFormIniciarSesion> modeloForm) {
    if ("submit".equals(modeloForm.getValores().getAccion())) {
      final ModeloFormIniciarSesion valores = modeloForm.getValores();
      final Principal userPrincipal = req.getUserPrincipal();
      if (userPrincipal == null) {
        try {
          final String id = valores.getId();
          final String contrasena = valores.getContrasena();
          req.login(id, contrasena);
          modeloForm.setSiguienteForm("torneo-inicio");
        } catch (ServletException e) {
          procesaError(modeloForm, "Error iniciando sesión.", e);
        }
      } else {
        modeloForm.setSiguienteForm("torneo-inicio");
      }
    }
  }
}
