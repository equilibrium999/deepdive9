package my.deepdive9.commands;

import java.util.Map;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.BusinessPartnerService;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataUpdateResult;

public class UpdateBusinessPartnerCommand extends ErpCommand<ODataUpdateResult> {

	private final ErpConfigContext erpConfigContext;
	private BusinessPartnerService businessPartnerService;
	private Map<String, Object> businessPartnerMap;

	public UpdateBusinessPartnerCommand(ErpConfigContext erpConfigContext,
			BusinessPartnerService businessPartnerService, EntityData entity) {
		super(UpdateBusinessPartnerCommand.class, erpConfigContext);
		this.erpConfigContext = erpConfigContext;
		this.businessPartnerService = businessPartnerService;
		this.businessPartnerMap = entity.asMap();
	}

	@Override
	protected ODataUpdateResult run() {
		BusinessPartner buPa = new BusinessPartner();
		buPa.setBusinessPartner((String) businessPartnerMap.get("BusinessPartner"));
		buPa.setFirstName((String) businessPartnerMap.get("FirstName"));
		buPa.setLastName((String) businessPartnerMap.get("LastName"));
		try {
			return businessPartnerService.updateBusinessPartner(buPa).execute(erpConfigContext);
		} catch (final ODataException e) {
			throw new HystrixBadRequestException(e.getMessage(), e);
		}
	}
}