package vn.gpay.jitin.core.porder_product;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IPOrder_Product_Service extends Operations<porder_product> {
	public List<porder_product> get_product_inporder(Long orgrootid_link, Long porderid_link);
}
