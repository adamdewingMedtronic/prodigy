package com.mdt.prodigy;

import com.mdt.prodigy.service.H2SchemaService;
import com.mdt.prodigy.service.IProdigyService;
import com.mdt.prodigy.service.ISChemaService;
import com.mdt.prodigy.service.ProdigyService;

public class ProdigyApplication {

	public static void main(String[] args) {
		IProdigyService prodigyService = new ProdigyService();

//		ISChemaService schemaService = new H2SchemaService();
//		schemaService.initializeData();
	}

}
