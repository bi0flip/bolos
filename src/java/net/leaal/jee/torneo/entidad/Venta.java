/*
 * Copyright 2017 Gilberto Pacheco Gallegos.
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import net.leaal.jee.Entidad;

/**
 *
 * @author Gilberto Pacheco Gallegos
 */
@Entity
@Table(name = "VENTA")
@NamedQuery(name = "Venta.reporte", query
    = "SELECT p.nombre AS Nombre, SUM(d.cantidad * d.precio) AS Total "
    + "FROM Venta v JOIN v.detalles d JOIN d.producto p "
    + "WHERE v.registro BETWEEN :fecha1 AND :fecha2 "
    + "AND NOT (v.registro IS NULL) "
    + "GROUP BY p.nombre "
    + "ORDER BY Total DESC")
public class Venta extends Entidad<Long> {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "VNT_ID")
  private Long id;
  @Basic(optional = true)
  @Column(name = "VNT_REGISTRO")
  @Temporal(TemporalType.TIMESTAMP)
  private Date registro;
  @Basic(optional = true)
  @Column(name = "VNT_ENT_TS")
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaHoraDeEntrega;
  @Basic(optional = false)
  @Size(min = 1, max = 255)
  @Column(name = "VNT_ENT_DIR")
  private String direccionDeEntrega;
  @Version
  private Integer version;
  @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID")
  @ManyToOne(fetch = FetchType.EAGER)
  private Cliente cliente;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "venta",
      fetch = FetchType.EAGER, orphanRemoval = true)
  private List<DetalleDeVenta> detalles;
  @Override public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Date getRegistro() {
    return registro;
  }
  public void setRegistro(Date registro) {
    this.registro = registro;
  }
  public Date getFechaHoraDeEntrega() {
    return fechaHoraDeEntrega;
  }
  public void setFechaHoraDeEntrega(Date fechaHoraDeEntrega) {
    this.fechaHoraDeEntrega = fechaHoraDeEntrega;
  }
  public String getDireccionDeEntrega() {
    return direccionDeEntrega;
  }
  public void setDireccionDeEntrega(String direccionDeEntrega) {
    this.direccionDeEntrega = direccionDeEntrega;
  }
  public List<DetalleDeVenta> getDetalles() {
    return detalles;
  }
  public void setDetalles(List<DetalleDeVenta> detalles) {
    this.detalles = detalles;
  }
  public Cliente getCliente() {
    return cliente;
  }
  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }
  public Optional<DetalleDeVenta> busca(Producto p) {
    return getDetalles().stream().
        filter(d -> Objects.equals(d.getProducto(), p)).findFirst();
  }
  public boolean elimina(Producto p) {
    for (final ListIterator<DetalleDeVenta> it = getDetalles().listIterator();
        it.hasNext();) {
      final DetalleDeVenta detalle = it.next();
      if (Objects.equals(detalle.getProducto(), p)) {
        detalle.setVenta(null);
        it.remove();
        return true;
      }
    }
    return false;
  }
  public BigDecimal getTotal() {
    return getDetalles().parallelStream().map(DetalleDeVenta::getSubtotal).reduce(
        BigDecimal.ZERO.setScale(2, RoundingMode.UP), BigDecimal::add);
  }
}
