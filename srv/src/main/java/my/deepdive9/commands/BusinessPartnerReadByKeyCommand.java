package my.deepdive9.commands;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerByKeyFluentHelper;

public class BusinessPartnerReadByKeyCommand extends ErpCommand<BusinessPartner> {

    private final ErpConfigContext erpConfigContext;
    private String businessPartner;

    public BusinessPartnerReadByKeyCommand(ErpConfigContext erpConfigContext, String businessPartner) {
        super(BusinessPartnerReadByKeyCommand.class, erpConfigContext);
        this.businessPartner = businessPartner;
        this.erpConfigContext = erpConfigContext;
    }

    @Override
    protected BusinessPartner run() {

        BusinessPartnerByKeyFluentHelper service = new DefaultBusinessPartnerService()
                .getBusinessPartnerByKey(businessPartner);

        try {
            return service.execute(erpConfigContext);
        } catch (final ODataException e) {
            throw new HystrixBadRequestException(e.getMessage(), e);
        }
    }
}