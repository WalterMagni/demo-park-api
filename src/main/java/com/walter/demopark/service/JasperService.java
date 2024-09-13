package com.walter.demopark.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JasperService {

    // Carregador de recursos para obter o arquivo de relatório Jasper.
    private final ResourceLoader resourceLoader;

    // Fonte de dados utilizada para preencher o relatório.
    private final DataSource dataSource;

    // Mapa de parâmetros que será passado para o relatório Jasper.
    private Map<String, Object> params = new HashMap<>();

    // Diretório onde os arquivos Jasper estão localizados.
    private static final String JASPER_DIRECTORY = "classpath:reports/";

    /**
     * Método responsável por adicionar parâmetros ao relatório.
     * Parâmetros padrões como diretório de imagens e localização são adicionados automaticamente.
     *
     * @param key   Nome do parâmetro.
     * @param value Valor do parâmetro.
     */
    public void addParams(String key, Object value) {
        // Adiciona o diretório de imagens e as configurações de localização padrão
        this.params.put("IMAGEM_DIRETORIO", JASPER_DIRECTORY);
        this.params.put("REPORT_LOCALE", new Locale("pt", "BR"));
        // Adiciona o parâmetro passado pelo usuário
        this.params.put(key, value);
    }

    /**
     * Método responsável por gerar o relatório PDF com base no template Jasper.
     *
     * @return Array de bytes contendo o conteúdo do relatório gerado.
     * @throws RuntimeException se houver erro durante a geração do relatório.
     */
    public byte[] generateReport() {
        byte[] bytes = null;
        try {
            // Carrega o arquivo .jasper do diretório especificado
            Resource resource = resourceLoader.getResource(JASPER_DIRECTORY.concat("estacionamentos.jasper"));
            InputStream stream = resource.getInputStream();

            // Preenche o relatório com os dados fornecidos e os parâmetros
            JasperPrint print = JasperFillManager.fillReport(stream, params, dataSource.getConnection());

            // Exporta o relatório preenchido para o formato PDF
            bytes = JasperExportManager.exportReportToPdf(print);
        } catch (IOException | SQLException | JRException e) {
            // Loga o erro e lança uma exceção de runtime caso ocorra algum erro
            log.error("Erro ao gerar relatório: {}", e.getCause());
            throw new RuntimeException(e);
        }
        return bytes;
    }
}

