package com.mdt.taymyr.service;

import com.mdt.taymyr.dto.discovery.CDSServices;
import com.mdt.taymyr.dto.discovery.ProdigyCDSService;
import org.springframework.stereotype.Service;

@Service
public class DiscoveryService implements IDiscoveryService{


    @Override
    public CDSServices getDiscoveryEndpoints() {
        CDSServices cdsServices = new CDSServices();
        cdsServices.getServices().add(new ProdigyCDSService());
        return cdsServices;
    }
}
