/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.leaal.jee.torneo.entidad;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author biofl
 */
@Entity
@Table(name = "torneo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Torneo.findAll", query = "SELECT t FROM Torneo t")
    , @NamedQuery(name = "Torneo.findById", query = "SELECT t FROM Torneo t WHERE t.id = :id")
    , @NamedQuery(name = "Torneo.findByNombre", query = "SELECT t FROM Torneo t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "Torneo.findByFecha", query = "SELECT t FROM Torneo t WHERE t.fecha = :fecha")
    , @NamedQuery(name = "Torneo.findByNoEquipos", query = "SELECT t FROM Torneo t WHERE t.noEquipos = :noEquipos")})
public class Torneo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NO_EQUIPOS")
    private int noEquipos;

    public Torneo() {
    }

    public Torneo(String id) {
        this.id = id;
    }

    public Torneo(String id, String nombre, Date fecha, int noEquipos) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.noEquipos = noEquipos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getNoEquipos() {
        return noEquipos;
    }

    public void setNoEquipos(int noEquipos) {
        this.noEquipos = noEquipos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Torneo)) {
            return false;
        }
        Torneo other = (Torneo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.leaal.jee.torneo.entidad.Torneo[ id=" + id + " ]";
    }
    
}
