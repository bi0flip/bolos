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
package net.leaal.servlets;

import java.util.List;

/**
 *
 * @author Soluciones LEAAL
 */
public class ModeloFormAbc {
  private String accion;
  private String filtro;
  private List<Renglon> lista;
  private String detalleId;
  private String muestraDetalle;
  private String nuevo;
  public String getAccion() {
    return accion;
  }
  public void setAccion(String accion) {
    this.accion = accion;
  }
  public String getFiltro() {
    return filtro;
  }
  public void setFiltro(String filtro) {
    this.filtro = filtro;
  }
  public List<Renglon> getLista() {
    return lista;
  }
  public void setLista(List<Renglon> lista) {
    this.lista = lista;
  }
  public String getDetalleId() {
    return detalleId;
  }
  public void setDetalleId(String detalleId) {
    this.detalleId = detalleId;
  }
  public String getMuestraDetalle() {
    return muestraDetalle;
  }
  public void setMuestraDetalle(String muestraDetalle) {
    this.muestraDetalle = muestraDetalle;
  }
  public String getNuevo() {
    return nuevo;
  }
  public void setNuevo(String nuevo) {
    this.nuevo = nuevo;
  }
}
