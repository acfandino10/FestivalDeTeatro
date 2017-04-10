package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Banco {

////Atributos

	/**
	 * Id del banco
	 */
	@JsonProperty(value="id")
	private int id;

	/**
	 * Nombre del banco
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	/**
	 * Direccion de la oficina principal
	 */
	@JsonProperty(value="dirPrincipal")
	private String dirPrincipal;

	/**
	 * Metodo constructor de la clase Banco
	 * <b>post: </b> Crea el banco con los valores que entran como parametro
	 * @param id - Id del banco.
	 * @param nombre - Nombre del banco. name != null
	 * @param dirPrincipal - Direccion de la oficina principal.
	 */
	public Banco(@JsonProperty(value="id")int id, @JsonProperty(value="nombre")String nombre,@JsonProperty(value="dirPrincipal") String dirPrincipal) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.dirPrincipal = dirPrincipal;
	}
	
	/**
	 * Metodo getter del atributo dirPrincipal
	 * @return direccion de la oficina principal
	 */
	public String getDirPrincipal() {
		return dirPrincipal;
	}

	/**
	 * Metodo setter del atributo dirPrincipal 
	 * <b>post: </b> La direccion de la oficina principal
	 * ha sido cambiado con el valor que entra como parametro
	 * @param dirPrincipal - Direccion de la oficina principal.
	 */
	public void setDirPrincipal(String dirPrincipal) {
		this.dirPrincipal = dirPrincipal;
	}

	/**
	 * Metodo getter del atributo id
	 * @return id del video
	 */
	public int getId() {
		return id;
	}

	/**
	 * Metodo setter del atributo id <b>post: </b> El id del banco ha sido
	 * cambiado con el valor que entra como parametro
	 * @param id - Id del banco
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Metodo getter del atributo nombre
	 * @return nombre del banco
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Metodo setter del atributo nombre 
	 * <b>post: </b> El nombre del banco ha sido
	 * cambiado con el valor que entra como parametro
	 * @param nombre - Nombre del banco
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
