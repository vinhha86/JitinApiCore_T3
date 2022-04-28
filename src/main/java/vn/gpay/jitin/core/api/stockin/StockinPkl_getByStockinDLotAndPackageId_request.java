package vn.gpay.jitin.core.api.stockin;

import vn.gpay.jitin.core.base.RequestBase;

public class StockinPkl_getByStockinDLotAndPackageId_request extends RequestBase{
	public Long stockinid_link;
	public Long stockindid_link;
	public String lotnumber;
	public Integer packageid;
}
