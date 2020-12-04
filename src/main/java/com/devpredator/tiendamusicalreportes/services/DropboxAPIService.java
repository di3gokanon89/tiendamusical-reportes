/**
 * 
 */
package com.devpredator.tiendamusicalreportes.services;

import java.io.IOException;

import javax.ws.rs.core.Response;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.UploadErrorException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author DevPredator
 * Interface que proporciona los metodos para acceder a la API de Dropbox.
 */
public interface DropboxAPIService {
	/**
	 * Metodo que permite descargar el archivo compilado de jasperreports para compilar el reporte y generar el pdf.
	 * @param dbxClientV2 {@link DbxClientV2} api para conectarse a Dropbox.
	 * @param orderID {@link String} numero de pedido de la compra de los productos.
	 * @param cliente {@link String} nombre completo del cliente que realizo la compra.
	 * @return {@link Response} respuesta generada por el webservice.
	 */
	Response descargarReporte(DbxClientV2 dbxClientV2, String orderID, String cliente);
	/**
	 * Metodo que permite cargar o subir el reporte pdf generado con JasperReports a Dropbox.
	 * @param dbxClientV2 {@link DbxClientV2} api para conectarse a Dropbox.
	 * @param orderID {@link String} numero de pedido de la compra de los productos.
	 * @param cliente {@link String} nombre completo del cliente que realizo la compra.
	 * @param jasperPrint {@link JasperPrint} archivo de jasper generado como PDF.
	 * @throws IOException {@link IOException} excepcion generada en caso de error al crear el archivo pdf en la carpeta temporal.
	 * @throws JRException {@link JRException} excepcion generada en caso de error al exportar la informacion del reporte al archivo temporal.
	 * @throws DbxException {@link DbxException} excepcion generada en caso de error al realizar el proceso de carga
	 * @throws UploadErrorException {@link UploadErrorException} excepcion generada en caso de error en el momento de que se esta subiendo el archivo pdf a Dropbox
	 */
	void cargarReporteToDropbox(DbxClientV2 dbxClientV2, String orderID, String cliente, JasperPrint jasperPrint) throws IOException, JRException, UploadErrorException, DbxException;
}
