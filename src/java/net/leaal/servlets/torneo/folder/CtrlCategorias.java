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
package net.leaal.servlets.torneo;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import net.leaal.servlets.Renglon;
import net.leaal.jee.torneo.entidad.Categoria;
import net.leaal.servlets.CtrlAbc;
import net.leaal.servlets.ModeloForm;
import static net.leaal.servlets.UtilServlets.texto;
import net.leaal.servlets.torneo.form.ModeloFormCategorias;

/**
 *
 * @author Soluciones LEAAL
 */
@WebServlet(name = "CtrlCategorias", urlPatterns = "ctrlCategorias")
public class CtrlCategorias
    extends CtrlAbc<Categoria, Long, ModeloFormCategorias> {
  public CtrlCategorias() {
    super(Categoria.FILTRO, Categoria.class, Long.class,
        ModeloFormCategorias.class, "Categorias", "Categoría Nueva");
  }
  @Override protected Renglon getRenglon(HttpServletRequest req,
      Categoria modelo) {
    return new Renglon(modelo.getId(), modelo.getNombre());
  }
  @Override protected String getTituloDeModelo(Categoria modelo) {
    return modelo.getNombre();
  }
  @Override protected void llenaModelo(EntityManager em,
      ModeloForm<ModeloFormCategorias> modeloForm, Categoria modelo) {
    modelo.setNombre(modeloForm.getValores().getNombre());
  }
  @Override protected void muestraModelo(EntityManager em,
      HttpServletRequest req, ModeloForm<ModeloFormCategorias> modeloForm,
      Categoria modelo) {
    modeloForm.getValores().setNombre(texto(modelo.getNombre()));
  }
}
