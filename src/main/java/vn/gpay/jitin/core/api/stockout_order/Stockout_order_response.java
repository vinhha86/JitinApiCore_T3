package vn.gpay.jitin.core.api.stockout_order;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout_order.Stockout_order;

public class Stockout_order_response extends ResponseBase{
	public List<Stockout_order> data;
	public long totalCount;
}
