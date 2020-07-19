package com.mdt.prodigy.controller;

import com.mdt.prodigy.service.IDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cds-services")
public class CDSServicesController {

    @Autowired
    private IDiscoveryService discoveryService;

    @GetMapping(value = "", produces = "application/json")
    public Object get(){
        return discoveryService.getDiscoveryEndpoints();
    }
}
