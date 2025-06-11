package org.example.pipeline;

import java.util.ArrayList;
import java.util.List;

public class PipelineTexto {
    public static void main(String[] args) {
        List<String> entrada = new ArrayList<>();
        entrada.add("hola");
        entrada.add("mi");
        entrada.add("mundo");
        entrada.add("es");
        entrada.add("genial");

        System.out.println("Entrada original: " + entrada);

        Pipeline pipeline = new Pipeline();
        pipeline.agregarFiltro(new FiltroEliminarPalabrasCortas());
        pipeline.agregarFiltro(new FiltroConvertirMayusculas());
        pipeline.agregarFiltro(new FiltroAgregarPrefijo("PRE_"));

        List<String> resultado = pipeline.ejecutar(entrada);
        System.out.println("Resultado final: " + resultado);
    }
}