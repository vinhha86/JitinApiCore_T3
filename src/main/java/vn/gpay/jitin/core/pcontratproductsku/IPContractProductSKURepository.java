package vn.gpay.jitin.core.pcontratproductsku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPContractProductSKURepository extends JpaRepository<PContractProductSKU, Long>, JpaSpecificationExecutor<PContractProductSKU> {
	@Query(value = "select c from PContractProductSKU c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and productid_link = :productid_link "
			+ "and pcontractid_link = :pcontractid_link")
	public List<PContractProductSKU> getlistsku_byproduct_and_pcontract(
			@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("productid_link")final  long productid_link, 
			@Param ("pcontractid_link")final  long pcontractid_link);
}
