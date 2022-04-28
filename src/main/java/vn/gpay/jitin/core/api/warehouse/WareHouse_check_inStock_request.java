package vn.gpay.jitin.core.api.warehouse;

import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;

public class WareHouse_check_inStock_request extends RequestBase{
	public Long stockid_link;
	public List<Long> skuIdList;
	
	public Long pcontract_poid_link;
}
