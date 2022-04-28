package vn.gpay.jitin.core.api.list;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stock.Stockrow;

public class StockRowResponse extends ResponseBase {

	public List<Stockrow>  data = new ArrayList<>();
}	