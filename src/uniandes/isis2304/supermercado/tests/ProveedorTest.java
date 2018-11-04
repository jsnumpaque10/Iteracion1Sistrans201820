package uniandes.isis2304.supermercado.tests;

import static org.junit.Assert.assertEquals;
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
import uniandes.isis2304.supermercados.negocio.VOProveedor;

public class ProveedorTest 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(ProveedorTest.class.getName());
	
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
	 * 			Métodos de prueba para la tabla Proveedor - Creación y borrado
	 *****************************************************************/
	/**
	 * Método que prueba las operaciones sobre la tabla Producto
	 * 1. Adicionar un producto
	 * 2. Listar el contenido de la tabla con 0, 1 y 2 registros insertados
     */
    @Test
    public void CRDProveedorTest()
    {
    	// Probar primero la conexión a la base de datos
    			try
    			{
    				log.info ("Probando las operaciones CRD sobre Producro");
    				superAndes = new SuperAndes (openConfig (CONFIG_TABLAS_A));
    			}
    			catch(Exception e)
    			{
    				log.info("Prueba de CRD de Proveedor incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
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
    				List <VOProveedor> lista = superAndes.darVOProveedor();
    				assertEquals ("No debe haber proveedores creados!!", 0, lista.size ());

    				// Lectura de los productos con un producto adicionado
    				int nit = 1;
    				String nombreProvee = "Bimbo";
    				double calificacion = 4.5;
    				
    				VOProveedor proveedor1 = superAndes.adicionarProveedor(nit, nombreProvee, calificacion);
    				lista = superAndes.darVOProveedor();
    				assertEquals ("Debe haber un proveedor creado !!", 1, lista.size ());
    				assertEquals ("El objeto creado y el traido de la BD deben ser iguales !!", proveedor1, lista.get (0));
    				
    				// Lectura de los productos con un producto adicionado
    				int nit2 = 2;
    				String nombreProvee2 = "Bimbo";
    				double calificacion2 = 4.5;

    				VOProveedor proveedor2 = superAndes.adicionarProveedor(nit2, nombreProvee2, calificacion2);
    				lista = superAndes.darVOProveedor();
    				assertEquals ("Debe haber dos productos creados !!", 2, lista.size ());
    				assertTrue ("El primer proveedor adicionado debe estar en la tabla", proveedor2.equals (lista.get (0)) || proveedor2.equals (lista.get (1)));
    				assertTrue ("El segundo proveedor adicionado debe estar en la tabla", proveedor2.equals (lista.get (0)) || proveedor2.equals (lista.get (1)));
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
     * Método de prueba de la restricción de unicidad sobre el nombre de Proveedor
     */
	@Test
	public void unicidadProveedorTest() 
	{
    	// Probar primero la conexión a la base de datos
		try
		{
			log.info ("Probando la restricción de UNICIDAD del id del proveedor");
			superAndes = new SuperAndes(openConfig (CONFIG_TABLAS_A));
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			log.info ("Prueba de UNICIDAD de Proveedor incompleta. No se pudo conectar a la base de datos !!. La excepción generada es: " + e.getClass ().getName ());
			log.info ("La causa es: " + e.getCause ().toString ());

			String msg = "Prueba de UNICIDAD de Proveedor incompleta. No se pudo conectar a la base de datos !!.\n";
			msg += "Revise el log de SuperAndes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);
			fail (msg);
		}
		
		// Ahora si se pueden probar las operaciones
		try
		{
			// Lectura de los tipos de bebida con la tabla vacía
			List <VOProveedor> lista = superAndes.darVOProveedor();
			assertEquals ("No debe haber proveedores creados!!", 0, lista.size ());

			// Lectura de los productos con un producto adicionado
			int nit = 1;
			String nombreProvee = "Bimbo";
			double calificacion = 4.5;
			
			VOProveedor proveedor1 = superAndes.adicionarProveedor(nit, nombreProvee, calificacion);
			lista = superAndes.darVOProveedor();
			assertEquals ("Debe haber un proveedor creado !!", 1, lista.size ());
			
			VOProveedor proveedor2 = superAndes.adicionarProveedor(nit, nombreProvee, calificacion);
			assertNull ("No puede adicionar dos proveedores con el mismo nombre !!", proveedor2);
		}
		catch (Exception e) 
		{
			String msg = "Error en la ejecución de las pruebas de UNICIDAD sobre la tabla Proveedor.\n";
			msg += "Revise el log de SuperAndes y el de datanucleus para conocer el detalle de la excepción";
			System.out.println (msg);

    		fail ("Error en las pruebas de UNICIDAD sobre la tabla Proveedor");
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
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de tablas válido: ", "ProveedorTest", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }	
	
}
