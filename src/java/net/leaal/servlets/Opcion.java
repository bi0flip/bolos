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

import java.util.Objects;

/**
 *
 * @author Soluciones LEAAL
 */
public class Opcion {
  private String id;
  private String texto;
  public Opcion() {
  }
  public Opcion(Object id, Object texto) {
    this.id = Objects.toString(id, null);
    this.texto = Objects.toString(texto, null);
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getTexto() {
    return texto;
  }
  public void setTexto(String texto) {
    this.texto = texto;
  }
}