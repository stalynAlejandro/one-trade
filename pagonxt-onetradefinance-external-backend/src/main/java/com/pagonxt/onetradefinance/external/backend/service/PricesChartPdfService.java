package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.configuration.JasperConfigurationProperties;
import com.pagonxt.onetradefinance.external.backend.configuration.SecurityConfigurationAuthorProperties;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionRequestService;
import com.pagonxt.onetradefinance.external.backend.service.mapper.PricesChartDataMapper;
import com.pagonxt.onetradefinance.external.backend.service.model.ExportCollectionPricesChartData;
import com.pagonxt.onetradefinance.external.backend.service.model.ImportCollectionPricesChartData;
import com.pagonxt.onetradefinance.external.backend.service.model.PricesChartData;
import com.pagonxt.onetradefinance.external.backend.service.trade.TradeRequestService;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
import com.pagonxt.onetradefinance.integrations.service.CollectionTypeService;
import com.pagonxt.onetradefinance.integrations.service.TradeSpecialPricesService;
import com.pagonxt.onetradefinance.integrations.util.ParseUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.AccountService
 * @see com.pagonxt.onetradefinance.integrations.service.CollectionTypeService
 * @see com.pagonxt.onetradefinance.integrations.service.TradeSpecialPricesService
 * @see com.pagonxt.onetradefinance.external.backend.configuration.JasperConfigurationProperties
 * @see ExportCollectionRequestService
 * @see com.pagonxt.onetradefinance.external.backend.configuration.SecurityConfigurationAuthorProperties
 * @see PricesChartDataMapper
 * @since jdk-11.0.13
 */
@Service
public class PricesChartPdfService {

    private static final Logger LOG = LoggerFactory.getLogger(PricesChartPdfService.class);

    private static final String EXPORT_COLLECTION_CHART_RESOURCE = "jrxml/Export-collection-Prices-chart.jasper";
    private static final String IMPORT_COLLECTION_CHART_RESOURCE = "jrxml/Import-collection-Prices-chart.jasper";
    private static final String BASE_PATH = "BASE_PATH";
    private static final String DATE_FORMAT = "yyyyMMdd";
    private final DateFormat dateFormat;

    /**
     * Class attributes
     */
    private final SecurityConfigurationAuthorProperties securityConfigurationAuthorProperties;
    private final ExportCollectionRequestService exportCollectionRequestService;
    private final TradeRequestService tradeRequestService;
    private final JasperConfigurationProperties jasperConfigurationProperties;
    private final TradeSpecialPricesService tradeSpecialPricesService;
    private final CollectionTypeService collectionTypeService;
    private final AccountService accountService;
    private final PricesChartDataMapper pricesChartDataMapper;

    public PricesChartPdfService(SecurityConfigurationAuthorProperties securityConfigurationAuthorProperties,
                                 ExportCollectionRequestService exportCollectionRequestService,
                                 TradeRequestService tradeRequestService,
                                 JasperConfigurationProperties jasperConfigurationProperties,
                                 TradeSpecialPricesService tradeSpecialPricesService,
                                 CollectionTypeService collectionTypeService,
                                 DateFormatProperties dateFormatProperties,
                                 AccountService accountService,
                                 PricesChartDataMapper pricesChartDataMapper) {
        this.securityConfigurationAuthorProperties = securityConfigurationAuthorProperties;
        this.exportCollectionRequestService = exportCollectionRequestService;
        this.tradeRequestService = tradeRequestService;
        this.jasperConfigurationProperties = jasperConfigurationProperties;
        this.tradeSpecialPricesService = tradeSpecialPricesService;
        this.collectionTypeService = collectionTypeService;
        this.accountService = accountService;
        dateFormat = dateFormatProperties.getDateFormatInstance(DATE_FORMAT);
        this.pricesChartDataMapper = pricesChartDataMapper;
    }

    /**
     * Class method to stream the prices chart of export collection
     * @param exportCollectionId the id of export collection
     * @param stream OutputStream object
     * @param reportDate string with the report date
     * @param requester UserInfo object of the requester
     * @see java.io.OutputStream
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void streamExportCollectionPricesChartPdf(String exportCollectionId,
                                                     OutputStream stream,
                                                     String reportDate,
                                                     UserInfo requester) {
        PricesChartData data = getExportCollectionPricesChartData(exportCollectionId, requester);
        streamPricesChartPdf(EXPORT_COLLECTION_CHART_RESOURCE, data, stream, reportDate);
    }

    /**
     * Class method to stream the prices chart of export collection
     * @param importCollectionId the id of export collection
     * @param stream OutputStream object
     * @param reportDate string with the report date
     * @see java.io.OutputStream
     */
    public void streamImportCollectionPricesChartPdf(String importCollectionId,
                                                     OutputStream stream,
                                                     String reportDate,
                                                     UserInfo requester) {
        PricesChartData data = getImportCollectionPricesChartData(importCollectionId, requester);
        streamPricesChartPdf(IMPORT_COLLECTION_CHART_RESOURCE, data, stream, reportDate);
    }

    /**
     * Class method to stream the prices chart
     * @param resourceName string with the resource name
     * @param data PricesChartData object
     * @param outputStream OutputStream object
     * @param reportDate string with the report date
     * @see com.pagonxt.onetradefinance.external.backend.service.model.PricesChartData
     * @see java.io.OutputStream
     */
    private void streamPricesChartPdf(String resourceName,
                                      PricesChartData data,
                                      OutputStream outputStream,
                                      String reportDate) {
        try (InputStream jasperInputStream = new ClassPathResource(resourceName).getInputStream()) {
            JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(data.getDatasourceCollection());
            Map<String, Object> parameters = data.getReportParameters();
            parameters.put(BASE_PATH, getBasePath());
            if(reportDate != null) {
                parameters.put("REPORT_DATE", reportDate);
            }
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperInputStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, datasource);
            streamJasperPrint(jasperPrint, outputStream);
        } catch (IOException | JRException e) {
            throw new ServiceException("Error generating report", "streamPricesChartPdfError", e);
        }
    }

    /**
     * Class method to stream jasper print
     * @param jasperPrint JasperPrint object
     * @param outputStream OutputStream object
     * @see java.io.OutputStream
     * @see net.sf.jasperreports.engine.JasperPrint
     * @throws JRException handles JasperReport exception
     */
    private void streamJasperPrint(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor(securityConfigurationAuthorProperties.getAuthor());
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        exporter.exportReport();
    }

    /**
     * Method to get the data of the price chart from an export collection
     * @param exportCollectionId the id of export collection
     * @param requester UserInfo object with the requester
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return ExportCollectionPricesChartData object
     */
    private ExportCollectionPricesChartData getExportCollectionPricesChartData(String exportCollectionId,
                                                                               UserInfo requester) {
        ExportCollectionRequest exportCollectionRequest = exportCollectionRequestService
                .getExportCollectionRequestDraft(exportCollectionId, requester);
        String locale = ParseUtils.getLocaleFromCountry(exportCollectionRequest.getCountry());
        ExportCollectionPricesChartData result = pricesChartDataMapper
                .mapExportCollectionPricesChartData(exportCollectionRequest, locale);

        TradeSpecialPricesQuery specialPricesQuery = TradeSpecialPricesQuery
                .TradeSpecialPricesQueryBuilder.tradeSpecialPricesQuery()
                .productId(collectionTypeService.getIdByKey(exportCollectionRequest.getCollectionType()))
                .conceptId(exportCollectionId)
                .customerId(exportCollectionRequest.getCustomer().getCustomerId())
                .country(exportCollectionRequest.getCountry())
                .currencyCode(exportCollectionRequest.getCurrency())
                .amount(exportCollectionRequest.getAmount())
                .queryDate(dateFormat.format(new Date()))
                .branchId(getBranchId(exportCollectionRequest))
                .build();

        List<TradeSpecialPrices> specialPricesList = tradeSpecialPricesService
                .getSpecialPrices(specialPricesQuery, locale);
        fillSpecialPricesData(specialPricesList, result);
        return result;
    }

    /**
     * Class method to get the branch id
     * @param exportCollectionRequest ExportCollectionRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return a string with the branch id
     */
    private String getBranchId(ExportCollectionRequest exportCollectionRequest) {
        if(exportCollectionRequest.getNominalAccount() == null || exportCollectionRequest
                .getNominalAccount().getAccountId() == null) {
            LOG.error("Nominal Account not found in request");
            throw new ServiceException("Nominal Account not found in request", "getBranchId");
        }
        return getBranchIdByAccountIdAndCountry(exportCollectionRequest.getCountry(),
                exportCollectionRequest.getNominalAccount().getAccountId());
    }

    private String getBranchIdByAccountIdAndCountry(String country, String nominalAccountId) {
        Account nominalAccount = accountService.getAccountById(nominalAccountId);
        if(nominalAccount == null || nominalAccount.getCostCenter() == null) {
            LOG.error("Nominal account data not found");
            throw new ServiceException("Nominal account data not found", "getBranchId");
        }
        return country + nominalAccount.getCostCenter();
    }

    /**
     * Method to get the data of the price chart from an import collection
     * @param importCollectionId    the id of import collection
     * @param requester             the requester
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return ExportCollectionPricesChartData object
     */
    private ImportCollectionPricesChartData getImportCollectionPricesChartData(String importCollectionId, UserInfo requester) {
        TradeRequest tradeRequest = tradeRequestService.getTradeRequest(importCollectionId, requester);
        Map<String, Object> details = tradeRequest.getDetails();
        String locale = ParseUtils.getLocaleFromCountry(tradeRequest.getCountry());
        ImportCollectionPricesChartData result = pricesChartDataMapper
                .mapImportCollectionPricesChartData(tradeRequest, locale);

        TradeSpecialPricesQuery specialPricesQuery = TradeSpecialPricesQuery
                .TradeSpecialPricesQueryBuilder.tradeSpecialPricesQuery()
                .productId(collectionTypeService.getIdByKey((String) details.get(REQUEST_COLLECTION_TYPE)))
                .conceptId(importCollectionId)
                .customerId(tradeRequest.getCustomer().getCustomerId())
                .country(tradeRequest.getCountry())
                .currencyCode((String) details.get(REQUEST_CURRENCY))
                .amount((Double) details.get(REQUEST_AMOUNT))
                .queryDate(dateFormat.format(new Date()))
                .branchId(getBranchIdByAccountIdAndCountry(tradeRequest.getCountry(),
                        (String) details.get(REQUEST_NOMINAL_ACCOUNT_ID)))
                .build();

        List<TradeSpecialPrices> specialPricesList = tradeSpecialPricesService
                .getSpecialPrices(specialPricesQuery, locale);
        fillSpecialPricesData(specialPricesList, result);
        return result;
    }

    /**
     * Method to fill document with special prices data
     * @param specialPrices list of special prices
     * @param data the data of prices chart
     * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices
     * @see com.pagonxt.onetradefinance.external.backend.service.model.PricesChartData
     */
    private void fillSpecialPricesData(List<TradeSpecialPrices> specialPrices, PricesChartData data) {
        specialPrices.forEach(tradeSpecialPrices -> data.getCommissions().add(new PricesChartData.CommissionGroup(
                List.of(new PricesChartData.Commission(tradeSpecialPrices))
        )));
    }

    /**
     * Class method to get Base Path
     * @return a string with the base path
     */
    private String getBasePath() {
        if (StringUtils.hasText(jasperConfigurationProperties.getResourcePath())) {
            return jasperConfigurationProperties.getResourcePath();
        } else {
            return this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        }
    }
}
