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
package net.leaal.jee.torneo.entidad;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import net.leaal.jee.Entidad;

/**
 *
 * @author Gilberto Pacheco Gallegos
 */
@Entity
@Table(name = "AVISO")
@NamedQueries({@NamedQuery(name = "Aviso.TODOS", query
      = "SELECT a FROM Aviso a WHERE a.eliminado = false")})
public class Aviso extends Entidad<String> {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "AVI_ID")
  private String id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "AVI_TITULO")
  private String titulo;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "AVI_TEXTO")
  private String texto;
  @Basic(optional = false)
  @NotNull
  @Column(name = "AVI_MODIFICACION")
  private long modificacion;
  @Basic(optional = false)
  @NotNull
  @Column(name = "AVI_ELIMINADO")
  private boolean eliminado;
  @Override public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getTitulo() {
    return titulo;
  }
  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }
  public String getTexto() {
    return texto;
  }
  public void setTexto(String texto) {
    this.texto = texto;
  }
  public long getModificacion() {
    return modificacion;
  }
  public void setModificacion(long modificacion) {
    this.modificacion = modificacion;
  }
  public boolean isEliminado() {
    return eliminado;
  }
  public void setEliminado(boolean eliminado) {
    this.eliminado = eliminado;
  }
}
