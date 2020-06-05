package com.mdt.prodigy.controller;

import com.mdt.prodigy.dto.request.CDSServiceRequest;
import com.mdt.prodigy.intersystems.IntersystemsSchemaService;
import com.mdt.prodigy.service.IDiscoveryService;
import com.mdt.prodigy.service.IProdigyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/cds-services/prodigy")
public class ProdigyController {

    @Autowired
    IProdigyService prodigyService;

    @Autowired
    IntersystemsSchemaService schemaService;

    @PostMapping(value = "", produces = "application/json")
    public Object post(@RequestBody CDSServiceRequest request){
        return prodigyService.getORIDCards(request);
    }

    @PostMapping(value="/initialize")
    public void post() throws Exception {
        schemaService.OnInit();
    }

}
