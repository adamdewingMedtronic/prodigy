package com.mdt.prodigy.service;

import com.mdt.prodigy.Type;
import com.mdt.prodigy.dao.CodeDao;
import com.mdt.prodigy.entity.Code;
import com.mdt.prodigy.entity.CodePK;
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
        loadCodes(OPIOD_CODES_FILENAME, Type.OPIOD_TYPE.getDescription());
    }

    private void loadSleepDisorderCodes(){
        loadCodes(SLEEP_DISORDER_CODES_FILENAME, Type.SLEEP_DISORDER_TYPE.getDescription());
    }

    private void loadHeartFailureCodes(){
        loadCodes(CHRONIC_HEART_FAILURE_CODES_FILENAME, Type.CHRONIC_HEART_FAILURE_TYPE.getDescription());
    }

    private void loadCodes(String filename, String type){
        List<String> codes = LoadCsv.load(filename);
        for(String value : codes){
            log.debug("Saving type:" + type + ", code:" + value);
            Code code = new Code(type, value);
            codeDao.saveOrUpdate(code);
        }
    }

}
