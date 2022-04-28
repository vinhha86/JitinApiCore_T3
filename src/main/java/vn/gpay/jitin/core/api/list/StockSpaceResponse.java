package vn.gpay.jitin.core.api.list;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.stock.Stockspace;

public class StockSpaceResponse extends ResponseBase {

	public List<Stockspace>  data = new ArrayList<>();
}	