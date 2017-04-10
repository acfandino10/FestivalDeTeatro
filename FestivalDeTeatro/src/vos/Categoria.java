package vos;



import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Categoria {

////Atributos

	/**
	 * Nombre de la categoria
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	/**
	 * Edad permitida para ver esa categoria
	 */
	@JsonProperty(value="edad")
	private int edad;


	

	/**
	 * Metodo constructor de la clase Categoria
	 * <b>post: </b> Crea la categoria con los valores que entran como parametro
	 * @param nombre - Nombre de la categoria. name != null
	 * @param edad - Edad minima permitida para ver esa categoria. edad != null
	 */
	public Categoria(@JsonProperty(value="nombre")String nombre, @JsonProperty(value="edad")int edad) {
		super();
		this.nombre = nombre;
		this.edad = edad;
	}
	
	/**
	 * Metodo getter del atributo nombre
	 * @return Nombre de la categoria
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Metodo setter del atributo nombre <b>post: </b> Nombre de la categoria
	 * ha sido cambiado con el valor que entra como parametro
	 * @param nombre - Nombre de la categoria.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Metodo getter del atributo edad
	 * @return edad minima permitida para esa categoria
	 */
	public int getEdad() {
		return edad;
	}

	/**
	 * Metodo setter del atributo edad <b>post: </b> Edad ha sido
	 * cambiado con el valor que entra como parametro
	 * @param edad - Edad minima para ver la categoria
	 */
	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	
}
