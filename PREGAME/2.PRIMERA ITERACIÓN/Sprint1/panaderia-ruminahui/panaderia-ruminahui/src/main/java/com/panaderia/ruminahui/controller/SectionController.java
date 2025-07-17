package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Section;
import com.panaderia.ruminahui.model.SectionRepository;
import com.panaderia.ruminahui.util.Validator;

import java.util.List;
import java.util.UUID;

public class SectionController {
    private final SectionRepository sectionRepository;

    public SectionController(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public String createSection(String name, String description) {
        if (!Validator.isValidSectionName(name)) {
            return "El nombre de la sección debe tener al menos 2 caracteres.";
        }
        if (!Validator.isValidSectionDescription(description)) {
            return "La descripción no puede estar vacía.";
        }

        Section section = new Section(UUID.randomUUID().toString(), name, description);
        sectionRepository.save(section);
        return null; // Success
    }

    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    public String updateSection(String id, String name, String description) {
        if (!Validator.isValidSectionName(name)) {
            return "El nombre de la sección debe tener al menos 2 caracteres.";
        }
        if (!Validator.isValidSectionDescription(description)) {
            return "La descripción no puede estar vacía.";
        }

        Section section = sectionRepository.findById(id);
        if (section == null) {
            return "Sección no encontrada.";
        }

        section.setName(name);
        section.setDescription(description);
        sectionRepository.update(section);
        return null; // Success
    }

    public String deleteSection(String id) {
        Section section = sectionRepository.findById(id);
        if (section == null) {
            return "Sección no encontrada.";
        }
        sectionRepository.delete(id);
        return null; // Success
    }
}