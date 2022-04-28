package vn.gpay.jitin.core.pcontractproduct;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IPContractProductService extends Operations<PContractProduct>{
	public List<PContractProduct> get_by_product_and_pcontract(long orgrootid_link, long productid_link, long pcontractid_link);
}
