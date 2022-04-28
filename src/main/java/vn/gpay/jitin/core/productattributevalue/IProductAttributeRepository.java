package vn.gpay.jitin.core.productattributevalue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IProductAttributeRepository extends JpaRepository<ProductAttributeValue, Long> {
	@Query(value = "select c from ProductAttributeValue c where c.productid_link= :productid_link ")
	public List<ProductAttributeValue> getall_byProductId(
			@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c from ProductAttributeValue c "
			+ "where c.attributeid_link = :attributeid_link"
			+ " and productid_link = :productid_link")
	public List<ProductAttributeValue> getlistvalue_byattribute(@Param ("attributeid_link")final  Long attributeid_link,
			@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select c from ProductAttributeValue c "
			+ "where c.attributeid_link = :attributeid_link "
			+ "and productid_link = :productid_link "
			+ "and attributevalueid_link = :attributevalueid_link")
	public List<ProductAttributeValue> getOne_byproduct_and_value(
			@Param ("attributeid_link")final  Long attributeid_link,
			@Param ("productid_link")final  Long productid_link,
			@Param ("attributevalueid_link")final  Long attributevalueid_link);
}
