package vn.gpay.jitin.core.pcontractproductbom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface IPContractProductBomRepository extends JpaRepository<PContractProductBom, Long>, JpaSpecificationExecutor<PContractProductBom> {
	@Query(value = "select c from PContractProductBom c "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link")
	public List<PContractProductBom> getall_material_in_pcontract_productBOM(
			@Param ("productid_link")final  Long productid_link, 
			@Param ("pcontractid_link")final  Long pcontractid_link);
	
	@Query(value = "select c from PContractProductBom c "
			+ "where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link "
			+ "and c.materialid_link = :materialid_link")
	public List<PContractProductBom> getby_material_pcontract_product(
			@Param ("productid_link")final  Long productid_link, 
			@Param ("pcontractid_link")final  Long pcontractid_link,
			@Param ("materialid_link")final  Long materialid_link);
	
	@Query(value = "select c.materialid_link from PContractProductBom c"
			+ " where c.productid_link = :productid_link "
			+ "and c.pcontractid_link = :pcontractid_link")
	public List<Long> getall_materialid_in_pcontract_productBOM(
			@Param ("productid_link")final  Long productid_link,
			@Param ("pcontractid_link")final  Long pcontractid_link);
}
