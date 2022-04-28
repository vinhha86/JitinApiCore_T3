package vn.gpay.jitin.core.stockout;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockOutPklistRepository extends JpaRepository<StockOutPklist, Long>, JpaSpecificationExecutor<StockOutPklist>{

	@Query(value = "select a from StockOutPklist a "
			+ "inner join StockOutD b on a.stockoutdid_link = b.id "
			+ "where b.stockoutid_link =:stockoutid_link")
	List<StockOutPklist> inv_getbyid(@Param ("stockoutid_link")final long stockoutid_link) ;
	
	@Query(value = "select a from StockOutPklist a "
			+ "where a.stockoutdid_link =:stockoutdid_link")
	List<StockOutPklist> getBystockOutDId(@Param ("stockoutdid_link")final Long stockoutdid_link);
	
	@Query(value = "select a from StockOutPklist a "
			+ "where a.stockoutdid_link =:stockoutdid_link "
			+ "and lower(a.lotnumber) = lower(:lotnumber) "
			+ "and a.packageid =:packageid "
			)
	List<StockOutPklist> getByLotnumberAndPackageId(
			@Param ("stockoutdid_link")final Long stockoutdid_link,
			@Param ("lotnumber")final String lotnumber,
			@Param ("packageid")final Integer packageid
			) ;
	
	@Query(value = "select a from StockOutPklist a "
			+ "where a.stockoutdid_link =:stockoutdid_link "
			+ "and lower(a.epc) = lower(:epc) "
			)
	List<StockOutPklist> getByEpc(
			@Param ("stockoutdid_link")final Long stockoutdid_link,
			@Param ("epc")final String epc
			) ;
	
	@Query(value = "select a from StockOutPklist a "
			+ "where a.stockoutdid_link =:stockoutdid_link "
			+ "and a.status = 1 " // status 1 -> cây vải đã xé
			)
	List<StockOutPklist> getBystockOutDId_rip(@Param ("stockoutdid_link")final Long stockoutdid_link) ;
	
	@Query(value = " select a from StockOutPklist a "
			+ " inner join StockOutD b on a.stockoutdid_link = b.id "
			+ " where b.stockoutid_link =:stockoutid_link "
			+ " and lower(a.lotnumber) like lower(concat('%',:lotnumber,'%')) "
			)
	List<StockOutPklist> getBy_Lotnumber_StockoutId(
			@Param ("lotnumber")final String lotnumber,
			@Param ("stockoutid_link")final Long stockoutid_link
			);
	
	@Query(value = " select a from StockOutPklist a "
			+ " inner join StockOutD b on a.stockoutdid_link = b.id "
			+ " inner join StockOut c on b.stockoutid_link = c.id "
			+ " where a.epc = :epc "
			+ " and c.status in :statuses "
			+ " and (c.id != :stockoutid or :stockoutid is null) "
			)
	List<StockOutPklist> getByEpc_in_Stockout(
			@Param ("epc")final String epc,
			@Param ("statuses")final List<Integer> statuses,
			@Param ("stockoutid")final Long stockoutid
			);
	
    @Modifying
    @Query("delete from StockOutPklist a where a.stockoutdid_link = :stockoutdid_link")
    void deleteByStockOutD(@Param ("stockoutdid_link")final long stockoutdid_link);
}
