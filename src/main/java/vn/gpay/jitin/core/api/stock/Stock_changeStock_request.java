package vn.gpay.jitin.core.api.stock;

import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.stock.StockTree;
import vn.gpay.jitin.core.warehouse.Warehouse;

public class Stock_changeStock_request extends RequestBase{
	public List<Warehouse> selection; // ds cay vai
	public StockTree selectedStock; // onj thong tin stock
	public Long orgid_link;
	public String row;
	public String space;
	public Integer floor;
}
