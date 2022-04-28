package vn.gpay.jitin.core.productpairing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface IProductPairingRepository extends JpaRepository<ProductPairing, Long>, JpaSpecificationExecutor<ProductPairing> {
	@Query(value = "select c from ProductPairing c "
			+ "inner join PContractProductPairing d on c.productpairid_link = d.productpairid_link "
			+ "where d.pcontractid_link = :pcontractid_link "
			+ "and c.orgrootid_link = :orgrootid_link ")
	public List<ProductPairing> getall_product_pair_bypcontract(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c.productid_link from ProductPairing c "
			+ "inner join PContractProductPairing d on c.productpairid_link = d.productpairid_link "
			+ "where d.pcontractid_link = :pcontractid_link "
			+ "and d.orgrootid_link = :orgrootid_link ")
	public List<Long> getall_productid_pair_bypcontract(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from ProductPairing c "
			+ "inner join PContractProductPairing d on c.productpairid_link = d.productpairid_link "
			+ "where d.pcontractid_link = :pcontractid_link "
			+ "and d.orgrootid_link = :orgrootid_link "
			+ "and c.productpairid_link = :productpairid_link")
	public List<ProductPairing> getall_product_pair_detail_bypcontract(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productpairid_link")final  Long productpairid_link,
			@Param ("orgrootid_link")final  Long orgrootid_link);
}
