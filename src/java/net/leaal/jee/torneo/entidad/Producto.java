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

import net.leaal.jee.Archivo;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import static net.leaal.jee.UtilJee.toUpperCase;
import net.leaal.jee.Entidad;

/**
 *
 * @author Soluciones LEAAL
 */
@Entity
@Table(name = "PRODUCTO")
@NamedQuery(name = Producto.FILTRO, query
    = "SELECT p "
    + "FROM Producto p "
    + "WHERE p.upperNombre LIKE :filtro "
    + "ORDER BY p.upperNombre")
public class Producto extends Entidad<Long> {
    private static final long serialVersionUID = 1L;
    public static final String FILTRO = "Producto.FILTRO";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "PRD_ID")
  private Long id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 255)
  @Column(name = "PRD_NOMBRE")
  private String nombre;
  @Basic(optional = false)
  @NotNull
  @Column(name = "PRD_EXISTENCIAS")
  private Long existencias;
  @Basic(optional = false)
  @NotNull @Digits(integer = 6, fraction = 2)
  @Column(name = "PRD_PRECIO")
  private BigDecimal precio;
  @JoinColumn(name = "IMAGEN_ID", referencedColumnName = "ARCH_ID")
  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Archivo imagen;
  @NotNull
  @JoinColumn(name = "CAT_ID", referencedColumnName = "CAT_ID")
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private Categoria categoria;
  @Version
  @Column(name = "VERSION")
  private Integer version;
  @Size(max = 255)
  @Column(name = "UPPER_NOMBRE")
  private String upperNombre;
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
  public Long getExistencias() {
    return existencias;
  }
  public void setExistencias(Long existencias) {
    this.existencias = existencias;
  }
  public BigDecimal getPrecio() {
    return precio;
  }
  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }
  public Archivo getImagen() {
    return imagen;
  }
  public void setImagen(Archivo imagen) {
    this.imagen = imagen;
  }
  public Categoria getCategoria() {
    return categoria;
  }
  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }
  public void decrementaExistencias(final long decremento) {
    setExistencias(getExistencias() - decremento);
  }
  @PrePersist @PreUpdate private void antesDeGuardar() {
    upperNombre = toUpperCase(getNombre());
  }
}
