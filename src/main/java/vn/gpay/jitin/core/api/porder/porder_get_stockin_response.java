package vn.gpay.jitin.core.api.porder;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockin.StockInD;

public class porder_get_stockin_response extends ResponseBase {
	public List<StockInD> data;
}
