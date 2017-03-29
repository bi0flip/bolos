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

import com.fasterxml.jackson.jr.ob.JSON;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import net.leaal.jee.torneo.entidad.Aviso;

/**
 *
 * @author Soluciones LEAAL
 */
public class DecoderAvisos implements Decoder.TextStream<List<Aviso>> {
  @Override
  public List<Aviso> decode(Reader r) throws DecodeException, IOException {
    return JSON.std.listOfFrom(Aviso.class, r);
  }
  @Override public void init(EndpointConfig config) {
  }
  @Override public void destroy() {
  }
}
