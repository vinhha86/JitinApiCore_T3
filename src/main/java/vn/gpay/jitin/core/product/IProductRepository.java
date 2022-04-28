package vn.gpay.jitin.core.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	@Query(value = "select c from Product c where c.orgrootid_link = :orgrootid_link "
			+ "and product_type = :product_type and c.status = 1")
	public List<Product> getall_product_byorgrootid_link(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("product_type")final  int product_type);
	
	@Query(value = "select c from Product c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and c.status = 1 "
			+ "and code = :code "
			+ "and id != :productid_link "
			+ "and product_type = :product_type")
	public List<Product> get_byorgid_link_and_code(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final  Long productid_link,
			@Param ("code")final  String code,
			@Param ("product_type")final  int product_type);
	
	
}
