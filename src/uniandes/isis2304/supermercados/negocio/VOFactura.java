package uniandes.isis2304.supermercados.negocio;

import java.sql.Timestamp;

public interface VOFactura
{
	/* ****************************************************************
	 * 			M�todos
	 *****************************************************************/
	
	public int getId();
	
	public int getIdCarritoCompras();
	
	public int getIdCliente();
	
	public Double getTotal();
	
	public Timestamp getFecha();
	
	public String toString();
}
