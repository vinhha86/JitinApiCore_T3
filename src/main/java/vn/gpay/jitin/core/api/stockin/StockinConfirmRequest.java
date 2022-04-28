package vn.gpay.jitin.core.api.stockin;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.stockin.StockIn;

public class StockinConfirmRequest extends RequestBase{
	public StockIn stockin;
	public Long stockinId;
	public Long approver_userid_link;
}
