package vn.gpay.jitin.core.stockout_order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Stockout_order_pkl_repository extends JpaRepository<Stockout_order_pkl, Long>  {
	
	@Query(value = "select a from Stockout_order_pkl a "
			+ " where a.stockoutorderdid_link =:stockoutorderdid_link")
	public List<Stockout_order_pkl> getByStockoutOrderDId(
			@Param ("stockoutorderdid_link")final Long stockoutorderdid_link
			);
	
	@Query(value = "select a from Stockout_order_pkl a "
			+ " where a.stockoutorderdid_link =:stockoutorderdid_link"
			+ " and a.lotnumber =:lotnumber"
			+ " and a.packageid =:packageid"
			)
	public List<Stockout_order_pkl> getByLotnumberAndPackageId(
			@Param ("stockoutorderdid_link")final Long stockoutorderdid_link,
			@Param ("lotnumber")final String lotnumber,
			@Param ("packageid")final Integer packageid
			);
	
	@Query(value = "select a from Stockout_order_pkl a "
			+ " where a.stockoutorderdid_link = :stockoutorderdid_link"
			+ " and a.lotnumber =:lotnumber"
			)
	public List<Stockout_order_pkl> getByStockoutOrderDId_lotnumber(
			@Param ("stockoutorderdid_link")final Long stockoutorderdid_link,
			@Param ("lotnumber")final String lotnumber
			);
	
	@Query(value = "select a from Stockout_order_pkl a "
			+ " where a.stockoutorderdid_link = :stockoutorderdid_link"
			+ " and a.epc =:epc"
			)
	public List<Stockout_order_pkl> getByStockoutOrderDId_epc(
			@Param ("stockoutorderdid_link")final Long stockoutorderdid_link,
			@Param ("epc")final String epc
			);
}
