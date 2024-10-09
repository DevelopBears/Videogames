package org.grizzielicious.VideoGames.bo;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.grizzielicious.VideoGames.dtos.MassUploadResponse;
import org.grizzielicious.VideoGames.entities.*;
import org.grizzielicious.VideoGames.exceptions.InvalidFileException;
import org.grizzielicious.VideoGames.exceptions.InvalidParameterException;
import org.grizzielicious.VideoGames.exceptions.VideojuegoNotFoundException;
import org.grizzielicious.VideoGames.service.VideojuegoService;
import org.grizzielicious.VideoGames.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

import static org.grizzielicious.VideoGames.constants.ValidationsConstants.PRICE_OUT_OF_RANGE;

@Slf4j
@Component
public class MassUploadBO implements Serializable {

    @Autowired
    private VideojuegoService videojuegoService;

    public MassUploadResponse<Precio> getPreciosFromFile(MultipartFile file) throws InvalidFileException {
        List<Precio> precioList = new ArrayList<>();
        MassUploadResponse<Precio> response = new MassUploadResponse<>();
        Precio tmp;
        try{
            this.validateFileType(file);
            log.info("Comenzando análisis de archivo de precios {}", file.getOriginalFilename());
            InputStream fis = file.getInputStream();
            Workbook libro = new XSSFWorkbook(fis);
            Sheet hoja = libro.getSheetAt(0);
            Cell celda;
            Videojuego videojuegoTmp;
            log.info("Total de filas detectadas: {}", hoja.getLastRowNum());
            for(Row fila : hoja) {
                if(fila.getRowNum() == 0 || this.checkIfRowIsEmpty(fila)) {
                    continue;
                }
                try {
                    Iterator<Cell> cellIterator = fila.cellIterator();
                    tmp = new Precio();

                    while(cellIterator.hasNext()) {
                        celda = cellIterator.next();
                        switch (celda.getColumnIndex()) {
                            case 0://nombre o id de Videojuego
                                String nombreOIdVideojuego = celda.getStringCellValue();
                                if(CommonUtils.isIntegerParseable(nombreOIdVideojuego)) {
                                    videojuegoTmp = videojuegoService
                                            .encontrarPorId(Integer.parseInt(nombreOIdVideojuego))
                                            .orElseThrow(() -> new VideojuegoNotFoundException("No existe el " +
                                                    "videojuego con Id <" + nombreOIdVideojuego + ">"));
                                } else {
                                    videojuegoTmp = videojuegoService
                                            .encontrarVideojuegoPorNombre(nombreOIdVideojuego)
                                            .orElseThrow(() -> new VideojuegoNotFoundException("No existe el " +
                                                    "videojuego con nombre <" + nombreOIdVideojuego + ">"));
                                }
                                tmp.setVideojuego(videojuegoTmp);
                                break;
                            case 1:
                                float precio = (float) celda.getNumericCellValue();
                                if(precio < 0 || precio > 8000) {
                                    throw new InvalidParameterException(PRICE_OUT_OF_RANGE);
                                }
                                tmp.setPrecioUnitario(precio);
                                break;
                            case 2:
                                tmp.setFechaInicioVigencia(celda.getLocalDateTimeCellValue());
                                break;
                            case 3:
                                if(!celda.getCellType().equals(CellType.STRING) ||
                                        !celda.getStringCellValue().equalsIgnoreCase("null")) {
                                    tmp.setFechaFinVigencia(celda.getLocalDateTimeCellValue().plusDays(1).minusSeconds(1));
                                }
                                break;
                        }
                    }
                    log.info("Precio cargado en memoria [{} : ${} : {} -> {}] ",
                            tmp.getVideojuego().getNombreVideojuego(), tmp.getPrecioUnitario(),
                            tmp.getFechaInicioVigencia(), tmp.getFechaFinVigencia());
                    precioList.add(tmp);
                    response.aumentaRegistrosAceptados();
                } catch (Exception e) {
                    response.aumentaRegistrosErroneos();
                    log.error("Excepción al procesar la fila {}. Se procede a saltar la fila. Mensaje original: {}",
                            fila.getRowNum(), e.getMessage());
                }
            }
            log.info("Procesamiento de archivo terminado. Se procesaron correctamente {} registros y fallaron {}",
                    response.getRegistrosAceptados(),response.getRegistrosErroneos());
            libro.close();
            response.setListaAceptados(precioList);
            return response;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InvalidFileException("Error al abrir el archivo. Mensaje original: " + e.getMessage());
        } catch (InvalidFileException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public MassUploadResponse<Videojuego> getVideojuegosFromFile(MultipartFile file) throws InvalidFileException {
        MassUploadResponse<Videojuego> response = new MassUploadResponse<>();
        List<Videojuego> videojuegoList = new ArrayList<>();
        Videojuego tmp;
        List<Plataforma> plataformasTmp;
        try {
            this.validateFileType(file);
            log.info("Comenzando análisis de archivo de videojuegos: {}", file.getOriginalFilename());
            InputStream fis = file.getInputStream();
            Workbook libro = new XSSFWorkbook(fis);
            Sheet hoja = libro.getSheetAt(0);
            Cell celda;
            Map<Integer, String> nombresPlataforma = getPlatformNames(hoja.getRow(0));
            log.info("Plataformas obtenidas: {}", nombresPlataforma );
            log.info("Total de videojuegos detectados: {}", hoja.getLastRowNum()-1);
            for( Row fila : hoja ) {
                if(fila.getRowNum() == 0 || this.checkIfRowIsEmpty(fila)) {
                    continue;
                }
                try {
                    Iterator<Cell> cellIterator = fila.cellIterator();
                    tmp = new Videojuego();
                    plataformasTmp = new ArrayList<>();
                    while(cellIterator.hasNext()) {
                        celda = cellIterator.next();
                        switch(celda.getColumnIndex()) {
                            case 0://videojuego
                                tmp.setNombreVideojuego( celda.getStringCellValue() );
                                break;
                            case 1://año lanzamiento
                                tmp.setAnioLanzamiento((int) celda.getNumericCellValue());
                                break;
                            case 2://Multijugador
                                tmp.setEsMultijugador( celda.getStringCellValue().equalsIgnoreCase("Si") );
                                break;
                            case 3:
                                tmp.setEstudioDesarrollador(
                                        Estudio.builder()
                                                .estudio(celda.getStringCellValue() )
                                                .build()
                                );
                                break;
                            case 4:
                                tmp.setGenero(
                                        Genero.builder()
                                                .descripcion( celda.getStringCellValue() )
                                                .build()
                                );
                                break;
                            default:
                                if(celda.getStringCellValue().equalsIgnoreCase("si")) {
                                    plataformasTmp.add(
                                            Plataforma.builder()
                                                    .plataforma( nombresPlataforma.get(celda.getColumnIndex()) )
                                                    .build()
                                    );
                                }
                        }
                    }
                    if(plataformasTmp.isEmpty()) {
                        throw new InvalidParameterException("El videojuego <" + tmp.getNombreVideojuego()
                                + "> no tiene asignada ninguna plataforma. Saltando registro");
                    }
                    tmp.setPlataformas(plataformasTmp);
                    videojuegoList.add(tmp);
                    response.aumentaRegistrosAceptados();
                } catch (Exception e) {
                    response.aumentaRegistrosErroneos();
                    log.error("Excepción capturada al procesar fila {}. Ignorando registro. Mensaje original: {}",
                            fila.getRowNum(), e.getMessage());
                }
            }
            log.info("Procesamiento de layout terminado. Se procesaron correctamente {} registros y fallaron {}",
                    response.getRegistrosAceptados(), response.getRegistrosErroneos());
            libro.close();
            response.setListaAceptados(videojuegoList);
            return response;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InvalidFileException("Excepción capturada al abrir archivo. Mensaje original: " + e.getMessage());
        } catch (InvalidFileException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private Map<Integer, String> getPlatformNames(Row row) throws InvalidFileException {
        Map<Integer, String> platforms = new HashMap<>();
        if(Objects.isNull(row) || row.getLastCellNum() <= 0) {
            throw new InvalidFileException("El layout no tiene los encabezados requeridos");
        }
        Cell cell;
        for(int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            cell = row.getCell(cellNum);
            if(cell.getColumnIndex() < 5) {//NombreVideojuego -> 0 ----> Género -> 4
                continue;
            }
            platforms.put(cellNum, cell.getStringCellValue());
        }
        return platforms;
    }

    private boolean checkIfRowIsEmpty (Row row) {
        if(Objects.isNull(row) || row.getLastCellNum() <= 0){
            return true;
        }
        boolean isEmpty = false;
        Cell cell;
        for(int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            cell = row.getCell(cellNum);
            if(Objects.isNull(cell) || cell.toString().isBlank()) {
                isEmpty = true;
                break;
            }
        }
        return isEmpty;
    }

    public void validateFileType(MultipartFile file) throws InvalidFileException {
        boolean isValid = true;
        String reason = "Tipo de archivo inválid. ";
        if(Objects.isNull(file) || Objects.isNull(file.getOriginalFilename())) {
            isValid = false;
            reason += "El archivo es nulo";
        } else if (!file.getOriginalFilename().contains(".")) {
            isValid = false;
            reason += "No se puede determinar la extensión del archivo";
        } else {
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            log.info("Extensión de archivo recibido: <{}>", extension);
            if(!extension.equalsIgnoreCase(".xlsx")) {
                isValid = false;
                reason += "El tipo de archivo no está permitido. El tipo de archivo debe de ser XLSX";
            }
        }
        if (!isValid) {
            throw new InvalidFileException(reason);
        }
    }
}
