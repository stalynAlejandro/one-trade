package com.pagonxt.onetradefinance.work.expression.common;

import com.flowable.dataobject.engine.DataObjectEngine;
import com.flowable.dataobject.engine.impl.persistence.entity.MasterDataInstanceEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * service class for language code expressions
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.dataobject.engine.DataObjectEngine
 * @since jdk-11.0.13
 */
@Service
public class MasterDataExpressions {

    //class attribute
    private final DataObjectEngine dataObjectEngine;

    /**
     * constructor method
     * @param dataObjectEngine : a DataObjectEngine object
     */
    public MasterDataExpressions(DataObjectEngine dataObjectEngine) {
        this.dataObjectEngine = dataObjectEngine;
    }

    /**
     * Method to get the instance name
     * @param masterDataDefinitionKey : a string with the definition key of the master data
     * @param instanceKey             : a String with the instance key
     * @param language                : a string with the language
     * @return a string with the instance name
     */
    public String getInstanceName(String masterDataDefinitionKey, String instanceKey, String language) {
        return getInstanceText(masterDataDefinitionKey, instanceKey, language, "name");
    }

    /**
     * Method to get the instance text
     * @param masterDataDefinitionKey : a string with the definition key of the master data
     * @param instanceKey             : a String with the instance key
     * @param language                : a string with the language
     * @param key                     : a string with the key
     * @return a string with the instance text
     */
    public String getInstanceText(String masterDataDefinitionKey, String instanceKey, String language, String key) {
        MasterDataInstanceEntity instance = (MasterDataInstanceEntity) dataObjectEngine.getDataObjectRuntimeService()
                .createMasterDataInstanceQuery()
                .definitionKey(masterDataDefinitionKey)
                .key(instanceKey)
                .includeTranslations()
                .singleResult();
        if(instance==null) {
            throw new NoSuchElementException(String.format("Instance %s not found for %s master data definition",
                    instanceKey, masterDataDefinitionKey));
        } else if(!instance.getTranslations().containsKey(language)) {
            throw new NoSuchElementException(String.format("Language %s not found in %s master" +
                    " data %s instance translations", language, masterDataDefinitionKey, instanceKey));
        } else if(!instance.getTranslations().get(language).containsKey(key)) {
            throw new NoSuchElementException(String.format("Key %s not found in %s master" +
                    " data %s instance %s translation", key, masterDataDefinitionKey, instanceKey, language));
        }
        return instance.getTranslations().get(language).get(key);
    }
}
