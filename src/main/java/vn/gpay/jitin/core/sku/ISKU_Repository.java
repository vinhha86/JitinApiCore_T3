package vn.gpay.jitin.core.sku;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@Transactional
public interface ISKU_Repository extends JpaRepository<SKU, Long> {
	@Query(value = "select c from SKU c "
			+ " where c.productid_link = :productid_link  order by c.id DESC")
	public List<SKU> getlist_byproduct(@Param ("productid_link")final  Long productid_link);
	
	@Query(value = "select a from SKU a where a.code = :skucode")
	public List<SKU> getlist_byskucode(@Param ("skucode")final String skucode);
	
	@Query(value = "select a from SKU a where a.skutypeid_link = :type and orgrootid_link = :orgrootid_link")
	public List<SKU> findSkuByType(@Param ("type")final int type, @Param ("orgrootid_link")final long orgrootid_link);
	}
