/*
 * Copyright 2016 Soluciones LEAAL.
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

import java.util.HashMap;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import net.leaal.jee.ReemplazoDeMensajes;

/** Indica como crear una instancia de Entity Manager.
 *
 * @author Soluciones LEAAL */
@Dependent
public class Configuracion {
  private static final HashMap<String, String> REEMPLAZO_DE_MENSAJES
      = new HashMap<>();
  static {
    REEMPLAZO_DE_MENSAJES.put("PRIMARY", "El identificador ya existe.");
    REEMPLAZO_DE_MENSAJES.put("CAT_PK", "El id de la categoría ya existe.");
    REEMPLAZO_DE_MENSAJES.put("CAT_NOUN", "El nombre la categoría ya existe.");
    REEMPLAZO_DE_MENSAJES.put("CAT_NOCH", "Falta el nombre de la categoría.");
    REEMPLAZO_DE_MENSAJES.put("USU_PK",
        "El identificador de usuario ya está registrado.");
    REEMPLAZO_DE_MENSAJES.put("USU_IDCH",
        "El identificador de usuario no puede tener menos de 5 caracteres.");
    REEMPLAZO_DE_MENSAJES.put("USU_COCH", "Falta la contraseña del usuario.");
    REEMPLAZO_DE_MENSAJES.put("USU_NOCH", "Falta el nombre del usuario.");
    REEMPLAZO_DE_MENSAJES.put("DIS_ID",
        "El identificador de dispositivo ya está registrado.");
    REEMPLAZO_DE_MENSAJES.put("DIS_USU",
        "El dispositivo debe apuntar a una usuario válido.");
    REEMPLAZO_DE_MENSAJES.put("ROL_PK",
        "El identificador del rol ya está registrado.");
    REEMPLAZO_DE_MENSAJES.put("UR_PK",
        "La combinación de usuario y rol ya está registrada.");
    REEMPLAZO_DE_MENSAJES.put("UR_USU",
        "La combinación de usuario y rol debe apuntar a un usuario válido.");
    REEMPLAZO_DE_MENSAJES.put("UR_ROL",
        "La combinación de usuario y rol debe apuntar a un rol válido.");
    REEMPLAZO_DE_MENSAJES.put("CLI_PK",
        "El identificador del cliente ya está registrado.");
    REEMPLAZO_DE_MENSAJES.put("CLI_VAUN",
        "La venta actual del cliente ya está registrada.");
    REEMPLAZO_DE_MENSAJES.put("CLI_USU",
        "El cliente debe apuntar a un usuario válido.");
    /*REEMPLAZO_DE_MENSAJES.put("ARC_PK",
    "El identificador del archivo ya está registrado.");
    REEMPLAZO_DE_MENSAJES.put("PRD_PK",
    "El identificador del producto ya está registrado.");
    REEMPLAZO_DE_MENSAJES.put("PRD_NOCH",
    "El nombre del producto no puede estar en blanco.");
    REEMPLAZO_DE_MENSAJES.put("PRD_NOUN",
    "El nombre del producto ya está registrado.");
    REEMPLAZO_DE_MENSAJES.put("PRD_CAT",
    "El producto debe apuntar a una categoría válida.");
    REEMPLAZO_DE_MENSAJES.put("PRD_IMUN",
    "La imagen del producto ya está registrada.");
    REEMPLAZO_DE_MENSAJES.put("PRD_IMG",
    "El producto debe apuntar a una imagen válida.");
    REEMPLAZO_DE_MENSAJES.put("VNT_PK",
    "El identificador de la venta ya está registrado.");
    REEMPLAZO_DE_MENSAJES.put("VNT_DICH",
    "La direccion de entrega de la venta no puede estar en blanco.");
    REEMPLAZO_DE_MENSAJES.put("VNT_CLI",
    "La venta debe apuntar a un cliente válido.");
    REEMPLAZO_DE_MENSAJES.put("DVT_PK",
    "El identificador del detalle de venta ya está registrado.");
    REEMPLAZO_DE_MENSAJES.put("DVT_VNT",
    "El detalle de venta debe apuntar a una venta válida.");
    REEMPLAZO_DE_MENSAJES.put("DVT_PRD",
    "El detalle de venta debe apuntar a un producto válido.");
    REEMPLAZO_DE_MENSAJES.put("CLI_VAC",
    "El cliente de venta debe apuntar a una venta actual válida.");*/
  }
  @Produces @PersistenceUnit EntityManagerFactory emf;
  @Produces @ReemplazoDeMensajes
  public HashMap<String, String> getREEMPLAZO_DE_MENSAJES() {
    return REEMPLAZO_DE_MENSAJES;
  }
}
