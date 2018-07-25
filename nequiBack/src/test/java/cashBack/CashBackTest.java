package cashBack;




import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import manejador.EjecutarArchivoPrueba;

public class CashBackTest {

	/**
	 * Prueba de CashBack y Bonos
	 * @throws SQLException 
	 */
	@Test
	public void validarQueAlHacerCashBackSeaExitoso() throws SQLException {
		EjecutarArchivoPrueba prueba = new EjecutarArchivoPrueba("D:\\Mateo\\Data\\Nequi_RecargarSaldo_Paypal.xls", "1",null);
		String numeroDeTransaccion = prueba.leerArchivoXML(prueba.xmlResponse,"TrnIdentifier","TrnId");
	//	String valorDeLaTransaccion = prueba.mapaDeValoresQueRemplazanElRequest.get("**amountValue**");
		//String valorEnBD = prueba.consultarEnBaseDatos("SELECT TRAN_AMT FROM TBAADM.DTD WHERE TRIM(TRAN_ID) = '"+numeroDeTransaccion+"' AND PART_TRAN_TYPE = 'C'");
		ResultSet resultados = prueba.consultarEnBaseDatosRecibirVariasFilas("SELECT TRAN_AMT,ACID FROM TBAADM.DTD WHERE TRIM(TRAN_ID) = '"+numeroDeTransaccion+"'");
		while(resultados.next()) {
			System.out.println("tran_amt:"+resultados.getDouble("TRAN_AMT"));
			System.out.println("acid:"+resultados.getString("ACID"));
		}
		//assertEquals(valorDeLaTransaccion, valorEnBD);
		
	}
	
	
	

	
	
	
	

}
