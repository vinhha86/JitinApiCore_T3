package vn.gpay.jitin.core.api.stockout;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockout.StockOut;
import vn.gpay.jitin.core.stockout.StockOutPklist;

public class StockoutByIDReponse extends ResponseBase{
	public StockOut data;
	public List<StockOutPklist> listepc;
}
