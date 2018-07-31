package my.deepdive9.crud;

import org.slf4j.LoggerFactory;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.operations.Create;
import com.sap.cloud.sdk.service.prov.api.request.CreateRequest;
import com.sap.cloud.sdk.service.prov.api.response.impl.ErrorResponseImpl;
import com.sap.cloud.sdk.service.prov.api.response.CreateResponse;
import java.util.stream.Collectors;
import my.deepdive9.commands.CreateBusinessPartnerCommand;
import org.slf4j.Logger;

public class BusinessPartnerCreate {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Create(serviceName = "CrudService", entity = "BusinessPartner")
	public CreateResponse createBusinessPartner(CreateRequest createRequest) {

		EntityData entity = createRequest.getData();

		System.out.println("Received the following entity for create Request: {} " + entity.asMap().entrySet().stream()
				.map(x -> x.getKey() + ":" + x.getValue()).collect(Collectors.joining(" | ")));

		logger.info("Received the following entity for create Request: {} ", entity.asMap().entrySet().stream()
				.map(x -> x.getKey() + ":" + x.getValue()).collect(Collectors.joining(" | ")));

		BusinessPartner response;
		try {
			response = new CreateBusinessPartnerCommand(new ErpConfigContext(), new DefaultBusinessPartnerService(),
					entity).execute();
		} catch (HystrixBadRequestException e) {
			return CreateResponse.setError(new ErrorResponseImpl(400, null, e.getMessage(), e.getCause(), null));
		}

		CreateResponse readResponse = CreateResponse.setSuccess().setData(response).response();

		return readResponse;
	}
}