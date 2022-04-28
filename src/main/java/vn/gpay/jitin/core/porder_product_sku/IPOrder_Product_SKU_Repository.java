package vn.gpay.jitin.core.porder_product_sku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface IPOrder_Product_SKU_Repository extends JpaRepository<POrder_Product_SKU, Long>, JpaSpecificationExecutor<POrder_Product_SKU> {
	@Query(value = "select c from POrder_Product_SKU c "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and porderid_link = :porderid_link ")
	public List<POrder_Product_SKU> get_sku_inporder(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("porderid_link")final  Long porderid_link);
	
	@Query(value = "select c from POrder_Product_SKU c "
			+ "where c.skuid_link = :skuid_link "
			+ "and porderid_link = :porderid_link ")
	public List<POrder_Product_SKU> get_sku_in_encode(@Param ("skuid_link")final  Long skuid_link,
			@Param ("porderid_link")final  Long porderid_link);
	
	@Query(value = "select d from POrder c "
			+ "inner join POrder_Product_SKU d on c.id = d.porderid_link "
			+ "inner join SKU e on e.id = d.skuid_link "
			+ "where c.orgrootid_link = :orgrootid_link "
			+ "and ordercode = :ordercode "
			+ "and (e.code = :skucode or '' = :skucode ) "
			+ "and status <> -1")
	public List<POrder_Product_SKU> get_by_code_and_sku(@Param ("orgrootid_link")final  Long orgrootid_link,
			@Param ("ordercode")final  String ordercode,
			@Param ("skucode")final  String skucode);
}
