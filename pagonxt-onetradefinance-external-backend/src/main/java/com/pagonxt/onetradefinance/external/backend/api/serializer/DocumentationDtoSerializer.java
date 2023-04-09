package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.FileDto;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Component;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has methods to convert DTO's into entities and viceversa, for documentation
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class DocumentationDtoSerializer {

    /**
     * Class variables
     */
    private final DateFormat dateTimeFormat;

    private static final FileNameMap FILE_NAME_MAP = URLConnection.getFileNameMap();

    /**
     * Class constructor
     * @param dateFormatProperties In this case, we use this object to get the date format
     */
    public DocumentationDtoSerializer(DateFormatProperties dateFormatProperties) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();
    }

    /**
     * This method extracts documents from a PagoNxt request object and builds a documentDto object with documents
     * @param from request
     * @see com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest
     * @return documentDto object
     */
    public DocumentationDto mapDocumentationDto(PagoNxtRequest from) {
        return mapDocumentsAndPriority(from.getDocuments(), from.getPriority());
    }

    /**
     * This method extracts documents from a trade request object and builds a documentDto object with documents
     * @param from request
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     * @return documentDto object
     */
    public DocumentationDto mapDocumentationTradeDto(TradeRequest from) {
        return mapDocumentsAndPriority(from.getDocuments(), from.getPriority());
    }

    public DocumentationDto mapDocumentsAndPriority(List<Document> documents, String priority) {
        DocumentationDto to = new DocumentationDto();
        if (documents != null) {
            for (Document document : documents) {
                FileDto file = new FileDto();
                file.setId(document.getDocumentId());
                file.setName(document.getFilename());
                if (document.getUploadedDate() != null) {
                    file.setUploadedDate(dateTimeFormat.format(document.getUploadedDate()));
                }
                file.setDocumentType(document.getDocumentType());
                file.setData(document.getData());
                to.getFiles().add(file);
            }
        }
        to.setPriority(priority);
        return to;
    }

    /**
     * This method gets a documentationDto object and builds a list of documents
     * @param documentationDto documentationDto object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto
     * @return list of documents
     */
    public List<Document> mapDocumentation(DocumentationDto documentationDto) {
        List<Document> documents = new ArrayList<>();
        if (documentationDto.getFiles() != null) {
            for (FileDto file : documentationDto.getFiles()) {
                Document document = new Document();
                document.setDocumentId(file.getId());
                document.setFilename(file.getName());
                if (Strings.isNotBlank(file.getUploadedDate())) {
                    try {
                        document.setUploadedDate(dateTimeFormat.parse(file.getUploadedDate()));
                    } catch (ParseException e) {
                        throw new DateFormatException(file.getUploadedDate(), e);
                    }
                }
                document.setMimeType(FILE_NAME_MAP.getContentTypeFor(file.getName()));
                document.setDocumentType(file.getDocumentType());
                document.setData(file.getData());
                documents.add(document);
            }
        }
        return documents;
    }
}
