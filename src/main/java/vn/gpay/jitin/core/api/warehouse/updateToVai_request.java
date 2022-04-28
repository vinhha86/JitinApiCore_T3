package vn.gpay.jitin.core.api.warehouse;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.stockout_order.Stockout_order_pkl;
import vn.gpay.jitin.core.warehouse_check.WarehouseCheck;

public class updateToVai_request extends RequestBase {
	public Stockout_order_pkl stockout_order_pkl;
	public WarehouseCheck warehouse_check;
	public Long stockoutorderid_link;
	public Long stockoutorderdid_link;
	public String lotnumber;
}
