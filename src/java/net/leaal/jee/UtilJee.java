/* Copyright 2017 Soluciones LEAAL
Soluciones LEAAL Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. */
package net.leaal.jee;

import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.http.Part;

/** Funciones de apoyo al procesamiento de datos. */
public class UtilJee {
  private static final String FORMATO_ENTERO = "#,##0";
  private static final String FORMATO_PRECIO = "#,##0.00";
  private static final String FORMATO_FECHA = "yyyy-MM-dd";
  private static final String FORMATO_HORA = "HH:mm";
  private static final String FORMATO_FECHA_HORA = FORMATO_FECHA + "'T'"
      + FORMATO_HORA;
  private static final String FORMATO_FECHA_HORA_TZ = FORMATO_FECHA_HORA + " z";
  private UtilJee() {
  }
  public static String toUpperCase(String s) {
    return s == null ? null : s.toUpperCase();
  }
  public static boolean isNullOrEmpty(String s) {
    return s == null || s.length() == 0;
  }
  public static boolean isNullOrEmpty(Collection<?> c) {
    return c == null || c.isEmpty();
  }
  public static String getMensaje(Throwable e) {
    final String localizedMessage = e.getLocalizedMessage();
    return isNullOrEmpty(localizedMessage) ? e.toString() : localizedMessage;
  }
  public static String encripta(final String texto) {
    try {
      if (UtilJee.isNullOrEmpty(texto)) {
        return null;
      } else {
        final MessageDigest md = MessageDigest.getInstance("SHA-256");
        final byte[] bytes = md.digest(texto.getBytes(UTF_8));
        return Base64.getEncoder().encodeToString(bytes);
      }
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
  /** Construye una instancia de <code>Archivo</code> con el contenido de una
   * parte.
   *
   * @param part parte que representa a un archivo
   * @return un arreglo de bytes con el contenido de la parte o null si el
   * tamaño del archivo es vacío o nulo. */
  public static byte[] getBytes(final Part part) {
    if (part == null) {
      return null;
    } else {
      final long size = part.getSize();
      if (size > 0) {
        final byte[] bytes = new byte[(int) size];
        try (DataInputStream dis = new DataInputStream(part.getInputStream())) {
          dis.readFully(bytes);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        return bytes;
      } else {
        return null;
      }
    }
  }
  public static DecimalFormat getFormatoEntero() {
    return new DecimalFormat(FORMATO_ENTERO);
  }
  public static DecimalFormat getFormatoPrecio() {
    final DecimalFormat formato = new DecimalFormat(FORMATO_PRECIO);
    formato.setParseBigDecimal(true);
    return formato;
  }
  public static SimpleDateFormat getFormatoFecha() {
    final SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA,
        Locale.US);
    dateFormat.setTimeZone(getUtc());
    return dateFormat;
  }
  public static SimpleDateFormat getFormatoHora() {
    final SimpleDateFormat dateFormat
        = new SimpleDateFormat(FORMATO_HORA, Locale.US);
    dateFormat.setTimeZone(getUtc());
    return dateFormat;
  }
  public static SimpleDateFormat getFormatoFechaHora(TimeZone timeZone) {
    final SimpleDateFormat dateFormat
        = new SimpleDateFormat(FORMATO_FECHA_HORA, Locale.US);
    dateFormat.setTimeZone(timeZone);
    return dateFormat;
  }
  public static SimpleDateFormat getFormatoWebFechaHoraTimeZone() {
    return new SimpleDateFormat(FORMATO_FECHA_HORA_TZ, Locale.US);
  }
  public static TimeZone getUtc() {
    return TimeZone.getTimeZone("UTC");
  }
  public static String format(Format formato, Object obj) {
    if (obj == null) {
      return "";
    } else {
      return formato.format(obj);
    }
  }
  public static <T> T parse(Class<T> tipo, Format formato, String txt,
      String mensajeDeError) {
    try {
      if (isNullOrEmpty(txt)) {
        return null;
      } else {
        return tipo.cast(formato.parseObject(txt));
      }
    } catch (ParseException e) {
      throw new RuntimeException(mensajeDeError);
    }
  }
  public static Long parseEntero(String texto, String mensajeDeError) {
    return parse(Long.class, getFormatoEntero(), texto, mensajeDeError);
  }
  public static BigDecimal parsePrecio(String texto, String mensajeDeError) {
    return parse(BigDecimal.class, getFormatoPrecio(), texto, mensajeDeError);
  }
  public static Date parseFechaWeb(String texto, String mensajeDeError) {
    return parse(Date.class, getFormatoWebFechaHoraTimeZone(),
        texto == null ? null : texto + "T00:00 UTC", mensajeDeError);
  }
  public static Date parseHoraWeb(String texto, String mensajeDeError) {
    return parse(Date.class, getFormatoWebFechaHoraTimeZone(),
        texto == null ? null : "1970-01-01T" + texto + " UTC", mensajeDeError);
  }
  public static Date parseFechaHoraWeb(String texto, TimeZone timeZone,
      String mensajeDeError) {
    return parse(Date.class, getFormatoFechaHora(timeZone), texto,
        mensajeDeError);
  }
}
