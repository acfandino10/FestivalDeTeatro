package vos;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class NotaDebito {

	@JsonProperty(value="id")
	private long id;
	
	@JsonProperty(value="nombre")
	private String nombre;
	
	@JsonProperty(value="fechaEmision")
	private Date fechaEmision;
	
	@JsonProperty(value="razonModificacion")
	private String razonModificacion;
	
	@JsonProperty(value="valorModificacion")
	private double valorModificacion;
	
	@JsonProperty(value="iva")
	private String iva;
	
	@JsonProperty(value="valorTotal")
	private double valorTotal;

	/**
	 * Metodo constructor de la clase Banco
	 * <b>post: </b> Crea el banco con los valores que entran como parametro
	 * @param id - Id del banco.
	 * @param nombre - Nombre del banco. name != null
	 * @param dirPrincipal - Direccion de la oficina principal.
	 */
	public NotaDebito(@JsonProperty(value="id")long id, @JsonProperty(value="nombre")String nombre,
			@JsonProperty(value="fechaEmision") Date fechaEmision,@JsonProperty(value="razonModificacion") String razonModificacion,
			@JsonProperty(value="valorModificacion") double valorModificacion, @JsonProperty(value="iva") String iva,
			@JsonProperty(value="valorTotal") double valorTotal) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fechaEmision=fechaEmision;
		this.razonModificacion=razonModificacion;
		this.valorModificacion=valorModificacion;
		this.iva=iva;
		this.valorTotal=valorTotal;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getRazonModificacion() {
		return razonModificacion;
	}

	public void setRazonModificacion(String razonModificacion) {
		this.razonModificacion = razonModificacion;
	}

	public double getValorModificacion() {
		return valorModificacion;
	}

	public void setValorModificacion(double valorModificacion) {
		this.valorModificacion = valorModificacion;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	
	
}
