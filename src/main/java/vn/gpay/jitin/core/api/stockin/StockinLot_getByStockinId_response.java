package vn.gpay.jitin.core.api.stockin;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stockin.StockinLot;

public class StockinLot_getByStockinId_response extends ResponseBase{
	public List<StockinLot> data;
}
