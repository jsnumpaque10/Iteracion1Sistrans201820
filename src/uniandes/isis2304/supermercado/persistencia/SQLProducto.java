package uniandes.isis2304.supermercado.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import uniandes.isis2304.supermercados.negocio.Producto;

class SQLProducto {
	
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
	public SQLProducto(PersistenciaSuperAndes pPs)
	{
		this.ps=pPs;
	}
	
	/**
	 * Método que adiciona una bodega a la base de datos de SuperAndes
	 */
	public long adicionarProducto(PersistenceManager pm, String codigoBarras, int tipo, String nombre, String marca, String presentacion, String unidadMedida, Double cantidadPresentacion, Double pesoEmpaque, Double volumenEmpaque)
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaProducto () + "(id, id_tipoproducto, nombre, marca, presentacion, unidad_de_medida, cantidad_presentacion, peso_empaque, volumen_empaque) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        q.setParameters(codigoBarras, tipo, nombre, marca, presentacion, unidadMedida, cantidadPresentacion, pesoEmpaque, volumenEmpaque);
        return (long) q.executeUnique();     
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la informaci�n de los PRODUCTOS de la 
	 * base de datos de SuperAndes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos PRODUCTO
	 */
	public List<Producto> darProductos(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaProducto());
		q.setResultClass(Producto.class);
		return (List<Producto>) q.executeList();
	}
	

}
