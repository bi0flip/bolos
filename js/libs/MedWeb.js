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
function MedWeb(form, url) {
  Object.defineProperties(this, {
    form: {enumerable: true, value: form},
    url: {enumerable: true, value: url},
    titulo: {enumerable: true, value: document.getElementById("titulo")}
  });
}
MedWeb.prototype = {
  inicia: function () {
    this.form.addEventListener("accion", this.procesaAccion.bind(this));
    this.form.
        addEventListener("submit", this.ejecuta.bind(this, "submit", null));
    this.descargaEstadoInicial();
    this.ejecutaInterruptores();
    this.ejecutaInterruptoresInvertidos();
    implementaAcciones(this.form);
    this.implementaInputs();
  },
  ejecutaInterruptores: function () {
    var interrumpidos = this.form.querySelectorAll("[data-interruptor]");
    for (var i = 0, length = interrumpidos.length; i < length; i++) {
      var interrumpido = interrumpidos[i];
      var interruptorId = interrumpido.dataset.interruptor;
      if (interruptorId) {
        var interruptor = document.getElementById(interruptorId);
        if (!interruptor) {
          interruptor = buscaName(this.form, interruptorId);
        }
        interrumpido.style.display
            = (interruptor && interruptor.value === "true") ? "block" : "none";
      }
    }
  },
  ejecutaInterruptoresInvertidos: function () {
    var interrumpidos
        = this.form.querySelectorAll("[data-interruptor-invertido]");
    for (var i = 0, length = interrumpidos.length; i < length; i++) {
      var interrumpido = interrumpidos[i];
      var interruptorId = interrumpido.dataset.interruptorInvertido;
      if (interruptorId) {
        var interruptor = document.getElementById(interruptorId);
        if (!interruptor) {
          interruptor = buscaName(this.form, interruptorId);
        }
        interrumpido.style.display
            = (interruptor && interruptor.value === "true") ? "none" : "block";
      }
    }
  },
  implementaInputs: function () {
    var acciones = this.form.querySelectorAll("[data-input]");
    for (var i = 0, length = acciones.length; i < length; i++) {
      var element = acciones[i];
      element.addEventListener("input", function () {
        element.dispatchEvent(new CustomEvent("accion", {
          detail: {accion: element.dataset.input},
          bubbles: true
        }));
      });
    }
  },
  descargaEstadoInicial: function () {
    this.limpiaErrores();
    var xhr = this.creaXhr();
    xhr.open("GET", this.url);
    xhr.send();
  },
  procesaAccion: function (event) {
    var detail = event.detail;
    this.ejecuta(detail.accion, detail.detalleId);
  },
  ejecuta: function (accion, detalleId) {
    this.limpiaErrores();
    var formData = new FormData(this.form);
    formData.append("accion", accion);
    if (detalleId) {
      formData.append("detalleId", detalleId);
    }
    this.llenaFormData(formData);
    var xhr = this.creaXhr();
    xhr.open("POST", this.url);
    xhr.send(formData);
  },
  llenaFormData: function (formData) {},
  limpiaErrores: function () {
    var campos = this.form.querySelectorAll(".campo");
    for (var i = 0, length = campos.length; i < length; i++) {
      var campo = campos[i];
      campo.dataset.error = "";
    }
  },
  creaXhr: function () {
    var xhr = new XMLHttpRequest();
    xhr.addEventListener("error", this.recibeError.bind(this));
    xhr.addEventListener("abort", this.abortado.bind(this));
    xhr.addEventListener("load", this.recibeModeloForm.bind(this));
    return xhr;
  },
  recibeError: function (evento) {
    this.muestraError(evento.target.statusText);
  },
  abortado: function () {
    this.muestraError("Abortado.");
  },
  recibeModeloForm: function (evento) {
    var xhr = evento.target;
    var status = xhr.status;
    if (200 <= status && status < 300) {
      var respuesta = xhr.responseText;
      try {
        var modeloForm = JSON.parse(respuesta);
        this.procesaModeloForm(modeloForm);
      } catch (e) {
        this.muestraError(getMensaje(e));
      }
    } else {
      this.muestraError(xhr.statusText);
    }
  },
  procesaModeloForm: function (modeloForm) {
    if (modeloForm.siguienteForm) {
      this.siguienteForm(modeloForm.siguienteForm);
    } else {
      this.muestraTitulo(modeloForm);
      this.muestraErrorModeloForm(modeloForm);
      this.muestraViolaciones(modeloForm);
      this.muestraOpciones(modeloForm);
      this.muestraValores(modeloForm);
      this.ejecutaInterruptores();
      this.ejecutaInterruptoresInvertidos();
    }
    this.formActualizada(modeloForm);
  },
  formActualizada: function (modeloForm) {
  },
  siguienteForm: function (form) {
    location.hash = "#" + form;
  },
  muestraTitulo: function (modeloForm) {
    if (modeloForm.titulo) {
      var titulo = texto(modeloForm.titulo);
      document.title = titulo;
      this.titulo.textContent = titulo;
    }
  },
  muestraErrorModeloForm: function (modeloForm) {
    var error = texto(modeloForm.error);
    if (error) {
      this.muestraError(error);
    }
  },
  muestraViolaciones: function (modeloForm) {
    var violaciones = modeloForm.violaciones;
    if (violaciones) {
      for (var propiedad in violaciones) {
        this.muestraViolacion(propiedad, violaciones[propiedad]);
      }
    }
  },
  muestraOpciones: function (modeloForm) {
    var opciones = modeloForm.opciones;
    if (opciones) {
      for (var propiedad in opciones) {
        this.muestraOpcion(propiedad, opciones[propiedad]);
      }
    }
  },
  muestraValores: function (modeloForm) {
    var valores = modeloForm.valores;
    if (valores) {
      for (var propiedad in valores) {
        this.muestraValor(propiedad, valores[propiedad]);
      }
    }
  },
  muestraError: function (mensaje) {
    muestraAlerta(mensaje);
  },
  muestraViolacion: function (propiedad, mensaje) {
    var element = document.getElementById(propiedad);
    if (element) {
      element.setAttribute("data-error", mensaje);
    } else {
      muestraError(propiedad + ": " + mensaje);
    }
  },
  muestraOpcion: function (propiedad, opciones) {
    // Primero busca por id.
    var element = document.getElementById(propiedad);
    if (element && "opciones" in element) {
      element.opciones = opciones;
    }
  },
  muestraValor: function (propiedad, valor) {
    // Primero busca por id.
    var element = document.getElementById(propiedad);
    if (!element) {
      // Si el id no existe, busca por name.
      element = buscaName(this.form, propiedad);
    }
    if (element) {
      if ("value" in element) {
        element.value = valor;
      } else if ("src" in element) {
        element.src = valor;
      } else {
        element.textContent = valor;
      }
    }
  }
};