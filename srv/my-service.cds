// using my.app from '../db/data-model';

// service CrudService {
// 	@cds.persistence.skip
// 	Entity BusinessPartner as projection on app.BusinessPartner;
// }
using API_BUSINESS_PARTNER as bp from './external/csn/API_BUSINESS_PARTNER';

service CrudService{

 @cds.persistence.skip
 Entity BusinessPartner as projection on bp.A_BusinessPartnerType{
   BusinessPartner,
   LastName,
   FirstName,
   BusinessPartnerCategory,
   BusinessPartnerUUID
  };
}