package vn.gpay.jitin.core.api.stockin;

import vn.gpay.jitin.core.base.RequestBase;

public class StockinPkl_getByStockinDIdAndStatus_request extends RequestBase{
	public Long stockindid_link;
	public String lotnumber;
	public Integer status;
}
