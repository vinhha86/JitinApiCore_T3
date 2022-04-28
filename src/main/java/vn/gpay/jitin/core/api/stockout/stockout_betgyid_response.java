package vn.gpay.jitin.core.api.stockout;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout.StockOut;
import vn.gpay.jitin.core.warehouse.Warehouse;

public class stockout_betgyid_response extends ResponseBase {
	public List<Warehouse> listepc;
	public StockOut data;
}
