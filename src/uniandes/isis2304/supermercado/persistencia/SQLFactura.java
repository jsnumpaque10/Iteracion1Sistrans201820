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
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un FACTURA a la base de datos de SuperAndes
	 * @param idFactura - Identificador de la factura
	 * @param idCarritoCompras - Identificador del carrito asociado
	 * @param total - Precio total relacionado a la factura
	 * @param fecha - fecha de facturaci�n
	 * @return El n�mero de tuplas insertadas
	 */
	public long generarFactura(PersistenceManager pm, int idFactura, int idCarritoCompras, int idCliente, double total, Timestamp fecha)
	{
		Query sql = pm.newQuery(SQL, "INSERT INTO " +psa.darTablaFactura() + "(id_factura, id_carritocompras, id_cliente, total, fecha) values (?, ?, ?, ?, ?)");
		sql.setParameters(idFactura, idCarritoCompras, idCliente, total, fecha);
		return (long) sql.executeUnique();
	}
}
