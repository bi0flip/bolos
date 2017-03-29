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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import net.leaal.jee.Entidad;
import static net.leaal.jee.UtilJee.toUpperCase;

/**
 *
 * @author biofl
 */
@Entity
@Table(name = "JUGADOR")
@NamedQuery(name = Jugador.FILTRO, query
        = "SELECT j "
        + "FROM Jugador j JOIN j.usuario u "
        + "WHERE j.upperId LIKE :filtro OR UPPER(u.nombre) LIKE :filtro "
        + "ORDER BY j.upperId")
public class Jugador extends Entidad<String> {

    private static final long serialVersionUID = 1L;
    public static final String FILTRO = "Jugador.FILTRO";

    @Id
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 16)
    @Column(name = "USU_ID")
    private String id;
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 16)
    @Column(name = "JUG_APP")
    private String apePat;
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 16)
    @Column(name = "JUG_APM")
    private String apeMat;
    @JoinColumn(name = "USU_ID", referencedColumnName = "USU_ID", insertable
            = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Usuario usuario;
    @Version
    @Column(name = "VERSION")
    private Integer version;
    @Column(name = "UPPER_ID")
    private String upperId;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @PrePersist @PreUpdate private void antesDeGuardar() {
        upperId = toUpperCase(getId());
        if (getUsuario() != null) {
            setId(getUsuario().getId());
        }
    }
}
