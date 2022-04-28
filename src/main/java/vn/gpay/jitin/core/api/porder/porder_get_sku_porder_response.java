package vn.gpay.jitin.core.api.porder;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.porder_product_sku.POrder_Product_SKU_Encode;

public class porder_get_sku_porder_response extends ResponseBase {
	public List<POrder_Product_SKU_Encode> data;
}
