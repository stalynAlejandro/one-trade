package com.pagonxt.onetradefinance.integrations.repository;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.CustomerQuery;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyDataDAO;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@UnitTest
class CompanyDataSpecificationTest {

    @Mock
    CriteriaQuery<?> mockCriteriaQuery;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Root<CompanyDataDAO> mockRoot;

    @Test
    void toPredicate_ok_returnPredicate() {
        // Given
        CustomerQuery query = new CustomerQuery("name1", "taxId1", "personNumber1", null);
        CompanyDataSpecification companyDataSpecification = new CompanyDataSpecification(query);
        CriteriaBuilder criteriaBuilderMock = mock(CriteriaBuilder.class);
        // When
        companyDataSpecification.toPredicate(mockRoot, mockCriteriaQuery, criteriaBuilderMock);
        // Then
        verify(criteriaBuilderMock, times(4)).and(any(), any());
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(criteriaBuilderMock, times(3)).like(any(), stringCaptor.capture());
        assertEquals("%name1%", stringCaptor.getAllValues().get(0));
        assertEquals("%personnumber1%", stringCaptor.getAllValues().get(1));
        assertEquals("%taxid1%", stringCaptor.getAllValues().get(2));
    }

    @Test
    void toPredicate_nullQueryParams_returnPredicate() {
        // Given
        CustomerQuery query = new CustomerQuery(null, null, null, null);
        CompanyDataSpecification companyDataSpecification = new CompanyDataSpecification(query);
        CriteriaBuilder criteriaBuilderMock = mock(CriteriaBuilder.class);
        // When
        companyDataSpecification.toPredicate(mockRoot, mockCriteriaQuery, criteriaBuilderMock);
        // Then
        verify(criteriaBuilderMock, times(1)).and(any(), any());
        verify(criteriaBuilderMock, times(0)).like(any(), anyString());
    }
}
