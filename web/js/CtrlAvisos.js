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
"use strict";
/** @constructor
 * @param {HTMLFormElement} form */
function CtrlAvisos(form) {
  Object.defineProperties(this, {
    form: {enumerable: true, value: form},
    titu: {enumerable: true, value: document.getElementById("titulo")},
    tit: {enumerable: true, value: form.querySelector("#tit")},
    fecha: {enumerable: true, value: form.querySelector("#fecha")},
    texto: {enumerable: true, value: form.querySelector("#texto")},
    lista: {enumerable: true, value: form.querySelector("#lista")},
    maestro: {enumerable: true, value: form.querySelector("#maestro")},
    detalle: {enumerable: true, value: form.querySelector("#detalle")},
    eliminar: {enumerable: true, value: form.querySelector("#eliminar")},
    elemento: {enumerable: true, value: form.querySelector(".elemento")}
  });
}
CtrlAvisos.prototype = {
  inicia: function () {
    this.uuid = 0;
    implementaAcciones(this.form);
    Object.defineProperties(this, {
      listenerSincronizado: {
        enumerable: true,
        value: this.estadoInicial.bind(this)
      },
      listenerBdAbierta: {
        enumerable: true,
        value: this.bdAbierta.bind(this)
      }
    });
    this.form.addEventListener("submit", this.guarda.bind(this));
    this.form.addEventListener("accion", this.procesaAccion.bind(this));
    window.addEventListener("sincronizado", this.listenerSincronizado);
    window.addEventListener("bdAbierta", this.listenerBdAbierta);
    if (!window.bdHelper) {
      window.bdHelper = new BdHelper();
      window.bdHelper.inicia();
    }
  },
  termina: function () {
    if (this.listenerSincronizado) {
      window.removeEventListener("sincronizado", this.listenerSincronizado);
    }
    if (this.listenerBdAbierta) {
      window.removeEventListener("bdAbiertaI", this.listenerBdAbierta);
    }
    if (this.sincronizacion && this.sincronizacion.webSocket) {
      this.sincronizacion.webSocket.close();
    }
  },
  bdAbierta: function (evento) {
    Object.defineProperty(this, "info", {
      enumerable: true,
      value: new InfoAviso(evento.detail)
    });
    this.estadoInicial();
    Object.defineProperty(this, "sincronizacion", {
      enumerable: true,
      value: new Sincronizacion(this.info)
    });
    this.sincronizacion.inicia();
  },
  procesaAccion: function (event) {
    var detail = event.detail;
    switch (detail.accion) {
      case "action_agregar":
        this.agrega();
        break;
      case "detalle":
        this.info.busca(detail.detalleId, this.procesaDetalle.bind(this));
        break;
      case "action_cancelar":
        this.estadoInicial();
        break;
      case "action_eliminar":
        this.elimina();
    }
  },
  estadoInicial: function () {
    this.titulo = "Avisos";
    this.maestro.style.display = "block";
    this.detalle.style.display = "none";
    this.info.consulta(this.muestraLista.bind(this));
  },
  muestraLista: function (lista) {
    this.lista.value = lista;
  },
  agrega: function () {
    this.modelo = {};
    this.configuraDetalle(true);
    this.tit.style.display = "block";
    this.fecha.style.display = "none";
    this.eliminar.style.display = "none";
    this.titulo = "Aviso Nuevo";
    this.tit.value = "";
    this.texto.value = "";
    this.tit.error = "";
    this.texto.error = "";
  },
  procesaDetalle: function (modelo) {
    this.modelo = modelo;
    this.configuraDetalle(false);
    this.tit.style.display = "none";
    this.fecha.style.display = "block";
    this.eliminar.style.display = "inline-block";
    this.titulo = modelo.titulo;
    this.fecha.value = new Date(modelo.modificacion).toLocaleString();
    this.texto.value = modelo.texto;
  },
  configuraDetalle: function (nuevo) {
    this.nuevo = nuevo;
    this.maestro.style.display = "none";
    this.detalle.style.display = "block";
  },
  guarda: function () {
    try {
      this.llenaModelo();
      if (this.nuevo) {
        this.info.agrega(this.modelo, this.estadoInicial.bind(this));
      } else {
        this.info.modifica(this.modelo, this.estadoInicial.bind(this));
      }
    } catch (e) {
      this.muestraError(getMensaje(e));
    }
  },
  elimina: function () {
    if (!this.nuevo) {
      this.info.elimina(this.modelo, this.estadoInicial.bind(this));
    }
  },
  set titulo(_titulo) {
    this.titu.textContent = texto(_titulo);
    document.title = _titulo;
  },
  llenaModelo: function () {
    if (this.nuevo) {
      this.modelo.id = Date.now().toString() + Math.random() + this.uuid++;
      var tit = this.tit.value.trim();
      if (tit) {
        this.modelo.titulo = tit;
      } else {
        throw new Error("Falta el título.");
      }
    }
    var texto = this.texto.value.trim();
    if (texto) {
      this.modelo.texto = texto;
    } else {
      throw new Error("Falta el texto.");
    }
  },
  muestraError: function (mensaje) {
    muestraAlerta(mensaje);
  }
};