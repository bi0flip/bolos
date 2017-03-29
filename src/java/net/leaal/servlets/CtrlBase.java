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
package net.leaal.servlets;

import com.fasterxml.jackson.jr.ob.JSON;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import net.leaal.jee.CtrlTransacciones;
import net.leaal.jee.ReemplazoDeMensajes;
import static net.leaal.jee.UtilJee.getMensaje;
import static net.leaal.jee.UtilJee.isNullOrEmpty;
import static net.leaal.servlets.UtilServlets.agrega;
import static net.leaal.servlets.UtilServlets.get;
import static net.leaal.servlets.UtilServlets.getPropertyDescriptors;
import static net.leaal.servlets.UtilServlets.set;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import static org.apache.commons.fileupload.util.Streams.asString;

/**
 *
 * @author Soluciones LEAAL
 * @param <MF>
 */
public abstract class CtrlBase<MF> extends HttpServlet {
  @Inject @ReemplazoDeMensajes
  Map<String, String> reemplazoDeMensajes;
  @Inject CtrlTransacciones ctrlTransacciones;
  protected final Class<MF> tipoModeloForm;
  public CtrlBase(Class<MF> tipoModeloForm) {
    this.tipoModeloForm = tipoModeloForm;
  }
  protected abstract void inicia(HttpServletRequest req,
      ModeloForm<MF> modeloForm);
  protected abstract void procesa(HttpServletRequest req,
      ModeloForm<MF> modeloForm);
  @Override protected void doGet(HttpServletRequest req,
      HttpServletResponse resp) throws ServletException, IOException {
    try {
      final ModeloForm<MF> modeloForm = new ModeloForm<>();
      modeloForm.setValores(tipoModeloForm.newInstance());
      inicia(req, modeloForm);
      JSON.std.write(modeloForm, resp.getOutputStream());
    } catch (InstantiationException | IllegalAccessException e) {
      throw new ServletException(e);
    }
  }
  @Override protected void doPost(HttpServletRequest req,
      HttpServletResponse resp) throws ServletException, IOException {
    try {
      final ModeloForm<MF> modeloForm = new ModeloForm<>();
      modeloForm.setValores(tipoModeloForm.newInstance());
      lee(req, modeloForm.getValores());
      procesa(req, modeloForm);
      JSON.std.write(modeloForm, resp.getOutputStream());
    } catch (InstantiationException | IllegalAccessException
        | IntrospectionException | InvocationTargetException
        | RuntimeException | FileUploadException e) {
      throw new ServletException(e);
    }
  }
  protected String getUsuarioId(HttpServletRequest request) {
    final Principal userPrincipal = request.getUserPrincipal();
    return userPrincipal == null ? null : userPrincipal.getName();
  }
  protected EntityManager iniciaTransaccion() {
    return ctrlTransacciones.inicia();
  }
  protected void commit(EntityManager em) {
    ctrlTransacciones.commit(em);
  }
  protected void cierra(EntityManager em) {
    ctrlTransacciones.cierra(em);
  }
  /** Analiza una excepción para poder mostrar el error de error que lleva.
   * Normalmente cuendo un EJB detecta un error, lanza otras excepciones y
   * guarda la causa original. Esta última es necesaria para mostrar el error
   * que manda el manejador de base de datos.
   *
   * @param modeloForm
   * @param mensaje un error relacionado con el fallo.
   * @param e excepción que describe un error. */
  protected void procesaError(ModeloForm<?> modeloForm, String mensaje,
      Throwable e) {
    Logger.getLogger(getClass().getName()).log(Level.SEVERE, mensaje, e);
    procesaError(modeloForm, e);
  }
  protected void procesaError(ModeloForm<?> modeloForm, Throwable e) {
    try {
      throw e;
    } catch (final ConstraintViolationException ex) {
      muestra(modeloForm, ex.getConstraintViolations());
    } catch (Throwable ex) {
      Throwable causa = ex.getCause();
      if (causa == null) {
        reemplaza(modeloForm, getMensaje(ex));
      } else {
        procesaError(modeloForm, causa);
      }
    }
  }
  /** Toma la primera violación y la muestra como error.
   *
   * @param modeloForm
   * @param violaciones conjunto de violaciones a restricciones de Bean
   * Validation. */
  protected void muestra(ModeloForm<?> modeloForm,
      Set<ConstraintViolation<?>> violaciones) {
    modeloForm.setError("Hay errores en los datos capturados.");
    modeloForm.setViolaciones(new HashMap<>());
    violaciones.stream().forEach((violacion) -> {
      final String propiedad = violacion.getPropertyPath().toString();
      final String mensaje = violacion.getMessage();
      if (isNullOrEmpty(propiedad)) {
        modeloForm.setError(mensaje);
      } else {
        muestraError(modeloForm, propiedad, mensaje);
      }
    });
  }
  protected void reemplaza(ModeloForm<?> modeloForm, String mensaje) {
    for (Map.Entry<String, String> entry : reemplazoDeMensajes.entrySet()) {
      final String llave = entry.getKey();
      final String valor = entry.getValue();
      if (llave != null && valor != null && mensaje.contains(llave)) {
        modeloForm.setError(valor);
        return;
      }
    }
    modeloForm.setError(mensaje);
  }
  /** Muestra un mensaje de error.
   *
   * @param modeloForm
   * @param propiedad propiedad de entity al que se asocia el mensaje.
   * @param mensaje versión corta del mensaje. */
  protected void muestraError(ModeloForm<?> modeloForm, String propiedad,
      String mensaje) {
    modeloForm.getViolaciones().put(propiedad, mensaje);
  }
  protected void lee(HttpServletRequest request, Object objeto) throws
      IntrospectionException, FileUploadException, IOException,
      IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    final Map<String, PropertyDescriptor> map = getPropertyDescriptors(objeto);
    ServletFileUpload upload = new ServletFileUpload();
    FileItemIterator iter = upload.getItemIterator(request);
    while (iter.hasNext()) {
      FileItemStream item = iter.next();
      InputStream stream = item.openStream();
      final PropertyDescriptor pd = map.get(item.getFieldName());
      if (pd != null) {
        final Class<?> propertyType = pd.getPropertyType();
        if (String.class.equals(propertyType)) {
          set(objeto, pd, getString(stream));
        } else if (String[].class.equals(propertyType)) {
          final String[] valor = (String[]) get(objeto, pd);
          if (valor == null) {
            set(objeto, pd, new String[]{getString(stream)});
          } else {
            set(objeto, pd, agrega(valor, getString(stream)));
          }
        } else if (byte[].class.equals(propertyType)) {
          set(objeto, pd, getBytes(stream));
        }
      }
    }
  }
  protected byte[] getBytes(InputStream stream) throws IOException {
    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Streams.copy(stream, baos, true);
    final byte[] b = baos.toByteArray();
    return b == null || b.length == 0 ? null : b;
  }
  protected static String getString(InputStream stream) throws IOException {
    final String s = asString(stream, UTF_8.name());
    return isNullOrEmpty(s) ? null : s;
  }
}
