package com.pagonxt.onetradefinance.external.backend.service.model;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@UnitTest
class ExportCollectionPricesChartDataTest {

    @Test
    void getReportParameters_ok_returnMap() {
        ExportCollectionPricesChartData  exportCollectionPricesChartData = new ExportCollectionPricesChartData();
        exportCollectionPricesChartData.setDebtor("debtor1");
        exportCollectionPricesChartData.setDebtorBank("debtorBank1");
        // When
        Map<String, Object> result = exportCollectionPricesChartData.getReportParameters();
        // Then
        assertEquals("debtor1", result.get("DEBTOR"));
        assertEquals("debtorBank1", result.get("DEBTOR_BANK"));
    }

    @Test
    void getDatasourceCollection_ok_returnListCommission() {
        ExportCollectionPricesChartData  exportCollectionPricesChartData = new ExportCollectionPricesChartData();
        PricesChartData.Commission commission = new PricesChartData.Commission();
        commission.setConcept("concept1");
        commission.setValue("value1");
        commission.setPeriodicity("periodicity1");
        commission.setPercentage("percentage1");
        List<PricesChartData.Commission> commissions = List.of(commission);
        exportCollectionPricesChartData.setCommissions(List.of(new PricesChartData.CommissionGroup(commissions)));
        // When
        List<PricesChartData.Commission> result = exportCollectionPricesChartData.getDatasourceCollection();
        // Then
        assertEquals(1, result.size());
        assertEquals("concept1", result.get(0).getConcept());
        assertFalse(result.get(0).isOdd());
    }

}
