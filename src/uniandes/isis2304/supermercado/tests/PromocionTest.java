package uniandes.isis2304.supermercado.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.supermercados.negocio.SuperAndes;
import uniandes.isis2304.supermercados.negocio.VOPromocion;

public class PromocionTest 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PromocionTest.class.getName());
	
	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos: La unidad de persistencia existe y el esquema de la BD también
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
	 * 			Métodos de prueba para la tabla Producto - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba las operaciones sobre la tabla Promocion
	 * 1. Adicionar una Promocion
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
	 * 3 Eliminar una promocion por cantidad y por fecha de expedición
     */
    @Test
    public void CRDPromocionTest()
    {
    	// Probar primero la conexión a la base de datos
    			try
    			{
    				log.info ("Probando las operaciones CRD sobre promocion");
    				superAndes = new SuperAndes (openConfig (CONFIG_TABLAS_A));
    			}
    			catch(Exception e)
    			{
    				log.info("Prueba de CRD de Promocion incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
    				log.info ("La causa es: " + e.getCause ().toString ());
    				
    				String msg = "Prueba de CRD de Producto incompleta. No se pudo conectar a la base de datos !!.\n";
    				msg += "Revise el log de superAndes y el de datanucleus para conocer el detalle de la excepción";
    				System.out.println (msg);
    				fail (msg);
    			}
    			// Ahora si se pueden probar las operaciones
    	    	try
    			{
    				// Lectura de los productos con la tabla vacía
    				List <VOPromocion> lista = superAndes.darVOPromocion();
    				assertEquals ("No debe haber promociones creadas!!", 0, lista.size ());

    				// Lectura de los productos con un producto adicionado
    				int idPromo = 1;
    				int idSucu = 2;
    				Timestamp fechaStart = null;
    				Timestamp fechaFinal = null;
    				int cantidadPromo = 5;
    				double precioFinalPromo = 2.2;
    				
    				VOPromocion promocion1 = superAndes.adicionarPromocion(idPromo, idSucu, fechaStart, fechaFinal, cantidadPromo, precioFinalPromo);
    				lista = superAndes.darVOPromocion();
    				assertEquals ("Debe haber una Promocion creada !!", 1, lista.size ());
    				assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", promocion1, lista.get (0));
    				
    				// Lectura de los productos con un producto adicionado
    				int idPromo2 = 2;
    				int idSucu2 = 3;
    				Timestamp fechaStart2 = null;
    				Timestamp fechaFinal2 = null;
    				int cantidadPromo2 = 6;
    				double precioFinalPromo2 = 2.5;

    				VOPromocion promocion2 = superAndes.adicionarPromocion(idPromo2, idSucu2, fechaStart2, fechaFinal2, cantidadPromo2, precioFinalPromo2);
    				lista = superAndes.darVOPromocion();
    				assertEquals ("Debe haber dos promociones creadas !!", 2, lista.size ());
    				assertTrue ("La primera Promocion adicionada debe estar en la tabla", promocion2.equals (lista.get (0)) || promocion2.equals (lista.get (1)));
    				assertTrue ("La segunda Promocion adicionada debe estar en la tabla", promocion2.equals (lista.get (0)) || promocion2.equals (lista.get (1)));
    			
    				long promoEliminada = superAndes.finalizarPromocionPorCantidad(promocion1.getIdSucursal());
    				assertEquals("Debe aberse eliminado una promocion!!", 1, promoEliminada);
    				lista = superAndes.darVOPromocion();
    				assertEquals("Debe haber una sola promocion!!", 1, lista.size());
    				assertFalse("La primera promocion no debe estar en la tabla", promocion1.equals(lista.get(0)));
    				assertTrue("La segunda promocion debe estar en la tabla", promocion2.equals(lista.get(0)));
    			}
    			catch (Exception e)
    			{
    				String msg = "Error en la ejecución de las pruebas de operaciones sobre la tabla producto.\n";
    				msg += "Revise el log de superAndes y el de datanucleus para conocer el detalle de la excepción";
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
     * Método de prueba de la restricción de unicidad sobre el nombre de Producto
     */
	@Test
	public void unicidadPromocionTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del identificador de la promocion");
			superAndes = new SuperAndes(openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de Promocion incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de Promocion incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de SuperAndes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOPromocion> lista = superAndes.darVOPromocion();
			assertEquals ("No debe haber promociones creadas!!", 0, lista.size ());

			// Lectura de los productos con un producto adicionado
			int idPromo = 1;
			int idSucu = 2;
			Timestamp fechaStart = null;
			Timestamp fechaFinal = null;
			int cantidadPromo = 5;
			double precioFinalPromo = 2.2;
			
			VOPromocion promocion1 = superAndes.adicionarPromocion(idPromo, idSucu, fechaStart, fechaFinal, cantidadPromo, precioFinalPromo);
			lista = superAndes.darVOPromocion();
			assertEquals ("Debe haber un producto creado !!", 1, lista.size ());
			
			VOPromocion promocion2 = superAndes.adicionarPromocion(idPromo, idSucu, fechaStart, fechaFinal, cantidadPromo, precioFinalPromo);
			assertNull ("No puede adicionar dos productos con el mismo nombre !!", promocion2);
		}
		catch (Exception e) 
		{
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla Promocion.\n";
			msg += "Revise el log de SuperAndes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla Promocion");
		}
		finally
		{
			superAndes.limpiarSuperAndes();
    		superAndes.cerrarUnidadPersistencia (); 
			
		}
	}  
	
	/* ****************************************************************
	 * 			Métodos de configuración
	 *****************************************************************/
    /**
     * Lee datos de configuración para la aplicación, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuración deseada
     * @param archConfig - Archivo Json que contiene la configuración
     * @return Un objeto JSON con la configuración del tipo especificado
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
			log.info ("Se encontró un archivo de configuración de tablas válido");
		} 
		catch (Exception e)
		{
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "PromocionTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }	
}
