package com.mdt.prodigy.intersystems;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This service gets the build version of the jar file and logs it.
 */
@Slf4j
public class VersionService extends BaseIntersystemsService {
    private static final String FILE_NAME = "taymyr.git.properties";
    private static Properties properties = new Properties();



    @Override
    public void OnInit() throws Exception{
        super.OnInit();
        InputStream inputStream = VersionService.class.getClassLoader().getResourceAsStream(FILE_NAME);
        if(inputStream == null){
            log.info("Could not find file " + FILE_NAME + " from the classpath");
        }else{
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                log.error("Could not load file " + FILE_NAME, e);
            }
        }
        LOGINFO("Verion: " + properties.getProperty("git.build.version") + ", build time: " + properties.getProperty("git.build.time"));
    }

}
