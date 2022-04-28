package vn.gpay.jitin.core.porder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.porder_product_sku.IPOrder_Product_SKU_Repository;
import vn.gpay.jitin.core.porder_product_sku.POrder_Product_SKU;

@Service
public class POrderService extends AbstractService<POrder> implements IPOrderService {
	@Autowired IPOrderRepository repo;
	@Autowired IPOrder_Product_SKU_Repository repo_sku;
	@Override
	protected JpaRepository<POrder, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<POrder_Product_SKU> get_list_sku(String ordercode, long orgrootid_link, String skucode) {
		// TODO Auto-generated method stub
		ordercode = '%'+ordercode+'%';
		Specification<POrder_Product_SKU> specification = Specifications.<POrder_Product_SKU>and()
	            .ne("porder.status", -1)
	            .eq("porder.orgrootid_link", orgrootid_link)
        		.predicate(Specifications.or()
        				.like("porder.ordercode",ordercode)
        	            .build())
        		.predicate(Specifications.or()
        				.like("sku.code",'%'+skucode+'%')
        	            .build())
	            .build();
		Sort sort = Sorts.builder()
		        .asc("porder.ordercode")
		        .build();
		
		List<POrder_Product_SKU> lst_sku = repo_sku.findAll(specification, sort);
		
		return lst_sku;
	}
	@Override
	public List<POrder> get_by_code(String ordercode, long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.get_by_code(orgrootid_link, ordercode);
	}
	@Override
	public List<POrder> get_list(String ordercode, long orgrootid_link) {
		return null;
	}
	@Override
	public List<POrder_Product_SKU> get_by_code_and_sku(String ordercode, long orgrootid_link, String skucode) {
		return repo_sku.get_by_code_and_sku(orgrootid_link, ordercode, skucode);
	}
	@Override
	public List<Long> getidby_buyercode(String buyercode) {
		return repo.getidby_buyercode(buyercode);
	}

}
