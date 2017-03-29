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
 * @constructor
 * @param {IDBDatabase} base */
function InfoAviso(base) {
  Object.defineProperty(this, "base", {
    enumerable: true,
    get: function () {
      return base;
    }});
}
InfoAviso.prototype = {
  busca: function (id, funcionExito) {
    this.avisoParaConsultas().get(id).onsuccess = function (evento) {
      var modelo = evento.target.result;
      if (modelo && !modelo.eliminado) {
        funcionExito.call(null, modelo);
      } else {
        funcionExito.call(null);
      }
    };
  },
  /** lista los objetos registrados que no tienen borrado lógico.
   * @param {function(Object)} funcionExito */
  consulta: function (funcionExito) {
    var lista = [];
    this.avisoParaConsultas().index("por_fecha").openCursor().onsuccess
        = function (evento) {
          var cursor = evento.target.result;
          if (cursor) {
            var modelo = cursor.value;
            if (!modelo.eliminado) {
              lista.push({
                detalleId: modelo.id,
                texto1: modelo.titulo,
                texto2: new Date(modelo.modificacion).toLocaleString()
              });
            }
            cursor.continue();
          } else {
            funcionExito.call(null, lista);
          }
        };
  },
  /** Lista todos los objetos para sincronización, incluyendo los que tienen
   * borrado lógico.
   * @param {function(Object)} funcionExito*/
  consultaTodos: function (funcionExito) {
    var lista = [];
    this.avisoParaConsultas().index("por_fecha").openCursor().onsuccess
        = function (evento) {
          var cursor = evento.target.result;
          if (cursor) {
            lista.push(cursor.value);
            cursor.continue();
          } else {
            funcionExito.call(null, lista);
          }
        };
  },
  /** Agrega un objeto.
   * @param {Object} modelo
   * @param {function(Object)} funcionExito */
  agrega: function (modelo, funcionExito) {
    modelo.modificacion = new Date().getTime();
    modelo.eliminado = false;
    var Aviso = this.avisoParaCambios(funcionExito, true);
    // Estas son las operaciones de la transacción.
    Aviso.add(modelo);
  },
  /** Modifica un objeto.
   * @param {Object} modelo
   * @param {function(Object)} funcionExito  */
  modifica: function (modelo, funcionExito) {
    modelo.modificacion = new Date().getTime();
    var Aviso = this.avisoParaCambios(funcionExito, true);
    Aviso.put(modelo);
  },
  /** Elimina un objeto
   * @param {Object} modelo
   * @param {function(Object)} funcionExito  */
  elimina: function (modelo, funcionExito) {
    modelo.modificacion = new Date().getTime();
    modelo.eliminado = true;
    var Aviso = this.avisoParaCambios(funcionExito, true);
    Aviso.put(modelo);
  },
  /** Borra el contenido del almacén y guarda el contenido del listado.
   * @param {Array<Object>} lista
   * @param {function(Object)} funcionExito */
  reemplaza: function (lista, funcionExito) {
    var Aviso = this.avisoParaCambios(funcionExito);
    Aviso.clear();
    for (var i in lista) {
      Aviso.add(lista[i]);
    }
  },
  /** @returns {IDBObjectStore} */
  avisoParaConsultas: function () {
    // Inicia una transacción de lectura indicando los almacenes que usa.
    var tx = this.base.transaction(["Aviso"], "readonly");
    return tx.objectStore("Aviso");
  },
  /** @returns {IDBObjectStore}
   * @param {function} funcionExito
   * @param {boolean} sincroniza */
  avisoParaCambios: function (funcionExito, sincroniza) {
    /* Inicia una transacción de lectura y escritura indicando los almacenes que
     * usa. */
    var tx = this.base.transaction(["Aviso"], "readwrite");
    // Función después de terminar.
    tx.oncomplete = this.notificaExito.bind(this, funcionExito, sincroniza);
    return tx.objectStore("Aviso");
  },
  notificaExito: function (funcionExito, sincroniza) {
    if (sincroniza) {
      window.dispatchEvent(new Event("sincronizar"));
    }
    funcionExito.call(null);
  }
};