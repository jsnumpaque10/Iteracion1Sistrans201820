package uniandes.isis2304.supermercado.persistencia;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import oracle.net.aso.v;
import uniandes.isis2304.supermercados.negocio.*;
public class PersistenciaSuperAndes 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecuciï¿½n
	 */
	private static Logger log = Logger.getLogger(PersistenciaSuperAndes.class.getName());
	
	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el ï¿½nico objeto de la clase - Patrï¿½n SINGLETON
	 */
	private static PersistenciaSuperAndes instance;
	
	/**
	 * Fï¿½brica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;
	
	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, tipoProducto, producto, Sucural, bebedor, gustan, sirven y visitan
	 */
	private List <String> tablas;
	
	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaParranderos
	 */
	private SQLUtil sqlUtil;
	
	/**
	 * Atributo para el acceso a la tabla ALBERGAN de la base de datos
	 */
	private SQLAlbergan sqlAlbergan;

	/**
	 * Atributo para el acceso a la tabla ALMACENAN de la base de datos
	 */
	private SQLAlmacenan sqlAlmacenan;
	
	/**
	 * Atributo para el acceso a la tabla BODEGA de la base de datos
	 */
	private SQLBodega sqlBodega;
	
	/**
	 * Atributo para el acceso a la tabla CLIENTE de la base de datos
	 */
	private SQLCliente sqlCliente;
	
	/**
	 * Atributo para el acceso a la tabla ESTANTE de la base de datos
	 */
	private SQLEstante sqlEstante;
	
	/**
	 * Atributo para el acceso a la tabla FACTURA de la base de datos
	 */
	private SQLFactura sqlFactura;
	
	/**
	 * Atributo para el acceso a la tabla INVENTARIO de la base de datos
	 */
	private SQLInventario sqlInventario;
	
	/**
	 * Atributo para el acceso a la tabla PEDIDO de la base de datos
	 */
	private SQLPedido sqlPedido;
	
	/**
	 * Atributo para el acceso a la tabla PRODUCTO de la base de datos
	 */
	private SQLProducto sqlProducto;
	
	/**
	 * Atributo para el acceso a la tabla PROMOCION de la base de datos
	 */
	private SQLPromocion sqlPromocion;
	
	/**
	 * Atributo para el acceso a la tabla PROMOCIONES de la base de datos
	 */
	private SQLPromociones sqlPromociones;
	
	/**
	 * Atributo para el acceso a la tabla PROVEEDOR de la base de datos
	 */
	private SQLProveedor sqlProveedor;
	
	/**
	 * Atributo para el acceso a la tabla PROVEEN de la base de datos
	 */
	private SQLProveen sqlProveen;
	
	/**
	 * Atributo para el acceso a la tabla SUCURSAL de la base de datos
	 */
	private SQLSucursal sqlSucursal;
	
	/**
	 * Atributo para el acceso a la tabla TIPOPRODUCTO de la base de datos
	 */
	private SQLTipoProducto sqlTipoProducto;
	
	/**
	 * Atributo para el acceso a la tabla CARRITOCOMPRAS de la base de datos
	 */
	private SQLCarritoCompras sqlCarritoCompras;
	
	/**
	 * Atributo para el acceso a la tabla SELECCIONPRODUCTOS de la base de datos
	 */
	private SQLSeleccionProductos sqlSeleccionProductos;
	
	/* ****************************************************************
	 * 			Mï¿½todos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrï¿½n SINGLETON
	 */
	private PersistenciaSuperAndes()
	{
		pmf = JDOHelper.getPersistenceManagerFactory("SuperAndes");		
		crearClasesSQL ();
		
		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add("SuperAndes_sequence");
		tablas.add("ALBERGAN");
		tablas.add("ALMACENAN");
		tablas.add("BODEGA");
		tablas.add("CLIENTE");
		tablas.add("ESTANTE");
		tablas.add("FACTURA");
		tablas.add("INVENTARIO");
		tablas.add("PEDIDO");
		tablas.add("PRODUCTO");
		tablas.add("PROMOCION");
		tablas.add("PROMOCIONES");
		tablas.add("PROVEEDOR");
		tablas.add("PROVEEN");
		tablas.add("SUCURSAL");
		tablas.add("TIPOPRODUCTO");
		tablas.add("CARRITOCOMPRAS");
		tablas.add("SELECCIONPRODUCTOS");
	}
	
	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json - Patrï¿½n SINGLETON
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de la unidad de persistencia a manejar
	 */
	private PersistenciaSuperAndes(JsonObject tableConfig)
	{
		crearClasesSQL ();
		tablas = leerNombresTablas(tableConfig);
		
		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}
	
	/**
	 * @return Retorna el ï¿½nico objeto PersistenciaSuperAndes existente - Patrï¿½n SINGLETON
	 */
	public static PersistenciaSuperAndes getInstance ()
	{
		if (instance == null)
		{
			instance = new PersistenciaSuperAndes();
		}
		return instance;
	}
	
	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el ï¿½nico objeto PersistenciaSuperAndes existente - Patrï¿½n SINGLETON
	 */
	public static PersistenciaSuperAndes getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaSuperAndes(tableConfig);
		}
		return instance;
	}
	
	/**
	 * Cierra la conexiï¿½n con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}
	
	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList <String> ();
		for (JsonElement nom : nombres)
		{
			resp.add (nom.getAsString ());
		}
		
		return resp;
	}

	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL ()
	{
		sqlAlbergan = new SQLAlbergan(this);
		sqlAlmacenan = new SQLAlmacenan(this);
		sqlBodega = new SQLBodega(this);
		sqlCliente = new SQLCliente(this);
		sqlEstante = new SQLEstante(this);
		sqlFactura = new SQLFactura(this);	
		sqlInventario = new SQLInventario(this);
		sqlPedido = new SQLPedido(this);
		sqlProducto = new SQLProducto(this);
		sqlPromocion = new SQLPromocion(this);
		sqlPromociones = new SQLPromociones(this);
		sqlProveedor = new SQLProveedor(this);
		sqlProveen = new SQLProveen(this);
		sqlSucursal = new SQLSucursal(this);
		sqlTipoProducto = new SQLTipoProducto(this);
		sqlCarritoCompras = new SQLCarritoCompras(this);
		sqlSeleccionProductos = new SQLSeleccionProductos(this);
		sqlUtil = new SQLUtil(this);
	}

	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de SuperAndes
	 */
	public String darSeqSuperAndes()
	{
		return tablas.get(0);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Albergan de SuperAndes
	 */
	public String darTablaAlbergan()
	{
		return tablas.get(1);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Almacenan de SuperAndes
	 */
	public String darTablaAlmacenan()
	{
		return tablas.get(2);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Bodega de SuperAndes
	 */
	public String darTablaBodega()
	{
		return tablas.get(4);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Cliente de SuperAndes
	 */
	public String darTablaClientes()
	{
		return tablas.get(5);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Estante de SuperAndes
	 */
	public String darTablaEstante()
	{
		return tablas.get(6);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Factura de SuperAndes
	 */
	public String darTablaFactura()
	{
		return tablas.get(7);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Inventario de SuperAndes
	 */
	public String darTablaInventario()
	{
		return tablas.get(8);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Pedido de SuperAndes
	 */
	public String darTablaPedido()
	{
		return tablas.get(9);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Producto de SuperAndes
	 */
	public String darTablaProducto()
	{
		return tablas.get(10);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Promocion de SuperAndes
	 */
	public String darTablaPromocion()
	{
		return tablas.get(11);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Promociones de SuperAndes
	 */
	public String darTablaPromociones()
	{
		return tablas.get(12);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Proveedor de SuperAndes
	 */
	public String darTablaProveedor()
	{
		return tablas.get(13);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Proveen de SuperAndes
	 */
	public String darTablaProveen()
	{
		return tablas.get(14);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Sucursal de SuperAndes
	 */
	public String darTablaSucursal()
	{
		return tablas.get(15);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de TipoProducto de SuperAndes
	 */
	public String darTablaTipoProducto()
	{
		return tablas.get(16);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de CarritoCompras de SuperAndes
	 */
	public String darTablaCarritoCompras()
	{
		return tablas.get(17);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de SeleccionProductos de SuperAndes
	 */
	public String darTablaSeleccionProductos()
	{
		return tablas.get(18);
	}
	
	/**
	 * Transacciï¿½n para el generador de secuencia de SuperAndes
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @return El siguiente nï¿½mero del secuenciador de SuperAndes
	 */
	private long nextval ()
	{
        long resp = sqlUtil.nextval (pmf.getPersistenceManager());
        log.trace ("Generando secuencia: "+ resp);
        return resp;
    }

	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle especï¿½fico del problema encontrado
	 * @param e - La excepciï¿½n que ocurrio
	 * @return El mensaje de la excepciï¿½n JDO
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}
	
	/**
	 * Método que consulta todas las tuplas en la tabla Productos
	 * @return La lista de objetos PRODUCTO, construidos con base en las tuplas de la tabla PRODUCTO
	 */
	public List<Producto> darProductos()
	{
		return sqlProducto.darProductos(pmf.getPersistenceManager());
	}
	
	/**
	 * Método que consulta todas las tuplas en la tabla Promocion
	 * @return La lista de objetos PROMOCION, construidos con base en las tuplas de la tabla PROMOCION
	 */
	public List<Promocion> darPromociones()
	{
		return sqlPromocion.darPromociones(pmf.getPersistenceManager());
	}
	
	/**
	 * Método que consulta todas las tuplas en la tabla Proveedor
	 * @return La lista de objetos PROMOCION, construidos con base en las tuplas de la tabla PROVEEDOR
	 */
	public List<Proveedor> darProveedor()
	{
		return sqlProveedor.darProveedores(pmf.getPersistenceManager());
	}
	
	/**
	 * Método que consulta todas las tuplas en la tabla SeleccionProductos
	 * @return La lista de objetos SELECCIONPRODUCTOS, construidos con base en las tuplas de la tabla SELECCIONPRODUCTOS
	 */
	public List<SeleccionProductos> darSeleccionProductos()
	{
		return sqlSeleccionProductos.darSeleccionProductos(pmf.getPersistenceManager());
	}
	
	/**
	 * Mï¿½todo que inserta, de manera transaccional, una tupla en la tabla Proveedores
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param idProveedor - El NIT del proveedor
	 * @param nombre - El nombre del proveedor
	 * @param calificacionCalidad - La calificaciï¿½n de calidad que recibe el proveedor
	 * @return El objeto Proveedor adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public Proveedor adicionarProveedor(int idProveedor, String nombre, double calificacionCalidad)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlProveedor.adicionarProveedor(pm, idProveedor, nombre, calificacionCalidad);
            tx.commit();
            
            log.trace ("Inserción del proveedor: "+idProveedor+", "+nombre+", "+calificacionCalidad+": " + tuplasInsertadas + " tuplas insertadas");
            
            return new Proveedor (idProveedor, nombre, calificacionCalidad);
        }
        catch (Exception e)
        {
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}

	/**
	 * Mï¿½todo que inserta, de manera transaccional, una tupla en la tabla TipoProducto
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param nombre - El nombre del tipo del producto
	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public TipoProducto adicionarTipoProducto(String nombre, String categoria)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction(); 
        try
        {
            tx.begin();
            int idTipoProducto = (int) nextval();
            long tuplasInsertadas = sqlTipoProducto.adicionarTipoProducto(pm, idTipoProducto, nombre, categoria);
            tx.commit();
            
            log.trace ("Inserción de tipo de producto: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");
            
            return new TipoProducto(idTipoProducto, nombre, categoria);
        }
        catch (Exception e)
        {
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<TipoProducto> darTiposProductos(String nombre)
	{
		return sqlTipoProducto.darTiposProducto(pmf.getPersistenceManager());
	}
	
	/**
	 * Mï¿½todo que inserta, de manera transaccional, una tupla en la tabla Clientes
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param nombre - El nombre del cliente
	 * @return El objeto Cliente adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public Cliente adicionarCliente(int idCliente, String nombre, String correo, String ciudad, String direccion)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlCliente.adicionarCliente(pm, idCliente, nombre, correo, ciudad, direccion);
            tx.commit();

            log.trace ("Inserción del cliente: "+idCliente+", "+nombre+", "+correo+", "+ciudad+", "+direccion+": " + tuplasInsertadas + " tuplas insertadas");    
            return new Cliente (idCliente, nombre, correo, ciudad, direccion);
        }
        catch (Exception e)
        {
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/**
	 * Mï¿½todo que inserta, de manera transaccional, una tupla en la tabla Productos
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param codigoDeBarras - El codigo de barras del producto
	 * @param idTipoProducto - El tipo de producto
	 * @param nombre - El nombre del producto
	 * @param marca - La marca del producto
	 * @param presentacion - La presentaciï¿½n del producto
	 * @param unidadDeMedida - La unidad en la cual estï¿½ medido el producto
	 * @param cantidadPresentacion - La cantidad de producto que hay en la presentaciï¿½n
	 * @param pesoEmpaque - El peso del empaque 
	 * @param volumenEmpaque - El volumen del empaque
	 * @return El objeto Producto adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public Producto adicionarProducto(String codigoDeBarras, int idTipoProducto, String nombre, String marca, String presentacion, String unidadDeMedida, double cantidadPresentacion, double pesoEmpaque, double volumenEmpaque)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try
        {
            tx.begin();
            long tuplasInsertadas = sqlProducto.adicionarProducto(pm, codigoDeBarras, idTipoProducto, nombre, marca, presentacion, unidadDeMedida, cantidadPresentacion, pesoEmpaque, volumenEmpaque);
            tx.commit();

            log.trace ("Inserción del proveedor: "+codigoDeBarras+", "+idTipoProducto+", "+nombre+", "+marca+", "+presentacion+", "+unidadDeMedida+", "+cantidadPresentacion+", "+pesoEmpaque+", "+volumenEmpaque+": " + tuplasInsertadas + " tuplas insertadas");    
            return new Producto(codigoDeBarras, idTipoProducto, nombre, marca, presentacion, cantidadPresentacion, unidadDeMedida, pesoEmpaque, volumenEmpaque);
        }
        catch (Exception e)
        {
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/**
	 * Mï¿½todo que inserta, de manera transaccional, una tupla en la tabla Sucursal
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param ciudad - La ciudad de la sucursal
	 * @param sector - El sector de la sucursal
	 * @param direccion - La direcciï¿½n de la sucursal
	 * @return El objeto Sucursal adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public Sucursal adicionarSucursal(String ciudad, String sector, String direccion)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            int idSucursal = (int) nextval();
            long tuplasInsertadas = sqlSucursal.adicionarSucursal(pm, idSucursal, ciudad, direccion, sector);
            tx.commit();

            log.trace ("Inserción de Sucursal: "+idSucursal+", "+ciudad+", "+sector+", "+direccion+": " + tuplasInsertadas + " tuplas insertadas");

            return new Sucursal(idSucursal, ciudad, sector, direccion);
        }
        catch (Exception e)
        {
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/**
	 * Mï¿½todo que inserta, de manera transaccional, una tupla en la tabla Sucursal
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param idTipoProducto - El tipo de producto que almaenarï¿½
	 * @param idSucursal - La sucursal en la cual estï¿½
	 * @param capacidadVolumen - La capacidad en volumen que podrï¿½ almacenar
	 * @param capacidadPeso - La capacidad en peso que podrï¿½ almacenar.
	 * @return El objeto Bodega adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public Bodega adicionarBodegaASucursal(int idTipoProducto, int idSucursal, double capacidadVolumen, double capacidadPeso)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            int idBodega = (int) nextval();
            long tuplasInsertadas = sqlBodega.adicionarBodega(pm, idBodega, idTipoProducto, idSucursal, capacidadVolumen, capacidadPeso);
            tx.commit();

            log.trace ("Inserción de Bodega: "+idBodega+", "+idTipoProducto+", "+idSucursal+", "+capacidadVolumen+", "+capacidadPeso+": " + tuplasInsertadas + " tuplas insertadas");

            return new Bodega(idBodega, idTipoProducto, idSucursal, capacidadVolumen, capacidadPeso);
        }
        catch (Exception e)
        {
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/**
	 * Mï¿½todo que inserta, de manera transaccional, una tupla en la tabla Sucursal
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param idTipoProducto - El tipo de producto que albergarï¿½
	 * @param idSucursal - La sucursal en la cual estarï¿½
	 * @param capacidadVolumen - Capacidad en volumen que podrï¿½ albergar
	 * @param capacidadPeso - Capacidad en peso que podrï¿½ albergar
	 * @param nivelAbastecimiento - Nivel de abastecimiento del estante
	 * @return El objeto Estante adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public Estante adicionarEstanteASucursal(int idTipoProducto, int idSucursal, double capacidadVolumen, double capacidadPeso, int nivelAbastecimiento)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            int idEstante = (int) nextval();
            long tuplasInsertadas = sqlEstante.adicionarEstante(pm, idEstante, idTipoProducto, idSucursal, capacidadVolumen, capacidadPeso, nivelAbastecimiento);
            tx.commit();

            log.trace ("Inserción de Estante: " +idEstante+", "+idTipoProducto+", "+idSucursal+", "+capacidadVolumen+", "+capacidadPeso+", "+nivelAbastecimiento+": " + tuplasInsertadas + " tuplas insertadas");

            return new Estante(idEstante, idTipoProducto, idSucursal, capacidadVolumen, capacidadPeso, nivelAbastecimiento);
        }
        catch (Exception e)
        {
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/**
	 * Mï¿½todo que inserta, de manera transaccional, una tupla en la tabla Promocion
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param idSucursal - El identificador de la sucursal
	 * @param fechaInicio - La fecha de inicio de la promocion
	 * @param fechaFinal - La fecha final de la promocion
	 * @param cantidadProductos - La cantidad del producto en promocion
	 * @param precioFinal - El precio final de la promocion
	 * @return El objeto Promocion adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public Promocion adicionarPromocion(int idSucursal, Timestamp fechaInicio, Timestamp fechaFinal, int cantidadProductos, double precioFinal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            int idPromocion = (int) nextval();
            long tuplasInsertadas = sqlPromocion.adicionarPromocion(pm, idPromocion, idSucursal, fechaInicio, fechaFinal, cantidadProductos, precioFinal);
            tx.commit();

            log.trace ("Inserción de Promocion: " +idPromocion+", "+idSucursal+", "+fechaInicio+", "+fechaFinal+", "+cantidadProductos+", "+precioFinal+": " + tuplasInsertadas + " tuplas insertadas");

            return new Promocion(idPromocion, idSucursal, fechaInicio, fechaFinal, cantidadProductos, precioFinal);
        }
        catch (Exception e)
        {
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/**
	 * Mï¿½todo que finaliza, de manera transaccional, una tupla en la tabla Promocion
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @return El objeto Promocion adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public long finalizarPromocionPorFecha(int idSucursal, Timestamp fechaFinal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
            long resp = sqlPromocion.finalizarPromocionPorFecha(pm, idSucursal, fechaFinal);
            tx.commit();

            return resp;
		}
		catch(Exception e)
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
		}
		finally 
		{
			 if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
		}
	}
	
	/**
	 * Mï¿½todo que finaliza, de manera transaccional, una tupla en la tabla Promocion
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @return El objeto Promocion adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public long finalizarPromocionPorCantidad(int idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
            long resp = sqlPromocion.finalizarPromocionPorCantidad(pm, idSucursal);
            tx.commit();

            return resp;
		}
		catch(Exception e)
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
		}
		finally 
		{
			 if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
		}
	}
	
	/**
	 * Mï¿½todo que inserta, de manera transaccional, una tupla en la tabla Pedido
	 * Adiciona entradas al log de la aplicación
	 * @param idSucursal - El identificador de la sucursal
	 * @param idProveedor - El identificador del proveedor
	 * @param fechaEsperadaEntrega - La fecha esperada de entrega del pedido
	 * @param cantidad - La cantidad de producto que se quiere pedir
	 * @param precioTotal - El precio total del pedido
	 * @param fechaEntrega - La fecha de entrega del pedido
	 * @param calidadProductos - La calidad de los productos pedidos
	 * @param estado - El estado actual del pedido 
	 * @return El objeto Pedido adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public Pedido adicionarPedidoAProveedor(int idSucursal, int idProveedor, int idProducto, Timestamp fechaEsperadaEntrega, int cantidad, double precioTotal, Timestamp fechaEntrega, int calidadProductos)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
            int idPedido = (int) nextval();
            String estadoInicial = "Pendiente"; 
            long tuplasInsertadas = sqlPedido.adicionarPedido(pm, idPedido, idSucursal, idProveedor, idProducto, fechaEsperadaEntrega, cantidad, precioTotal, fechaEntrega, calidadProductos, estadoInicial);
            tx.commit();

            log.trace ("Inserción de Pedido: " +idPedido+", "+idSucursal+", "+idProducto+", "+fechaEsperadaEntrega+", "+cantidad+", "+precioTotal+", "+fechaEntrega+", "+calidadProductos+", "+estadoInicial+": " + tuplasInsertadas + " tuplas insertadas");

            return new Pedido(idPedido, idSucursal, idProveedor, idProducto, fechaEsperadaEntrega, cantidad, precioTotal, fechaEntrega, calidadProductos, estadoInicial);
		}
		catch(Exception e)
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
		}
		finally 
		{
			 if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
		}
	}

	/**
	 * Mï¿½todo que elimina, de manera transaccional, una tupla en la tabla Pedido
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param idSucursal - El identificador de la sucursal
	 * @param idProveedor - El identificador del proveedor
	 * @param estado - El estado actual del pedido 
	 * @return El objeto Pedido adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public long llegadaPedidoAProveedor(int idPedido, int idSucursal, int idProveedor)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
            String estadoFinal = "Entregado"; 
            long resp = sqlPedido.llegadaPedido(pm, idPedido, idSucursal, idProveedor, estadoFinal);
            tx.commit();

            return resp;
		}
		catch(Exception e)
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
		}
		finally 
		{
			 if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
		}
	}
	
	/**
	 * RF12
	 * Se adiciona un carrito y es asignado a un cliente
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param pIdSucursal
	 * @param pIdCliente
	 * @param pDisponibilidad
	 * @param pAbandonado
	 * @param pFechaVisista
	 * @return El objeto CarritoCompras adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public CarritoCompras solicitarCarritoCompras(int pIdSucursal, int pIdCliente, Timestamp pFechaVisista)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			int idCarrito = (int) nextval();
			String pDisponibilidad = "SI";
			String pAbandonado = "NO";
			long resp = sqlCarritoCompras.solicitarCarritoCompras(pm, idCarrito, pIdSucursal, pIdCliente, pDisponibilidad, pAbandonado, pFechaVisista);
			tx.commit();
			return new CarritoCompras(idCarrito, pIdSucursal, pIdCliente, pDisponibilidad, pAbandonado);
		}
		catch (Exception e) 
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
		}
		finally 
		{
			 if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
		}
	}
	
	/**
	 * RF13
	 * Se adicionan productos al carrito de compras
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param pIdProducto
	 * @param pIdCarritoCompras
	 * @param pCantidad
	 * @return El objeto SeleccionProductos adicionado. null si ocurre alguna Excepciï¿½n
	 */
	public long adicionarProductosACarrito(int pIdProducto, int pIdCarritoCompras, int pCantidad, int pIdEstante)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlSeleccionProductos.adicionarSeleccionProductos(pm, pIdProducto, pIdCarritoCompras, pCantidad);
			long resp2 = sqlAlbergan.disminuirProductoEnEstante(pm, pIdEstante, pIdProducto, pCantidad);
			tx.commit();
			//Inserción y Eliminación
			return resp + resp2;
		}
		catch (Exception e)
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
		}
	}
	
	/**
	 * RF14.1
	 * Devuelve un producto del carrito de compras
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param pIdProducto
	 * @param pIdCarritoCompras
	 * @return El objeto SeleccionProductos eliminado. null si ocurre alguna Excepciï¿½n
	 */
	public long devolverProductoDelCarrito(int pIdProducto, int pIdCarritoCompras, int pIdEstante, int pCantidad)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlSeleccionProductos.devolverSeleccionProductos(pm, pIdProducto, pIdCarritoCompras);
			long resp2 = sqlAlbergan.adicionarAlbergan(pm, pIdProducto, pIdEstante, pCantidad);
			tx.commit();
			//Eliminación + Inserción
			return resp + resp2;
		}
		catch (Exception e) 
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
		}
	}
	
	/**
	 * RF14.2
	 * Devuelve una cantidad de un producto del carrito de compras
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param pIdProducto
	 * @param pIdCarrtioCompras
	 * @param pCantidad
	 * @return El objeto SeleccionProductos actualizado. null si ocurre alguna Excepciï¿½n
	 */
	public long devolverCantidadProductosDelCarrito(int pIdProducto, int pIdCarrtioCompras, int pIdEstante, int pCantidad)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlSeleccionProductos.devolverCantidadProductos(pm, pIdProducto, pIdCarrtioCompras, pCantidad);
			long resp2 = sqlAlbergan.aumentarProductoEnEstante(pm, pIdEstante, pIdProducto, pCantidad);
			tx.commit();
			//Eliminación + Inserción
			return resp + resp2;
		}
		catch (Exception e) 
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
		}
	}
	
	/**
	 * RF15
	 * Genera la factura corresponiente a la compra de un clinete
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param pIdSucursal
	 * @param pIdProducto
	 * @param pCantidad
	 * @param pIdFactura
	 * @param pIdCarritoCompras
	 * @param pTotal
	 * @param pFecha
	 * @return El objeto Factura insertado. null si ocurre alguna Excepciï¿½n
	 */
	public Factura pagarCompra(int pIdSucursal, int pIdCliente, int pIdProducto, int pCantidad, int pIdCarritoCompras, int pTotal, Timestamp pFecha)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			int pIdFactura = (int) nextval();
			long resp = sqlFactura.generarFactura(pm, pIdFactura, pIdCarritoCompras, pIdCliente, pTotal, pFecha);
			long resp2 = sqlInventario.disminuirInventario(pm, pIdSucursal, pIdProducto, pCantidad);
			long resp3 = sqlCarritoCompras.actualizarDisponibilidad(pm, pIdSucursal, pIdCarritoCompras);
			tx.commit();
			
			log.trace ("Inserción de Factura: " +pIdFactura+", "+pIdCarritoCompras+", "+pIdCliente+", "+pTotal+", "+pFecha+", tuplas insertadas: "+resp); 
			log.trace("Actualización de Inventario: "+pIdSucursal+", "+pIdProducto+", "+pCantidad+", tuplas actualizadas: "+resp2);
			log.trace("Actualización de Carrito: "+pIdSucursal+", "+pIdCarritoCompras+", tuplas insertadas: "+resp3);
			
			return new Factura(pIdFactura, pIdCarritoCompras, pIdCliente, pTotal, pFecha);
		}
		catch(Exception e)
		{
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;	
		}
		finally 
		{
			 if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
		}
	}
	
	/**
	 * RF16
	 * Metodo que actualiza el estado de abandonado del carrito de compras
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @param pId - Identificador del carrito
	 * @param pIdSucursal - Indentificador de la sucursal
	 * @param idCliente - Identificador del cliente
	 * @return El objeto CarritoCompras actualizado. null si ocurre alguna Excepciï¿½n
	 */
	public long abandonarCarritoCompras(int pIdSucursal, int pIdCarrito)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlCarritoCompras.actualizarAbandonoSI(pm, pIdSucursal, pIdCarrito);	
			long resp2 = sqlCarritoCompras.actualizarDisponibilidad(pm, pIdSucursal, pIdCarrito);
			tx.commit();
			return resp + resp2;
		}
		catch(Exception e)
		{
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
	}
	
	/**
	 * RF17
	 * Método que recolecta los productos de los carritos abandonados y los coloca en los estantes
	 * Adiciona entradas al log de la aplicaciï¿½n
	 * @return El objeto Albergan actualizado. null si ocurre alguna Excepciï¿½n
	 */
	public Albergan recolectarProductosAbandonados(int pIdSucursal, int pIdProducto, int pIdEstante, int pCantidad)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			for(CarritoCompras carrito : sqlCarritoCompras.darCarritosAbandonados(pm))
			{
				tx.begin();
				long resp = sqlSeleccionProductos.devolverSeleccionProductos(pm, pIdProducto, carrito.id);
				long resp2 = sqlAlbergan.adicionarAlbergan(pm, pIdEstante, pIdProducto, pCantidad);
				tx.commit();
				
				log.trace("Eliminacion de SeleccionProductos: "+pIdProducto+", "+carrito.id+", tuplas eliminadas: "+resp);
				log.trace ("Inserción de Albergan: "+pIdEstante+", "+pIdProducto+", "+pCantidad+", tuplas insertadas: "+resp2);
			}
			return new Albergan(pIdEstante, pIdProducto, pCantidad);
		}
		catch (Exception e) 
		{
			log.error("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally 
		{
			 if (tx.isActive())
	            {
	                tx.rollback();
	            }
	            pm.close();
		}
	}
	
	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de SuperAndes
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @return Un arreglo con 17 números que indican el número de tuplas borradas en las tablas ALBERGAN, ALMACENAN, BODEGA,
	 * CARRITOCOMPRAS, CLIENTE, ESTANTE, FACTURA, INVENTARIO, PEDIDO, PRODUCTO, PROMOCION, PROMOCIONES, PROVEEDOR, PROVEEN,
	 * SELECCIONPRODUCTOS, SUCURSAL, RIPOPRODUCTO respectivamente
	 */
	public long[] limpiarSuperAndes()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try
		{
			tx.begin();
			long[] resp = sqlUtil.limpiarSuperAndes(pm);
			tx.commit();
			log.info("Borrada la base de datos");
			return resp;
		}
		catch(Exception e)
		{
			log.error("Exception: "+e.getMessage()+"\n"+darDetalleException(e));
			return new long[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
		}
		finally
		{
			if(tx.isActive())
				tx.rollback();
			pm.close();
		}
	}
}
