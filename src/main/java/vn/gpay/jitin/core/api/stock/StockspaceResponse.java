package vn.gpay.jitin.core.api.stock;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stock.Stockspace;

public class StockspaceResponse extends ResponseBase{
	public Stockspace stockspace;
	public boolean isSpaceNew;
	public boolean isFloorNew;
}
