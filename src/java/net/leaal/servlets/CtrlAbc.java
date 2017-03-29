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
package net.leaal.servlets;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import net.leaal.jee.Entidad;
import static net.leaal.servlets.UtilServlets.newInstance;

/**
 *
 * @author Soluciones LEAAL
 * @param <M>
 * @param <I>
 * @param <MF>
 */
public abstract class CtrlAbc<M extends Entidad<I>, I, MF extends ModeloFormAbc>
    extends CtrlBase<MF> {
  protected final String consultaFiltro;
  protected final Class<M> tipoModelo;
  protected final Class<I> tipoIdentificador;
  protected final String tituloMaestro;
  protected final String tituloDeNuevo;
  protected abstract Renglon getRenglon(HttpServletRequest req,M modelo);
  protected abstract void llenaModelo(EntityManager em,
      ModeloForm<MF> modeloForm, M modelo);
  protected abstract void muestraModelo(EntityManager em, HttpServletRequest req,
      ModeloForm<MF> modeloForm, M modelo);
  protected abstract String getTituloDeModelo(M modelo);
  public CtrlAbc(String consultaTodos, Class<M> tipoModelo,
      Class<I> tipoIdentificador, Class<MF> tipoModeloForm, String tituloMaestro,
      String tituloDeNuevo) {
    super(tipoModeloForm);
    this.consultaFiltro = consultaTodos;
    this.tipoModelo = tipoModelo;
    this.tipoIdentificador = tipoIdentificador;
    this.tituloMaestro = tituloMaestro;
    this.tituloDeNuevo = tituloDeNuevo;
  }
  @Override protected void inicia(HttpServletRequest req,
      ModeloForm<MF> modeloForm) {
    EntityManager em = null;
    try {
      em = iniciaTransaccion();
      inicia(em, req, modeloForm);
      commit(em);
    } catch (Exception e) {
      procesaError(modeloForm, "Error inicializando.", e);
    } finally {
      cierra(em);
    }
  }
  @Override protected void procesa(HttpServletRequest req,
      ModeloForm<MF> modeloForm) {
    final String accion = modeloForm.getValores().getAccion();
    if (accion != null) {
      switch (accion) {
      case "action_filtrar":
        inicia(req, modeloForm);
        break;
      case "action_agregar":
        agrega(req, modeloForm);
        break;
      case "action_cancelar":
        inicia(req, modeloForm);
        break;
      case "detalle":
        detalle(req, modeloForm);
        break;
      case "submit":
      case "action_guardar":
        guarda(req, modeloForm);
        break;
      case "action_eliminar":
        elimina(req, modeloForm);
        break;
      default:
        procesaAccion(req, modeloForm);
      }
    }
  }
  protected void agrega(HttpServletRequest req, ModeloForm<MF> modeloForm) {
    EntityManager em = null;
    try {
      configuraDetalle(modeloForm.getValores(), "true");
      em = iniciaTransaccion();
      muestraOpciones(em, req, modeloForm);
      final M modelo = creaModelo();
      setTitulo(modeloForm, modelo);
      muestraModelo(em, req, modeloForm, modelo);
      commit(em);
    } catch (Exception e) {
      procesaError(modeloForm, "Error preparando para agregar.", e);
    } finally {
      cierra(em);
    }
  }
  protected void detalle(HttpServletRequest req, ModeloForm<MF> modeloForm) {
    EntityManager em = null;
    try {
      configuraDetalle(modeloForm.getValores(), "false");
      em = iniciaTransaccion();
      muestraOpciones(em, req, modeloForm);
      M modelo = buscaModelo(modeloForm, em);
      setTitulo(modeloForm, modelo);
      muestraModelo(em, req, modeloForm, modelo);
      commit(em);
    } catch (RuntimeException e) {
      procesaError(modeloForm, "Error recuperando detalle.", e);
    } finally {
      cierra(em);
    }
  }
  protected void guarda(HttpServletRequest req, ModeloForm<MF> modeloForm) {
    EntityManager em = null;
    try {
      em = iniciaTransaccion();
      muestraOpciones(em, req, modeloForm);
      if (isNuevo(modeloForm)) {
        M modelo = creaModelo();
        setTitulo(modeloForm, modelo);
        llenaModelo(em, modeloForm, modelo);
        em.persist(modelo);
      } else {
        final M modelo = buscaModelo(modeloForm, em);
        setTitulo(modeloForm, modelo);
        llenaModelo(em, modeloForm, modelo);
        em.merge(modelo);
      }
      em.flush();
      inicia(em, req, modeloForm);
      commit(em);
    } catch (RuntimeException e) {
      procesaError(modeloForm, "Error agregando.", e);
    } finally {
      cierra(em);
    }
  }
  protected void elimina(HttpServletRequest req, ModeloForm<MF> modeloForm) {
    EntityManager em = null;
    try {
      em = iniciaTransaccion();
      muestraOpciones(em, req, modeloForm);
      final I detalleId = getDetalleId(modeloForm);
      final M modelo = busca(em, detalleId);
      if (modelo != null) {
        setTitulo(modeloForm, modelo);
        em.remove(modelo);
      }
      em.flush();
      inicia(em, req, modeloForm);
      commit(em);
    } catch (Exception e) {
      procesaError(modeloForm, "Error modificando.", e);
    } finally {
      cierra(em);
    }
  }
  protected void muestraOpciones(EntityManager em, HttpServletRequest req,
      ModeloForm<MF> modeloForm) {
  }
  protected void inicia(EntityManager em, HttpServletRequest req,
      ModeloForm<MF> modeloForm) {
    final MF valores = modeloForm.getValores();
    valores.setMuestraDetalle("false");
    valores.setLista(consulta(em, req, modeloForm));
    setTitulo(modeloForm, null);
  }
  protected void setTitulo(ModeloForm<MF> modeloForm, M modelo) {
    if (isMuestraDetalle(modeloForm)) {
      modeloForm.setTitulo(isNuevo(modeloForm) || modelo == null
          ? tituloDeNuevo : getTituloDeModelo(modelo));
    } else {
      modeloForm.setTitulo(tituloMaestro);
    }
  }
  protected List<Renglon> consulta(EntityManager em, HttpServletRequest req,
      ModeloForm<MF> modeloForm) {
    return em.createNamedQuery(consultaFiltro, tipoModelo).
        setParameter("filtro", getFiltroCompleto(modeloForm)).
        getResultList().stream().map(c -> getRenglon(req, c)).
        collect(Collectors.toList());
  }
  protected String getFiltroCompleto(ModeloForm<MF> modeloForm) {
    return "%" + Objects.toString(modeloForm.getValores().getFiltro(), "").
        toUpperCase() + "%";
  }
  protected M busca(EntityManager em, I id) {
    return em.find(tipoModelo, id);
  }
  protected M buscaModelo(ModeloForm<MF> modeloForm, EntityManager em) {
    final I detalleId = getDetalleId(modeloForm);
    final M modelo = em.find(tipoModelo, detalleId);
    if (modelo == null) {
      throw new RuntimeException("El registro solicitado ha sido eliminado");
    }
    return modelo;
  }
  protected void configuraDetalle(MF modeloForm, String nuevo) {
    modeloForm.setNuevo(nuevo);
    modeloForm.setMuestraDetalle("true");
  }
  protected M creaModelo() {
    try {
      final M modelo = tipoModelo.newInstance();
      return modelo;
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
  protected boolean isNuevo(ModeloForm<MF> modeloForm) {
    return "true".equals(modeloForm.getValores().getNuevo());
  }
  protected boolean isMuestraDetalle(ModeloForm<MF> modeloForm) {
    return "true".equals(modeloForm.getValores().getMuestraDetalle());
  }
  protected I getDetalleId(ModeloForm<MF> modeloForm) {
    final String detalleId = modeloForm.getValores().getDetalleId();
    if (detalleId == null) {
      throw new RuntimeException("Falta el id de la selección.");
    }
    return newInstance(tipoIdentificador, detalleId);
  }
  protected void procesaAccion(HttpServletRequest req, ModeloForm<MF> modeloForm) {
  }
}
