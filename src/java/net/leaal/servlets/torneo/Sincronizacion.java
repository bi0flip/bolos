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
package net.leaal.servlets.torneo;

import java.util.List;
import net.leaal.jee.torneo.entidad.Aviso;

public class Sincronizacion {
  private boolean finalizada;
  private List<Aviso> lista;
  private String error;
  public Sincronizacion() {
  }
  public Sincronizacion(boolean finalizada, List<Aviso> lista) {
    this.finalizada = finalizada;
    this.lista = lista;
  }
  public boolean isFinalizada() {
    return finalizada;
  }
  public void setFinalizada(boolean finalizada) {
    this.finalizada = finalizada;
  }
  public List<Aviso> getLista() {
    return lista;
  }
  public void setLista(List<Aviso> lista) {
    this.lista = lista;
  }
  public String getError() {
    return error;
  }
  public void setError(String error) {
    this.error = error;
  }
}
