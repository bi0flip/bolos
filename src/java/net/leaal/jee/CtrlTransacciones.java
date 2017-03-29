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
package net.leaal.jee;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Soluciones LEAAL
 */
@ApplicationScoped
public class CtrlTransacciones {
  @Inject private EntityManagerFactory emf;
  public EntityManager inicia() {
    final EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    return em;
  }
  public void commit(EntityManager em) {
    em.getTransaction().commit();
  }
  public void rollback(EntityManager em) {
    em.getTransaction().rollback();
  }
  public void cierra(EntityManager em) {
    if (em != null) {
      try {
        if (em.getTransaction().isActive()) {
          try {
            rollback(em);
          } catch (Exception e) {
            Logger.getLogger(getClass().getName()).
                log(Level.SEVERE, "Error al hacer rollback.", e);
          }
        }
      } finally {
        em.close();
      }
    }
  }
}
