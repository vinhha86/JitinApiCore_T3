package vn.gpay.jitin.core.api.stock;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stock.StockTree;
import vn.gpay.jitin.core.stock.Stockrow;

public class StockResponse extends ResponseBase{
	public List<Stockrow> stockrow_list;
	public List<StockTree> children;
	public List<String> space_list;
}
