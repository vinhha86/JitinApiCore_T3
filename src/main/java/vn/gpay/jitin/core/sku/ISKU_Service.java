package vn.gpay.jitin.core.sku;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface ISKU_Service extends Operations<SKU> {
	public List<SKU> getlist_byProduct(Long productid_link);
	
	public SKU get_bySkucode(String skucode);
	
	public List<SKU> getlist_bycode (List<String> listcode);
	public List<SKU> findSkuByType(int type, long orgrootid_link);
}
