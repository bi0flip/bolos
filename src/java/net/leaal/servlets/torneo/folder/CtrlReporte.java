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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import static net.leaal.jee.UtilJee.format;
import static net.leaal.jee.UtilJee.getFormatoPrecio;
import net.leaal.servlets.CtrlBase;
import net.leaal.servlets.ModeloForm;
import net.leaal.servlets.torneo.form.ModeloFormReporte;
import net.leaal.servlets.torneo.form.RegistroVenta;

/**
 *
 * @author Soluciones LEAAL
 */
@WebServlet(name = "CtrlReporte", urlPatterns = "ctrlReporte")
public class CtrlReporte extends CtrlBase<ModeloFormReporte> {
  public CtrlReporte() {
    super(ModeloFormReporte.class);
  }
  @Override
  protected void inicia(HttpServletRequest req,
      ModeloForm<ModeloFormReporte> modeloForm) {
    EntityManager em = null;
    try {
      final Date día = new Date();
      em = iniciaTransaccion();
      final Calendar c1 = creaCalendar();
      c1.setTime(día);
      c1.set(Calendar.HOUR_OF_DAY, 0);
      c1.set(Calendar.MINUTE, 0);
      c1.set(Calendar.SECOND, 0);
      c1.set(Calendar.MILLISECOND, 0);
      final Calendar c2 = creaCalendar();
      c2.setTime(día);
      c2.set(Calendar.HOUR_OF_DAY, 23);
      c2.set(Calendar.MINUTE, 59);
      c2.set(Calendar.SECOND, 59);
      c2.set(Calendar.MILLISECOND, 999);
      final Date fecha1 = c1.getTime();
      final Date fecha2 = c2.getTime();
      final List<Object[]> resultList
          = em.createNamedQuery("Venta.reporte", Object[].class).
              setParameter("fecha1", fecha1, TemporalType.TIMESTAMP).
              setParameter("fecha2", fecha2, TemporalType.TIMESTAMP).
              getResultList();
      final DecimalFormat fmt = getFormatoPrecio();
      final List<RegistroVenta> lista = resultList.stream().map(
          arr -> new RegistroVenta(arr[0], format(fmt, arr[1]))).
          collect(Collectors.toList());
      modeloForm.getValores().setLista(lista);
      final BigDecimal total = resultList.stream().map(
          arr -> (BigDecimal) arr[1]).reduce(
              BigDecimal.ZERO.setScale(2, RoundingMode.DOWN),
              BigDecimal::add);
      modeloForm.getValores().setTotal(format(fmt, total));
      commit(em);
    } catch (Exception e) {
      procesaError(modeloForm, "Error cargando lista.", e);
    } finally {
      cierra(em);
    }
  }
  private static Calendar creaCalendar() {
    return Calendar.getInstance(getTimeZone(), getLocale());
  }
  @Override protected void procesa(HttpServletRequest req,
      ModeloForm<ModeloFormReporte> modeloForm) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  protected static TimeZone getTimeZone() {
    return TimeZone.getTimeZone("America/Mexico_City");
  }
  private static Locale getLocale() {
    return new Locale("es-MX");
  }
}
