package com.pagonxt.onetradefinance.work.service;

import com.flowable.core.content.api.CoreContentService;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.model.Office;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides some methods to parse documents and perform different operations against DataObject
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.core.content.api.CoreContentService
 * @see com.flowable.dataobject.api.runtime.DataObjectRuntimeService
 * @since jdk-11.0.13
 */
@Service
public class DocumentParserService {

    private static final String DEFINITION_KEY = "PGN_DO001";
    private static final String FIND_BY_ID = "findById";
    private static final String OFFICE_CODE = "officeCode";
    private static final String MIDDLE_OFFICE_CODE = "middleOfficeCode";
    private static final String COUNTRY = "country";
    private static final String ADDRESS = "address";
    private static final String PLACE = "place";
    private static final String EMAIL = "email";
    private static final String REGISTRATION_DATE = "registrationDate";
    private static final String REGISTER_UPDATE_DATE = "registerUpdateDate";
    private static final String PARSE_OFFICE = "parseOffice";
    private static final String DEREGISTRATION_DATE = "deregistrationDate";

    private DataObjectInstanceVariableContainerQuery dataObjectQueryOffice;
    private DataObjectInstanceVariableContainer officeExists;

    //member variables
    private final CoreContentService coreContentService;
    private final DataObjectRuntimeService dataObjectRuntimeService;

    /**
     * Constructor method
     * @param coreContentService        : CoreContentService object
     * @param dataObjectRuntimeService  : DataObjectRuntimeService object
     */
    public DocumentParserService(CoreContentService coreContentService,
                                 DataObjectRuntimeService dataObjectRuntimeService){
        this.coreContentService = coreContentService;
        this.dataObjectRuntimeService = dataObjectRuntimeService;
    }

    /**
     * Method to parse a document with offices
     * @param contentItemId : a String with the contentItemId
     */
    @Transactional
    public void parseOffice( String contentItemId) throws IOException {

        InputStream inputStream = coreContentService.getContentItemData(contentItemId);
        if (inputStream == null) {
            throw new ResourceNotFoundException("Content item with id '" + contentItemId +
                    "' doesn't have nothing associated with it.", PARSE_OFFICE);
        }

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)) ){

                List<String> lines = reader.lines().collect(Collectors.toList());
                List<String> joinedLines = joinSplitLines(lines);

                List<Office> offices = new ArrayList<>();

                for(String line : joinedLines){
                    Office office =  readOffice(line);
                    if(office != null){
                        offices.add(office);
                    }
                }

                for(Office office : offices){
                    addOrUpdateOffice(office);
                }

                deleteOffices(offices);

            }

    }

    private List<String> joinSplitLines(List<String> lines){
        List<String> joinedLines = new ArrayList<>();
        StringBuilder lineAux = null;
        for (String line : lines) {
            if (line.length() < 235) {
                lineAux = new StringBuilder(line);
            } else {
                if (lineAux != null) {
                    lineAux.append(line);
                    joinedLines.add(lineAux.toString());
                    lineAux = null;
                } else {
                    joinedLines.add(line);
                }
            }

        }
        return joinedLines;
    }

    /**
     * Method to parse a document with offices
     * @param contentItemId : a String with the contentItemId
     */
    @Transactional
    public void parseMiddleOffice( String contentItemId) throws IOException {

        InputStream inputStream = coreContentService.getContentItemData(contentItemId);


            if (inputStream == null) {
                throw new ResourceNotFoundException("Content item with id '" + contentItemId +
                        "' doesn't have content associated with it.", PARSE_OFFICE);
            }

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String result = reader.lines().parallel().collect(Collectors.joining("\n"));
            String[] lines = result.split("\n");

            for (String line : lines) {
                if (line.length() > 16 && line.length() < 50) {
                    updateMiddleOffice(line);
                }
            }
        }

    }

    /**
     * Method to get a line and take necessary data to build an office object
     * When the object is built, this method calls method to create or update de register in dataObject
     * @param line : a string with the line content
     */
    private Office readOffice(String line){

        Office office = null;

        if (line.startsWith("0049")) {
            office = new Office();
            office.setCountry("ES");
            office.setOfficeId(line.substring(4, 8));
            office.setAddress(line.substring(62, 141).trim() + ", " + line.substring(135, 185).trim());
            office.setPlace(line.substring(202, 232).trim());
            office.setEmail(line.substring(384).trim());
        }
        return office;
    }

    private void addOrUpdateOffice(Office office){
        dataObjectQueryOffice = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY).operation(FIND_BY_ID).value(OFFICE_CODE, office.getOfficeId());
        officeExists = dataObjectQueryOffice.singleResult();
        if (officeExists == null) {
            createOffice(office);
        } else {
            updateOffice(office);
        }
    }

    /**
     * Method to create a new register in dataObject
     * @param office
     */
    private void createOffice(Office office) {

        dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey(DEFINITION_KEY)
                .value(COUNTRY, office.getCountry())
                .value(OFFICE_CODE, office.getOfficeId())
                .value(ADDRESS, office.getAddress())
                .value(PLACE, office.getPlace())
                .value(EMAIL, office.getEmail())
                .value(REGISTRATION_DATE, new Date())
                .value(DEREGISTRATION_DATE,null)
                .create();
    }

    /**
     * Method to update a register in dataObject
     * @param office
     */
    private void updateOffice(Office office) {

        dataObjectRuntimeService.createDataObjectModificationBuilder().definitionKey(DEFINITION_KEY)
                .lookupId(office.getOfficeId())
                .value(OFFICE_CODE, office.getOfficeId())
                .value(COUNTRY, office.getCountry())
                .value(ADDRESS, office.getAddress())
                .value(PLACE, office.getPlace())
                .value(EMAIL, office.getEmail())
                .value(REGISTER_UPDATE_DATE, new Date())
                .value(DEREGISTRATION_DATE,null)
                .modify();
    }

    /**
     * Method to update a register in dataObject
     */
    private void updateMiddleOffice(String line) {

        dataObjectQueryOffice = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY).operation(FIND_BY_ID).value(OFFICE_CODE,  line.substring(4,8));
        officeExists = dataObjectQueryOffice.singleResult();
        if( officeExists != null) {
            dataObjectRuntimeService.createDataObjectModificationBuilder().definitionKey(DEFINITION_KEY)
                    .lookupId(line.substring(4,8))
                    .value(MIDDLE_OFFICE_CODE,line.substring(20,24))
                    .value(REGISTER_UPDATE_DATE, new Date())
                    .modify();
        }
    }

    /**
     * Method to delete a register in dataObject
     */
    private void deleteOffices(List<Office> offices) {

        dataObjectQueryOffice = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY).operation("findCountry").value(COUNTRY, "");
        List<DataObjectInstanceVariableContainer> results = dataObjectQueryOffice.list();
        List<String> officesDO = new ArrayList<>();
        if(!results.isEmpty()){
            for (DataObjectInstanceVariableContainer result : results) {
                officesDO.add(result.getString(OFFICE_CODE));
            }
        }

        List<String> officesToDelete = new ArrayList<>();
        String officeToAddToDeleteList = null;

        for (String officeDo : officesDO) {
            for (Office office : offices) {
                if (!officeDo.equals(office.getOfficeId())) {
                    officeToAddToDeleteList = officeDo;
                } else {
                    officeToAddToDeleteList = null;
                    break;
                }
            }
            if(officeToAddToDeleteList != null){
                officesToDelete.add(officeToAddToDeleteList);
            }
        }

        for (String officeId : officesToDelete) {
            dataObjectRuntimeService.createDataObjectModificationBuilder().definitionKey(DEFINITION_KEY)
                    .lookupId(officeId)
                    .value(DEREGISTRATION_DATE, new Date())
                    .modify();
        }

    }
}
