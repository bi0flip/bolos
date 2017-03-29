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

import java.util.Objects;

/**
 *
 * @author gilberto
 */
public class RegistroVenta {
  private String prdNombre;
  private String total;
  public RegistroVenta() {
  }
  public RegistroVenta(Object prdNombre, Object total) {
    this.prdNombre = Objects.toString(prdNombre, "");
    this.total = Objects.toString(total, "");
  }
  public String getPrdNombre() {
    return prdNombre;
  }
  public void setPrdNombre(String prdNombre) {
    this.prdNombre = prdNombre;
  }
  public String getTotal() {
    return total;
  }
  public void setTotal(String total) {
    this.total = total;
  }
}
