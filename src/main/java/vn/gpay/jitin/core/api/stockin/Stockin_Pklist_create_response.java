package vn.gpay.jitin.core.api.stockin;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockin.StockInPklist;

public class Stockin_Pklist_create_response extends ResponseBase {
	public StockInPklist data;
	public long id;
	public long rfprintid_link;
}
