package vn.gpay.jitin.core.pcontratproductsku;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;


@Service
public class PContractProductSKUService extends AbstractService<PContractProductSKU> implements IPContractProductSKUService {
	@Autowired IPContractProductSKURepository repo;
	@Override
	protected JpaRepository<PContractProductSKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(long orgrootid_link, long productid_link,
			long pcontractid_link) {
		// TODO Auto-generated method stub
		return repo.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
	}

}
