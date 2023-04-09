package com.pagonxt.onetradefinance.external.backend.service.model;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class ImportCollectionPricesChartDataTest {

    @Test
    void getReportParameters_ok_returnMap() {
        ImportCollectionPricesChartData importCollectionPricesChartData = new ImportCollectionPricesChartData();
        importCollectionPricesChartData.setBeneficiary("beneficiary1");
        importCollectionPricesChartData.setBeneficiaryBank("beneficiaryBank1");
        // When
        Map<String, Object> result = importCollectionPricesChartData.getReportParameters();
        // Then
        assertEquals("beneficiary1", result.get("BENEFICIARY"));
        assertEquals("beneficiaryBank1", result.get("BENEFICIARY_BANK"));
    }
}
