package my.deepdive9.crud;

import org.slf4j.LoggerFactory;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataUpdateResult;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.operations.Update;
import com.sap.cloud.sdk.service.prov.api.request.UpdateRequest;
import com.sap.cloud.sdk.service.prov.api.response.UpdateResponse;
import com.sap.cloud.sdk.service.prov.api.response.impl.ErrorResponseImpl;
import java.util.stream.Collectors;
import my.deepdive9.commands.UpdateBusinessPartnerCommand;
import org.slf4j.Logger;

public class BusinessPartnerUpdate {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Update(serviceName = "CrudService", entity = "BusinessPartner")
	public UpdateResponse updateBusinessPartner(UpdateRequest updateRequest) {
		System.out.println("Received the following map data for update Request: {} " + updateRequest.getMapData()
				.entrySet().stream().map(x -> x.getKey() + ":" + x.getValue()).collect(Collectors.joining(" | ")));
		System.out.println("Received the following keys for update Request: {} " + updateRequest.getKeys().entrySet()
				.stream().map(x -> x.getKey() + ":" + x.getValue()).collect(Collectors.joining(" | ")));

		logger.info("Received the following map data for update Request: {} ", updateRequest.getMapData().entrySet()
				.stream().map(x -> x.getKey() + ":" + x.getValue()).collect(Collectors.joining(" | ")));
		logger.info("Received the following keys for update Request: {} ", updateRequest.getKeys().entrySet().stream()
				.map(x -> x.getKey() + ":" + x.getValue()).collect(Collectors.joining(" | ")));

		EntityData entity = updateRequest.getData();

		ODataUpdateResult updateResult;
		try {
			updateResult = new UpdateBusinessPartnerCommand(new ErpConfigContext(), new DefaultBusinessPartnerService(),
					entity).execute();
		} catch (HystrixBadRequestException e) {
			return UpdateResponse.setError(new ErrorResponseImpl(500, null, e.getMessage(), e.getCause(), null));
		}

		UpdateResponse response;

		int httpStatusCode = updateResult.getHttpStatusCode();
		if (httpStatusCode < 200 || httpStatusCode >= 300)
			response = UpdateResponse
					.setError(new ErrorResponseImpl(httpStatusCode, null, updateResult.toString(), null, null));
		else
			response = UpdateResponse.setSuccess().response();

		return response;
	}

}