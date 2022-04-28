package vn.gpay.jitin.core.api.stockout;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.warehouse.Warehouse;

public class StockOut_Pklist_themMatTem_request extends RequestBase{
	public Warehouse warehouse;
	public Long skuid_link;
	public Long stockoutid_link;
	public Long stockoutdid_link;
}
