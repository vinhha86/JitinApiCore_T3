package vn.gpay.jitin.core.api.stockin;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockin.StockinProduct;

public class StockinProduct_getByStockinId_response extends ResponseBase{
	public List<StockinProduct> data;
}
