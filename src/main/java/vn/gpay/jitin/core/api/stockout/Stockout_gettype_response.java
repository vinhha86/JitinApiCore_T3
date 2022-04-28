package vn.gpay.jitin.core.api.stockout;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout_type.StockoutType;

public class Stockout_gettype_response extends ResponseBase {
	public List<StockoutType> data;
}
