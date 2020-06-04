package com.mdt.prodigy.service;

import com.mdt.prodigy.dto.discovery.CDSServices;

public interface IDiscoveryService {

    /**
     * Finds all CDSServices available by this micro-service.
     * @return
     */
    public CDSServices getDiscoveryEndpoints();
}
