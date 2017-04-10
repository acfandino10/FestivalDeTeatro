package vos;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Espectaculo {
////Atributos

	/**
	 * Id del espectaculo
	 */
	@JsonProperty(value="id")
	private int id;
	
	/**
	 * Nombre del espectaculo
	 */
	@JsonProperty(value="nombre")
	private String nombre;
	
	/**
	 * Rentabilidad que ha generado el espectaculo
	 */
	private double rentabilidad;
	
	/**
	 * Dice si el espectaculo merece tener otra función
	 */
	private boolean nuevaFuncion;
	
	/**
	 * Descripcion del espectaculo
	 */
	@JsonProperty(value="descripcion")
	private String descripcion;
	
	/**
	 * Duracion del evento
	 */
	@JsonProperty(value="duracion")
	private double duracion;
	
	/**
	 * Idioma 
	 */
	@JsonProperty(value="idioma")
	private String idioma;
	
	
	/**
	 * True si tiene traduccion al espanol, false de lo contrario
	 */
	@JsonProperty(value="traduccionEspanol")
	private boolean traduccionEspanol;
	
	@JsonProperty(value="id_representante")
	private int id_representante;
	
	
	@JsonProperty(value="categorias")
	private List<Categoria> categorias;
	
 /**
  * Metodo constructor de la clase evento
  * @param id
  * @param fecha
  * @param descripcion
  * @param duracion
  * @param hora
  */
	
	public Espectaculo(@JsonProperty(value="id")int id, @JsonProperty(value="nombre") String nombre,
			@JsonProperty(value="descripcion")String descripcion, @JsonProperty(value="duracion")double duracion, 
			@JsonProperty(value="idioma")String idioma, @JsonProperty(value="traduccionEspanol") boolean traduccionEspanol,
	        @JsonProperty(value="id_representante")int repr) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.rentabilidad = 0;
		this.nuevaFuncion = false;
		this.descripcion = descripcion;
		this.duracion = duracion;
		this.idioma = idioma;
		this.traduccionEspanol = traduccionEspanol;
		this.id_representante = repr;
		categorias = null;
		
	}	
	        
	        //Getters and setters

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

public double getRentabilidad() {
	return rentabilidad;
}

public void setRentabilidad(double rentabilidad) {
	this.rentabilidad = rentabilidad;
}

public boolean isNuevaFuncion() {
	return nuevaFuncion;
}

public void setNuevaFuncion(boolean nuevaFuncion) {
	this.nuevaFuncion = nuevaFuncion;
}

public String getDescripcion() {
	return descripcion;
}

public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}

public int getId_representante() {
	return id_representante;
}

public void setId_representante(int id_representante) {
	this.id_representante = id_representante;
}

public double getDuracion() {
	return duracion;
}

public void setDuracion(double duracion) {
	this.duracion = duracion;
}

public String getIdioma() {
	return idioma;
}

public void setIdioma(String idioma) {
	this.idioma = idioma;
}

public boolean isTraduccionEspanol() {
	return traduccionEspanol;
}

public void setTraduccionEspanol(boolean traduccionEspanol) {
	this.traduccionEspanol = traduccionEspanol;
}
	
	
}
