package ar.edu.utn.frba.dds.utilidades;

import ar.edu.utn.frba.dds.excepcionesPersonalizadas.dominio.GuardarArchivoException;
import ar.edu.utn.frba.dds.utilidades.CSV.ResultadoValidacion;
import io.javalin.http.UploadedFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GestorArchivos {

    public static void subirArchivo(UploadedFile uploadedFile, String path) {

        Path destination = Paths.get(path);



        try {
            // Obtén el InputStream del archivo subido
            InputStream uploadedFileStream = uploadedFile.content();

            // Copia el contenido del InputStream al archivo de destino
            Files.copy(uploadedFileStream, destination);

            // Cierra el InputStream
            uploadedFileStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static ResultadoValidacion validarArchivoCSV(UploadedFile archivoCSV) {
        if (archivoCSV == null) {
            return new ResultadoValidacion(false, "No se ha subido ningún archivo. Intente nuevamente.");
        }

        // Validación de la extensión del archivo
        if (!archivoCSV.filename().toLowerCase().endsWith(".csv")) {
            return new ResultadoValidacion(false, "El archivo subido no es un archivo CSV. Por favor, suba un archivo con extensión .csv.");
        }

        // Validación de tipo MIME
        String mimeType = archivoCSV.contentType();
        if (!mimeType.equals("text/csv") && !mimeType.equals("application/vnd.ms-excel")) {
            return new ResultadoValidacion(false, "El tipo MIME del archivo no corresponde a un CSV válido.");
        }

        return new ResultadoValidacion(true, "El archivo es válido.");
    }

    public static File uploadedFileToFileConverter(UploadedFile uf) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        //Add you expected file encoding here:
        System.setProperty("file.encoding", "UTF-8");
        File newFile = new File(uf.filename());
        try {
            inputStream = uf.content();
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            throw new GuardarArchivoException("Error al modificar archivo");
        }
        return newFile;
    }
}
