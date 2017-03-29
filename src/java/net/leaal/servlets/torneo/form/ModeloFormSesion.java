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

/**
 *
 * @author Soluciones LEAAL
 */
public class ModeloFormSesion {
  private String accion;
  private String formUsuarioId;
  private String rolAdministrador;
  private String rolJugador;
  private String sesionIniciada;
  public String getAccion() {
    return accion;
  }
  public void setAccion(String accion) {
    this.accion = accion;
  }
  public String getFormUsuarioId() {
    return formUsuarioId;
  }
  public void setFormUsuarioId(String formUsuarioId) {
    this.formUsuarioId = formUsuarioId;
  }
  public String getRolAdministrador() {
    return rolAdministrador;
  }
  public void setRolAdministrador(String rolAdministrador) {
    this.rolAdministrador = rolAdministrador;
  }
  public String getRolJugador() {
    return rolJugador;
  }
  public void setRolJugador(String rolJugador) {
    this.rolJugador = rolJugador;
  }
  public String getSesionIniciada() {
    return sesionIniciada;
  }
  public void setSesionIniciada(String sesionIniciada) {
    this.sesionIniciada = sesionIniciada;
  }
}
