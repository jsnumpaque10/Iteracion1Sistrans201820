package uniandes.isis2304.supermercado.persistencia;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.supermercado.persistencia.*;
class SQLFactura 
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
	public SQLFactura(PersistenciaSuperAndes psa)
	{
		this.psa = psa;
	}
	
	public long generarFactura(PersistenceManager pm, int idFactura, int idCarritoCompras, double total, Timestamp fecha)
	{
		Query sql = pm.newQuery(SQL, "INSERT INTO " +psa.darTablaFactura() + "(id_factura, id_carritocompras, total, fecha) values (?, ?, ?, ?)");
		sql.setParameters(idFactura, idCarritoCompras, total, fecha);
		return (long) sql.executeUnique();
	}
}
