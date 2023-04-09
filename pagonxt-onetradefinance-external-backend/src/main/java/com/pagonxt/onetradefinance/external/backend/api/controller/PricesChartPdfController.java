package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.PricesChartPdfService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static com.pagonxt.onetradefinance.integrations.util.LogUtils.sanitizeLog;

/**
 * Controller with endpoints to generate a price chart for export and import collections
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.PricesChartPdfService
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@RestController
@Validated
@RequestMapping("${controller.path}/prices-charts")
public class PricesChartPdfController extends RequestController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(PricesChartPdfController.class);

    /**
     * Class attributes
     */
    private final PricesChartPdfService pricesChartPdfService;

    /**
     * Prices Chart Pdf Constructor
     * @param pricesChartPdfService Service that provides necessary functionality to this controller
     * @param officeInfoService Service that provides necessary functionality with office
     * @param userInfoService Service that provides necessary functionality with userInfo
     */
    public PricesChartPdfController(PricesChartPdfService pricesChartPdfService,
                                    OfficeInfoService officeInfoService,
                                    UserInfoService userInfoService) {
        super(officeInfoService, userInfoService);
        this.pricesChartPdfService = pricesChartPdfService;
    }

    /**
     * This method generates a prices chart for export Collections
     * @param exportCollectionId a string with the export Collection id
     * @return a pdf document with the prices chart
     */
    @GetMapping("/export-collection")
    public ResponseEntity<StreamingResponseBody> generateExportCollectionPricesChartPdf(
            @RequestParam ( name = "export_collection_id") String exportCollectionId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("generateExportCollectionPricesChartPdf(exportCollectionId:" +
                    " {})", sanitizeLog(exportCollectionId));
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prices_chart.pdf")
                .body(outputStream -> pricesChartPdfService.
                        streamExportCollectionPricesChartPdf(exportCollectionId, outputStream, null, getUserInfo()));
    }

    /**
     * This method generates a prices chart for import Collections
     * @param importCollectionId a string with the import Collection id
     * @return a pdf document with the prices chart
     */
    @GetMapping("/import-collection")
    public ResponseEntity<StreamingResponseBody> generateImportCollectionPricesChartPdf(
            @RequestParam (name = "import_collection_id") String importCollectionId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("generateImportCollectionPricesChartPdf(importCollectionId:" +
                    " {})", sanitizeLog(importCollectionId));
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prices_chart.pdf")
                .body(outputStream -> pricesChartPdfService.streamImportCollectionPricesChartPdf(
                        importCollectionId, outputStream, null, getUserInfo()));
    }
}
