package vn.gpay.jitin.core.api.stockout;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout.StockOutObj;

public class StockoutObjResponse extends ResponseBase{
	public List<StockOutObj> data;
	public long totalCount;
}
