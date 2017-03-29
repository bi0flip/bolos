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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "CATEGORIA")
@NamedQueries({
  @NamedQuery(name = "Categoria.TODAS", query
      = "SELECT c FROM Categoria c ORDER BY c.upperNombre"),
  @NamedQuery(name = Categoria.FILTRO, query
      = "SELECT c "
      + "FROM Categoria c "
      + "WHERE c.upperNombre LIKE :filtro "
      + "ORDER BY c.upperNombre")})
public class Categoria extends Entidad<Long> {
  private static final long serialVersionUID = 1L;
  public static final String FILTRO = "Categoria.FILTRO";
  @Id
  @Basic(optional = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CAT_ID")
  private Long id;
  @NotNull
  @Size(min = 1, max = 255)
  @Basic(optional = false)
  @Column(name = "CAT_NOMBRE")
  private String nombre;
  @Version
  @Column(name = "VERSION")
  private Integer version;
  @Column(name = "UPPER_NOMBRE")
  private String upperNombre;
  public Categoria() {
  }
  public Categoria(String nombre) {
    this.nombre = nombre;
  }
  @Override public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getNombre() {
    return nombre;
  }
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  @PrePersist @PreUpdate private void antesDeGuardar() {
    upperNombre = toUpperCase(getNombre());
  }
}
