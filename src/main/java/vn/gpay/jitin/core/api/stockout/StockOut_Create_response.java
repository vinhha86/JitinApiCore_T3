package vn.gpay.jitin.core.api.stockout;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout.StockOut;
import vn.gpay.jitin.core.stockout.StockOutPklist;

public class StockOut_Create_response extends ResponseBase {
	public long id;
	public StockOut data;
	public List<StockOutPklist> epc_err = new ArrayList<StockOutPklist>();
}
