package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class DocumentServiceTest {

    @InjectMocks
    DocumentService documentService;

    @Mock
    public RestTemplateWorkService restTemplateWorkService;

    @Test
    void findDocument_whenPassingDocumentId_thenReturnDocument() {
        // Given
        Document document = new Document();
        String id = "docId";
        document.setDocumentId(id);
        ControllerResponse controllerResponse = ControllerResponse.success("", document);
        when(restTemplateWorkService.findDocument(eq(id), any())).thenReturn(controllerResponse);
        // When
        Document documentResponse = documentService.findDocument(id, null);
        // Then
        assertEquals(document, documentResponse);
    }

    @Test
    void decodeDocumentData_whenPassingDocument_thenStreamingResponseBody() throws IOException {
        // Given
        Document document = new Document();
        String encodedData = "eW91ciBmaWxlIGNvbnRlbnRz";
        document.setData(encodedData);
        OutputStream outputStream = mock(OutputStream.class);
        // When
        documentService.writeDocumentData(outputStream, document);
        // Then
        ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
        verify(outputStream).write(captor.capture());
        String decodedData = new String(captor.getValue(), StandardCharsets.UTF_8);
        assertEquals("your file contents", decodedData);
    }

    @Test
    void decodeDocumentData_whenPassingDocumentWithCorruptedData_thenThrowIllegalArgumentException() {
        // Given
        Document document = new Document();
        String encodedData = "eW91ciBmaWxlIGNvbnRlbnRzError";
        document.setData(encodedData);
        // When, Then
        assertThrows(IllegalArgumentException.class, () -> documentService.writeDocumentData(mock(OutputStream.class), document));
    }

    @Test
    void decodeDocumentData_whenPassingDocumentWithCorruptedData_thenThrowServiceException() throws IOException {
        // Given
        Document document = new Document();
        String encodedData = "eW91ciBmaWxlIGNvbnRlbnRz";
        document.setData(encodedData);
        OutputStream outputStream = mock(OutputStream.class);
        doThrow(IOException.class).when(outputStream).write(any());
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> documentService.writeDocumentData(outputStream, document));
        // Then
        assertEquals("Error writing document data", exception.getMessage());
        assertEquals("writeDocumentDataError", exception.getKey());
    }

}
