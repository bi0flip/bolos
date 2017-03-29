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

import java.io.IOException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Soluciones LEAAL
 */
@ServerEndpoint("/ctrlNotificacion")
public class CtrlNotificacion {
  private static final Logger LOGGER = Logger.getLogger(CtrlNotificacion.class.
      getName());
  /* Cola para todas las sesiones de WebSocket abiertas. */
  private static final Queue<Session> QUEUE = new ConcurrentLinkedQueue<>();
  public static void notifica(final String usuarioId, final String mensaje)
      throws IOException {
    QUEUE.parallelStream().
        filter(sesion -> sesion.isOpen() && sesion.getUserPrincipal() != null
        && Objects.equals(usuarioId, sesion.getUserPrincipal().getName())).
        forEach(sesion -> {
          try {
            sesion.getBasicRemote().sendText(mensaje);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
  }
  @OnOpen public void openConnection(Session session) {
    /* Registra la conexión en la cola. */
    QUEUE.add(session);
    LOGGER.log(Level.INFO, "Sesión de CtrlNotificacion abierta.");
  }
  @OnClose public void closedConnection(Session session) {
    /* Saca la sesión de la cola. */
    QUEUE.remove(session);
    LOGGER.log(Level.INFO, "Sesión de CtrlNotificacion cerrada.");
  }
  @OnError public void error(Session session, Throwable e) {
    /* Saca la sesión de la cola. */
    QUEUE.remove(session);
    LOGGER.log(Level.SEVERE, "Error de notificación.", e);
  }
}
