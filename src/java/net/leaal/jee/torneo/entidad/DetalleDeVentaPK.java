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

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Soluciones LEAAL
 */
@Embeddable
public class DetalleDeVentaPK implements Serializable {
  @Basic(optional = false)
  @NotNull
  @Column(name = "VNT_ID")
  private long vntId;
  @Basic(optional = false)
  @NotNull
  @Column(name = "PRD_ID")
  private long prdId;
  public DetalleDeVentaPK() {
  }
  public DetalleDeVentaPK(String id) {
    final String[] ids = id.split(",");
    this.vntId = Long.valueOf(ids[0]);
    this.prdId = Long.valueOf(ids[1]);
  }
  public long getVntId() {
    return vntId;
  }
  public void setVntId(long vntId) {
    this.vntId = vntId;
  }
  public long getPrdId() {
    return prdId;
  }
  public void setPrdId(long prdId) {
    this.prdId = prdId;
  }
  @Override public int hashCode() {
    return Objects.hash(getVntId(), getPrdId());
  }
  @Override public String toString() {
    return getVntId() + "," + getPrdId();
  }
  @Override public boolean equals(Object object) {
    if (object instanceof DetalleDeVentaPK) {
      final DetalleDeVentaPK otro = (DetalleDeVentaPK) object;
      return getVntId() == otro.getVntId() && getPrdId() == otro.getPrdId();
    } else {
      return false;
    }
  }
}
