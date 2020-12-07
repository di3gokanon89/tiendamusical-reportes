/**
 * 
 */
package com.devpredator.tiendamusicalreportes.services;

import javax.ws.rs.core.Response;

import com.dropbox.core.v2.DbxClientV2;

/**
 * @author DevPredator
 * Interface que proporciona los metodos para realizar la logica de negocio para notificacion
 * por email.
 */
public interface MailService {
	/**
	 * Metodo que permite enviar un correo a traves del servicio de AWS SES.
	 * @param dbxClientV2 {@link DbxClientV2} objeto con la informacion de dropbox.
	 * @param destinatario {@link String} email a enviar el correo.
	 * @param cliente {@link String} nombre completo del cliente a enviar el correo.
	 * @param orderID {@link String} orden del pedido de la compra.
	 * @return {@link Response} respuesta generada al enviar el correo.
	 */
	public Response enviarEmail(DbxClientV2 dbxClientV2, String destinatario, String cliente, String orderID);
}
