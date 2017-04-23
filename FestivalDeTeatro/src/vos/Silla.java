package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Silla {
	////Atributos

	/**
	 * Numero de la silla
	 */
	@JsonProperty(value="numero")
	private int numero;

	/**
	 * Indica si la silla est� reservada
	 */
	private boolean estaReservada;

	/**
	 * Costo de esa silla
	 */
	@JsonProperty(value="costo")
	private double costo;
	
	/**
	 * Localidad de la silla
	 */
	@JsonProperty(value="localidad")
	private String localidad;
	
	/**
	 * Localidad de la silla
	 */
	@JsonProperty(value="id_sitio")
	private int id_sitio;
	
	@JsonProperty(value="id_reserva")
	private int id_reserva;
	
/**
 * Metodo constructor
 * @param numero
 * @param costo
 * @param localidad
 */
	public Silla(@JsonProperty(value="numero")int numero, @JsonProperty(value="costo")double costo,
			@JsonProperty(value="localidad")String localidad, @JsonProperty(value="id_sitio")int id_sitio) {
		super();
		this.numero = numero;
		estaReservada = false;
		this.costo = costo;
		this.localidad = localidad;
		this.id_sitio = id_sitio;
		this.id_reserva=0;
	} 
	
	//Getters and Setters

	
	public int getIdReserva(){
		return this.id_reserva;
	}
	public void setIdReserva(int id){
		this.id_reserva=id;
	}
	
public int getNumero() {
	return numero;
}

public void setNumero(int numero) {
	this.numero = numero;
}

public boolean isEstaReservada() {
	return estaReservada;
}

public void setEstaReservada(boolean estaReservada) {
	this.estaReservada = estaReservada;
}

public double getCosto() {
	return costo;
}

public void setCosto(double costo) {
	this.costo = costo;
}

public String getLocalidad() {
	return localidad;
}

public void setLocalidad(String localidad) {
	this.localidad = localidad;
}
	
public int getId_sitio() {
	return id_sitio;
}

public void setId_sitio(int id_sitio) {
	this.id_sitio = id_sitio;
}

public int getId_reserva() {
	return id_reserva;
}

public void setId_reserva(int id_reserva) {
	this.id_reserva = id_reserva;
}

}
