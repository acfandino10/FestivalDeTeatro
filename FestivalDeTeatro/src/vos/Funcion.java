package vos;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Funcion {

	////Atributos

	/**
	 * Id del evento
	 */
	@JsonProperty(value="id")
	private int id;

	/**
	 * Fecha del evento
	 */
	@JsonProperty(value="fecha")
	private Date fecha;
	
	/**
	 * Nombre del evento
	 */
	@JsonProperty(value="id_espectaculo")
	private int id_espectaculo;

	/**
	 * Disponibilidad del evento
	 */
	private boolean disponibilidad;
	
	/**
	 * Ganancias que ha generado el evento
	 */
	private double ganancias;
	
	/**
	 * Hora del evento
	 */
	@JsonProperty(value="hora")
	private Timestamp hora;
	
	@JsonProperty(value="id_sitio")
	private int id_sitio;
	
	@JsonProperty(value="id_sitio")
	private String estado;
	
	public static String ESTADO_ENCURSO="ESTADO_ENCURSO";
	public static String ESTADO_REALIZADA="ESTADO_REALIZADA";
	public static String ESTADO_PREVISTA="ESTADO_PREVISTA";
	
 /**
  * Metodo constructor de la clase evento
  * @param id
  * @param fecha
  * @param descripcion
  * @param duracion
  * @param hora
  */
	
	public Funcion(@JsonProperty(value="id")int id, @JsonProperty(value="fecha") Date fecha, @JsonProperty(value="id_espectaculo") int nombre,
			@JsonProperty(value="hora")Timestamp hora,
			@JsonProperty(value="id_sitio")int sitio, @JsonProperty(value="estado")String estado) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.id_espectaculo = nombre;
		this.disponibilidad = true;
		this.ganancias = 0;
		this.hora = hora;
		this.id_sitio = sitio;
		this.estado=estado;
		
	}

	//Getters and setters
	

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public Date getFecha() {
	return fecha;
}

public void setFecha(Date fecha) {
	this.fecha = fecha;
}


public boolean isDisponibilidad() {
	return disponibilidad;
}

public void setDisponibilidad(boolean disponibilidad) {
	this.disponibilidad = disponibilidad;
}

public double getGanancias() {
	return ganancias;
}

public void setGanancias(double ganancias) {
	this.ganancias = ganancias;
}

public void setEstado(String estado){
	this.estado = estado;
}

public String getEstado(){
	return estado;
}

public int getId_espectaculo() {
	return id_espectaculo;
}

public void setId_espectaculo(int id_espectaculo) {
	this.id_espectaculo = id_espectaculo;
}

public int getId_sitio() {
	return id_sitio;
}

public void setId_sitio(int id_sitio) {
	this.id_sitio = id_sitio;
}

public Timestamp getHora() {
	return hora;
}

public void setHora(Timestamp hora) {
	this.hora = hora;
}	
	
	
}
