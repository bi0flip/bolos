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
package net.leaal.jee;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Soluciones LEAAL
 */
@Entity
@Table(name = "ARCHIVO")
public class Archivo extends Entidad<Long> {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ARCH_ID")
  private Long id;
  @Basic(optional = false)
  @NotNull
  @Lob
  @Column(name = "ARCH_BYTES")
  private byte[] bytes;
  @Version
  @Column(name = "VERSION")
  private Integer version;
  public Archivo() {
  }
  public Archivo(byte[] bytes) {
    this.bytes = bytes;
  }
  @Override public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public byte[] getBytes() {
    return bytes;
  }
  public void setBytes(byte[] bytes) {
    this.bytes = bytes;
  }
}
