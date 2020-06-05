package com.mdt.prodigy.intersystems;

import com.mdt.prodigy.service.H2SchemaService;
import com.mdt.prodigy.service.ISChemaService;
import com.mdt.prodigy.service.IntersystemsDBSchemaService;
import org.springframework.stereotype.Service;

@Service
public class IntersystemsSchemaService extends BaseIntersystemsService implements IIntersystemsService{

    private ISChemaService schemaService;

    /**
     * Creates the schema in the database and loads default data.
     * @throws Exception
     */
    public void OnInit() throws Exception{
        if(isRunningAsJavaApp()){
            schemaService = new H2SchemaService();
        }else{
            schemaService = new IntersystemsDBSchemaService();
        }
        schemaService.createSchema();
        schemaService.initializeData();
    }

    /**
     * Does nothing.
     * @param object
     * @return
     * @throws Exception
     */
    public Object ProcessInput(Object object) throws Exception{

        return null;
    }

    /**
     * Does nothing.
     * @throws Exception
     */
    public void OnTearDown() throws Exception{

    }

}
