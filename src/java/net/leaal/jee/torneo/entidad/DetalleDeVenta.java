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

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import net.leaal.jee.Entidad;

/**
 *
 * @author Soluciones LEAAL
 */
@Entity
@Table(name = "DETALLE_VENTA")
public class DetalleDeVenta extends Entidad<DetalleDeVentaPK> {
  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected DetalleDeVentaPK id;
  @Basic(optional = false)
  @NotNull
  @Min(1)
  @Column(name = "DVT_CANTIDAD")
  private Long cantidad;
  @Basic(optional = false)
  @NotNull
  @Digits(integer = 6, fraction = 2)
  @Column(name = "DVT_PRECIO")
  private BigDecimal precio;
  @Version
  @Column(name = "VERSION")
  private Integer version;
  @JoinColumn(name = "PRD_ID", referencedColumnName = "PRD_ID",
      insertable = false, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade
      = CascadeType.MERGE)
  private Producto producto;
  @JoinColumn(name = "VNT_ID", referencedColumnName = "VNT_ID",
      insertable = false, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  private Venta venta;
  public DetalleDeVenta() {
  }
  public DetalleDeVenta(Venta venta, Producto producto) {
    this.venta = venta;
    this.producto = producto;
    cantidad = 1L;
    precio = producto.getPrecio();
  }
  @Override public DetalleDeVentaPK getId() {
    return id;
  }
  public void setId(DetalleDeVentaPK id) {
    this.id = id;
  }
  public Long getCantidad() {
    return cantidad;
  }
  public void setCantidad(Long cantidad) {
    this.cantidad = cantidad;
  }
  public BigDecimal getPrecio() {
    return precio;
  }
  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }
  public Producto getProducto() {
    return producto;
  }
  public void setProducto(Producto producto) {
    this.producto = producto;
  }
  public Venta getVenta() {
    return venta;
  }
  public void setVenta(Venta venta) {
    this.venta = venta;
  }
  public BigDecimal getSubtotal() {
    return getProducto().getPrecio().multiply(new BigDecimal(getCantidad()));
  }
  @PrePersist @PreUpdate private void antesDeGuardar() {
    if (getId() == null) {
      setId(new DetalleDeVentaPK());
    }
    getId().setPrdId(getProducto().getId());
    getId().setVntId(getVenta().getId());
  }
}
