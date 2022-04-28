package vn.gpay.jitin.core.salebill;

import java.util.Date;
import java.util.List;

import vn.gpay.jitin.core.base.Operations;



public interface ISaleBillService extends Operations<SaleBill>{
	
	
	public List<SaleBill> salebill_list(Long orgbillid_link, String billcode,Date salebilldate_from,Date salebilldate_to);
	
}
