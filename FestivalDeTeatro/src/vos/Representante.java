package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Representante extends Usuario{

	////Atributos

	/**
	 * Id del representante
	 */
	@JsonProperty(value="id")
	private int id;

	/**
	 * Nombre del representante
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	/**
	 * Correo del representante
	 */
	@JsonProperty(value="correo")
	private String correo;
	
	/**
	 * Password del representante
	 */
	@JsonProperty(value="password")
	private String password;
	
	/**
	 * Compania del representante
	 */
	@JsonProperty(value="compania")
	private int id_compania;
	

	/**
	 * Metodo constructor de la clase espectador
	 */
	public Representante(@JsonProperty(value="id")int id, @JsonProperty(value="nombre")String nombre,@JsonProperty(value="correo") String correo, @JsonProperty(value="password") String password, @JsonProperty(value="compania") int id_compania) {
		super(id, nombre, correo, password, "ROL_ADMINISTRADOR");
		this.id = id;
		this.nombre = nombre;
		this.correo = correo;
		this.password = password;
		this.id_compania = id_compania;
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





	public String getCorreo() {
		return correo;
	}





	public void setCorreo(String correo) {
		this.correo = correo;
	}





	public String getPassword() {
		return password;
	}





	public void setPassword(String password) {
		this.password = password;
	}





	public int getCompania() {
		return id_compania;
	}





	public void setCompania(int compania) {
		this.id_compania = compania;
	}

	
	
}
