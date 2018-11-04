package uniandes.isis2304.supermercado.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.supermercados.negocio.Promocion;
import uniandes.isis2304.supermercados.negocio.Proveedor;

class SQLProveedor {

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	
	private final static String SQL =PersistenciaSuperAndes.SQL;
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private PersistenciaSuperAndes ps;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Constructor de la clase
	 */
	public SQLProveedor(PersistenciaSuperAndes pPs)
	{
		this.ps=pPs;
	}
	
	/**
	 * Método que adiciona una bodega a la base de datos de SuperAndes
	 */
	public long adicionarProveedor(PersistenceManager pm, int NIT, String nombre, Double calificacionCalidad)
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaProveedor () + "(id, nombre, calificacion_calidad) values (?, ?, ?, ?)");
        q.setParameters(NIT, nombre, calificacionCalidad);
        return (long) q.executeUnique();     
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la informaci�n de la Proveedor de la 
	 * base de datos de SuperAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Proveedor
	 */
	public List<Proveedor> darProveedores(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaProveedor());
		q.setResultClass(Proveedor.class);
		return (List<Proveedor>) q.executeList();
	}
	
}
