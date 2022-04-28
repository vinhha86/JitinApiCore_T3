package vn.gpay.jitin.core.api.porder;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockin.StockInD;

public class get_sku_by_porder_response extends ResponseBase{
	public List<StockInD> data;
}
