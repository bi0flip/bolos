/* 
 * Copyright 2016 Gilberto Pacheco Gallegos.
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
function MedNotificacion() {
  Object.defineProperty(this, "url", {
    enumerable: true,
    value: "ws://" + location.hostname + ":" + location.port
        + "/ctrlNotificacion"
  });
}
MedNotificacion.prototype = {
  inicia: function () {
    this.webSocket = new WebSocket(this.url);
    this.webSocket.onopen = this.webSocketAbierto.bind(this);
    this.webSocket.onmessage = this.mensajeRecibido.bind(this);
    this.webSocket.onerror = this.muestraError.bind(this);
    this.webSocket.onclose = this.webSocketCerrado.bind(this);
  },
  webSocketAbierto: function () {
    window.console.log("Web socket notiicación abierto.");
  },
  mensajeRecibido: function (evento) {
    muestraMensaje(evento.data, "img/ic_launcher_72px.png");
  },
  webSocketCerrado: function () {
    window.console.log("Web socket cerrado.");
  },
  muestraError: function (evento) {
    muestraAlerta(evento.type);
  }
};