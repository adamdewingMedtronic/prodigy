package com.mdt.prodigy.intersystems;

import com.mdt.prodigy.service.IProdigyService;
import com.mdt.prodigy.service.ProdigyService;

public class ProdigyBusinessService extends BaseIntersystemsService {

    IProdigyService prodigyService = null;

    public void OnInit() throws Exception {
        prodigyService = new ProdigyService();
    }

    public void OnTearDown() throws Exception {
    }


    public Object OnProcessInput(Object messageInput) throws Exception {
        
        return null;
    }
}
