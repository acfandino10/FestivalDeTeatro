package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Sitio {
	
	////Atributos

	/**
	 * Id del sitio
	 */
	@JsonProperty(value="id")
	private int id;

	/**
	 * Direccion del sitio
	 */
	@JsonProperty(value="direccion")
	private String direccion;
	
	/**
	 * Ciudad del sitio
	 */
	@JsonProperty(value="ciudad")
	private String ciudad;
	
	/**
	 * Indica para quienes es apto el sitio
	 */
	@JsonProperty(value="apto")
	private String apto;
	
	@JsonProperty(value="capacidad")
	private int capacidad;
	
	
/**
 * Metodo constructor
 * @param id
 * @param direccion
 * @param ciudad
 * @param apto
 * @param capacidad 
 */
	

	public Sitio(@JsonProperty(value="id")int id, @JsonProperty(value="direccion")String direccion, @JsonProperty(value="ciudad") String ciudad,@JsonProperty(value="apto") String apto,@JsonProperty(value="capacidad") int capacidad) {
		super();
		this.id = id;
		this.direccion = direccion;
		this.ciudad = ciudad;
		this.apto = apto;
		this.capacidad = capacidad;
	}   
	
	
	//Getters and Setters
public int getCapacidad(){
	return capacidad;
}

public int getId() {
	return id;
}


public void setId(int id) {
	this.id = id;
}


public String getDireccion() {
	return direccion;
}


public void setDireccion(String direccion) {
	this.direccion = direccion;
}


public String getCiudad() {
	return ciudad;
}


public void setCiudad(String ciudad) {
	this.ciudad = ciudad;
}


public String getApto() {
	return apto;
}


public void setApto(String apto) {
	this.apto = apto;
}

public void setCapacidad(int capacidad){
	this.capacidad = capacidad;
}
	
}
