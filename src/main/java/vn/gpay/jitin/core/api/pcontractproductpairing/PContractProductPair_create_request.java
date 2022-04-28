package vn.gpay.jitin.core.api.pcontractproductpairing;

import java.util.List;

import vn.gpay.jitin.core.productpairing.ProductPairing;


public class PContractProductPair_create_request {
	public long pcontractid_link;
	public long productpairid_link;
	public List<ProductPairing> listpair;
}
