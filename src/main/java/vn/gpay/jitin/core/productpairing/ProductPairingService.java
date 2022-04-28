package vn.gpay.jitin.core.productpairing;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.pcontractproduct.IPContractProductRepository;
import vn.gpay.jitin.core.pcontractproductpairing.IPContractProductPairingRepository;


@Service
public class ProductPairingService extends AbstractService<ProductPairing> implements IProductPairingService {
	@Autowired IProductPairingRepository repo;
	@Autowired IPContractProductPairingRepository pppair_repo;
	@Autowired IPContractProductRepository pp_repo;
	@Override
	protected JpaRepository<ProductPairing, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<ProductPairing> getproduct_pairing_bycontract(long orgrootid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getall_product_pair_bypcontract(pcontractid_link, orgrootid_link);
	}
	@Override
	public List<Long> getproductid_pairing_bycontract(long orgrootid_link, long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getall_productid_pair_bypcontract(pcontractid_link, orgrootid_link);
	}
	@Override
	public List<ProductPairing> getproduct_pairing_detail_bycontract(long orgrootid_link, long pcontractid_link,
			long productpairid_link) {
		// TODO Auto-generated method stub
		return repo.getall_product_pair_detail_bypcontract(pcontractid_link, productpairid_link, orgrootid_link);
	}

}
