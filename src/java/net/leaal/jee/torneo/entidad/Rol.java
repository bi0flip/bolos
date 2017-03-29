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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import static net.leaal.jee.UtilJee.toUpperCase;
import net.leaal.jee.Entidad;

/**
 *
 * @author Soluciones LEAAL
 */
@Entity
@Table(name = "ROL")
@NamedQueries({
  @NamedQuery(name = Rol.TODOS, query
      = "SELECT r FROM Rol r ORDER BY r.upperId")})
public class Rol extends Entidad<String> {
  private static final long serialVersionUID = 1L;
  public static final String TODOS = "Rol.TODOS";
  @Id
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "ROL_ID")
  private String id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "ROL_DESCRIPCION")
  private String descripcion;
  @Version
  private Integer version;
  @Size(max = 255)
  @Column(name = "UPPER_ID")
  private String upperId;
  public Rol() {
  }
  public Rol(String id, String descripcion) {
    this.id = id;
    this.descripcion = descripcion;
  }
  @Override public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getDescripcion() {
    return descripcion;
  }
  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
  @PrePersist @PreUpdate private void antesDeGuardar() {
    upperId = toUpperCase(getId());
  }
}
