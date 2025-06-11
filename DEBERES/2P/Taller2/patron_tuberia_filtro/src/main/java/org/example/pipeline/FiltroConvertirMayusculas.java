package org.example.pipeline;

import java.util.ArrayList;
import java.util.List;

public class FiltroConvertirMayusculas implements Filtro {
    @Override
    public List<String> procesar(List<String> entrada) {
        List<String> resultado = new ArrayList<>();
        for (String palabra : entrada) {
            resultado.add(palabra.toUpperCase());
        }
        System.out.println("Después de convertir a mayúsculas: " + resultado);
        return resultado;
    }
}