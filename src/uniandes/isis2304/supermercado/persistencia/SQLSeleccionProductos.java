package uniandes.isis2304.supermercado.persistencia;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLSeleccionProductos 
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra ac� para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaSuperAndes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicaci�n
	 */
	private PersistenciaSuperAndes psa;

	/* ****************************************************************
	 * 			M�todos
	 *****************************************************************/
	/**
	 * Constructor
	 * @param psa - El Manejador de persistencia de la aplicaci�n
	 */
	public SQLSeleccionProductos(PersistenciaSuperAndes psa)
	{
		this.psa = psa;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una SELECCIONPRODUCTOS a la base de datos de SuperAndes
	 * @param id - Identificador de la seleccion
	 * @param idVisitaMercado - Identificador de la visita
	 * @return EL n�mero de tuplas insertadas
	 */	
	public long adicionarSeleccionProductos(PersistenceManager pm, int idProducto, int idCarritoCompras, int cantidad)
	{
		Query sql = pm.newQuery(SQL, "INSERT INTO "+psa.darTablaSeleccionProductos()+" (id_producto, id_carritocompras, cantidad) values (?, ?, ?)");
		sql.setParameters(idProducto, idCarritoCompras, cantidad);
		return (long) sql.executeUnique();
	}	

	/**
	 * Crea y ejecuta la sentencia SQL para eliminar una SELECCIONPRODUCTOS a la base de datos de SuperAndes
	 * @param idProducto - Identificador del producto
	 * @param idCarritoCompras - Identificador del carrito
	 * @return EL n�mero de tuplas eliminadas
	 */
	public long devolverSeleccionProductos(PersistenceManager pm, int idProducto, int idCarritoCompras)
	{
		Query sql = pm.newQuery(SQL, "DELETE FROM "+psa.darTablaSeleccionProductos()+" WHERE id_producto = "+idProducto+" AND id_carritocompras = "+idCarritoCompras);
		sql.setParameters(idProducto, idCarritoCompras);
		return (long) sql.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para actualizar una SELECCIONPRODUCTOS a la base de datos de SuperAndes
	 * @param idProducto - Identificador del producto
	 * @param idCarritoCompras - Identficador del carrtio
	 * @param cantidad - Cantidad del producto
	 * @return EL n�mero de tuplas actualizadas
	 */
	public long devolverCantidadProductos(PersistenceManager pm, int idProducto, int idCarritoCompras, int cantidad)
	{
		Query sql = pm.newQuery(SQL, "UPDATE "+psa.darTablaSeleccionProductos()+" SET cantidad = "+cantidad);
		sql.setParameters(idProducto, idCarritoCompras, cantidad);
		return (long) sql.executeUnique();
	}
}
