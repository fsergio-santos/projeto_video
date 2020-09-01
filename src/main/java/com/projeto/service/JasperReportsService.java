package com.projeto.service;

import net.sf.jasperreports.engine.JasperPrint;

public interface JasperReportsService {
	
	JasperPrint imprimeRelatorio(String file);
	
	byte[] imprimeRelatorios(String file);

}
