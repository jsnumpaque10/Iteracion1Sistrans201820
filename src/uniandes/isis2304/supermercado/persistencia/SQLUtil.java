package uniandes.isis2304.supermercado.persistencia;
/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: SuperAndes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos de SuperAndes
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 * @author Germán Bravo
 */
class SQLUtil
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaSuperAndes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaSuperAndes psa;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLUtil (PersistenciaSuperAndes psa)
	{
		this.psa = psa;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public long nextval (PersistenceManager pm)
	{
        Query sql = pm.newQuery(SQL, "SELECT "+psa.darSeqSuperAndes()+" .nextval FROM DUAL");
        sql.setResultClass(Long.class);
        long resp = (long) sql.executeUnique();
        return resp;
	}
	
	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 17 n�meros que indican el n�mero de tuplas borradas en las tablas 
	 */
	public long [] limpiarSuperAndes(PersistenceManager pm)
	{
        Query sqlAlbergan = pm.newQuery(SQL, "DELETE FROM " + psa.darTablaAlbergan());          
        Query sqlAlmacenan = pm.newQuery(SQL, "DELETE FROM " + psa.darTablaAlmacenan());
        Query sqlBodega = pm.newQuery(SQL, "DELETE FROM " + psa.darTablaBodega());
        Query sqlCliente = pm.newQuery(SQL, "DELETE FROM " + psa.darTablaClientes());
        Query sqlEstante = pm.newQuery(SQL, "DELETE FROM " + psa.darTablaEstante());
        Query sqlFactura = pm.newQuery(SQL, "DELETE FROM " + psa.darTablaFactura());
        Query sqlInventario = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaInventario());
        Query sqlPedido = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaPedido());
        Query sqlProducto = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaProducto());
        Query sqlPromocion = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaPromocion());
        Query sqlPromociones = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaPromociones());
        Query sqlProveedor = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaProveedor());
        Query sqlProveen = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaProveen());
        Query sqlSucursal = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaSucursal());
        Query sqlTipoProducto = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaTipoProducto());
        Query sqlCarrtioCompras = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaCarritoCompras());
        Query sqlSeleccionProductos = pm.newQuery(SQL, "DELETE FROM" +psa.darTablaSeleccionProductos());
        
        long alberganEliminados = (long) sqlAlbergan.executeUnique();
        long almacenanEliminados = (long) sqlAlmacenan.executeUnique();
        long bodegaEliminados = (long) sqlBodega.executeUnique();
        long clienteEliminados = (long) sqlCliente.executeUnique();
        long estanteEliminados = (long) sqlEstante.executeUnique();
        long facturaEliminados = (long) sqlFactura.executeUnique();
        long inventarioEliminados = (long) sqlInventario.executeUnique();
        long pedidoEliminados = (long) sqlPedido.executeUnique();
        long productoEliminados = (long) sqlProducto.executeUnique();
        long promocionEliminados = (long) sqlPromocion.executeUnique();
        long promocionesEliminados = (long) sqlPromociones.executeUnique();
        long proveedorEliminados = (long) sqlProveedor.executeUnique();
        long proveenEliminados = (long) sqlProveen.executeUnique();
        long sucursalEliminados = (long) sqlSucursal.executeUnique();
        long tipoProductoEliminados = (long) sqlTipoProducto.executeUnique();
        long carritoComprasEliminados = (long) sqlCarrtioCompras.executeUnique();
        long seleccionProductosEliminados = (long) sqlSeleccionProductos.executeUnique();
        return new long[] {alberganEliminados, almacenanEliminados,bodegaEliminados, clienteEliminados, estanteEliminados,facturaEliminados, 
        		            inventarioEliminados, pedidoEliminados, productoEliminados, promocionEliminados, promocionesEliminados,
        		            proveedorEliminados, proveenEliminados, sucursalEliminados, tipoProductoEliminados, 
        		            carritoComprasEliminados, seleccionProductosEliminados};
	}
}
