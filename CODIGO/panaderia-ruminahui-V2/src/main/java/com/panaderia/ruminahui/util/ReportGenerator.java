package com.panaderia.ruminahui.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.panaderia.ruminahui.model.*;

import java.io.File;
import java.util.List;

public class ReportGenerator {
    private final StockRepository stockRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;
    private final ProduccionRepository produccionRepository;
    private final RecetaRepository recetaRepository;
    private final SeccionRepository seccionRepository;
    private final DetalleRecetaRepository detalleRecetaRepository;

    public ReportGenerator() {
        this.stockRepository = new StockRepository();
        this.materiaPrimaRepository = new MateriaPrimaRepository();
        this.produccionRepository = new ProduccionRepository();
        this.recetaRepository = new RecetaRepository();
        this.seccionRepository = new SeccionRepository();
        this.detalleRecetaRepository = new DetalleRecetaRepository();
    }

    public void generateSeccionReport(String filePath) throws Exception {
        PdfWriter writer = new PdfWriter(new File(filePath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Panadería Rumiñahui - Reporte de Secciones")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        float[] columnWidths = {100, 200, 300};
        Table table = new Table(columnWidths);
        table.addHeaderCell("ID");
        table.addHeaderCell("Nombre");
        table.addHeaderCell("Descripción");

        List<Seccion> secciones = seccionRepository.findAll();
        for (Seccion seccion : secciones) {
            table.addCell(seccion.getId());
            table.addCell(seccion.getNombre());
            table.addCell(seccion.getDescripcion());
        }

        document.add(table);
        document.close();
    }

    public void generateMateriaPrimaReport(String filePath) throws Exception {
        PdfWriter writer = new PdfWriter(new File(filePath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Panadería Rumiñahui - Reporte de Materias Primas")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        float[] columnWidths = {100, 150, 200, 100, 100, 100};
        Table table = new Table(columnWidths);
        table.addHeaderCell("ID");
        table.addHeaderCell("Nombre");
        table.addHeaderCell("Descripción");
        table.addHeaderCell("Stock Mínimo");
        table.addHeaderCell("Unidad");
        table.addHeaderCell("Sección");

        List<MateriaPrima> materias = materiaPrimaRepository.findAll();
        for (MateriaPrima materia : materias) {
            Seccion seccion = seccionRepository.findById(materia.getIdSeccion());
            table.addCell(materia.getId());
            table.addCell(materia.getNombre());
            table.addCell(materia.getDescripcion());
            table.addCell(String.valueOf(materia.getStockMinimo()));
            table.addCell(materia.getUnidadMedida());
            table.addCell(seccion != null ? seccion.getNombre() : materia.getIdSeccion());
        }

        document.add(table);
        document.close();
    }

    public void generateStockReport(String filePath) throws Exception {
        PdfWriter writer = new PdfWriter(new File(filePath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Panadería Rumiñahui - Reporte de Stock")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        float[] columnWidths = {200, 100, 100, 100};
        Table table = new Table(columnWidths);
        table.addHeaderCell("Materia Prima");
        table.addHeaderCell("Cantidad");
        table.addHeaderCell("Unidad");
        table.addHeaderCell("Fecha");

        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock : stocks) {
            MateriaPrima materia = materiaPrimaRepository.findById(stock.getIdMateria());
            table.addCell(materia != null ? materia.getNombre() : stock.getIdMateria());
            table.addCell(String.valueOf(stock.getCantidad()));
            table.addCell(stock.getUnidadMedida());
            table.addCell(stock.getFecha());
        }

        document.add(table);
        document.close();
    }

    public void generateRecetaReport(String filePath) throws Exception {
        PdfWriter writer = new PdfWriter(new File(filePath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Panadería Rumiñahui - Reporte de Recetas")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        float[] columnWidths = {100, 200, 300};
        Table table = new Table(columnWidths);
        table.addHeaderCell("ID");
        table.addHeaderCell("Nombre");
        table.addHeaderCell("Descripción");

        List<Receta> recetas = recetaRepository.findAll();
        for (Receta receta : recetas) {
            table.addCell(receta.getId());
            table.addCell(receta.getNombre());
            table.addCell(receta.getDescripcion());
        }

        document.add(table);
        document.close();
    }

    public void generateDetalleRecetaReport(String filePath) throws Exception {
        PdfWriter writer = new PdfWriter(new File(filePath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Panadería Rumiñahui - Reporte de Detalles de Receta")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        float[] columnWidths = {100, 150, 150, 100, 100};
        Table table = new Table(columnWidths);
        table.addHeaderCell("ID");
        table.addHeaderCell("Receta");
        table.addHeaderCell("Materia Prima");
        table.addHeaderCell("Cantidad");
        table.addHeaderCell("Unidad");

        List<DetalleReceta> detalles = detalleRecetaRepository.findAll();
        for (DetalleReceta detalle : detalles) {
            Receta receta = recetaRepository.findById(detalle.getIdReceta());
            MateriaPrima materia = materiaPrimaRepository.findById(detalle.getIdMateria());
            table.addCell(detalle.getId());
            table.addCell(receta != null ? receta.getNombre() : detalle.getIdReceta());
            table.addCell(materia != null ? materia.getNombre() : detalle.getIdMateria());
            table.addCell(String.valueOf(detalle.getCantidad()));
            table.addCell(detalle.getUnidadMedida());
        }

        document.add(table);
        document.close();
    }

    public void generateProductionReport(String filePath) throws Exception {
        PdfWriter writer = new PdfWriter(new File(filePath));
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Panadería Rumiñahui - Reporte de Producción")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        float[] columnWidths = {200, 100, 100};
        Table table = new Table(columnWidths);
        table.addHeaderCell("Receta");
        table.addHeaderCell("Cantidad");
        table.addHeaderCell("Fecha");

        List<Produccion> producciones = produccionRepository.findAll();
        for (Produccion produccion : producciones) {
            Receta receta = recetaRepository.findById(produccion.getIdReceta());
            table.addCell(receta != null ? receta.getNombre() : produccion.getIdReceta());
            table.addCell(String.valueOf(produccion.getCantidad()));
            table.addCell(produccion.getFecha());
        }

        document.add(table);
        document.close();
    }
}