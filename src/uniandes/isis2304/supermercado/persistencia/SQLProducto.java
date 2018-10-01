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
	public long adicionarProducto(PersistenceManager pm, int codigoBarras, int tipo, String nombre, String marca, String presentacion, String unidadMedida, Double cantidadPresentacion, Double pesoEmpaque, Double volumenEmpaque)
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaProducto () + "(codigoBarras, tipoProducto, nombre, marca, presentacion, unidadMedida, cantidadPresentacion, pesoEmpaque, volumenEmpaque) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        q.setParameters(codigoBarras, tipo, nombre, marca, presentacion, unidadMedida, cantidadPresentacion, pesoEmpaque, volumenEmpaque);
        return (long) q.executeUnique();     
	}
	

}
