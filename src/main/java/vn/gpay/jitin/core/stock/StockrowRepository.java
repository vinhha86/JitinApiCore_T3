package vn.gpay.jitin.core.stock;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface StockrowRepository extends JpaRepository<Stockrow, Long>{

	@Query(value = "select c from Stockrow c where orgid_link =:orgid")
	public List<Stockrow> findStockrowByOrgID(@Param ("orgid")final long orgid);
	
	@Query(value = "select c from Stockrow c where c.orgid_link = :orgid_link "
			+ "and c.code = :code"
			)
	public List<Stockrow> findStockrowByOrgIDAndCode(
			@Param ("orgid_link")final Long orgid_link,
			@Param ("code")final String code
			);
	
	@Query(value = "select c from Stockrow c where c.orgid_link = :orgid_link "
			+ "and c.code = :code and c.id != :id"
			)
	public List<Stockrow> findStockrowByOrgIDAndCodeAndNotId(
			@Param ("orgid_link")final Long orgid_link,
			@Param ("code")final String code,
			@Param ("id")final Long id
			);
	
	@Query(value = "select c from Stockrow c " 
			+ "inner join Org b on c.orgid_link = b.id "
			+ "where b.orgtypeid_link = :orgtypeid_link "
			)
	public List<Stockrow> findStockRowByOrgtype(
			@Param ("orgtypeid_link")final Integer orgtypeid_link
			);
}
