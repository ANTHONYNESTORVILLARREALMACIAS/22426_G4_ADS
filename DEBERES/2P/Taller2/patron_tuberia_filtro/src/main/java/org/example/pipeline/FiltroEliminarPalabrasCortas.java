package org.example.pipeline;

import java.util.ArrayList;
import java.util.List;

public class FiltroEliminarPalabrasCortas implements Filtro {
    @Override
    public List<String> procesar(List<String> entrada) {
        List<String> resultado = new ArrayList<>();
        for (String palabra : entrada) {
            if (palabra.length() >= 4) {
                resultado.add(palabra);
            }
        }
        System.out.println("Después de eliminar palabras cortas: " + resultado);
        return resultado;
    }
}