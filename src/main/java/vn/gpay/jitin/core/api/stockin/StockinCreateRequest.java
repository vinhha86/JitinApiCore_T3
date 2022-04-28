package vn.gpay.jitin.core.api.stockin;

import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.stockin.StockIn;

public class StockinCreateRequest extends RequestBase{
	public List<StockIn> data;
	//
	public Long stockindid_link;
	public Integer TPGroupStoreValue;
}
