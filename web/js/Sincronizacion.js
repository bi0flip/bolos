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
function Sincronizacion(info) {
  Object.defineProperties(this, {
    info: {enumerable: true, value: info},
    url: {
      enumerable: true,
      value: "ws://" + location.hostname + ":" + location.port
          + "/sincronizacion"
    }});
}
Sincronizacion.prototype = {
  inicia: function () {
    Object.defineProperty(this, "webSocket", {
      enumerable: true,
      value: new WebSocket(this.url)
    });
    this.webSocket.onopen = this.webSocketAbierto.bind(this);
    this.webSocket.onmessage = this.mensajeRecibido.bind(this);
    this.webSocket.onerror = this.muestraError.bind(this);
    this.webSocket.onclose = this.webSocketCerrado.bind(this);
  },
  webSocketAbierto: function () {
    window.console.log("Web socket sincronizacion abierto.");
    window.addEventListener("sincronizar", this.sincroniza.bind(this));
    this.sincroniza();
  },
  mensajeRecibido: function (evento) {
    var sincronizacion = window.JSON.parse(evento.data);
    if (sincronizacion.error) {
      alert(sincronizacion.error);
    } else if (sincronizacion.finalizada) {
      this.info.reemplaza(sincronizacion.lista || [],
          this.sincronizado.bind(this));
    } else {
      this.sincroniza();
    }
  },
  webSocketCerrado: function () {
    window.console.log("Web socket cerrado.");
  },
  muestraError: function (evento) {
    muestraAlerta(evento.type);
  },
  sincroniza: function () {
    this.info.consultaTodos(this.recibeTodos.bind(this));
  },
  recibeTodos: function (lista) {
    this.webSocket.send(window.JSON.stringify(lista));
  },
  sincronizado: function () {
    window.dispatchEvent(new Event("sincronizado"));
  }
};