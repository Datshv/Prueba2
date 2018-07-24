package cashBack;



import static org.junit.Assert.assertEquals;

import org.junit.Test;

import manejador.EjecutarArchivoPrueba;

public class CashBackTest {

	/**
	 * Prueba de CashBack y Bonos
	 */
	@Test
	public void validarQueAlHacerCashBackSeaExitoso() {
		EjecutarArchivoPrueba prueba = new EjecutarArchivoPrueba("D:\\Mateo\\Data\\Nequi_RecargarSaldo_Paypal.xls", "1",null);
		String numeroDeTransaccion = prueba.leerArchivoXML(prueba.xmlResponse,"TrnIdentifier","TrnId");
		String valorDeLaTransaccion = prueba.mapaDeValoresQueRemplazanElRequest.get("**amountValue**");
		String valorEnBD = prueba.consultarEnBaseDatos("SELECT TRAN_AMT FROM TBAADM.DTD WHERE TRIM(TRAN_ID) = '"+numeroDeTransaccion+"' AND PART_TRAN_TYPE = 'C'");
		assertEquals(valorDeLaTransaccion, valorEnBD);
		
		
	}
	
	
	
//	@Test
//	public void recargarPayPal() {
//		EjecutarArchivoPrueba prueba2 = new EjecutarArchivoPrueba("D:\\Mateo\\Data\\Nequi_RecargarSaldo_Paypal.xls", "1", null);
//		String valorQueYoRecargue = prueba2.mapaDeValoresQueRemplazanElRequest.get("**amountValue**");
//		String valorQuePusoEnElRequest = prueba2.leerArchivoXML(prueba2.xmlRequest, "TrnAmt", "amountValue");
//		assertEquals(valorQueYoRecargue, valorQuePusoEnElRequest);
//		String resultadoEsperado = "SUCCESS";
//		String resultadoObtenido = prueba2.leerArchivoXML(prueba2.xmlResponse, "HostTransaction", "Status");
//		assertEquals(resultadoEsperado, resultadoObtenido);
//
//	}
	
	
	
	

}
