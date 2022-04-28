package vn.gpay.jitin.core.api.stockin;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockin.StockIn;

public class StockInResponse extends ResponseBase{
	public List<StockIn> data;
	public long totalCount;
}
