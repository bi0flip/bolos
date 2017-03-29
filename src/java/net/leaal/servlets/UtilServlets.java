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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import net.leaal.jee.Entidad;
import net.leaal.jee.UtilJee;
import static net.leaal.jee.UtilJee.isNullOrEmpty;

/**
 *
 * @author Soluciones LEAAL
 */
public class UtilServlets {
  private UtilServlets() {
  }
  public static String texto(String s) {
    return s == null ? "" : s;
  }
  public static String[] agrega(final String[] valor, String s) throws
      IOException {
    final String[] nuevoValor = new String[valor.length + 1];
    System.arraycopy(valor, 0, nuevoValor, 0, valor.length);
    nuevoValor[valor.length] = s;
    return nuevoValor;
  }
  public static Map<String, PropertyDescriptor> getPropertyDescriptors(
      Object objeto) throws IntrospectionException {
    final BeanInfo beanInfo = Introspector.getBeanInfo(objeto.getClass());
    final PropertyDescriptor[] propertyDescriptors =
        beanInfo.getPropertyDescriptors();
    final HashMap<String, PropertyDescriptor> map = new HashMap<>();
    Arrays.stream(propertyDescriptors).forEach(pd -> map.put(pd.getName(), pd));
    return map;
  }
  public static void set(Object objeto, PropertyDescriptor pd, Object valor)
      throws
      IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    pd.getWriteMethod().invoke(objeto, valor);
  }
  public static Object get(Object objeto, PropertyDescriptor pd) throws
      IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    return pd.getReadMethod().invoke(objeto);
  }
  /**
   * Crea un objeto usando un constructor que reciba un <code>String</code>.
   *
   * @param <T> Clase del objeto que se crea.
   * @param tipo variable de clase que crea el objeto.
   * @param texto parámetro para el constructorm
   * @return en objeto creado.
   */
  public static <T> T newInstance(Class<T> tipo, String texto) {
    try {
      return tipo.getConstructor(String.class).newInstance(texto);
    } catch (NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
  public static <T> T buscaId(EntityManager em, Class<T> tipo,
      Class<?> tipoId, String id) {
    return id == null ? null : em.find(tipo, newInstance(tipoId, id));
  }
  public static <T, I> List<T> buscaIds(EntityManager em, Class<T> tipo,
      Class<I> tipoId, String[] ids) {
    return (ids == null || ids.length == 0)
        ? Collections.emptyList()
        : Arrays.stream(ids).map(i -> buscaId(em, tipo, tipoId, i)).
            collect(Collectors.toList());
  }
  public static String getId(Entidad<?> e) {
    return e == null ? "" : e.getId().toString();
  }
  public static <I> String[] getIds(Collection<? extends Entidad<I>> c) {
    return isNullOrEmpty(c)
        ? new String[0]
        : c.stream().map(e -> Objects.toString(e.getId(), null)).
            toArray(String[]::new);
  }
  public static <T> List<Opcion> getOpciones(String mensajeNull,
      Collection<T> c, Function<T, Opcion> f) {
    final List<Opcion> lista =
        new ArrayList<>(1 + (c == null ? 0 : c.size()));
    if (!UtilJee.isNullOrEmpty(mensajeNull)) {
      final Opcion opcion = new Opcion("", mensajeNull);
      lista.add(opcion);
    }
    if (!UtilJee.isNullOrEmpty(c)) {
      lista.addAll(c.stream().map(f).collect(Collectors.toList()));
    }
    return lista;
  }
  public static String getImagenUrl(HttpServletRequest request, Long id) {
    return request.getScheme() + "://" + request.getServerName() + ":"
        + request.getLocalPort() + "/ctrlArchivo/" + id;
  }
}
