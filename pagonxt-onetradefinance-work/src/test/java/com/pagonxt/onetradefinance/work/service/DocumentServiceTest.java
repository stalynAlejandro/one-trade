package com.pagonxt.onetradefinance.work.service;

import com.flowable.core.content.api.CoreContentItem;
import com.flowable.core.content.api.CoreContentService;
import com.flowable.core.content.api.MetadataService;
import com.flowable.platform.service.content.PlatformContentItemService;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.DocumentSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class DocumentServiceTest {

    @InjectMocks
    private DocumentService documentService;

    @Mock
    private DocumentSecurityService documentSecurityService;
    @Mock
    private PlatformContentItemService contentItemService;
    @Mock
    private CoreContentService contentService;
    @Mock
    private MetadataService metadataService;

    @Test
    void findDocument_whenPassingValidId_thenReturnDocument() throws IOException {
        // Given
        String id = "doc-1";
        CoreContentItem coreContentItem = mock(CoreContentItem.class);
        when(contentItemService.getContentItem(id)).thenReturn(coreContentItem);
        when(coreContentItem.isContentAvailable()).thenReturn(true);
        InputStream inputStream = IOUtils.toInputStream("Test data", "UTF-8");
        when(contentService.getContentItemData(id)).thenReturn(inputStream);
        when(coreContentItem.getId()).thenReturn(id);
        when(coreContentItem.getName()).thenReturn("filenameTest");
        when(coreContentItem.getMimeType()).thenReturn("mimeTypeTest");
        when(metadataService.getMetadataValue(id, "typology")).thenReturn("letter");
        UserInfo user = mock(UserInfo.class);
        // When
        Document result = documentService.findDocument(user, id);
        // Then
        assertEquals(id, result.getDocumentId());
        assertEquals("filenameTest", result.getFilename());
        assertEquals("mimeTypeTest", result.getMimeType());
        assertEquals("letter", result.getDocumentType());
        assertEquals("VGVzdCBkYXRh", result.getData());
    }

    @Test
    void findDocument_whenPassingInvalidId_thenThrowResourceNotFoundException() {
        // Given
        String id = "doc-1";
        CoreContentItem coreContentItem = mock(CoreContentItem.class);
        when(contentItemService.getContentItem(id)).thenReturn(coreContentItem);
        when(coreContentItem.isContentAvailable()).thenReturn(false);
        UserInfo user = mock(UserInfo.class);
        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> documentService.findDocument(user, id));
        // Then
        assertEquals("No data available for content item null", exception.getMessage());
    }

    @Test
    void findDocument_whenPassingIdNoContent_thenThrowResourceNotFoundException() {
        // Given
        String id = "doc-1";
        CoreContentItem coreContentItem = mock(CoreContentItem.class);
        when(contentItemService.getContentItem(id)).thenReturn(coreContentItem);
        when(coreContentItem.isContentAvailable()).thenReturn(true);
        when(contentService.getContentItemData(id)).thenReturn(null);
        UserInfo user = mock(UserInfo.class);
        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> documentService.findDocument(user, id));
        // Then
        assertEquals("Content item with id 'doc-1' doesn't have content associated with it.", exception.getMessage());
    }

    @Test
    void findDocument_whenInputStreamError_thenReturnServiceException() throws IOException {
        // Given
        String id = "doc-1";
        CoreContentItem coreContentItem = mock(CoreContentItem.class);
        when(contentItemService.getContentItem(id)).thenReturn(coreContentItem);
        when(coreContentItem.isContentAvailable()).thenReturn(true);
        InputStream inputStream = mock(InputStream.class);
        when(contentService.getContentItemData(id)).thenReturn(inputStream);
        when(inputStream.read(any())).thenThrow(new IOException());
        UserInfo user = mock(UserInfo.class);
        // When
        Exception exception = assertThrows(ServiceException.class, () -> documentService.findDocument(user, id));
        // Then
        assertEquals("Error getting data from document with id doc-1", exception.getMessage());
    }
}
