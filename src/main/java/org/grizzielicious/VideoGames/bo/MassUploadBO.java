package org.grizzielicious.VideoGames.bo;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.grizzielicious.VideoGames.dtos.MassUploadResponse;
import org.grizzielicious.VideoGames.entities.Precio;
import org.grizzielicious.VideoGames.entities.Videojuego;
import org.grizzielicious.VideoGames.exceptions.InvalidFileException;
import org.grizzielicious.VideoGames.exceptions.InvalidParameterException;
import org.grizzielicious.VideoGames.exceptions.VideojuegoNotFoundException;
import org.grizzielicious.VideoGames.service.VideojuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.grizzielicious.VideoGames.constants.ValidationsConstants.PRICE_OUT_OF_RANGE;

@Slf4j
@Component
public class MassUploadBO implements Serializable {

    @Autowired
    private VideojuegoService videojuegoService;

    public MassUploadResponse getPreciosFromFile(MultipartFile file) throws InvalidFileException {
        List<Precio> precioList = new ArrayList<>();
        MassUploadResponse response = new MassUploadResponse();
        Precio tmp;
        try{
            this.validateFileType(file);
            log.info("Comenzando análisis de archivo{}", file.getOriginalFilename());
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
                            case 0://nombreVideojuego
                                String nombreVideojuego = celda.getStringCellValue();
                                videojuegoTmp = videojuegoService.encontrarVideojuegoPorNombre(nombreVideojuego)
                                        .orElseThrow(() -> new VideojuegoNotFoundException("No hay ningún videojuego " +
                                                "coincidente con <" + nombreVideojuego + ">"));
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
            response.setPrecios(precioList);
            return response;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InvalidFileException("Error al abrir el archivo. Mensaje original: " + e.getMessage());
        } catch (InvalidFileException e) {
            log.error(e.getMessage());
            throw e;
        }
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
