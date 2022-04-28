package vn.gpay.jitin.core.attributevalue;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IAttributeValueService extends Operations<Attributevalue> {
	public List<Attributevalue> getlist_byidAttribute(Long attributeid_link);
	public List<Attributevalue> getlistid_notin_pcontract_attribute(long orgrootid_link, long pcontractid_link, long productid_link, long attributeid_link);
}
