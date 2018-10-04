package uniandes.isis2304.supermercado.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

class SQLProveen 
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
	public SQLProveen(PersistenciaSuperAndes psa)
	{
		this.psa = psa;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un PROVEEN a la base de datos de SuperAndes
	 * @param pm - El manejador de persistencia
	 * @param idProveedor - El identificador del proveedor
	 * @param idProducto - El identificador del producto
	 * @param idSucursal - El identificador de la sucursal
	 * @return EL n�mero de tuplas insertadas
	 */
	public long adicionarProveen(PersistenceManager pm, int idProveedor, int idProducto, int idSucursal) 
	{
        Query sql = pm.newQuery(SQL, "INSERT INTO " + psa.darTablaProveen() + "(id_proveedor, id_producto, id_sucursal) values (?, ?, ?)");
        sql.setParameters(idProducto, idProducto, idSucursal);
        return (long)sql.executeUnique();            
	}	
}
