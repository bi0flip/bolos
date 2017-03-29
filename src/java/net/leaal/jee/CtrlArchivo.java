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
package net.leaal.jee;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static java.util.Objects.requireNonNull;
import javax.persistence.EntityManager;

/** Servlet que permite descargar archivos.
 *
 * @author Soluciones LEAAL */
@WebServlet(name = "CtrlArchivo", urlPatterns = {"/ctrlArchivo/*"})
public class CtrlArchivo extends HttpServlet {
  private static final long serialVersionUID = 1L;
  @Inject private CtrlTransacciones ctrlTransacciones;
  /** Breve descripción del servlet.
   *
   * @return un texto que contiene la descripción del servlet. */
  @Override public String getServletInfo() {
    return "Descarga un archivo.";
  }
  /** Realiza el método <code>GET</code> de HTTP.
   *
   * @param request solicitud al servlet
   * @param response respuesta del servlet
   * @throws ServletException si ocurre un error específico del servlet
   * @throws IOException si ocurre un error de entrada/salida */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    final String id = requireNonNull(request.getPathInfo(),
        "Falta el Id de archivo");
    EntityManager em = null;
    try {
      em = iniciaTransaccion();
      final Archivo modelo = requireNonNull(
          em.find(Archivo.class, new Long(id.substring(1))),
          "Id de archivo inválido.");
      final byte[] bytes = modelo.getBytes();
      // Como el archivo devuelto puede cambiar, no se debe guardar en cache.
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
      response.setHeader("Pragma", "no-cache");
      response.setHeader("Expires", "0");
      // Obtiene el tipo de los datos del archivo.
      final String contentType = URLConnection.guessContentTypeFromStream(
          new ByteArrayInputStream(bytes));
      response.setContentType(contentType);
      final ServletOutputStream out = response.getOutputStream();
      out.write(bytes);
      commit(em);
    } finally {
      ctrlTransacciones.cierra(em);
    }
  }
  private EntityManager iniciaTransaccion() {
    return ctrlTransacciones.inicia();
  }
  private void commit(EntityManager em) {
    ctrlTransacciones.commit(em);
  }
}
