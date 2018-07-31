package my.deepdive9.crud;

import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.service.prov.api.operations.Query;
import com.sap.cloud.sdk.service.prov.api.response.QueryResponse;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;
import com.sap.cloud.sdk.service.prov.api.operations.Read;
import com.sap.cloud.sdk.service.prov.api.response.ReadResponse;
import com.sap.cloud.sdk.service.prov.api.request.ReadRequest;
import java.util.List;
import java.util.stream.Collectors;
import my.deepdive9.commands.BusinessPartnerReadByKeyCommand;
import my.deepdive9.commands.BusinessPartnerReadCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sap.cloud.sdk.cloudplatform.security.user.User;
import com.sap.cloud.sdk.cloudplatform.security.user.UserAccessor;

public class BusinessPartnerRead {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Read(serviceName = "CrudService", entity = "BusinessPartner")
	public ReadResponse readSingleCustomerByKey(ReadRequest readRequest) {
		System.out.println("Received the following keys: {} " + readRequest.getKeys().entrySet().stream()
				.map(x -> x.getKey() + ":" + x.getValue()).collect(Collectors.joining(" | ")));

		logger.info("Received the following keys: {} ", readRequest.getKeys().entrySet().stream()
				.map(x -> x.getKey() + ":" + x.getValue()).collect(Collectors.joining(" | ")));

		String id = String.valueOf(readRequest.getKeys().get("BusinessPartner"));

		BusinessPartner partner = new BusinessPartnerReadByKeyCommand(new ErpConfigContext(), id).execute();

		ReadResponse readResponse = ReadResponse.setSuccess().setData(partner).response();

		return readResponse;
	}

	@Query(serviceName = "CrudService", entity = "BusinessPartner")
	public QueryResponse queryCustomers(QueryRequest qryRequest) {
		final User currentUser = UserAccessor.getCurrentUser();
		System.out.println("User: " + currentUser);

		List<BusinessPartner> businessPartners = new BusinessPartnerReadCommand(new ErpConfigContext(),
				qryRequest.getTopOptionValue(), qryRequest.getSkipOptionValue(), qryRequest.getSelectProperties(),
				qryRequest.getOrderByProperties()).execute();

		QueryResponse queryResponse = QueryResponse.setSuccess().setData(businessPartners).response();
		return queryResponse;
	}
}
