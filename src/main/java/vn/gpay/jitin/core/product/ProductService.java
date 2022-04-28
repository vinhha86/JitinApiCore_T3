package vn.gpay.jitin.core.product;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.jitin.core.api.product.Product_getall_request;
import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.pcontractproductbom.IPContractProductBomRepository;

@Service
public class ProductService extends AbstractService<Product> implements IProductService {

	@Autowired IProductRepository repo;
	@Autowired IProductBOMRepository productbom_repo;
	@Autowired IPContractProductBomRepository ppbom_repo;

	@Override
	protected JpaRepository<Product, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<Product> getall_by_orgrootid(Long orgrootid_link, int product_type) {
		// TODO Auto-generated method stub
		return repo.getall_product_byorgrootid_link(orgrootid_link, product_type);
	}

	@Override
	public List<Product> getone_by_code(Long orgrootid_link, String code, Long productid_link, int product_type) {
		// TODO Auto-generated method stub
		return repo.get_byorgid_link_and_code(orgrootid_link,productid_link, code, product_type);
	}

	@Override
	public Page<Product> getall_by_orgrootid_paging(Long orgrootid_link, Product_getall_request request) {
		// TODO Auto-generated method stub
		
		Specification<Product> specification = Specifications.<Product>and()
	            .eq("product_type", request.product_type)
	            .eq("status", 1)
	            .eq("orgrootid_link", orgrootid_link)
        		.predicate(Specifications.or()
        				.like("name","%"+request.name.toUpperCase()+"%")
        	            .like("name","%"+request.name.toLowerCase()+"%")
        	            .build())
        		.predicate(Specifications.or()
                		.like("code","%"+request.code.toLowerCase()+"%")
                		.like("code","%"+request.code.toUpperCase()+"%")
        	            .build())
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		
		Page<Product> lst = repo.findAll(specification, PageRequest.of(request.page - 1, request.limit, sort));
		return lst;
	}

	@Override
	public List<Product> getList_material_notin_ProductBOM(Long orgrootid_link, String code, String name,
			String TenMauNPL, Long productid_link, int product_type) {

		List<Long> lstbom = productbom_repo.getall_materialid_in_productBOM(productid_link);
		
		Specification<Product> specification = Specifications.<Product>and()
				.eq(product_type !=0 ,"product_type", product_type)
	            .eq("status", 1)
	            .ne(product_type ==0 ,"product_type", 1)
	            .ne(product_type ==0 ,"product_type", 5)
	            .eq("orgrootid_link", orgrootid_link)
	            .like("name","%"+name+"%")
        		.like("code","%"+code+"%")
        		.notIn(lstbom.size() > 0,"id", lstbom.toArray())
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		List<Product> listNPL = repo.findAll(specification , sort);
		if(!TenMauNPL.equals(""))
		listNPL.removeIf(c -> !c.getTenMauNPL().equals(TenMauNPL));
		return listNPL;
	}

	@Override
	public List<Product> getList_material_notin_PContractProductBOM(Long orgrootid_link, String code, String name,
			String TenMauNPL, Long productid_link, int product_type, long pcontractid_link) {
List<Long> lstbom = ppbom_repo.getall_materialid_in_pcontract_productBOM(productid_link, pcontractid_link);
		
		Specification<Product> specification = Specifications.<Product>and()
				.eq(product_type !=0 ,"product_type", product_type)
	            .eq("status", 1)
	            .ne(product_type ==0 ,"product_type", 1)
	            .ne(product_type ==0 ,"product_type", 5)
	            .eq("orgrootid_link", orgrootid_link)
	            .like("name","%"+name+"%")
        		.like("code","%"+code+"%")
        		.notIn(lstbom.size() > 0,"id", lstbom.toArray())
	            .build();
		Sort sort = Sorts.builder()
		        .asc("name")
		        .build();
		List<Product> listNPL = repo.findAll(specification , sort);
		if(!TenMauNPL.equals(""))
		listNPL.removeIf(c -> !c.getTenMauNPL().equals(TenMauNPL));
		return listNPL;
	}

}
