package vn.gpay.jitin.core.api.warehouse;

import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;

public class Warehouse_list_request extends RequestBase{
	public Long stockoutorderid_link;
	public Long skuid_link;
	public Long pordergrantid_link;
	public List<String> listSelectedEpc;
}
