package vn.gpay.jitin.core.pcontractattributevalue;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IPContractProductAtrributeValueService extends Operations<PContractAttributeValue> {
	public List<PContractAttributeValue> getattribute_by_product_and_pcontract(long orgrootid_link, long pcontractid_link, long productid_link);
	public List<Long> getvalueid_by_product_and_pcontract_and_attribute(long orgrootid_link, long pcontractid_link, long productid_link, long attributeid_link);
	public List<PContractAttributeValue> getvalue_by_product_and_pcontract_and_attribute(long orgrootid_link, long pcontractid_link, long productid_link, long attributeid_link);
}
