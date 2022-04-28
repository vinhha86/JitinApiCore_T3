package vn.gpay.jitin.core.api.warehouse;

import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.warehouse.Warehouse;

public class Warehouse_checkInStockout_request extends RequestBase{
	public List<Warehouse> data;
	public Long stockoutid_link;
}
