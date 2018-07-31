package my.deepdive9.commands;

import java.util.List;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;
import com.sap.cloud.sdk.s4hana.connectivity.ErpCommand;
import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;
import com.sap.cloud.sdk.s4hana.datamodel.odata.helper.Order;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartner;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerFluentHelper;
import com.sap.cloud.sdk.s4hana.datamodel.odata.namespaces.businesspartner.BusinessPartnerField;
import com.sap.cloud.sdk.service.prov.api.request.OrderByExpression;
import com.sap.cloud.sdk.s4hana.datamodel.odata.services.DefaultBusinessPartnerService;

public class BusinessPartnerReadCommand extends ErpCommand<List<BusinessPartner>> {
	private final int top;
	private final int skip;
	private final BusinessPartnerField[] selectedProperties;
	private final List<OrderByExpression> orderByProperties;
	private final ErpConfigContext erpConfigContext;
	private static final String CATEGORY_PERSON = "1";

	public BusinessPartnerReadCommand(ErpConfigContext erpConfigContext, int top, int skip, List<String> properties,
			List<OrderByExpression> orderByProperties) {
		super(BusinessPartnerReadCommand.class, erpConfigContext);
		this.erpConfigContext = erpConfigContext;
		this.top = top;
		this.skip = skip;
		selectedProperties = properties.stream().map(property -> new BusinessPartnerField(property))
				.toArray(BusinessPartnerField[]::new);
		this.orderByProperties = orderByProperties;
	}

	@Override
	protected List<BusinessPartner> run() {

		BusinessPartnerFluentHelper service = new DefaultBusinessPartnerService().getAllBusinessPartner();

		// service.filter(BusinessPartner.BUSINESS_PARTNER_CATEGORY.eq(CATEGORY_PERSON)).orderBy(BusinessPartner.LAST_NAME,
		// Order.DESC);

		orderByProperties.stream().forEach((expression) -> {
			System.out.println("Order by property:" + expression.getOrderByProperty());
			System.out.println("Order by descending:" + expression.isDescending());
			service.orderBy(new BusinessPartnerField<>(expression.getOrderByProperty()),
					expression.isDescending() ? Order.DESC : Order.ASC);
		});

		// orderByProperties.stream()
		// .forEach(expression -> service.orderBy(new
		// BusinessPartnerField<>(expression.getOrderByProperty()),
		// expression.isDescending() ? Order.DESC : Order.ASC);
		// );

		service.select(selectedProperties);

		if (skip > 0)
			service.skip(skip);

		if (top > 0)
			service.top(top);

		try {
			return service.execute(erpConfigContext);

		} catch (final ODataException e) {
			throw new HystrixBadRequestException(e.getMessage(), e);
		}
	}
}
