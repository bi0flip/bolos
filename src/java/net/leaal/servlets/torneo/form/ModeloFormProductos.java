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
public class ModeloFormProductos extends ModeloFormAbc {
  private String img;
  private byte[] imagen;
  private String nombre;
  private String categoria;
  private String existencias;
  private String precio;
  public String getImg() {
    return img;
  }
  public void setImg(String img) {
    this.img = img;
  }
  public byte[] getImagen() {
    return imagen;
  }
  public void setImagen(byte[] imagen) {
    this.imagen = imagen;
  }
  public String getNombre() {
    return nombre;
  }
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  public String getCategoria() {
    return categoria;
  }
  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }
  public String getExistencias() {
    return existencias;
  }
  public void setExistencias(String existencias) {
    this.existencias = existencias;
  }
  public String getPrecio() {
    return precio;
  }
  public void setPrecio(String precio) {
    this.precio = precio;
  }
}
