package my.deepdive9.commands;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import java.util.Map;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;

public class CreateBusinessPartnerCommand extends ErpCommand<BusinessPartner> {

    private final ErpConfigContext erpConfigContext;
    private BusinessPartnerService businessPartnerService;
    private Map<String, Object> businessPartnerMap;

    public CreateBusinessPartnerCommand(ErpConfigContext erpConfigContext, BusinessPartnerService businessPartnerService, EntityData entity) {
        super(CreateBusinessPartnerCommand.class, erpConfigContext);
        this.erpConfigContext = erpConfigContext;
        this.businessPartnerService = businessPartnerService;
        this.businessPartnerMap = entity.asMap();
    }


    @Override
    protected BusinessPartner run() {
        //TODO replace when SDK allows BuPa.fromMap
        BusinessPartner buPa = new BusinessPartner();
        buPa.setBusinessPartner((String) businessPartnerMap.get("BusinessPartner"));
        buPa.setBusinessPartnerCategory((String) businessPartnerMap.get("BusinessPartnerCategory"));
        buPa.setLastName((String) businessPartnerMap.get("LastName"));
        buPa.setFirstName((String) businessPartnerMap.get("FirstName"));

        try {
            return businessPartnerService
                            .createBusinessPartner(buPa)
                            .execute(erpConfigContext);
        } catch (final ODataException e) {
            throw new HystrixBadRequestException(e.getMessage(), e);
        }
    }
}