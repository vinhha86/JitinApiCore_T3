package vn.gpay.jitin.core.porder_product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class POrder_Product_Service extends AbstractService<porder_product> implements IPOrder_Product_Service  {
	@Autowired IPOrder_product_repository repo;
	@Override
	protected JpaRepository<porder_product, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<porder_product> get_product_inporder(Long orgrootid_link, Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.get_product_inporder(orgrootid_link, porderid_link);
	}

}
