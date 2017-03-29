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
public class ModeloFormClientes extends ModeloFormAbc {
  private String usuario;
  private String nacimiento;
  private String horaFavorita;
  public String getUsuario() {
    return usuario;
  }
  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }
  public String getNacimiento() {
    return nacimiento;
  }
  public void setNacimiento(String nacimiento) {
    this.nacimiento = nacimiento;
  }
  public String getHoraFavorita() {
    return horaFavorita;
  }
  public void setHoraFavorita(String horaFavorita) {
    this.horaFavorita = horaFavorita;
  }
}
