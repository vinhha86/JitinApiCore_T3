package vn.gpay.jitin.core.pcontractproductpairing;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;




@Repository
@Transactional
public interface IPContractProductPairingRepository extends JpaRepository<PContractProductPairing, Long>, JpaSpecificationExecutor<PContractProductPairing> {
	@Query(value = "select c from PContractProductPairing c "
			+ "where c.pcontractid_link = :pcontractid_link "
			+ "and c.orgrootid_link = :orgrootid_link ")
	public List<PContractProductPairing> getall_product_pair_bypcontract(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from PContractProductPairing c "
			+ "where c.pcontractid_link = :pcontractid_link "
			+ "and c.orgrootid_link = :orgrootid_link "
			+ "and c.productpairid_link = :productpairid_link")
	public List<PContractProductPairing> getall_product_pairdetail_bypcontract(
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("productpairid_link")final  Long productpairid_link,
			@Param ("orgrootid_link")final  Long orgrootid_link);
//	
//	@Query(value = "select d.productid_link from PContractProductPairing c "
//			+ "inner join ProductPairing d on c.productpairid_link = d.productpairid_link "
//			+ "where c.pcontractid_link = :pcontractid_link "
//			+ "and c.orgrootid_link = :orgrootid_link "
//			+ "and (0= :productpairid_link or c.productpairid_link = :productpairid_link)")
//	public List<Long> getall_productid_pair_bypcontract(
//			@Param ("pcontractid_link")final  Long pcontractid_link,
//			@Param ("orgrootid_link")final  Long orgrootid_link,
//			@Param ("productpairid_link")final  Long productpairid_link);
}
