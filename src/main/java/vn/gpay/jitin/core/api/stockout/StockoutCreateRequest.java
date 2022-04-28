package vn.gpay.jitin.core.api.stockout;

import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.stockout.StockOut;

public class StockoutCreateRequest extends RequestBase{

	public List<StockOut> data;
	//
	public Long stockoutdid_link;
	public Integer TPGroupStoreValue;
}
