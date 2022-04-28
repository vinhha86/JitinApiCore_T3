package vn.gpay.jitin.core.api.stockout;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout.StockOutPklist;

public class StockOut_Pklist_response extends ResponseBase{
	public List<StockOutPklist> data;
}
