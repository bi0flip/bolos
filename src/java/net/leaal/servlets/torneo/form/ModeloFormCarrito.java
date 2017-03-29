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
package net.leaal.servlets.torneo.form;

import net.leaal.servlets.ModeloFormAbc;

/**
 *
 * @author Soluciones LEAAL
 */
public class ModeloFormCarrito extends ModeloFormAbc {
  private String img;
  private String total;
  private String fechaHoraDeEntrega;
  private String direccionDeEntrega;
  private String nombre;
  private String cantidad;
  private String precio;
  private String categoria;
  public String getImg() {
    return img;
  }
  public void setImg(String img) {
    this.img = img;
  }
  public String getTotal() {
    return total;
  }
  public void setTotal(String total) {
    this.total = total;
  }
  public String getFechaHoraDeEntrega() {
    return fechaHoraDeEntrega;
  }
  public void setFechaHoraDeEntrega(String fechaHoraDeEntrega) {
    this.fechaHoraDeEntrega = fechaHoraDeEntrega;
  }
  public String getDireccionDeEntrega() {
    return direccionDeEntrega;
  }
  public void setDireccionDeEntrega(String direccionDeEntrega) {
    this.direccionDeEntrega = direccionDeEntrega;
  }
  public String getNombre() {
    return nombre;
  }
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  public String getCantidad() {
    return cantidad;
  }
  public void setCantidad(String cantidad) {
    this.cantidad = cantidad;
  }
  public String getPrecio() {
    return precio;
  }
  public void setPrecio(String precio) {
    this.precio = precio;
  }
  public String getCategoria() {
    return categoria;
  }
  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }
}
