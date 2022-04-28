package vn.gpay.jitin.core.api.stockout;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout.StockOutPklist;
import vn.gpay.jitin.core.warehouse.Warehouse;

public class StockOut_Pklist_rip_response extends ResponseBase{
	public StockOutPklist stockOutPklist;
	public Warehouse warehouse;
}
