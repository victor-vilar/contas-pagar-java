package br.com.victorvilar.contaspagar.util;

import br.com.victorvilar.contaspagar.ErpApplication;
import br.com.victorvilar.contaspagar.entities.MovimentoPagamentoParaRelatorio;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class ReportUtil {
    
    private static final String REPORTS_FOLDER = "reports/";
    private static final String JASPER_FILES_SUFIX = ".jasper";
    private static final String PDF_FILES_SUFIX = ".pdf";
    private static final String LOGO_FILES_SUFIX = ".img";
    
    private Map<String,Object> parametros;
    private String pastaDeExportPadrao;
        
    public ReportUtil(){
        parametros = new HashMap<>();
        parametros.put("companyName","Teste");
        parametros.put("nomeSistema","Esse é o meu sistema");
        parametros.put("companyLogo","img/logo.jpg");
        
    }
    
    public  void generate(List<?> datasource, String nomeArquivoJasper, String nomeArquivoDeSaida, Map<String,Object> params){

        try {
            parametros.putAll(params);
            JasperReport report = buscarArquivoJasper(nomeArquivoJasper);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datasource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report,parametros,dataSource);
            exportarRelatorioParaPdf(jasperPrint,nomeArquivoDeSaida);
        } catch (JRException | IllegalArgumentException | IOException | URISyntaxException  e) {
            throw new RuntimeException(e);
        }
    }

    private JasperReport buscarArquivoJasper(String nomeArquivoJasper) throws JRException{
        InputStream stream = getClass().getClassLoader().getResourceAsStream(REPORTS_FOLDER + nomeArquivoJasper + JASPER_FILES_SUFIX );
        return (JasperReport) JRLoader.loadObject(stream);
    }
    
    private void exportarRelatorioParaPdf(JasperPrint relatorio, String nomeDoPdf) throws JRException, IOException, URISyntaxException{
        String pastaAtual = buscarPastaDoJar();
        JasperExportManager.exportReportToPdfFile(relatorio, pastaAtual + "/" + nomeDoPdf + PDF_FILES_SUFIX);
        Desktop.getDesktop().open(new File(pastaAtual + "/" +nomeDoPdf + PDF_FILES_SUFIX));
    }
    
    private String buscarPastaDoJar() throws URISyntaxException{
        Path path = Paths.get(ErpApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        File jarFile = path.toFile();
        File jarDir = jarFile.getParentFile();
        return jarDir.getAbsolutePath();   
    }
    
   

}
