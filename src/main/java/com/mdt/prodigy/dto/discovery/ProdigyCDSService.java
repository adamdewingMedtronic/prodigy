package com.mdt.prodigy.dto.discovery;

public class ProdigyCDSService extends CDSService{

        public ProdigyCDSService(){
        setId("prodigy");
        setTitle("Prodigy");
        setDescription("Returns a Prodigy score.");
        setHook("patient-view");
        getPrefetch().put("patient", "Patient/{{context.patientId}}");
        getPrefetch().put("medications", "MedicationStatement?patient={{context.patientId}}&status=active,stopped,on-hold&category=outpatient");
        getPrefetch().put("conditions", "Condition?patient={{context.patientId}}&clinical-status=active,resolved,inactive");
        getPrefetch().put("conditionsEnc", "Condition?patient={{context.patientId}}&encounter={{context.encounterId}}&category=encounter-diagnosis");
    }
}
