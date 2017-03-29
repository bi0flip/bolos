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
var main = document.querySelector("main");
// hide body to prevent FOUC
document.body.style.opacity = 0;
if (navigator.serviceWorker) {
  navigator.serviceWorker.register('sw.js');
}
window.addEventListener("WebComponentsReady", function () {
  // show body now that everything is ready
  document.body.style.opacity = 1;
  window.addEventListener("hashchange", cambioDeHash);
  cambioDeHash();
});
function cambioDeHash() {
  borraTodosLosElementos(main);
  var hash = location.hash;
  switch (hash) {
    case "":
    case "#":
      main.appendChild(document.createElement("torneo-inicio"));
      break;
    default :
      var componente = hash.substring(1);
      if (componente.startsWith("form")) {
        main.appendChild(document.createElement("form", hash.substring(1)));
      } else {
        main.appendChild(document.createElement(hash.substring(1)));
      }
  }
}