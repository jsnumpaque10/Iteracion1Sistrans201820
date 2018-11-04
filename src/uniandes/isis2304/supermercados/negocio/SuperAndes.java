package uniandes.isis2304.supermercados.negocio;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import uniandes.isis2304.supermercado.persistencia.PersistenciaSuperAndes;

public class SuperAndes

{

	/*
	 * ****************************************************************
	 * 
	 * Constantes
	 * 
	 *****************************************************************/

	/**
	 * 
	 * Logger para escribir la traza de la ejecución
	 * 
	 */

	private static Logger log = Logger.getLogger(SuperAndes.class.getName());

	/*
	 * ****************************************************************
	 * 
	 * Atributos
	 * 
	 *****************************************************************/

	/**
	 * 
	 * El manejador de persistencia
	 * 
	 */

	private PersistenciaSuperAndes psa;

	/*
	 * ****************************************************************
	 * 
	 * Métodos
	 * 
	 *****************************************************************/

	/**
	 * 
	 * El constructor por defecto
	 * 
	 */

	public SuperAndes()

	{
		psa = PersistenciaSuperAndes.getInstance();
	}

	/**
	 * 
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * 
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad
	 *                    de persistencia
	 * 
	 */

	public SuperAndes(JsonObject tableConfig)

	{
		this.psa = PersistenciaSuperAndes.getInstance(tableConfig);
	}

	/**
	 * 
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 * 
	 */

	public void cerrarUnidadPersistencia()

	{
		psa.cerrarUnidadPersistencia();
	}

	public Cliente adicionarCliente(int idCliente, String nombre, String correo, String ciudad, String direccion) 
	{
		log.info("Adicionando cliente con nombre : " + nombre);
		Cliente cliente = psa.adicionarCliente(idCliente, nombre, correo, ciudad, direccion);
		log.info("Adicionando cliente con nombre : " + nombre);
		return cliente;
	}

	public VOSucursal adicionarSucursal(String ciudad, String sector, String direccion) 
	{
		log.info("Adicionando sucursal con direccion : " + direccion);
		Sucursal sucursal = psa.adicionarSucursal(ciudad, sector, direccion);
		log.info("Adicionando sucursal con direccion : " + direccion);
		return sucursal;
	}

	public Bodega adicionarBodega(int tipoProducto, int sucursal, Double capacidadVolumen, Double capacidadPeso) 
	{
		log.info("Adicionando bodega");
		Bodega bodega = psa.adicionarBodegaASucursal(tipoProducto, sucursal, capacidadVolumen, capacidadPeso);
		log.info("Adicionando bodega");
		return bodega;
	}

	public Estante adicionarEstante(int tipoProducto, int sucursal, Double capacidadVolumen, Double capacidadPeso, int nivelAbastecimiento) 
	{
		log.info("Adicionando estante");
		Estante estante = psa.adicionarEstanteASucursal(tipoProducto, sucursal, capacidadVolumen, capacidadPeso, nivelAbastecimiento);
		log.info("Adicionando estante");
		return estante;
	}

	public TipoProducto adicionarTipoProducto(String nombreTipo, String categoria) 
	{
		log.info("Adicionando Tipo de producto: " + nombreTipo);
		TipoProducto tipoProducto = psa.adicionarTipoProducto(nombreTipo, categoria);
		log.info("Adicionando Tipo de producto: " + nombreTipo);
		return tipoProducto;

	}

	public Proveedor adicionarProveedor(int nit, String nombre, Double calificacionCalidad) 
	{
		log.info("Adicionando Proveedor");
		Proveedor proveedor = psa.adicionarProveedor(nit, nombre, calificacionCalidad);
		log.info("Adicionando proveedor");
		return proveedor;
	}

	public Producto adicionarProducto(String codigoBarras, int tipoProducto, String nombre, String marca, String presentacion, String unidadMedida, Double cantidadPresentacion, Double pesoEmpaque, Double volumenEmpaque) 
	{
		log.info("Adicionando Producto");
		Producto producto = psa.adicionarProducto(codigoBarras, tipoProducto, nombre, marca, presentacion, unidadMedida, cantidadPresentacion, pesoEmpaque, volumenEmpaque);
		log.info("Adicionando Producto");
		return producto;
	}

	public Promocion adicionarPromocion(int idPromocion, int idSucursal, Timestamp fechaInicio, Timestamp fechaFinal, int cantidadProductos, double precioFinal)
	{
		log.info("Adicionando promocion: " + idPromocion);
		Promocion promocion = psa.adicionarPromocion(idSucursal, fechaInicio, fechaFinal, cantidadProductos, precioFinal);
		log.info("Adicionando promocion: " + promocion);
		return promocion;
	}

	public Pedido adicionarPedidoAProveedor(int idPedido, int idSucursal, int idProveedor, int idProducto,
			Timestamp fechaEsperadaEntrega, int cantidad, double precioTotal, Timestamp fechaEntrega,
			int calidadProductos, String estado)
	{
		log.info("Adicionando pedido: " + idPedido);
		Pedido pedido = psa.adicionarPedidoAProveedor(idSucursal, idProveedor, idProducto, fechaEsperadaEntrega,
				cantidad, precioTotal, fechaEntrega, calidadProductos);
		log.info("Adicionando pedido: " + pedido);
		return pedido;
	}

	public long finalizarPromocionPorFecha(int idSucursal, Timestamp fechaFinal)
	{
		log.info("Eliminando promoci�n por fecha: " + fechaFinal);
		long resp = psa.finalizarPromocionPorFecha(idSucursal, fechaFinal);
		log.info("Eliminando promoci�n por fecha: " + resp + " tuplas eliminadas");
		return resp;
	}

	public long finalizarPromocionPorCantidad(int idSucursal)
	{
		long resp = psa.finalizarPromocionPorCantidad(idSucursal);
		log.info("Eliminando promoci�n por fecha: " + resp + " tuplas eliminadas");
		return resp;
	}

	public long finalizarPedidoAProveedor(int idPedido, int idSucursal, int idProveedor)
	{
		log.info("Finalizando pedido: " + idPedido);
		long resp = psa.llegadaPedidoAProveedor(idPedido, idSucursal, idProveedor);
		log.info("Eliminando promoci�n por fecha: " + resp + " tuplas eliminadas");
		return resp;
	}

	public long solicitarCarritoCompras(int pIdSucursal, int pIdCliente, Timestamp pFechaVisista)
	{
		long resp = psa.solicitarCarritoCompras(pIdSucursal, pIdCliente, pFechaVisista);
		return resp;
	}

	public long adicionarProductosACarrito(int pIdProducto, int pIdCarritoCompras, int pCantidad, int pIdEstante)
	{
		long resp = psa.adicionarProductosACarrito(pIdProducto, pIdCarritoCompras, pCantidad, pIdEstante);
		return resp;
	}

	public long devolverProductosDelCarrito(int pIdProducto, int pIdCarrtioCompras, int pIdEstante, int pCantidad)

	{
		long resp = psa.devolverCantidadProductosDelCarrito(pIdProducto, pIdCarrtioCompras, pIdEstante, pCantidad);
		return resp;
	}
}
