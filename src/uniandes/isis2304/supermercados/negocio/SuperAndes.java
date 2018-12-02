package uniandes.isis2304.supermercados.negocio;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

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
	
	/**
	 * Encuentra todos los productos en SuperAndes y los devuelve como una lista de VOProducto
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOProducto con todos los productos que conoce la aplicación, llenos con su información básica
	 */
	public List<VOProducto> darVOProducto()
	{
		log.info ("Generando los VO de Producto");        
        List<VOProducto> voTipos = new LinkedList<VOProducto> ();
        for (Producto p : psa.darProductos())
        {
        	voTipos.add (p);
        }
        log.info ("Generando los VO de Producto: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	
	/**
	 * Encuentra toda la promocion en la base de datos de SuperAndes y los devuelve como una lista de VOPromocion
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOPromocion con toda la promocion que conoce la aplicación, llenos con su información básica
	 */
	public List<VOPromocion> darVOPromocion()
	{
		log.info ("Generando los VO de Promocion");        
        List<VOPromocion> voTipos = new LinkedList<VOPromocion> ();
        for (Promocion p : psa.darPromociones())
        {
        	voTipos.add (p);
        }
        log.info ("Generando los VO de Promocion: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	/**
	 * Encuentra toda la promocion en la base de datos de SuperAndes y los devuelve como una lista de VOProveedor
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOProveedor con toda la promocion que conoce la aplicación, llenos con su información básica
	 */
	public List<VOProveedor> darVOProveedor()
	{
		log.info ("Generando los VO de Proveedor");        
        List<VOProveedor> voTipos = new LinkedList<VOProveedor> ();
        for (Proveedor p : psa.darProveedor())
        {
        	voTipos.add (p);
        }
        log.info ("Generando los VO de Proveedor: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	/**
	 * Encuentra toda la promocion en la base de datos de SuperAndes y los devuelve como una lista de VOSeleccionProductos
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOSeleccionProductos con toda la promocion que conoce la aplicación, llenos con su información básica
	 */
	public List<VOSeleccionProductos> darVOSeleccionProductos()
	{
		log.info ("Generando los VO de Seleccion Productos");        
        List<VOSeleccionProductos> voTipos = new LinkedList<VOSeleccionProductos> ();
        for (SeleccionProductos sp : psa.darSeleccionProductos())
        {
        	voTipos.add (sp);
        }
        log.info ("Generando los VO de SeleccionProductos: " + voTipos.size() + " existentes");
        return voTipos;
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

	public CarritoCompras solicitarCarritoCompras(int pIdSucursal, int pIdCliente, Timestamp pFechaVisista)
	{
		CarritoCompras resp = psa.solicitarCarritoCompras(pIdSucursal, pIdCliente, pFechaVisista);
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
	
	public List<Object []> darConsumoSuperAndes(String producto, Timestamp fecha1, Timestamp fecha2)
	{
		log.info("Listando los clientes...");
		List<Object []> tuplas = psa.darConsumoEnSuperAndes(producto, fecha1, fecha2);
		log.info("Listado de clientes: Listo");
		return tuplas;
	}
	
	public List<Object []> darNoConsumoSuperAndes(String producto, Timestamp fecha1, Timestamp fecha2)
	{
		log.info("Listando los clientes...");
		List<Object []> tuplas = psa.darNoConsumoEnSuperAndes(producto, fecha1, fecha2);
		log.info("Listado de clientes: Listo");
		return tuplas;
	}
	
	public List<Object[]> darProductoMasVendido(Timestamp fecha1, Timestamp fecha2)
	{
		log.info("Listando productos...");
		List<Object[]> tuplas = psa.darProductosMasVendidosSemanal(fecha1, fecha2);
		log.info("Obtenido el producto: Listo");
		return tuplas;
	}
	
	public List<Object[]> darProductoMenosVendido(Timestamp fecha1, Timestamp fecha2)
	{
		log.info("Listando productos...");
		List<Object[]> tuplas = psa.darProductosMenosVendidosSemanal(fecha1, fecha2);
		log.info("Obtenido el producto: Listo");
		return tuplas;
	}
	
	public List<Object[]> darProveedorMasSolicitado(Timestamp fecha1, Timestamp fecha2)
	{
		log.info("Listando proveedores...");
		List<Object[]> tuplas = psa.darProveedorMasSolicitadoSemanal(fecha1, fecha2);
		log.info("Obtenido el proveedor: Listo");
		return tuplas;
	}
	
	public List<Object[]> darProveedorMenosSolicitado(Timestamp fecha1, Timestamp fecha2)
	{
		log.info("Listando proveedores...");
		List<Object[]> tuplas = psa.darProveedorMenosSolicitadoSemanal(fecha1, fecha2);
		log.info("Obtenido el proveedor: Listo");
		return tuplas;
	}
	
	public List<Object[]> darClientesConComprasMensuales()
	{
		log.info("Listando clientes...");
		List<Object[]> tuplas = psa.darClientesConComprasMensuales();
		log.info("Listado de clientes: Listo");
		return tuplas;
	}
	
	public List<Object[]> darClientesConComprasCostosas()
	{
		log.info("Listando clientes...");
		List<Object[]> tuplas = psa.darClientesConComprasCostosas();
		log.info("Listado de clientes: Listo");
		return tuplas;
	}
	
	public List<Object[]> darClientesConComprasTecnologicas()
	{
		log.info("Listando clientes...");
		List<Object[]> tuplas = psa.darClientesConComprasTecnologicas();
		log.info("Listado de clientes: Listo");
		return tuplas;
	}
	
	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de SuperAndes
	 * @return Un arreglo con 17 números que indican el número de tuplas borradas en las tablas
	 */
	public long [] limpiarSuperAndes()
	{
        log.info ("Limpiando la BD de SuperAndes");
        long [] borrrados = psa.limpiarSuperAndes();	
        log.info ("Limpiando la BD de SuperAndes: Listo!");
        return borrrados;
	}
}
