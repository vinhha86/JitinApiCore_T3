package vn.gpay.jitin.core.api.stockin;
import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockin.StockInD;

public class StockinList_ByInvoiceAndSku_Response extends ResponseBase{
	public List<StockInD> data;
}
