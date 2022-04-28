package vn.gpay.jitin.core.stockout_order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface Stockout_order_d_repository extends JpaRepository<Stockout_order_d, Long>{
	@Query(value = "select c from Stockout_order_d c where stockoutorderid_link = :stockoutorderid_likn ")
	public List<Stockout_order_d> getby_stockout_order(
			@Param ("stockoutorderid_likn")final  long stockoutorderid_likn);
	
	@Query(value = "select a from Stockout_order_d a "
			+ " where a.stockoutorderid_link =:stockoutorderid_link")
	public List<Stockout_order_d> getByStockoutOrderId(
			@Param ("stockoutorderid_link")final Long stockoutorderid_link
			);
	
	@Query(value = "select a from Stockout_order_d a inner join Stockout_order b on a.stockoutorderid_link = b.id"
			+ " where b.pcontractid_link =:pcontractid_link"
			+ " and (:pcontract_poid_link is null or b.pcontract_poid_link = :pcontract_poid_link)"
			+ " and (:porderid_link is null or b.porderid_link = :porderid_link)"
			+ " and (:stockid_link is null or b.orgid_from_link = :stockid_link)"
			+ " and a.material_skuid_link = :skuid_link")
	public List<Stockout_order_d> findBy_PContractAndSku(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("porderid_link")final Long porderid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
}
