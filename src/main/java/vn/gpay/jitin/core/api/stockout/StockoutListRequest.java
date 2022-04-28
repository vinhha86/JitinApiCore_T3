package vn.gpay.jitin.core.api.stockout;

import java.util.Date;
import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;

public class StockoutListRequest extends RequestBase{

	public Long orgid_to_link;
	public Long orgid_from_link;
	public String stockoutcode;
	public Date stockoutdate_from;
	public Date stockoutdate_to;
	public Integer stockouttypeid_link;
	public int page;
	public int limit;
	public Long stockouttypefrom;
	public Long stockouttypeto;
	public Integer status =  null;
	public List<Integer> statuses =  null;
	public Long skuid_link;
	public String product;
	public String maNpl;
	public String lotnumber;
}
