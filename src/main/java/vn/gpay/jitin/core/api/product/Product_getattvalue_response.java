package vn.gpay.jitin.core.api.product;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.productattributevalue.ProductAttributeValueBinding;


public class Product_getattvalue_response extends ResponseBase {
	public List<ProductAttributeValueBinding> data;
}
