package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery;
import com.pagonxt.onetradefinance.integrations.service.OfficeService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static com.pagonxt.onetradefinance.work.config.TestUtils.getRawFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class MyRequestsListQueryComposerTest {

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Europe/Madrid");

    private static OfficeService officeService;

    private static MyRequestsListQueryComposer myRequestsListQueryComposer;

    @BeforeAll
    public static void setup() {
        officeService = mock(OfficeService.class);
        DateFormatProperties dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone("Europe/Madrid");
        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setTimeZone(TIME_ZONE);
        myRequestsListQueryComposer = new MyRequestsListQueryComposer(dateFormatProperties, officeService, mapper);
    }

    @Test
    void givenMiddleOfficeUserRequest_compose_returnsValidResponse() {
        // Given
        User requester = new User(null, null, "MO");
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(requester);
        when(officeService.getRequesterMiddleOffice(requester)).thenReturn("MO1234");
        String expectedResult = getRawFile("/data/myRequests/middle-office-user-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenOfficeUserRequest_compose_returnsValidResponse() {
        // Given
        User requester = new User(null, null, "OFFICE");
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(requester);
        when(officeService.getRequesterOffice(requester)).thenReturn("1234");
        String expectedResult = getRawFile("/data/myRequests/office-user-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenClientUserRequest_compose_returnsValidResponse() {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(new User("requester", null, "CUSTOMER"));
        String expectedResult = getRawFile("/data/myRequests/client-user-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCodeRequest_compose_returnsValidResponse() {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setCode("CLE-001");
        String expectedResult = getRawFile("/data/myRequests/code-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenProductIdListRequest_compose_returnsValidResponse() {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setProductId(List.of("CLE", "CLI"));
        String expectedResult = getRawFile("/data/myRequests/product-id-list-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenEventIdListRequest_compose_returnsValidResponse() {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setEventId(List.of("REQUEST", "MODIFICATION"));
        String expectedResult = getRawFile("/data/myRequests/event-id-list-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenPriorityListRequest_compose_returnsValidResponse() {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setPriority(List.of("normal", "urgent"));
        String expectedResult = getRawFile("/data/myRequests/priority-list-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCurrencyListRequest_compose_returnsValidResponse() {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setCurrency(List.of("EUR", "GBP"));
        String expectedResult = getRawFile("/data/myRequests/currency-list-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenAmountRangeRequest_compose_returnsValidResponse() {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setFromAmount(10000D);
        query.setToAmount(20000D);
        String expectedResult = getRawFile("/data/myRequests/amount-range-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenDateRangeRequest_compose_returnsValidResponse() throws Exception {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmssZ");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        query.setFromDate(simpleDateFormat.parse("20220719-135300+0000"));
        query.setToDate(simpleDateFormat.parse("20220721-135300+0000"));
        String expectedResult = getRawFile("/data/myRequests/date-range-request.txt");

        // When
        String result = myRequestsListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }
}
