package com.pagonxt.onetradefinance.external.backend.service.mapper;

import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.model.ExportCollectionPricesChartData;
import com.pagonxt.onetradefinance.external.backend.service.model.ImportCollectionPricesChartData;
import com.pagonxt.onetradefinance.external.backend.service.model.PricesChartData;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.OfficeInfo;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesDAO;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesId;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import com.pagonxt.onetradefinance.integrations.util.ParseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * This class maps data from a request and insert them into the Prices Chart
 * @author -
 * @version jdk-11.0.13
 * @see org.slf4j.Logger
 * @see OfficeInfoService
 * @since jdk-11.0.13
 */
@Component
public class PricesChartDataMapper {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(PricesChartDataMapper.class);

    /**
     * Class attribute
     */
    private final OfficeInfoService officeInfoService;
    private final CustomerService customerService;

    /**
     * Class constructor
     *
     * @param officeInfoService : The office info service
     * @param customerService   : The customer service
     */
    public PricesChartDataMapper(OfficeInfoService officeInfoService, CustomerService customerService) {
        this.officeInfoService = officeInfoService;
        this.customerService = customerService;
    }

    /**
     * This method collects data to insert into the Prices Chart
     * @param request ExportCollectionRequest object with the data of the request
     * @param locale a string with locale
     * @see com.pagonxt.onetradefinance.integrations.model
     * @return an object with the data to insert into the Prices Chart
     */
    public ExportCollectionPricesChartData mapExportCollectionPricesChartData
            (ExportCollectionRequest request, String locale) {
        ExportCollectionPricesChartData exportCollectionPricesChartData = new ExportCollectionPricesChartData();
        exportCollectionPricesChartData.setDebtor(request.getDebtorName());
        exportCollectionPricesChartData.setDebtorBank(request.getDebtorBank());
        injectOfficeData(exportCollectionPricesChartData, request.getOffice());
        exportCollectionPricesChartData.setCustomerName(request.getCustomer().getName());
        //Get MAIN company address
        injectCompanyAddress(exportCollectionPricesChartData, request.getCustomer().getPersonNumber());
        exportCollectionPricesChartData.setOurReference(request.getCode());
        exportCollectionPricesChartData.setClientReference(request.getClientReference());
        exportCollectionPricesChartData.setAmount(ParseUtils.parseDoubleToString(request.getAmount(), locale));
        exportCollectionPricesChartData.setCurrencyCode(request.getCurrency());
        return exportCollectionPricesChartData;
    }

    /**
     * This method collects data to insert into the Prices Chart
     * @param request ExportCollectionRequest object with the data of the request
     * @param locale a string with locale
     * @see com.pagonxt.onetradefinance.integrations.model
     * @return an object with the data to insert into the Prices Chart
     */
    public ImportCollectionPricesChartData mapImportCollectionPricesChartData
    (TradeRequest request, String locale) {
        ImportCollectionPricesChartData importCollectionPricesChartData = new ImportCollectionPricesChartData();
        Map<String, Object> details = request.getDetails();
        importCollectionPricesChartData.setBeneficiary((String) details.get(REQUEST_DEBTOR_NAME));
        importCollectionPricesChartData.setBeneficiaryBank((String) details.get(REQUEST_DEBTOR_BANK));
        injectOfficeData(importCollectionPricesChartData, request.getOffice());
        importCollectionPricesChartData.setCustomerName(request.getCustomer().getName());
        //Get MAIN company address
        injectCompanyAddress(importCollectionPricesChartData, request.getCustomer().getPersonNumber());
        importCollectionPricesChartData.setOurReference(request.getCode());
        importCollectionPricesChartData.setClientReference((String) details.get(REQUEST_CUSTOMER_REFERENCE));
        importCollectionPricesChartData.setAmount(ParseUtils.parseDoubleToString(
                (Double) details.get(REQUEST_AMOUNT), locale));
        importCollectionPricesChartData.setCurrencyCode((String) details.get(REQUEST_CURRENCY));
        return importCollectionPricesChartData;
    }

    private void injectCompanyAddress(PricesChartData pricesChartData, String customerPersonNumber) {
        Customer customer = customerService.getCustomerByPersonNumber(customerPersonNumber);
        CompanyAddressesId companyAddressesId = new CompanyAddressesId(
                customer.getCountryISO(),
                customer.getCustomerId(),
                "MAIN");
        CompanyAddressesDAO companyAddressesDAO = customerService
                .getCustomerAddressByCompanyAddressesId(companyAddressesId);

        String streetAddress = "-";
        String localityAddress = "-";
        String provinceAddress = "-";
        if (companyAddressesDAO != null) {
            // Street
            List<String> streetAddressList = Stream.of(companyAddressesDAO.getDepartment(),
                    companyAddressesDAO.getSubdepartment(), companyAddressesDAO.getStreet(),
                    companyAddressesDAO.getBuilding(), companyAddressesDAO.getStreetBuilding(),
                    companyAddressesDAO.getFloor(), companyAddressesDAO.getDistrict(),
                    companyAddressesDAO.getPostCode()).filter(Objects::nonNull).collect(Collectors.toList());
            if (!streetAddressList.isEmpty()) {
                streetAddress = String.join(" ", streetAddressList);
            }
            // Locality
            List<String> localityAddressList = Stream.of(companyAddressesDAO.getLocation(),
                    companyAddressesDAO.getTown()).filter(Objects::nonNull).collect(Collectors.toList());
            if (!localityAddressList.isEmpty()) {
                localityAddress = String.join(" ", localityAddressList);
            }
            // Province
            if (companyAddressesDAO.getProvince() != null) {
                provinceAddress = companyAddressesDAO.getProvince();
            }
        }
        pricesChartData.setCustomerAddress(streetAddress);
        pricesChartData.setCustomerAddressLocality(localityAddress);
        pricesChartData.setCustomerAddressProvince(provinceAddress);
        // Address country
        Locale countryLocale = new Locale("", customer.getCountryISO());
        pricesChartData.setCustomerAddressCountry(countryLocale.getDisplayCountry());
    }

    /**
     * This method insert the Office code into the object data of the Prices Chart
     * @param pricesChartData the object data with the data of the Prices Chart
     * @param officeCode a string with the office code
     * @see com.pagonxt.onetradefinance.external.backend.service.model.PricesChartData
     */
    private void injectOfficeData(PricesChartData pricesChartData, String officeCode) {
        if(officeCode == null) {
            LOG.warn("Office code not found");
            return ;
        }
        pricesChartData.setOfficeId(officeCode);
        OfficeInfo officeInfo = officeInfoService.getOfficeInfo(officeCode);
        if(officeInfo == null) {
            LOG.warn("Office info not found");
            return ;
        }
        pricesChartData.setOfficeAddress(officeInfo.getAddress() + " " + officeInfo.getPlace());
        pricesChartData.setOfficeAddressCountry(officeInfo.getCountry());
    }
}
