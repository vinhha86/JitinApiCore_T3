package vn.gpay.jitin.core.pcontractproductpairing;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface IPContractProductPairingService extends Operations<PContractProductPairing> {
	public List<PContractProductPairing> getall_bypcontract(long orgrootid_link, long pcontractid_link);
	public List<PContractProductPairing> getdetail_bypcontract_and_productpair(long orgrootid_link, long pcontractid_link, long productpairid_link);
}
