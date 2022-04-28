package vn.gpay.jitin.core.productpairing;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IProductPairingService extends Operations<ProductPairing> {
	public List<ProductPairing> getproduct_pairing_bycontract(long orgrootid_link, long pcontractid_link);
	public List<ProductPairing> getproduct_pairing_detail_bycontract(long orgrootid_link, long pcontractid_link, long productpairid_link);
	public List<Long> getproductid_pairing_bycontract(long orgrootid_link, long pcontractid_link);
}
