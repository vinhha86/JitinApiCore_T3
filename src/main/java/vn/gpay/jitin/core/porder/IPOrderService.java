package vn.gpay.jitin.core.porder;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;
import vn.gpay.jitin.core.porder_product_sku.POrder_Product_SKU;

public interface IPOrderService extends Operations<POrder> {
	public List<POrder> get_list(String ordercode, long orgrootid_link);
	public List<POrder> get_by_code(String ordercode, long orgrootid_link);
	public List<POrder_Product_SKU> get_list_sku(String ordercode, long orgrootid_link, String skucode);
	public List<POrder_Product_SKU> get_by_code_and_sku(String ordercode, long orgrootid_link, String skucode);
	List<Long> getidby_buyercode(String buyercode);
}
