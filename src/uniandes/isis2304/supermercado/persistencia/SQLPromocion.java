package uniandes.isis2304.supermercado.persistencia;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.supermercado.persistencia.*;
import uniandes.isis2304.supermercados.negocio.Producto;
import uniandes.isis2304.supermercados.negocio.Promocion;

class SQLPromocion 
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
	public SQLPromocion(PersistenciaSuperAndes psa)
	{
		this.psa = psa;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una PROMOCION a la base de datos de SuperAndes
	 * @param idPromocion - El identificador de la promocion
	 * @param idSucursal - El identificador de la sucursal
	 * @param fechaInicio - La fecha de inicio de la promocion
	 * @param fechaFinal - La fecha de fin de la promocion
	 * @param cantidadProductos - La cantidad de producto en la promocion
	 * @param precioFinal - El precio final de la promocion
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarPromocion(PersistenceManager pm, int idPromocion, int idSucursal, Timestamp fechaInicio, Timestamp fechaFinal, int cantidadProductos, double precioFinal)
	{
		Query sql = pm.newQuery(SQL, "INSERT INTO "+psa.darTablaPromociones() + "(id, id_sucursal, fecha_inicio, fecha_final, cantidad_productos, precio_total) values (?, ?, ?, ?, ?, ?)");
		sql.setParameters(idPromocion, idSucursal, fechaInicio, fechaFinal, cantidadProductos, precioFinal);
		return (long) sql.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para finalizar una PROMOCION a la base de datos de SuperAndes
	 * @param idPromocion - El identificador de la promocion
	 * @param idSucursal - El identificador de la sucursal
	 * @return EL número de tuplas insertadas
	 */
	public long finalizarPromocionPorFecha(PersistenceManager pm, int idSucursal, Timestamp fechaFinal)
	{		
		//Fecha Actual
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
	    Date date = new Date();  
	 
		Query sql = pm.newQuery(SQL, "DELETE FROM "+psa.darTablaPromocion() + "WHERE id_sucursal = ? AND fecha_final = "+date+"");
		sql.setParameters(idSucursal);
		return (long) sql.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para finalizar una PROMOCION a la base de datos de SuperAndes
	 * @param idPromocion - El identificador de la promocion
	 * @param idSucursal - El identificador de la sucursal
	 * @return EL número de tuplas insertadas
	 */
	public long finalizarPromocionPorCantidad(PersistenceManager pm, int idSucursal)
	{
		Query sql = pm.newQuery(SQL, "DELETE FROM "+psa.darTablaPromocion() + "WHERE cantidad_productos = 0");
		sql.setParameters(idSucursal);
		return (long) sql.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de la Promocion de la 
	 * base de datos de SuperAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Promocion
	 */
	public List<Promocion> darPromociones(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + psa.darTablaPromocion());
		q.setResultClass(Promocion.class);
		return (List<Promocion>) q.executeList();
	}
}
