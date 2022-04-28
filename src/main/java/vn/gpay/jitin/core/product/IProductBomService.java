package vn.gpay.jitin.core.product;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IProductBomService extends Operations<ProductBOM> {
	public List<ProductBOM> getproductBOMbyid(long productid_link);
}
