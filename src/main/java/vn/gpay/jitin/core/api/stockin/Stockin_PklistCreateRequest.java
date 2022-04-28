package vn.gpay.jitin.core.api.stockin;

import vn.gpay.jitin.core.base.RequestBase;
import vn.gpay.jitin.core.stockin.StockInPklist;

public class Stockin_PklistCreateRequest extends RequestBase{
	public boolean rfid_enable = false;
	public boolean isprintlabel = false;
	public StockInPklist data;
}
