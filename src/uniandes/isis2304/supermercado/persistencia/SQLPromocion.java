package uniandes.isis2304.supermercado.persistencia;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.supermercado.persistencia.*;

class SQLPromocion 
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
	 * @param pp - El Manejador de persistencia de la aplicaci�n
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
	 * @return EL n�mero de tuplas insertadas
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
	 * @return EL n�mero de tuplas insertadas
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
	 * @return EL n�mero de tuplas insertadas
	 */
	public long finalizarPromocionPorCantidad(PersistenceManager pm, int idSucursal)
	{
		Query sql = pm.newQuery(SQL, "DELETE FROM "+psa.darTablaPromocion() + "WHERE cantidad_productos = 0");
		sql.setParameters(idSucursal);
		return (long) sql.executeUnique();
	}
}
