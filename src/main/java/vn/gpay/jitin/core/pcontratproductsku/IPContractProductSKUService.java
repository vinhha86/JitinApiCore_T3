package vn.gpay.jitin.core.pcontratproductsku;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IPContractProductSKUService extends Operations<PContractProductSKU> {
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(long orgrootid_link, long productid_link, long pcontractid_link);
	
}
