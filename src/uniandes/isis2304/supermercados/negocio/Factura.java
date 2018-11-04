package uniandes.isis2304.supermercados.negocio;

import java.sql.Timestamp;

public class Factura implements VOFactura
{
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * Identificador único de la factura
	 */
	
	private int id;
	
	/**
	 * Identificador del carrito 
	 */
	
	private int idCarritoCompras;
	
	/**
	 * Identifiador del Cliente
	 */
	private int idCliente;
	
	/**
	 * Total de la factura
	 */
	private Double total;
	
	/**
	 * Fecha de la compra
	 */
	private Timestamp fecha;
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * Constructor por defecto 
	 */
	
	public Factura()
	{
		id = 0;
		idCarritoCompras = 0;
		idCliente = 0;
		total = (double) 0;
		fecha = new Timestamp(0);
	}
	
	public Factura (int pId, int pIdCarritoCompras, int pIdCliente, double pTotal, Timestamp pFecha)
	{
		this.id=pId;
		this.idCarritoCompras = pIdCarritoCompras;
		this.idCliente = pIdCarritoCompras;
		this.total=pTotal;
		this.fecha=pFecha;
	}
	
	/**
	 * Retorna una cadena de caracteres con la informacion de la factura
	 */
	
	@Override
	public String toString() {
		return "Factura [id=" + id + ", visitaMercado=" + idCarritoCompras + ", total=" + total + ", fecha ="+fecha+"]";
	}
	
	/* ****************************************************************
	 * 			Métodos Get y Set
	 *****************************************************************/
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdCarritoCompras() {
		return idCarritoCompras;
	}

	public void setCantidadProductos(int pIdCarritoCompras) {
		this.idCarritoCompras = pIdCarritoCompras;
	}
	
	public int getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(int pIdCliente) {
		this.idCliente = pIdCliente;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
	
	public Timestamp getFecha() {
		return fecha;
	}
	
	public void setFecha(Timestamp pFecha) {
		this.fecha = pFecha;
	}
}
