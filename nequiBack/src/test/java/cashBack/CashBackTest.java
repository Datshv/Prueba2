package cashBack;



import static org.junit.Assert.assertEquals;

import org.junit.Test;

import baseDatos.BaseDeDatos;
import manejador.EjecutarArchivoPrueba;

class CashBackTest {

	/**
	 * Prueba de CashBack y Bonos
	 */
	@Test
	void validarQueAlHacerCashBackSeaExitoso() {
		EjecutarArchivoPrueba prueba = new EjecutarArchivoPrueba("D:\\Mateo\\Data\\Nequi_Cashback_y_Bonos.xls", "1",null);
		String resultadoEsperado = prueba.mapaDeValoresDelDataDriven.get(6);
	    String resultado = prueba.leerArchivoXML(prueba.xmlResponse, "HostTransaction", "Status");
	    System.out.println(prueba.consultarEnBaseDatos("SELECT 1+1 FROM DUAL")); 
	    
	    assertEquals(resultadoEsperado, resultado);
	}
	
	
	
	@Test
	void recargarPayPal() {
		EjecutarArchivoPrueba prueba2 = new EjecutarArchivoPrueba("D:\\Mateo\\Data\\Nequi_RecargarSaldo_Paypal.xls", "1", null);
		String valorQueYoRecargue = prueba2.mapaDeValoresQueRemplazanElRequest.get("**amountValue**");
		String valorQuePusoEnElRequest = prueba2.leerArchivoXML(prueba2.xmlRequest, "TrnAmt", "amountValue");
		assertEquals(valorQueYoRecargue, valorQuePusoEnElRequest);
		String resultadoEsperado = "SUCCESS";
		String resultadoObtenido = prueba2.leerArchivoXML(prueba2.xmlResponse, "HostTransaction", "Status");
		assertEquals(resultadoEsperado, resultadoObtenido);
	
		
		
	}
	
	
	
	

}
