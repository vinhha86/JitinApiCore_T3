package vn.gpay.jitin.core.api.list;

import java.util.ArrayList;
import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.sku.SKU;

public class SkuResponse extends ResponseBase {

	public List<SKU>  data = new ArrayList<>();
}	
