package com.panaderia.ruminahui.util;

public interface ReportGeneratorFunction {
    void generate(ReportGenerator reportGenerator, String filePath) throws Exception;
}