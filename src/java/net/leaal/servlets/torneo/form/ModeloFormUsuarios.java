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
public class ModeloFormUsuarios extends ModeloFormAbc {
  private String id;
  private String contrasena;
  private String confirma;
  private String debeConfirmar;
  private String nombre;
  private String[] roles;
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getContrasena() {
    return contrasena;
  }
  public void setContrasena(String contrasena) {
    this.contrasena = contrasena;
  }
  public String getConfirma() {
    return confirma;
  }
  public void setConfirma(String confirma) {
    this.confirma = confirma;
  }
  public String getDebeConfirmar() {
    return debeConfirmar;
  }
  public void setDebeConfirmar(String debeConfirmar) {
    this.debeConfirmar = debeConfirmar;
  }
  public String getNombre() {
    return nombre;
  }
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  public String[] getRoles() {
    return roles;
  }
  public void setRoles(String[] roles) {
    this.roles = roles;
  }
}
