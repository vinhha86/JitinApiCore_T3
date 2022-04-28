package vn.gpay.jitin.core.packinglist;

import java.util.List;

import vn.gpay.jitin.core.packinglist.LotNumber;
import vn.gpay.jitin.core.packinglist.PackingList;
import vn.gpay.jitin.core.base.Operations;

public interface IPackingListService extends Operations<PackingList>{

	public List<PackingList> findByStockinDID(long stockindid);
	
	public List<PackingList> inv_getbyid(long invoiceid_link);
	
	public List<PackingList> getPKL_by_invoiced(long invoicedid_link);
	
	public List<LotNumber> getLotNumber_byinvoiced(long invoicedid_link);
	
	public List<PackingList> getbylotnumber(String lotnumber, long invoicedid_link);
}
