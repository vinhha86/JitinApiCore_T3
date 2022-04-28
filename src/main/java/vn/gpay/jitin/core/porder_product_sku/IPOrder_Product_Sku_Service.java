package vn.gpay.jitin.core.porder_product_sku;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IPOrder_Product_Sku_Service extends Operations<POrder_Product_SKU> {
	public List<POrder_Product_SKU> getlist_sku_in_porder(Long orgrootid_link, Long porderid_link);
	public POrder_Product_SKU get_sku_in_encode(Long porderid_link , Long skuid_link);
}
