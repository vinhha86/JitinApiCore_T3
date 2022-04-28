package vn.gpay.jitin.core.stockin;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockInPklistRepository extends JpaRepository<StockInPklist, Long>,JpaSpecificationExecutor<StockInPklist>{

	@Query(value = "select a from StockInPklist a inner join StockInD b on a.stockindid_link = b.id where b.stockinid_link =:stockinid_link")
	List<StockInPklist> inv_getbyid(@Param ("stockinid_link")final long stockinid_link) ;
	
	@Query(value = "select max(a.lasttimeupdate) from StockInPklist a "
			+ "where a.stockinid_link =:stockinid_link")
	Date getLastTimePklistUpdate(
			@Param ("stockinid_link")final Long stockinid_link
			) ;
	
	
	@Query(value = "select a from StockInPklist a "
			+ "where a.stockindid_link =:stockindid_link "
			+ "and a.lotnumber = :lotnumber "
			+ "and a.status >= :status")
	List<StockInPklist> getStockinPklByStockinDIdAndGreaterThanStatus(
			@Param ("stockindid_link")final Long stockindid_link,
			@Param ("lotnumber")final String lotnumber,
			@Param ("status")final Integer status
			) ;
	
	@Query(value = "select a from StockInPklist a "
			+ "where a.stockindid_link =:stockindid_link "
			+ "and a.lotnumber = :lotnumber "
			+ "and a.status = :status")
	List<StockInPklist> getStockinPklByStockinDIdAndEqualStatus(
			@Param ("stockindid_link")final Long stockindid_link,
			@Param ("lotnumber")final String lotnumber,
			@Param ("status")final Integer status
			) ;
	
	@Query(value = "select a from StockInPklist a "
			+ "where a.stockindid_link = :stockindid_link "
			+ "and lower(a.lotnumber) = lower(:lotnumber) "
			+ "and a.packageid = :packageid")
	List<StockInPklist> getBy_Lot_Packageid(
			@Param ("stockindid_link")final Long stockindid_link,
			@Param ("lotnumber")final String lotnumber,
			@Param ("packageid")final Integer packageid
			) ;
	
	@Query(value = "select a from StockInPklist a "
			+ "where a.stockindid_link = :stockindid_link "
			+ "and lower(a.lotnumber) = lower(:lotnumber) "
			+ "and a.packageid = :packageid "
			+ "and a.status < 2"
			)
	List<StockInPklist> getStockinPklByStockinDLotAndPackageIdNotCheck10(
			@Param ("stockindid_link")final Long stockindid_link,
			@Param ("lotnumber")final String lotnumber,
			@Param ("packageid")final Integer packageid
			) ;
	
	@Query(value = "select a from StockInPklist a "
			+ "where a.stockindid_link = :stockindid_link "
			+ "and lower(a.lotnumber) = lower(:lotnumber) "
			+ "and a.packageid = :packageid "
			+ "and a.id != :id "
			)
	List<StockInPklist> getBy_Lot_Packageid_other(
			@Param ("stockindid_link")final Long stockindid_link,
			@Param ("lotnumber")final String lotnumber,
			@Param ("packageid")final Integer packageid,
			@Param ("id")final Long id
			) ;
	
	@Query(value = "select a from StockInPklist a "
			+ "where a.stockindid_link = :stockindid_link "
			+ "and lower(a.lotnumber) = lower(:lotnumber) "
			+ "and a.packageid = :packageid "
			+ "and a.id != :id ")
	List<StockInPklist> getBy_Lot_Packageid_and_notId(
			@Param ("stockindid_link")final Long stockindid_link,
			@Param ("lotnumber")final String lotnumber,
			@Param ("packageid")final Integer packageid,
			@Param ("id")final Long id
			) ;
	
	@Query(value = "select a from StockInPklist a "
			+ "where a.stockindid_link = :stockindid_link "
			+ "and lower(a.lotnumber) = lower(:lotnumber) ")
	List<StockInPklist> getBy_Lot_StockinD(
			@Param ("stockindid_link")final Long stockindid_link,
			@Param ("lotnumber")final String lotnumber
			) ;
	
	@Query(value = "select a from StockInPklist a "
			+ "where a.stockindid_link = :stockindid_link ")
	List<StockInPklist> getByStockinDId(
			@Param ("stockindid_link")final Long stockindid_link
			) ;
	
	@Query(value = "select a from StockInPklist a "
			+ "inner join StockInD b on a.stockindid_link = b.id "
			+ "where b.stockinid_link = :stockinid_link ")
	List<StockInPklist> getByStockinId(
			@Param ("stockinid_link")final Long stockinid_link
			) ;
	
	@Query(value = "select distinct a.lotnumber from StockInPklist a "
			+ "where a.stockindid_link = :stockindid_link ")
	List<String> getLotnumberList_By_stockind(
			@Param ("stockindid_link")final Long stockindid_link
			) ;
}
