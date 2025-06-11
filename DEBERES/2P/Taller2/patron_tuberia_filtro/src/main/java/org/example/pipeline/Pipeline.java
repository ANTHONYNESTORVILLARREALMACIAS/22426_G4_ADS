package org.example.pipeline;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
    private List<Filtro> filtros = new ArrayList<>();

    public void agregarFiltro(Filtro filtro) {
        filtros.add(filtro);
    }

    public List<String> ejecutar(List<String> entrada) {
        List<String> resultado = entrada;
        for (Filtro filtro : filtros) {
            resultado = filtro.procesar(resultado);
        }
        return resultado;
    }
}