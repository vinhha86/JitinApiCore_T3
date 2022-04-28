package vn.gpay.jitin.core.api.encode;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.encode.Encode;
import vn.gpay.jitin.core.porder_product_sku.POrder_Product_SKU_Encode;

public class Encode_update_epc_response extends ResponseBase{
	public Encode data;
	public POrder_Product_SKU_Encode skuencode; 
}
