package vn.gpay.jitin.core.api.stockout;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout.StockOut;

public class StockoutResponse extends ResponseBase{
	public List<StockOut> data;
	public long totalCount;
}
