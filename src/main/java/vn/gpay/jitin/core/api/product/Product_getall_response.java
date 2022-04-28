package vn.gpay.jitin.core.api.product;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.product.Product;
import vn.gpay.jitin.core.product.ProductBinding;

public class Product_getall_response extends ResponseBase {
	public List<Product> data;
	public List<ProductBinding> pagedata;
	public long totalCount;
}
