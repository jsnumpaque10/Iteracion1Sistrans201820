package uniandes.isis2304.supermercado.persistencia;

import java.sql.Timestamp;
import java.util.List;
import javax.jdo.PersistenceManager;

import uniandes.isis2304.supermercados.negocio.Albergan;
import uniandes.isis2304.supermercados.negocio.CarritoCompras;
import javax.jdo.Query;
public class SQLCarritoCompras 
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
	public SQLCarritoCompras(PersistenciaSuperAndes psa)
	{
		this.psa = psa;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un CARRITOCOMPRAS a la base de datos de SuperAndes
	 * @param id - Identificador del carrito
	 * @param idSucursal - Identificador de la sucursal
	 * @param Disponibilidad - Disponibilidad del carrito
	 * @param Abandonado - El carrito tiene cliente o no
	 * @return EL número de tuplas insertadas
	 */	
	public long solicitarCarritoCompras(PersistenceManager pm, int id, int idSucursal, int idCliente, String Disponibilidad, String Abandonado, Timestamp fechaVisista)
	{
		Query sql = pm.newQuery(SQL, "INSERT INTO "+psa.darTablaCarritoCompras()+ "(id, id_sucursal, id_cliente, disponibilidad, abandonado, fecha_visita) values (?, ? , ?, ?, ?, ?)");
		sql.setParameters(id, idSucursal, idCliente, Disponibilidad, Abandonado, fechaVisista);
		return (long) sql.executeUnique();
	}
	
	/**
	 * Actualiza y ejecuta la sentencia SQL para actualizar un CARRTITOCOMPRAS a la base de datos de SuperAndes
	 * @param idSucursal - Identificar de la sucursal
	 * @param id - Identificador del carrito
	 * @return El número de tuplas actualizadas
	 */
	public long actualizarDisponibilidad(PersistenceManager pm, int idSucursal, int id)
	{
		Query sql = pm.newQuery(SQL, "UPDATE "+psa.darTablaCarritoCompras()+" SET disponibilidad = SI WHERE id_sucursal = "+idSucursal+" AND id = "+id);
		sql.setParameters(idSucursal, id);
		return  (long) sql.executeUnique();
	}
	
	/**
	 * Actualiza y ejecuta la sentencia SQL para actualizar un CARRTITOCOMPRAS a la base de datos de SuperAndes
	 * @param idSucursal - Identificar de la sucursal
	 * @param id - Identificador del carrito
	 * @return El número de tuplas actualizadas
	 */
	public long actualizarAbandonoSI(PersistenceManager pm, int idSucursal, int id)
	{
		Query sql = pm.newQuery(SQL, "UPDATE "+psa.darTablaCarritoCompras()+" SET abandonado = 'SI' WHERE id_sucursal = "+idSucursal+" AND id = "+id);
		sql.setParameters(idSucursal, id);
		return (long) sql.executeUnique();
	}
	
	/**
	 * Actualiza y ejecuta la sentencia SQL para actualizar un CARRTITOCOMPRAS a la base de datos de SuperAndes
	 * @param idSucursal - Identificar de la sucursal
	 * @param id - Identificador del carrito
	 * @return El número de tuplas actualizadas
	 */
	public long actualizarAbandonoNO(PersistenceManager pm, int idSucursal, int id)
	{
		Query sql = pm.newQuery(SQL, "UPDATE "+psa.darTablaCarritoCompras()+" SET abandonado = 'NO' WHERE id_sucursal = "+idSucursal+" AND id = "+id);
		sql.setParameters(idSucursal, id);
		return (long) sql.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar los carritos abandonados de los CARRITOCOMPRAS de la 
	 * base de datos de SuperAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos CARRITOCOMPRAS
	 */
	public List<CarritoCompras> darCarritosAbandonados(PersistenceManager pm)
	{
		Query sql = pm.newQuery(SQL, "SELECT id FROM " +psa.darTablaCarritoCompras()+" WHERE abandonado = 'SI'");
		sql.setResultClass(CarritoCompras.class);
		return (List<CarritoCompras>) sql.execute();
	}
	
}
