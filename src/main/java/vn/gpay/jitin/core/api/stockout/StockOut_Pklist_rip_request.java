package vn.gpay.jitin.core.api.stockout;

import vn.gpay.jitin.core.base.RequestBase;

public class StockOut_Pklist_rip_request extends RequestBase{
	public Long id;
    public String lotnumber;
    public Integer packageid;
    public Float met_check;
    public Float ydscheck;
    public Float met_remain;
    public Float yds_remain;
}
