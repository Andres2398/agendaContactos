package agenda;

public class Contacto {
	int id;
	String nombre;
	String teléfono;
	String email;
	int activo;  // 1 activo ------ 0 no activo
	static int idContador=1;
	
	
	
	public Contacto(String nombre, String teléfono, String email) {

		this.nombre = nombre;
		this.teléfono = teléfono;
		this.email = email;
		id=idContador;
		idContador++;
		activo=1;
	}

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

	public String getTeléfono() {
		return teléfono;
	}

	public void setTeléfono(String teléfono) {
		this.teléfono = teléfono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getActivo() {
		return activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

}
