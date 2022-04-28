package vn.gpay.jitin.core.api.encode;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.porder_product_sku.POrder_Product_SKU;

public class Encode_porder_getlistsku_response extends ResponseBase {
	public List<POrder_Product_SKU> data;
}
