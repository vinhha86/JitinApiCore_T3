package vn.gpay.jitin.core.api.stockout;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.stockout.StockOutD;
import vn.gpay.jitin.core.stockout.StockOutPklist;

public class Stockout_PklistUpdateRequest extends RequestBase{
	public StockOutD data_stockout_d;
	public StockOutPklist data_pklist;//Dùng để update 1 dòng pklist đơn
}
