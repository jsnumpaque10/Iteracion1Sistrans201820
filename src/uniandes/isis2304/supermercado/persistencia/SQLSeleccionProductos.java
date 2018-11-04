package uniandes.isis2304.supermercado.persistencia;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.supermercados.negocio.Producto;
import uniandes.isis2304.supermercados.negocio.SeleccionProductos;

public class SQLSeleccionProductos 
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
	 * @param psa - El Manejador de persistencia de la aplicación
	 */
	public SQLSeleccionProductos(PersistenciaSuperAndes psa)
	{
		this.psa = psa;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una SELECCIONPRODUCTOS a la base de datos de SuperAndes
	 * @param id - Identificador de la seleccion
	 * @param idVisitaMercado - Identificador de la visita
	 * @return EL número de tuplas insertadas
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
	 * @return EL número de tuplas eliminadas
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
	 * @return EL número de tuplas actualizadas
	 */
	public long devolverCantidadProductos(PersistenceManager pm, int idProducto, int idCarritoCompras, int cantidad)
	{
		Query sql = pm.newQuery(SQL, "UPDATE "+psa.darTablaSeleccionProductos()+" SET cantidad = "+cantidad);
		sql.setParameters(idProducto, idCarritoCompras, cantidad);
		return (long) sql.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de los SELECCIONPRODUCTOS de la 
	 * base de datos de SuperAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos SELECCIONPRODUCTOS
	 */
	public List<SeleccionProductos> darSeleccionProductos(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + psa.darTablaSeleccionProductos());;
		q.setResultClass(SeleccionProductos.class);
		return (List<SeleccionProductos>) q.executeList();
	}
}
