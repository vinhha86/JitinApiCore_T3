package vn.gpay.jitin.core.api.stockin;


import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockin.StockIn;
import vn.gpay.jitin.core.stockin.StockInPklist;

public class GetStockinByIDResponse extends ResponseBase{
	public StockIn data;
	public List<StockInPklist> listepc;
}
