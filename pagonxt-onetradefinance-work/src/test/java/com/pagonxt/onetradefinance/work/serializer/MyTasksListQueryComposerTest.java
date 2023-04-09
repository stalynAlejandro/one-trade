package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery;
import com.pagonxt.onetradefinance.integrations.service.OfficeService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class MyTasksListQueryComposerTest {

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Europe/Madrid");

    private static OfficeService officeService;

    private static MyTasksListQueryComposer myTasksListQueryComposer;

    @BeforeAll
    public static void setup() {
        officeService = mock(OfficeService.class);
        DateFormatProperties dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone("Europe/Madrid");
        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setTimeZone(TIME_ZONE);
        myTasksListQueryComposer = new MyTasksListQueryComposer(dateFormatProperties, officeService, mapper);
    }

    @Test
    void givenMiddleOfficeUserRequest_compose_returnsValidResponse() {
        // Given
        User requester = new User("middleOffice", "MiddleOffice", "MO");
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(requester);
        when(officeService.getRequesterMiddleOffice(requester)).thenReturn("MO1234");
        String expectedResult = getRawFile("/data/myTasks/middle-office-user-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenOfficeUserRequest_compose_returnsValidResponse() {
        // Given
        User requester = new User("office", "Office", "OFFICE");
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(requester);
        when(officeService.getRequesterOffice(requester)).thenReturn("1234");
        String expectedResult = getRawFile("/data/myTasks/office-user-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenClientUserRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User("requester", "Requester", "CUSTOMER"));
        String expectedResult = getRawFile("/data/myTasks/client-user-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCodeRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User("backoffice", "Backoffice", "BACKOFFICE"));
        query.setCode("CLE-001");
        String expectedResult = getRawFile("/data/myTasks/code-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenProductIdListRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setScope("all");
        query.setProductId(List.of("CLE", "CLI"));
        String expectedResult = getRawFile("/data/myTasks/product-id-list-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenEventIdListRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setScope("all");
        query.setEventId(List.of("REQUEST", "MODIFICATION"));
        String expectedResult = getRawFile("/data/myTasks/event-id-list-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenTaskIdListRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setScope("all");
        query.setTaskId(List.of("REFERED_RELEASE", "EXTERNAL_REQUEST_DRAFT"));
        String expectedResult = getRawFile("/data/myTasks/task-id-list-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenPriorityListRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setPriority(List.of("normal", "urgent"));
        String expectedResult = getRawFile("/data/myTasks/priority-list-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenDraftStatusRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setStatus(List.of("DRAFT"));
        query.setScope("all");
        String expectedResult = getRawFile("/data/myTasks/draft-status-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenNoDraftStatusRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setStatus(List.of("IN_PROGRESS"));
        query.setScope("all");
        String expectedResult = getRawFile("/data/myTasks/non-draft-status-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCurrencyListRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setCurrency(List.of("EUR", "GBP"));
        String expectedResult = getRawFile("/data/myTasks/currency-list-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenAmountRangeRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setFromAmount(10000D);
        query.setToAmount(20000D);
        String expectedResult = getRawFile("/data/myTasks/amount-range-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenDateRangeRequest_compose_returnsValidResponse() throws Exception {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmssZ");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        query.setFromDate(simpleDateFormat.parse("20220719-135300+0000"));
        query.setToDate(simpleDateFormat.parse("20220721-135300+0000"));
        String expectedResult = getRawFile("/data/myTasks/date-range-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCustomerNameRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setScope("all");
        query.setCustomerName("customerName");
        String expectedResult = getRawFile("/data/myTasks/customer-name-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCustomerRequester_composeWithCustomerName_responseDoesNotContainCustomerNameArgument() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User("customer", "Customer", "CUSTOMER"));
        query.setScope("mine");
        query.setCustomerName("customerName");
        String expectedResult = getRawFile("/data/myTasks/customer-requester-requests.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCustomerPersonNumberRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setScope("all");
        query.setCustomerPersonNumber("customerPersonNumber");
        String expectedResult = getRawFile("/data/myTasks/customer-person-number-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCustomerRequester_composeWithPersonNumber_responseDoesNotContainPersonNumberArgument() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User("customer", "Customer", "CUSTOMER"));
        query.setScope("mine");
        query.setCustomerPersonNumber("customerPersonNumber");
        String expectedResult = getRawFile("/data/myTasks/customer-requester-requests.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCustomerTaxIdRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setScope("all");
        query.setCustomerTaxId("customerTaxId");
        String expectedResult = getRawFile("/data/myTasks/customer-tax-id-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCustomerRequester_composeWithCustomerTaxId_responseDoesNotContainCustomerTaxIdArgument() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User("customer", "Customer", "CUSTOMER"));
        query.setScope("mine");
        query.setCustomerTaxId("customerTaxId");
        String expectedResult = getRawFile("/data/myTasks/customer-requester-requests.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenRequesterNameRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setScope("all");
        query.setRequesterName("requesterName");
        String expectedResult = getRawFile("/data/myTasks/requester-name-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCustomerRequester_composeWithRequesterName_responseDoesNotContainRequesterNameArgument() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User("customer", "Customer", "CUSTOMER"));
        query.setScope("mine");
        query.setRequesterName("requesterName");
        String expectedResult = getRawFile("/data/myTasks/customer-requester-requests.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenOwnerNameRequest_compose_returnsValidResponse() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User(null, null, "BACKOFFICE"));
        query.setScope("all");
        query.setOwnerName("ownerName");
        String expectedResult = getRawFile("/data/myTasks/owner-name-request.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenCustomerRequester_composeWithOwnerName_responseDoesNotContainOwnerNameArgument() {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        query.setRequester(new User("customer", "Customer", "CUSTOMER"));
        query.setScope("mine");
        query.setOwnerName("ownerName");
        String expectedResult = getRawFile("/data/myTasks/customer-requester-requests.txt");

        // When
        String result = myTasksListQueryComposer.compose(query);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    private String getRawFile(String fileName) {
        try {
            URL resource = getClass().getResource(fileName);
            assert resource != null;
            return Files.readString(Paths.get(resource.toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(String.format("Error getting template '%s'", fileName), e);
        }
    }
}
