package vn.gpay.jitin.core.porder_product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface IPOrder_product_repository extends JpaRepository<porder_product, Long>, JpaSpecificationExecutor<porder_product> {
	@Query(value = "select c from porder_product c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and porderid_link = :porderid_link ")
	public List<porder_product> get_product_inporder(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("porderid_link")final  Long porderid_link);
}
