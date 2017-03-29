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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Soluciones LEAAL
 * @param <T>
 */
public class ModeloForm<T> {
  private String titulo;
  private String siguienteForm;
  private String error;
  private Map<String, String> violaciones;
  private Map<String, List<Opcion>> opciones = new HashMap<>();
  private T valores;
  public String getTitulo() {
    return titulo;
  }
  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }
  public String getSiguienteForm() {
    return siguienteForm;
  }
  public void setSiguienteForm(String siguienteForm) {
    this.siguienteForm = siguienteForm;
  }
  public String getError() {
    return error;
  }
  public void setError(String error) {
    this.error = error;
  }
  public Map<String, String> getViolaciones() {
    return violaciones;
  }
  public void setViolaciones(Map<String, String> violaciones) {
    this.violaciones = violaciones;
  }
  public Map<String, List<Opcion>> getOpciones() {
    return opciones;
  }
  public void setOpciones(Map<String, List<Opcion>> opciones) {
    this.opciones = opciones;
  }
  public T getValores() {
    return valores;
  }
  public void setValores(T valores) {
    this.valores = valores;
  }
}
