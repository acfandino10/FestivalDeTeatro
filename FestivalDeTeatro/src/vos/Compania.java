package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;


public class Compania {

////Atributos

	/**
	 * Id de la compania
	 */
	@JsonProperty(value="id")
	private int id;

	/**
	 * Nombre de la compania
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	/**
	 * Dia de llegada a una ciudad
	 */
	@JsonProperty(value="diaLlegada")
	private Date diaLlegada;
	
	/**
	 * Dia de salida de una ciudad
	 */
	@JsonProperty(value="diaSalida")
	private Date diaSalida;
	
	/**
	 * Ciudad donde estar� la compania
	 */
	@JsonProperty(value="ciudad")
	private String ciudad;
	

	/**
	 * Metodo constructor de la clase Compania
	 * <b>post: </b> Crea la compania con los valores que entran como parametro
	 * @param id - Id de la compania.
	 * @param nombre - Nombre de la compania. name != null
	 * @param diaLlegada - Dia de llegada de la compania
	 * @param diaSalida - Dia salida de la compania
	 * @param ciudad - Ciudad donde estar� la compania
	 * 
	 */
	public Compania(@JsonProperty(value="id")int id, @JsonProperty(value="nombre")String nombre,@JsonProperty(value="diaLlegada") Date diaLlegada,@JsonProperty(value="diaSalida") Date diaSalida, @JsonProperty(value="ciudad") String ciudad ) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.diaLlegada = diaLlegada;
		this.diaSalida = diaSalida;
		this.ciudad = ciudad;
	}

	//Getters and Setters
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getDiaLlegada() {
		return diaLlegada;
	}

	public void setDiaLlegada(Date diaLlegada) {
		this.diaLlegada = diaLlegada;
	}

	public Date getDiaSalida() {
		return diaSalida;
	}

	public void setDiaSalida(Date diaSalida) {
		this.diaSalida = diaSalida;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	
}
