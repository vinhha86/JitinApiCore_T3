package vn.gpay.jitin.core.api.stockout;

import vn.gpay.jitin.core.base.RequestBase;

public class StockoutPkl_getByStockoutDLotAndPackageId_request extends RequestBase{
	public Long stockoutdid_link;
	public String lotnumber;
	public Integer packageid;
}
