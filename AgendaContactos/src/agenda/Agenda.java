package agenda;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Agenda {
	File fichero;
	Scanner sc;
	static final int LONGITUD_REGISTRO = 226;

	public Agenda() {
		File f = new File("./files/datos");
		fichero = new File(f.getAbsolutePath());
		sc = new Scanner(System.in);
	}

	public void crearContacto() {
		System.out.println("Introduce el nombre del contacto:");
		String nombre = sc.nextLine();
		System.out.println("Introduce el teléfono:");
		String telefono = sc.nextLine();
		System.out.println("Introduce el email:");
		String email = sc.nextLine();

		int id = 1;
		try (RandomAccessFile file = new RandomAccessFile(fichero, "rw")) {
			long numRegistros = file.length() / LONGITUD_REGISTRO;
			id = (int) (numRegistros + 1);

			long posicion = (id - 1) * LONGITUD_REGISTRO;
			file.seek(posicion);

			// ID
			file.writeInt(id);

			// NOMBRE
			StringBuffer buffer = new StringBuffer(nombre);
			buffer.setLength(50);
			file.writeChars(buffer.toString());

			// TELEFONO
			buffer = new StringBuffer(telefono);
			buffer.setLength(9);
			file.writeChars(buffer.toString());

			// EMAIL
			buffer = new StringBuffer(email);
			buffer.setLength(50);
			file.writeChars(buffer.toString());

			// ACTIVO
			file.writeInt(1);

			System.out.println("Contacto guardado con ID: " + id);
		} catch (IOException e) {
			System.out.println("Error al escribir: " + e.getMessage());
		}
	}

	public void readContacto() {

		int idBuscar = 0;
		boolean encontrado = false;

		System.out.println("Introduce el ID:");
		idBuscar = sc.nextInt();

		try (RandomAccessFile file = new RandomAccessFile(fichero, "r")) {
			long numRegistros = file.length() / LONGITUD_REGISTRO;

			for (int i = 0; i < numRegistros; i++) {
				file.seek(i * LONGITUD_REGISTRO);
				if (!encontrado) {
					int id = file.readInt();

					char[] nombreCar = new char[50];
					for (int j = 0; j < nombreCar.length; j++)
						nombreCar[j] = file.readChar();
					String nombre = new String(nombreCar);

					char[] telefonoCar = new char[9];
					for (int j = 0; j < telefonoCar.length; j++)
						telefonoCar[j] = file.readChar();
					String telefono = new String(telefonoCar);

					char[] emailCar = new char[50];
					for (int j = 0; j < emailCar.length; j++)
						emailCar[j] = file.readChar();
					String email = new String(emailCar);

					int activo = file.readInt();

					if (id == idBuscar && activo == 1) {
						System.out.println("\nID: " + id);
						System.out.println("Nombre: " + nombre);
						System.out.println("Teléfono: " + telefono);
						System.out.println("Email: " + email);
						encontrado = true;

					}
				}
			}

			if (!encontrado) {
				System.out.println("Contacto no encontrado.");
			}
		} catch (IOException e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
	}

	public void update() {
		System.out.println("Introduce el id del contacto que quieres cambiar");
		int id = sc.nextInt();
		if (sc.hasNextLine()) {
			sc.nextLine();
		}
		try (RandomAccessFile file = new RandomAccessFile(fichero, "rw")) {

			long numRegistros = file.length() / LONGITUD_REGISTRO;
			for (int i = 0; i < numRegistros; i++) {
				file.seek(i * LONGITUD_REGISTRO);
				int idFichero = file.readInt();
				if (id == idFichero) {
					if (cambiar("nombre")) {
						System.out.println("Introduce el nombre");
						String nombre = sc.nextLine();
						StringBuffer buffer = new StringBuffer(nombre);
						buffer.setLength(50);
						file.writeChars(buffer.toString());
					} else {
						file.seek(file.getFilePointer() + 100);
					}
					if (cambiar("Telefono")) {
						System.out.println("Introduce el telefono");
						String telefono = sc.nextLine();
						StringBuffer buffer = new StringBuffer(telefono);
						buffer.setLength(9);
						file.writeChars(buffer.toString());
					} else {
						file.seek(file.getFilePointer() + 18);
					}
					if (cambiar("email")) {
						System.out.println("Introduce el email");
						String email = sc.nextLine();
						StringBuffer buffer = new StringBuffer(email);
						buffer.setLength(50);
						file.writeChars(buffer.toString());
					} else {
						file.seek(file.getFilePointer() + 100);
					}
				}
			}

		} catch (IOException e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
	}

	private boolean cambiar(String string) {

		System.out.println("Quieres cambiar el " + string + " del contacto: (S/N)");
		String input = sc.nextLine();

		return input.equalsIgnoreCase("s");
	}

	public void delete() {
		System.out.println("Introduce el id del contacto que quieres borrar");
		int id = sc.nextInt();
		if (sc.hasNextLine()) {
			sc.nextLine();
		}
		
		try (RandomAccessFile file = new RandomAccessFile(fichero, "rw")) {

			long numRegistros = file.length() / LONGITUD_REGISTRO;
			for (int i = 0; i < numRegistros; i++) {
				file.seek(i * LONGITUD_REGISTRO);
				int idFichero = file.readInt();
				if (id == idFichero) {
					file.seek(file.getFilePointer() + 218);
					file.writeInt(0); 
					
					System.out.println("Se ha borrado el contacto con el id: " + idFichero);
				}

			}
		} catch (IOException e) {
			System.out.println("Error al leer: " + e.getMessage());
		}

	}

	public void mostrarTodos() {
		try (RandomAccessFile file = new RandomAccessFile(fichero, "rw")) {

			long numRegistros = file.length() / LONGITUD_REGISTRO;
			for (int i = 0; i < numRegistros; i++) {
				file.seek(i * LONGITUD_REGISTRO);
				int idFichero = file.readInt();

				char[] nombreCar = new char[50];
				for (int j = 0; j < nombreCar.length; j++)
					nombreCar[j] = file.readChar();
				String nombre = new String(nombreCar);

				char[] telefonoCar = new char[9];
				for (int j = 0; j < telefonoCar.length; j++)
					telefonoCar[j] = file.readChar();
				String telefono = new String(telefonoCar);

				char[] emailCar = new char[50];
				for (int j = 0; j < emailCar.length; j++)
					emailCar[j] = file.readChar();
				String email = new String(emailCar);

				int activo = file.readInt();
				
				if (activo == 1) {
					System.out.println("ID: " + idFichero);
					System.out.println("nombre : " + nombre);
					System.out.println("telefono : " + telefono );
					System.out.println("email : " + email);
					System.out.println();
				}

			}
		} catch (IOException e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Agenda agenda = new Agenda();

		while (true) {
			System.out.println("\n1. Crear contacto");
			System.out.println("2. Buscar contacto");
			System.out.println("3. actualizar contacto");
			System.out.println("4. borrar contacto");
			System.out.println("5. mostrar todos los contactos");
			String input = sc.nextLine();

			switch (input) {
			case "1":
				agenda.crearContacto();
				break;
			case "2":
				agenda.readContacto();
				break;
			case "3":
				agenda.update();
				break;
			case "4":
				agenda.delete();
				break;
			case "5":
				agenda.mostrarTodos();
				break;
			default:
				System.out.println("Opción no válida");
			}
		}
	}

}
