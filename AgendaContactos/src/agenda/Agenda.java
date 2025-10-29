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
		System.out.println("Buscar por:");
		System.out.println("1. ID");
		System.out.println("2. Nombre");
		String input = sc.nextLine();

		int idBuscar = 0;
		boolean encontrado = false;

		if (input.equals("1")) {
			System.out.println("Introduce el ID:");
			idBuscar = sc.nextInt();
			
		}

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

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Agenda agenda = new Agenda();

		while (true) {
			System.out.println("\n1. Crear contacto");
			System.out.println("2. Buscar contacto");
			String input = sc.nextLine();

			switch (input) {
			case "1":
				agenda.crearContacto();
				break;
			case "2":
				agenda.readContacto();
				break;
			default:
				System.out.println("Opción no válida");
			}
		}
	}
}
