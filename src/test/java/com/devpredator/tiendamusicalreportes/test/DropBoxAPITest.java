/**
 * 
 */
package com.devpredator.tiendamusicalreportes.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

/**
 * @author DevPredator
 * Prueba Unitaria para verificar la comunicacion de un aplicativo java con DropBox.
 */
public class DropBoxAPITest {

	@Test
	public void test() {
		// :::::: SE CONFIGURA EL TOKEN DE ACCESO A LA APP CREADA EN DROPBOX :::::::
		String TOKEN = "sl.Amg1Js6MW6T79AwfznoCAdemrAzyO4fOEhrfvd98JNmMd6UUzg3qIPjbnvKHI1cTrMJ1xYKkAJDVBigpYLVclb01ucWK3_XBVjMYN7O88c2akGa67YFqP2Qm9oRXwiQ-xqqQDsk";
		
		// :::::: SE CONFIGURA EL TOKEN Y EL AMBIENTE DE CONFIGURACION INICIAL DE DROPBOX :::::::
		DbxRequestConfig dbxRequestConfig = DbxRequestConfig.newBuilder("devpredator/test-dropbox").build();
		DbxClientV2 dbxClientV2 = new DbxClientV2(dbxRequestConfig, TOKEN);
		
		try {
			assertNotNull(dbxClientV2);
			
			//:::::: SE OBTIENE Y SE MUESTRA LA INFORMACION DE LA CUENTA PERTECIENTE A LA APP :::::::::
			FullAccount fullAccount = dbxClientV2.users().getCurrentAccount();
			System.out.println("Nombre de la cuenta: " + fullAccount.getEmail());
		} catch (DbxException e) {
			e.printStackTrace();
			assertNull(dbxClientV2);
		}
	}

}
