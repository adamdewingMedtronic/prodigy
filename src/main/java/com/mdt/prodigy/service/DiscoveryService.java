package com.mdt.prodigy.service;

import com.mdt.prodigy.dto.discovery.CDSServices;
import com.mdt.prodigy.dto.discovery.ProdigyCDSService;

public class DiscoveryService implements IDiscoveryService{

    public CDSServices getDiscoveryEndpoints() {
        CDSServices cdsServices = new CDSServices();
        cdsServices.getServices().add(new ProdigyCDSService());
        return cdsServices;
    }
}
