package com.mdt.taymyr.service;

import com.mdt.taymyr.dto.discovery.CDSServices;

public interface IDiscoveryService {

    /**
     * Finds all CDSServices available by this micro-service.
     * @return
     */
    public CDSServices getDiscoveryEndpoints();
}
