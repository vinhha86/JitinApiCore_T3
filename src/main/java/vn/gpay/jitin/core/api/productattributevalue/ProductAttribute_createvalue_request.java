package vn.gpay.jitin.core.api.productattributevalue;

import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;


public class ProductAttribute_createvalue_request extends RequestBase {
	public Long attributeid_link;
	public Long productid_link;
	public List<Long> listvalue;
}
