package org.example.pipeline;

import java.util.ArrayList;
import java.util.List;

public class FiltroAgregarPrefijo implements Filtro {
    private String prefijo;

    public FiltroAgregarPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    @Override
    public List<String> procesar(List<String> entrada) {
        List<String> resultado = new ArrayList<>();
        for (String palabra : entrada) {
            resultado.add(prefijo + palabra);
        }
        System.out.println("Después de agregar prefijo: " + resultado);
        return resultado;
    }
}