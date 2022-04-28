package vn.gpay.jitin.core.api.stockout_order;

import java.util.Date;

import vn.gpay.jitin.core.base.RequestBase;

public class Stockout_order_getBySearch_request extends RequestBase{
	public Date stockoutorderdate_from;
	public Date stockoutorderdate_to;
	public Integer stockouttypeid_link;
	public Long porder_grantid_link;
	public Integer status;
	public int page;
	public int limit;
}
