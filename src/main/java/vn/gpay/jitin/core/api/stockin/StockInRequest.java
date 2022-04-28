package vn.gpay.jitin.core.api.stockin;

import java.util.List;

import vn.gpay.jitin.core.stockin.StockIn;
import vn.gpay.jitin.core.stockin.StockInD;

public class StockInRequest {
	public StockIn stockin;
	public List<StockInD> stockind;
	public Long skuid_link;
    public String lotnumber;
    public Integer packageid;
    public Long stockindid_link;
}
