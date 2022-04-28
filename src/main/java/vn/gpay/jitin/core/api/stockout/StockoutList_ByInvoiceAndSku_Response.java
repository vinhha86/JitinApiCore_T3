package vn.gpay.jitin.core.api.stockout;
import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout.StockOutD;

public class StockoutList_ByInvoiceAndSku_Response extends ResponseBase{
	public List<StockOutD> data;
}
