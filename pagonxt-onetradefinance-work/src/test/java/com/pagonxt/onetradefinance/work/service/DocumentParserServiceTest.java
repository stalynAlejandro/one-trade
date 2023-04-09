package com.pagonxt.onetradefinance.work.service;

import com.flowable.core.content.api.CoreContentService;
import com.flowable.dataobject.api.runtime.*;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import org.apache.tika.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@UnitTest
class DocumentParserServiceTest {

    private static final String DEFINITION_KEY = "PGN_DO001";
    private static final String FIND_BY_ID = "findById";
    private static final String OFFICE = "officeCode";

    @Mock
    private CoreContentService contentService;
    @Mock
    private DataObjectRuntimeService dataObjectRuntimeService;
    @InjectMocks
    private DocumentParserService documentParserUtils;

    @Test
    void parseOffice_whenPassingIdNoContent_thenThrowResourceNotFoundException() {
        // Given
        String id = "offices";
        when(contentService.getContentItemData(id)).thenReturn(null);
        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> documentParserUtils.parseOffice(id));
        // Then
        assertEquals("Content item with id 'offices' doesn't have nothing associated with it.", exception.getMessage());
    }

    @Test
    void parseMiddleOffice_whenPassingIdNoContent_thenThrowResourceNotFoundException() {
        // Given
        String id = "offices";
        when(contentService.getContentItemData(id)).thenReturn(null);
        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> documentParserUtils.parseMiddleOffice(id));
        // Then
        assertEquals("Content item with id 'offices' doesn't have content associated with it.", exception.getMessage());
    }

    @Test
    void parseOffice_whenPassingIdWithContentUpdateOffice() throws IOException {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        DataObjectModificationBuilder dataObjectModificationBuilderMock = mock(DataObjectModificationBuilder.class, RETURNS_DEEP_STUBS);
        String id = "id";
        String line = "00493763SURBA.C. SANTIBAÑEZ DE LA PEÑA                        A.C.SANTIA.C.SANTIBAÑEZDELACRMAGDALENA                                         40                                                    00000  SANTIBAÑEZ DE LA PEÑA          ‘* \f3434870ES979-860379          2019-09-129999-12-31               \f       \f       \f   \f AN976338 2021-09-10S         \f4       \fU S  \f01011MSI26P3MINR OFIUNI_CASTILLALEON@gruposantander.com                                                                            ";
        InputStream inputStream = IOUtils.toInputStream(line, "UTF-8");
        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(contentService.getContentItemData(id)).thenReturn(inputStream);
        when(bufferedReader.readLine()).thenReturn(line);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "3763")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectRuntimeService.createDataObjectModificationBuilder()).thenReturn(dataObjectModificationBuilderMock);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation("findCountry")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("country",  "")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(List.of(dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainer.getString(OFFICE)).thenReturn("3763");

        documentParserUtils.parseOffice(id);

        assertEquals(line, bufferedReader.readLine());

    }

    @Test
    void parseOffice_whenPassingIdWithContentCreateOffice() throws IOException {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        DataObjectModificationBuilder dataObjectModificationBuilderMock = mock(DataObjectModificationBuilder.class, RETURNS_DEEP_STUBS);
        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class, RETURNS_DEEP_STUBS);
        String id = "id";
        String line = "00493763SURBA.C. SANTIBAÑEZ DE LA PEÑA                        A.C.SANTIA.C.SANTIBAÑEZDELACRMAGDALENA                                         40                                                    00000  SANTIBAÑEZ DE LA PEÑA          ‘* \f3434870ES979-860379          2019-09-129999-12-31               \f       \f       \f   \f AN976338 2021-09-10S         \f4       \fU S  \f01011MSI26P3MINR OFIUNI_CASTILLALEON@gruposantander.com                                                                            ";
        InputStream inputStream = IOUtils.toInputStream(line, "UTF-8");
        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(contentService.getContentItemData(id)).thenReturn(inputStream);
        when(bufferedReader.readLine()).thenReturn(line);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "3763")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((null));
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation("findCountry")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("country",  "")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(List.of(dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainer.getString(OFFICE)).thenReturn("3762");
        when(dataObjectRuntimeService.createDataObjectModificationBuilder()).thenReturn(dataObjectModificationBuilderMock);

        documentParserUtils.parseOffice(id);

        assertEquals(line, bufferedReader.readLine());

    }

    @Test
    void parseOffice_whenLineIsSmallerThan498Chars() throws IOException {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        DataObjectModificationBuilder dataObjectModificationBuilderMock = mock(DataObjectModificationBuilder.class, RETURNS_DEEP_STUBS);
        String id = "id";
        String line = "00493866SURBAA FF SANT JOAN DE VILATORRADA MAYOR 1            AA FF SA AA FF SANT JOAN DECLMAYOR                                             1                                                     0      SANT JOAN DE VILATORRADA      °\n" +
                "@ \f0808250ES938-76504093-87650392013-03-199999-12-31               \f       \f       \f   \f AN976338 2021-06-30S         \f4       \fU S  \f01011MSIAFP3MINR OFIUNIUniversal_CAT@gruposantander.es                                                                             \n";
        InputStream inputStream = IOUtils.toInputStream(line, "UTF-8");
        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(contentService.getContentItemData(id)).thenReturn(inputStream);
        when(bufferedReader.readLine()).thenReturn(line);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "3866")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectRuntimeService.createDataObjectModificationBuilder()).thenReturn(dataObjectModificationBuilderMock);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation("findCountry")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("country",  "")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(List.of(dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainer.getString(OFFICE)).thenReturn("3866");

        documentParserUtils.parseOffice(id);

        assertEquals(line, bufferedReader.readLine());
    }

    @Test
    void parseMiddleOffice_whenPassingIdWithContent() throws IOException {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        DataObjectModificationBuilder dataObjectModificationBuilderMock = mock(DataObjectModificationBuilder.class, RETURNS_DEEP_STUBS);
        String id = "id";
        String line = "004900010049835400498913004994470049944800499400";
        InputStream inputStream = IOUtils.toInputStream(line, "UTF-8");
        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(contentService.getContentItemData(id)).thenReturn(inputStream);
        when(bufferedReader.readLine()).thenReturn(line);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "0001")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectRuntimeService.createDataObjectModificationBuilder()).thenReturn(dataObjectModificationBuilderMock);

        documentParserUtils.parseMiddleOffice(id);

        assertEquals(line, bufferedReader.readLine());

    }

}