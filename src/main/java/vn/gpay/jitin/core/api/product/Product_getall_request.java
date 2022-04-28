package vn.gpay.jitin.core.api.product;

import vn.gpay.jitin.core.base.RequestBase;

public class Product_getall_request extends RequestBase {
	public int product_type;
	public int limit;
	public int page;
	public String name;
	public String code;
}
