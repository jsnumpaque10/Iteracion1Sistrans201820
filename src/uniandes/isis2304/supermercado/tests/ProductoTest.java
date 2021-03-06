package uniandes.isis2304.supermercado.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.FileReader;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import uniandes.isis2304.supermercados.negocio.SuperAndes;
import uniandes.isis2304.supermercados.negocio.VOProducto;

public class ProductoTest 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecuci�n
	 */
	private static Logger log = Logger.getLogger(ProductoTest.class.getName());
	
	/**
	 * Ruta al archivo de configuraci�n de los nombres de tablas de la base de datos: La unidad de persistencia existe y el esquema de la BD tambi�n
	 */
	private static final String CONFIG_TABLAS_A = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
	/**
	 * La clase que se quiere probar
	 */
    private SuperAndes superAndes;
    
    /* ****************************************************************
	 * 			M�todos de prueba para la tabla Producto - Creaci�n y borrado
	 *****************************************************************/
	/**
	 * M�todo que prueba las operaciones sobre la tabla Producto
	 * 1. Adicionar un producto
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
     */
    @Test
    public void CRDProductoTest()
    {
    	// Probar primero la conexi�n a la base de datos
    			try
    			{
    				log.info ("Probando las operaciones CRD sobre Producro");
    				superAndes = new SuperAndes (openConfig (CONFIG_TABLAS_A));
    			}
    			catch(Exception e)
    			{
    				log.info("Prueba de CRD de Producto incompleta. No se pudo conectar a la base de datos !!. La excepci�n generada es: " + e.getClass ().getName ());
    				log.info ("La causa es: " + e.getCause ().toString ());
    				
    				String msg = "Prueba de CRD de Producto incompleta. No se pudo conectar a la base de datos !!.\n";
    				msg += "Revise el log de superAndes y el de datanucleus para conocer el detalle de la excepci�n";
    				System.out.println (msg);
    				fail (msg);
    			}
    			// Ahora si se pueden probar las operaciones
    	    	try
    			{
    				// Lectura de los productos con la tabla vac�a
    				List <VOProducto> lista = superAndes.darVOProducto();
    				assertEquals ("No debe haber tipos de bebida creados!!", 0, lista.size ());

    				// Lectura de los productos con un producto adicionado
    				String codigoBarras = "f1f0f1f0f1f0";
    				int tipoProducto = 11;
    				String nombreProducto = "Queso Alpina";
    				String marcaProducto = "Alpina";
    				String presentacionProducto = "En caja";
    				String unidadDeMedida = "0.2kg";
    				double cantidadPresentacion = 1.2;
    				double pesoDeEmpaque = 0.2;
    				double volumenEmpaque = 2.3;
    				
    				VOProducto producto1 = superAndes.adicionarProducto(codigoBarras, tipoProducto, nombreProducto, marcaProducto, presentacionProducto, unidadDeMedida, cantidadPresentacion, pesoDeEmpaque, volumenEmpaque);
    				lista = superAndes.darVOProducto();
    				assertEquals ("Debe haber un Producto creado !!", 1, lista.size ());
    				assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", producto1, lista.get (0));
    				
    				// Lectura de los productos con un producto adicionado
    				String codigoBarras2 = "g1g0g1g0g1g0";
    				int tipoProducto2 = 12;
    				String nombreProducto2 = "Leche Alpina";
    				String marcaProducto2 = "Alpina";
    				String presentacionProducto2 = "En bolsa";
    				String unidadDeMedida2 = "0.5kg";
    				double cantidadPresentacion2 = 1.5;
    				double pesoDeEmpaque2 = 0.5;
    				double volumenEmpaque2 = 2.5;

    				VOProducto producto2 = superAndes.adicionarProducto(codigoBarras2, tipoProducto2, nombreProducto2, marcaProducto2, presentacionProducto2, presentacionProducto2, cantidadPresentacion2, pesoDeEmpaque2, volumenEmpaque2);
    				lista = superAndes.darVOProducto();
    				assertEquals ("Debe haber dos productos creados !!", 2, lista.size ());
    				assertTrue ("El primer producto adicionado debe estar en la tabla", producto2.equals (lista.get (0)) || producto2.equals (lista.get (1)));
    				assertTrue ("El segundo producto adicionado debe estar en la tabla", producto2.equals (lista.get (0)) || producto2.equals (lista.get (1)));
    			}
    			catch (Exception e)
    			{
    				String msg = "Error en la ejecuci�n de las pruebas de operaciones sobre la tabla producto.\n";
    				msg += "Revise el log de superAndes y el de datanucleus para conocer el detalle de la excepci�n";
    				System.out.println (msg);

    	    		fail ("Error en las pruebas sobre la tabla SuperAndes");
    			}
    	    	finally
    			{
    				superAndes.limpiarSuperAndes();
    	    		superAndes.cerrarUnidadPersistencia ();    		
    			}
    }
    
    /**
     * M�todo de prueba de la restricci�n de unicidad sobre el nombre de Producto
     */
	@Test
	public void unicidadProductoTest() 
	{
    	// Probar primero la conexi�n a la base de datos
		try
		{
			log.info ("Probando la restricci�n de UNICIDAD del id del producto");
			superAndes = new SuperAndes(openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de Producto incompleta. No se pudo conectar a la base de datos !!. La excepci�n generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de Producto incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de SuperAndes y el de datanucleus para conocer el detalle de la excepci�n";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los tipos de bebida con la tabla vac�a
			List <VOProducto> lista = superAndes.darVOProducto();
			assertEquals ("No debe haber productos creados!!", 0, lista.size ());

			// Lectura de los productos con un producto adicionado
			String codigoBarras = "f1f0f1f0f1f0";
			int tipoProducto = 11;
			String nombreProducto = "Queso Alpina";
			String marcaProducto = "Alpina";
			String presentacionProducto = "En caja";
			String unidadDeMedida = "0.2kg";
			double cantidadPresentacion = 1.2;
			double pesoDeEmpaque = 0.2;
			double volumenEmpaque = 2.3;
			
			VOProducto producto1 = superAndes.adicionarProducto(codigoBarras, tipoProducto, nombreProducto, marcaProducto, presentacionProducto, unidadDeMedida, cantidadPresentacion, pesoDeEmpaque, volumenEmpaque);
			lista = superAndes.darVOProducto();
			assertEquals ("Debe haber un producto creado !!", 1, lista.size ());
			
			VOProducto producto2 = superAndes.adicionarProducto(codigoBarras, tipoProducto, nombreProducto, marcaProducto, presentacionProducto, unidadDeMedida, cantidadPresentacion, pesoDeEmpaque, volumenEmpaque);
			assertNull ("No puede adicionar dos productos con el mismo nombre !!", producto2);
		}
		catch (Exception e) 
		{
			String msg = "Error en la ejecuci�n de las pruebas de UNICIDAD sobre la tabla Producto.\n";
			msg += "Revise el log de SuperAndes y el de datanucleus para conocer el detalle de la excepci�n";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla Producto");
		}
		finally
		{
			superAndes.limpiarSuperAndes();
    		superAndes.cerrarUnidadPersistencia (); 
			
		}
	}  
	
	/* ****************************************************************
	 * 			M�todos de configuraci�n
	 *****************************************************************/
    /**
     * Lee datos de configuraci�n para la aplicaci�n, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuraci�n deseada
     * @param archConfig - Archivo Json que contiene la configuraci�n
     * @return Un objeto JSON con la configuraci�n del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontr� un archivo de configuraci�n de tablas v�lido");
		} 
		catch (Exception e)
		{
			log.info ("NO se encontr� un archivo de configuraci�n v�lido");			
			JOptionPane.showMessageDialog(null, "No se encontr� un archivo de configuraci�n de tablas v�lido: ", "ProductoTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }	
}
