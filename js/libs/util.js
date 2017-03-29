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
function texto(s) {
  return s ? s : "";
}
function getMensaje(e) {
  return e.message;
}
function buscaName(padre, name) {
  return padre.querySelector("[name='" + name + "']");

}
function registraElement(nombre, estructura) {
  return document.registerElement(nombre, estructura);
}
function elementPrevio() {
  return document._currentScript.previousElementSibling;
}
function cargaTemplate(padre, template, selector) {
  var content = document.importNode(template.content, true);
  if (selector && window.ShadowDOMPolyfill) {
    WebComponents.ShadowCSS.shimStyling(content, selector);
  }
  padre.appendChild(content);
}
function iniciaAtributos(elemento) {
  for (var i = 0, attributes = elemento.attributes,
      length = attributes.length; i < length; i++) {
    var atributo = attributes[i];
    if (atributo) {
      elemento.attributeChangedCallback(attributes[i].name, null,
          attributes[i].value);
    }
  }
}
function borraTodosLosElementos(padre) {
  while (padre.firstChild) {
    padre.removeChild(padre.firstChild);
  }
}
function  implementaAcciones(element) {
  var acciones = element.querySelectorAll("[data-accion]");
  for (var i = 0, length = acciones.length; i < length; i++) {
    var e = acciones[i];
    e.addEventListener("click", function (evento) {
      var target = evento.target;
      target.dispatchEvent(new CustomEvent("accion", {
        detail: {accion: target.dataset.accion},
        bubbles: true
      }));
    });
  }
}
function muestraAlerta(mensaje) {
  window.console.error(mensaje);
  muestraMensaje(mensaje, "img/error-flat.png");
}
function muestraMensaje(mensaje, icono) {
  if (window.Notification) {
    // Solicita que se autoricen las notificaciones.
    window.Notification.requestPermission(function (permission) {
      // Si el usuario acepta, se crea la notificación.
      if (permission === "granted") {
        var opciones = {
          body: mensaje,
          icon: icono
        };
        try {
          new Notification("Bowling AMF", opciones);
        } catch (e) {
          navigator.serviceWorker.ready.then(function (registro) {
            registro.showNotification("Bowling AMF", opciones);
          });
        }
      }
    });
  } else {
    alert(mensaje);
  }
}