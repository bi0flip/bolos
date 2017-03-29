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
/** Maneja el almacenamiento local usando IndexedDb con borrado lógico.
 * @constructor */
function BdHelper() {}
BdHelper.prototype = {
  inicia: function () {
    var solicitud = window.indexedDB.open("boliche", 1);
    solicitud.onerror = this.muestraError.bind(this);
    // Al crear la base o hacerle cambios hay que crear las tablas.
    solicitud.onupgradeneeded = this.actualizaBd.bind(this);
    // Función al abrir la base.
    solicitud.onsuccess = this.bdAbierta.bind(this);
  },
  bdAbierta: function (evento) {
    var base = evento.target.result;
    // Función en caso de error.
    base.onerror = this.muestraError.bind(this);
    console.log("BD abierta");
    window.dispatchEvent(new CustomEvent("bdAbierta", {detail: base}));
  },
  actualizaBd: function (evento) {
    var base = evento.target.result;
    // Automáticamente se inicia una transacción para el cambio de versión.
    evento.target.transaction.onerror = this.muestraError.bind(this);
    // Como hay cambio de versión borra el almacén si la bd existe.
    if (base.objectStoreNames.contains("Aviso")) {
      base.deleteObjectStore("Aviso");
    }
    // Crea el almacén "Nota" con el campo llave "id".
    var Aviso = base.createObjectStore("Aviso", {keyPath: "id"});
    // Crea un índice para ordenar el nombre.
    Aviso.createIndex("por_fecha", "modificacion");
  },
  muestraError: function (evento) {
    alert(evento.target.error.name);
  }
};