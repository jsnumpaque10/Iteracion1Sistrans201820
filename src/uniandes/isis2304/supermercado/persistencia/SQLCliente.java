package uniandes.isis2304.supermercado.persistencia;

import java.math.BigDecimal;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.supermercado.persistencia.*;
class SQLCliente 
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
	public SQLCliente(PersistenciaSuperAndes psa)
	{
		this.psa = psa;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un CLIENTE a la base de datos de SuperAndes
	 * @param pm - El manejador de persistencia
	 * @param idCliente - El identificador del bebedor
	 * @param nombre - El nombre del cliente
	 * @param correo - El correo del cliente
	 * @param ciudad - La ciudad del cliente
	 * @param direccion - La direcci�n del cliente
	 * @return EL n�mero de tuplas insertadas
	 */	
	public long adicionarCliente(PersistenceManager pm, int idCliente, String nombre, String correo, String ciudad, String direccion)
	{
		Query sql = pm.newQuery(SQL, "INSERT INTO "+psa.darTablaCliente() + "(id, nombre, correo, ciudad, direccion) values (?, ?, ? , ?, ?)");
		sql.setParameters(idCliente, nombre, correo, ciudad, direccion);
		return (long) sql.executeUnique();
	}
}
