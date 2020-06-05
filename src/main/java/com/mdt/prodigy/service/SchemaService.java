package com.mdt.prodigy.service;

import com.mdt.prodigy.dao.CodeDao;
import com.mdt.prodigy.entity.Code;
import com.mdt.prodigy.util.HibernateUtil;
import com.mdt.prodigy.util.LoadCsv;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public abstract class SchemaService implements ISChemaService {

    public static final String OPIOD_CODES_FILENAME = "opiodCodes.csv";
    public static final String CHRONIC_HEART_FAILURE_CODES_FILENAME = "heartFailureCodes.csv";
    public static final String SLEEP_DISORDER_CODES_FILENAME = "sleepDisorderCodes.csv";

    public static final String OPIOD_TYPE = "OPIOD";
    public static final String SLEEP_DISORDER_TYPE = "SD";
    public static final String CHRONIC_HEART_FAILURE_TYPE = "CHF";

    private CodeDao codeDao = new CodeDao();

    @Override
    public void initializeData() {
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        loadHeartFailureCodes();
        loadOpiodCodes();
        loadSleepDisorderCodes();
        transaction.commit();
    }

    private void loadOpiodCodes(){
        loadCodes(OPIOD_CODES_FILENAME, OPIOD_TYPE);
    }

    private void loadSleepDisorderCodes(){
        loadCodes(SLEEP_DISORDER_CODES_FILENAME, SLEEP_DISORDER_TYPE);
    }

    private void loadHeartFailureCodes(){
        loadCodes(CHRONIC_HEART_FAILURE_CODES_FILENAME, CHRONIC_HEART_FAILURE_TYPE);
    }

    private void loadCodes(String filename, String type){
        List<String> codes = LoadCsv.load(filename);
        for(String code : codes){
            log.debug("Saving type:" + type + ", code:" + code);
            codeDao.saveOrUpdate(new Code(type, code));
        }
    }

}
