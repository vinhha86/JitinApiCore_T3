package vn.gpay.jitin.core.api.stockout_order;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout_order.Stockout_orderObj;

public class Stockout_orderObj_response extends ResponseBase{
	public List<Stockout_orderObj> data;
	public long totalCount;
}
