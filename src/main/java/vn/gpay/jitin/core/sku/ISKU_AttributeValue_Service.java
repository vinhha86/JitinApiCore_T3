package vn.gpay.jitin.core.sku;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface ISKU_AttributeValue_Service extends Operations<SKU_Attribute_Value> {
	public List<SKU_Attribute_Value> getlist_byProduct_and_value(Long productid_link, Long attributevalueid_link);
	
	public List<SKU_Attribute_Value> getlist_byProduct_and_attribute(Long productid_link, Long attributeid_link);

	public List<SKU_Attribute_Value> getlist_byproduct(Long productid_link);
	
	public long getsku_byproduct_and_valuemau_valueco(long productid_link, long valuemau, long valueco);
}	
