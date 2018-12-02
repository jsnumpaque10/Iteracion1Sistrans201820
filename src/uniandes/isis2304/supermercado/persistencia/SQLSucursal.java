package uniandes.isis2304.supermercado.persistencia;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oracle.jdbc.dcn.DatabaseChangeRegistration;

class SQLSucursal 
{
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	
	private final static String SQL =PersistenciaSuperAndes.SQL;
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	private PersistenciaSuperAndes ps;
	
	/* ****************************************************************
	 * 			MÃ©todos
	 *****************************************************************/
	/**
	 * Constructor de la clase
	 */
	public SQLSucursal(PersistenciaSuperAndes pPs)
	{
		this.ps=pPs;
	}
	
	/**
	 * MÃ©todo que adiciona una sucursal a la base de datos de SuperAndes
	 */
	public long adicionarSucursal(PersistenceManager pm, int id, String ciudad, String sector, String direccion)
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaSucursal () + "(id, ciudad, sector, direccion) values (?, ?, ?, ?)");
        q.setParameters(id, ciudad, sector, direccion);
        return (long) q.executeUnique();     
	}
	
	/**
	 * RFC10: Consumo en SuperAndes
	 * Clientes que por lo menos compraron un determinado producto dentro de un rango de fechas
	 * @param pm
	 * @param nombreProducto
	 * @param fechaInical
	 * @param fechaFinal
	 * @return
	 */
	public List<Object> consultarConsumoEnSuperAndes(PersistenceManager pm, String nombreProducto, Timestamp fechaInicial, Timestamp fechaFinal)
	{
		String sql = "SELECT CLIENTE.ID, CLIENTE.NOMBRE, CLIENTE.CORREO, CLIENTE.CIUDAD, CLIENTE.DIRECCION, FACTTURA.FECHA, PRODUCTO.NOMBRE, SELECCIONPRODUCTOS.CANTIDAD";
		sql += "FROM "+ps.darTablaClientes()+", "+ps.darTablaFactura()+", "+ps.darTablaCarritoCompras()+", "+ps.darTablaSeleccionProductos()+", "+ps.darTablaProducto()+"";
		sql += "WHERE "+ps.darTablaClientes()+".ID = "+ps.darTablaFactura()+".ID_CLIENTE";
		sql += "AND "+ps.darTablaFactura()+".ID_CARRITOCOMPRAS = "+ps.darTablaCarritoCompras()+".ID";
		sql += "AND "+ps.darTablaCarritoCompras()+".ID = "+ps.darTablaSeleccionProductos()+".ID_CARRITOCOMPRAS";
		sql += "AND "+ps.darTablaSeleccionProductos()+".ID_PRODUCTO = "+ps.darTablaProducto()+".ID";
		sql += "AND "+ps.darTablaProducto()+".NOMBRE = '"+nombreProducto+"'";
		sql += "AND "+ps.darTablaFactura()+".FECHA >= '"+fechaInicial+"' AND "+ps.darTablaFactura()+".FECHA <= '"+fechaFinal+"'";
		sql += "ORDER BY "+ps.darTablaClientes()+".CIUDAD";
		
		Query q = pm.newQuery(SQL, sql);
		q.setParameters(nombreProducto, fechaInicial, fechaFinal);
		return q.executeList();
	}
	
	/**
	 * RFC11: Consumo en SuperAndes VOL 2
	 * Clientes que no compraron un determinado producto dentro de un rango de fechas
	 * @param pm
	 * @param nombreProducto
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public List<Object> consultarNoConsumoEnSuperAndes(PersistenceManager pm, String nombreProducto, Timestamp fechaInicial, Timestamp fechaFinal)
	{
		String sql = "SELECT CLIENTE.ID, CLIENTE.NOMBRE, CLIENTE.CORREO, CLIENTE.CIUDAD, CLIENTE.DIRECCION";
		sql += "INNER JOIN (SELECT ID, FROM "+ps.darTablaClientes();
		sql += "MINUS (SELECT DISTINCT "+ps.darTablaClientes()+".ID";
		sql += "FROM "+ps.darTablaClientes()+", "+ps.darTablaFactura()+", "+ps.darTablaCarritoCompras()+", "+ps.darTablaSeleccionProductos()+", "+ps.darTablaProducto()+"";
		sql += "WHERE "+ps.darTablaClientes()+".ID = "+ps.darTablaFactura()+".ID_CLIENTE";
		sql += "AND "+ps.darTablaFactura()+".ID_CARRITOCOMPRAS = "+ps.darTablaCarritoCompras()+".ID";
		sql += "AND "+ps.darTablaCarritoCompras()+".ID = "+ps.darTablaSeleccionProductos()+".ID_CARRITOCOMPRAS";
		sql += "AND "+ps.darTablaSeleccionProductos()+".ID_PRODUCTO = "+ps.darTablaProducto()+".ID";
		sql += "AND "+ps.darTablaProducto()+".NOMBRE = '"+nombreProducto+"'";
		sql += "AND "+ps.darTablaFactura()+".FECHA < '"+fechaInicial+"' OR "+ps.darTablaFactura()+".FECHA > '"+fechaFinal+"')) TABLA";
		sql += "ON "+ps.darTablaClientes()+".ID = TABLA.ID";
		sql += "ORDER BY "+ps.darTablaClientes()+".CIUDAD";
		
		Query q = pm.newQuery(SQL, sql);
		q.setParameters(nombreProducto, fechaInicial, fechaFinal);
		return q.executeList();
	}
	
	/**
	 * RFC12.1: CONSULTAR FUNCIONAMIENTO 
	 * Productos más vendidos
	 * @param pm
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public List<Object> consultarProductosMasVendidosSemanal(PersistenceManager pm, Timestamp fechaInicial, Timestamp fechaFinal)
	{
		String sql = "SELECT PRODUCTO.ID, PRODUCTO.NOMBRE, LATABLAFINAL.CUENTA_PRODUCTOS";
		sql += "FROM (SELECT ID, CUENTA_PRODUCTOS, FROM (SELECT SUM ("+ps.darTablaSeleccionProductos()+".CANTIDAD) AS CUENTA_PRODUCTOS, "+ps.darTablaProducto()+".ID";
		sql += "FROM "+ps.darTablaFactura()+", "+ps.darTablaCarritoCompras()+", "+ps.darTablaSeleccionProductos()+", "+ps.darTablaProducto();
		sql += "WHERE "+ps.darTablaFactura()+".ID_CARRITOCOMPRAS = "+ps.darTablaCarritoCompras()+".ID";
		sql += "AND "+ps.darTablaCarritoCompras()+".ID = "+ps.darTablaSeleccionProductos()+".ID_CARRITOCOMPRAS";
		sql += "AND "+ps.darTablaSeleccionProductos()+".ID_PRODUCTO = "+ps.darTablaProducto()+".ID";
		sql += "AND "+ps.darTablaFactura()+".FECHA >= '"+fechaFinal+"' AND "+ps.darTablaFactura()+".FECHA <= '"+fechaFinal+"'";
		sql += "GROUP BY "+ps.darTablaProducto()+".ID) LATABLA";
		sql += "WHERE CUENTA_PRODUCTOS = (SELECT MAX(CUENTA_PRODUCTOS) FROM (SELECT SUM("+ps.darTablaSeleccionProductos()+".CANTIDAD) AS CUENTA_PRODUCTOS, "+ps.darTablaProducto()+".ID";
		sql += "FROM "+ps.darTablaFactura()+", "+ps.darTablaCarritoCompras()+", "+ps.darTablaSeleccionProductos()+", "+ps.darTablaProducto();
		sql += "WHERE "+ps.darTablaFactura()+".ID_CARRITOCOMPRAS = "+ps.darTablaCarritoCompras()+".ID ";
		sql += "AND "+ps.darTablaCarritoCompras()+".ID = "+ps.darTablaSeleccionProductos()+".ID_CARRITOCOMPRAS";
		sql += "AND "+ps.darTablaSeleccionProductos()+".ID_PRODUCTO = "+ps.darTablaProducto()+".ID";
		sql += "AND "+ps.darTablaFactura()+".FECHA >= '"+fechaInicial+"' AND "+ps.darTablaFactura()+" <= '"+fechaFinal+"'";
		sql += "GROUP BY "+ps.darTablaProducto()+".ID))) LATABLAFINAL INNER JOIN "+ps.darTablaProducto();
		sql += "ON LATABLAFINAL.ID = "+ps.darTablaProducto()+".ID";
		
		Query q = pm.newQuery(SQL, sql);
		q.setParameters(fechaInicial, fechaFinal);
		return q.executeList();
	}
	
	/**
	 * RFC12.2: CONSULTAR FUNCIONAMIENTO
	 * Productos menos vendidos
	 * @param pm
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public List<Object> consultarProductosMenosVendidosSemanal(PersistenceManager pm, Timestamp fechaInicial, Timestamp fechaFinal)
	{
		String sql = "SELECT PRODUCTO.ID, PRODUCTO.NOMBRE, LATABLAFINAL.CUENTAPRODUCTOS";
		sql += "FROM (SELECT ID, CUENTA_PRODUCTOS FROM (SELECT SUM("+ps.darTablaSeleccionProductos()+".CANTIDAD AS CUENTA_PRODUCTOS, "+ps.darTablaProducto()+".ID";
		sql += "FROM "+ps.darTablaFactura()+", "+ps.darTablaCarritoCompras()+", "+ps.darTablaSeleccionProductos()+" "+ps.darTablaSeleccionProductos()+", "+ps.darTablaProducto();
		sql += "WHERE "+ps.darTablaFactura()+".ID_CARRITOCOMPRAS = "+ps.darTablaCarritoCompras()+".ID";
		sql += "AND "+ps.darTablaCarritoCompras()+".ID = "+ps.darTablaSeleccionProductos()+".ID_CARRITOCOMPRAS";
		sql += "AND "+ps.darTablaSeleccionProductos()+".ID_PRODUCTO = "+ps.darTablaProducto()+".ID";
		sql += "AND "+ps.darTablaFactura()+".FECHA >= '"+fechaInicial+"' AND "+ps.darTablaFactura()+".FECHA <= '"+fechaFinal+"'";
		sql += "GROUP BY "+ps.darTablaProducto()+".ID) LATABLA";
		sql += "WHERE CUENTA_PRODUCTOS = (SELECT MIN(CUENTA_PRODUCTOS) FROM (SELECT SUM("+ps.darTablaSeleccionProductos()+".CANTIDAD AS CUENTA_PRODUCTOS, "+ps.darTablaProducto()+".ID";
		sql += "FROM "+ps.darTablaFactura()+", "+ps.darTablaCarritoCompras()+", "+ps.darTablaSeleccionProductos()+", "+ps.darTablaProducto();
		sql += "WHERE "+ps.darTablaFactura()+".ID_CARRITOCOMPRAS = "+ps.darTablaCarritoCompras()+".ID";
		sql += "AND "+ps.darTablaCarritoCompras()+".ID = "+ps.darTablaSeleccionProductos()+".ID_CARRITOCOMPRAS";
		sql += "AND "+ps.darTablaSeleccionProductos()+".ID_PRODUCTO = "+ps.darTablaProducto()+".ID";
		sql += "AND "+ps.darTablaFactura()+".FECHA >= '"+fechaInicial+"' AND "+ps.darTablaFactura()+".FECHA <= '"+fechaFinal+"'";
		sql += "GROUP BY "+ps.darTablaProducto()+".ID))) LATABLAFINAL INNER JOIN "+ps.darTablaProducto();
		sql += "ON LATABLAFINAL.ID = "+ps.darTablaProducto()+".ID";
		
		Query q = pm.newQuery(SQL, sql);
		q.setParameters(fechaInicial, fechaFinal);
		return q.executeList();
	}
 	
	/**
	 * RFC12.3: CONSULTAR FUNCIONAMIENTO
	 * Proveedor más solicitado
	 * @param pm
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public List<Object> consultarProveedorMasSolicitadoSemanal(PersistenceManager pm, Timestamp fechaInicial, Timestamp fechaFinal)
	{
		String sql = "SELECT DISTINCT "+ps.darTablaProveedor()+".ID, "+ps.darTablaProveedor()+".NOMBRE, "+ps.darTablaProveedor()+".CALIFICACION_CALIDAD, TABLAPROVEEDORES.PEDIDOS";
		sql += "FROM (SELECT ID_PROVEEDOR, PEDIDOS FROM (SELECT COUNT(*) AS PEDIDOS, ID_PROVEEDOR FROM "+ps.darTablaPedido();
		sql += "WHERE "+ps.darTablaPedido()+".FECHA_ENTREGA >= '"+fechaInicial+"' AND "+ps.darTablaPedido()+".FECHA_ENTREGA <= '"+fechaFinal+"'";
		sql += "GROUP BY ID_PROVEEDOR)";
		sql += "WHERE (SELECT MAX(CUENTA_PROVEEDORES) AS CANTIDAD_PEDIDOS";
		sql += "FROM (SELECT COUNT(*) AS CUENTA_PROVEEDORES, ID_PROVEEDOR";
		sql += "FROM "+ps.darTablaPedido();
		sql += "WHERE "+ps.darTablaPedido()+".FECHA_ENTREGA >= '"+fechaInicial+"' AND "+ps.darTablaPedido()+".FECHA_ENTREGA <= '"+fechaFinal+"'";
		sql += "GROUP BY ID_PROVEEDOR)) = PEDIDOS) TABLAPROVEEDORES";
		sql += "INNER JOIN "+ps.darTablaProveedor()+" ON "+ps.darTablaProveedor()+".ID = TABLAPROVEEDORES.ID_PROVEEDOR";
		
		Query q = pm.newQuery(SQL, sql);
		q.setParameters(fechaInicial, fechaFinal);
		return q.executeList();
	}
	
	/**
	 * RFC12.4: CONSULTAR FUNCIONAMIENTO
	 * Proveedor menos solicitado
	 * @param pm
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public List<Object> consultarProveedorMenosSolicitadoSemanal(PersistenceManager pm, Timestamp fechaInicial, Timestamp fechaFinal)
	{
		String sql = "SELECT DISTINCT "+ps.darTablaProveedor()+".ID, "+ps.darTablaProveedor()+".NOMBRE, "+ps.darTablaProveedor()+".CALIFICACION_CALIDAD, TABLAPROVEEDORES.PEDIDOS";
		sql += "FROM (SELECT ID_PROVEEDOR, PEDIDOS FROM (SELECT COUNT(*) AS PEDIDOS, ID_PROVEEDOR FROM "+ps.darTablaPedido();
		sql += "WHERE "+ps.darTablaPedido()+".FECHA_ENTREGA >= '"+fechaInicial+"' AND "+ps.darTablaPedido()+".FECHA_ENTREGA <= '"+fechaFinal+"'";
		sql += "GROUP BY ID_PROVEEDOR)";
		sql += "WHERE (SELECT MIN(CUENTA_PROVEEDORES) AS CANTIDAD_PEDIDOS";
		sql += "FROM (SELECT COUNT(*) AS CUENTA_PROVEEDORES, ID_PROVEEDOR";
		sql += "FROM "+ps.darTablaPedido();
		sql += "WHERE "+ps.darTablaPedido()+".FECHA_ENTREGA >= '"+fechaInicial+"' AND "+ps.darTablaPedido()+".FECHA_ENTREGA <= '"+fechaFinal+"'";
		sql += "GROUP BY ID_PROVEEDOR)) = PEDIDOS) TABLAPROVEEDORES";
		sql += "INNER JOIN "+ps.darTablaProveedor()+" ON "+ps.darTablaProveedor()+".ID = TABLAPROVEEDORES.ID_PROVEEDOR";
		
		Query q = pm.newQuery(SQL, sql);
		q.setParameters(fechaInicial, fechaFinal);
		return q.executeList();
	}
	
	/**
	 * RF13.1 CONSULTAR BUENOS CLIENTES
	 * Clientes que compran por lo menos una vez al mes en SuperAndes
	 * @param pm
	 * @return
	 */
	public List<Object> consularClientesConComprasMensuales(PersistenceManager pm)
	{
		String sql = "SELECT CLIENTE.ID, CLIENTE.NOMBRE, CLIENTE.CORREO, CLIENTE.CIUDAD, CLIENTE.DIRECCION";
		sql += "FROM "+ps.darTablaClientes()+" INNER JOIN (SELECT ID_CLIENTE FROM (SELECT ID_CLIENTE, EXTRACT (MONTH FROM "+ps.darTablaFactura()+".FECHA) AS MES, COUNT(*) AS NUMVISITAS FROM "+ps.darTablaFactura();
		sql += "GROUP BY ID_CLIENTE, EXTRACT (MONTH FROM "+ps.darTablaFactura()+".FECHA)) TABLA WHERE TABLA.NUMVISITAS = 12) CLIENTEFRECUENTE";
		sql += "ON CLIENTE.ID = CLIENTEFRECUENTE.ID_CLIENTE";
		
		Query q = pm.newQuery(SQL, sql);
		return q.executeList();
	}
	
	/**
	 * RF13.2: CONSULTAR BUENOS CLIENTES
	 * Clientes que siempre compran por lo menos un producto costoso
	 * @param pm
	 * @return
	 */
	public List<Object> consultarClientesConComprasCostosas(PersistenceManager pm)
	{
		String sql = "SELECT CLIENTE.ID, CLIENTE.NOMBRE, CLIENTE.CIUDAD, CLIENTE.DIRECCION";
		sql += "FROM "+ps.darTablaClientes()+" INNER JOIN (SELECT "+ps.darTablaCarritoCompras()+".ID_CLIENTE FROM ((SELECT "+ps.darTablaSeleccionProductos()+".ID_CARRITOCOMPRAS";
		sql += "FROM ((SELECT ID_PRODUCTO FROM "+ps.darTablaInventario()+" WHERE PRECIO_UNITARIO >= 500000) TABLA";
		sql += "INNER JOIN "+ps.darTablaSeleccionProductos()+" ON "+ps.darTablaSeleccionProductos()+".ID_PRODUCTO = TABLA.ID_PRODUCTO)) CARRITOSMASCAROS";
		sql += "INNER JOIN "+ps.darTablaCarritoCompras()+" ON "+ps.darTablaCarritoCompras()+".ID = CARRITOSMASCAROS.ID_CARRITOCOMPRAS)) CLIENTEFRECUENTE";
		sql += "ON "+ps.darTablaClientes()+".ID = CLIENTEFRECUENTE.ID_CLIENTE";
		
		Query q = pm.newQuery(SQL, sql);
		return q.executeList();
	}
	
	/**
	 * RF13.3: CONSULTAR BUENOS CLIENTES
	 * Clientes que compran productos de tecnología o herramientas
	 * @param pm
	 * @return
	 */
	public List<Object> consultarClientesConComprasTecnologia(PersistenceManager pm)
	{
		String sql = "SELECT DISTINCT "+ps.darTablaClientes()+".ID, "+ps.darTablaClientes()+".NOMBRE, "+ps.darTablaClientes()+".CORREO, "+ps.darTablaClientes()+".CIUDAD, "+ps.darTablaClientes()+".DIRECCION";
		sql += "FROM "+ps.darTablaClientes()+" INNER JOIN (SELECT "+ps.darTablaCarritoCompras()+".ID_CLIENTE FROM ((SELECT "+ps.darTablaSeleccionProductos()+".ID_CARRITOCOMPRAS";
		sql += "FROM ((SELECT "+ps.darTablaProducto()+".ID FROM ";
		sql += "(SELECT ID FROM "+ps.darTablaTipoProducto()+" WHERE NOMBRE = 'Electronics' OR NOMBRE = 'Tools' OR NOMBRE = 'Computers') TIPOSPRODUCTO";
		sql += "INNER JOIN "+ps.darTablaProducto()+" ON "+ps.darTablaProducto()+".ID_TIPOPRODUCTO = TIPOSPRODUCTO.ID) TABLA";
		sql += "INNER JOIN "+ps.darTablaSeleccionProductos()+" ON "+ps.darTablaSeleccionProductos()+".ID_PRODUCTO = TABLA.ID)) CARRITOSCONPRODUCTOSIN";
		sql += "INNER JOIN "+ps.darTablaCarritoCompras()+" ON "+ps.darTablaCarritoCompras()+".ID = CARRITOSCONPRODUCTOSIN.ID_CARRITOCOMPRAS)) CLIENTEFRECUENTE";
		sql += "ON "+ps.darTablaClientes()+".ID = CLIENTEFRECUENTE.ID_CLIENTE";
		
		Query q = pm.newQuery(SQL, sql);
		return q.executeList();
	}
}
