package vn.gpay.jitin.core.api.stockin;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockin.StockIn;

public class GetStockinByCodeOutput extends ResponseBase{
	public List<StockIn> data;
}
