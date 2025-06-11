package org.example.pipeline;

import java.util.List;

public interface Filtro {
    List<String> procesar(List<String> entrada);
}