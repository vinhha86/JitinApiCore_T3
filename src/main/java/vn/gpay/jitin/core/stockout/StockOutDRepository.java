package vn.gpay.jitin.core.stockout;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockOutDRepository extends JpaRepository<StockOutD, Long>, JpaSpecificationExecutor<StockOutD>{
	
	@Query(value = "select c from StockOutD c "
			+ "where c.stockoutid_link = :stockoutid_link"
			)
	public List<StockOutD> getBystockOutId(
			@Param ("stockoutid_link")final Long stockoutid_link
			);
	
	
	@Query(value = "select a from StockOutD a inner join StockOut b on a.stockoutid_link = b.id"
			+ " where b.pcontractid_link =:pcontractid_link"
			+ " and (:pcontract_poid_link is null or b.pcontract_poid_link = :pcontract_poid_link)"
			+ " and (:porderid_link is null or b.porderid_link = :porderid_link)"
			+ " and (:stockid_link is null or b.orgid_from_link = :stockid_link)"
			+ " and a.skuid_link = :skuid_link")
	public List<StockOutD> findBy_PContractAndSku(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("porderid_link")final Long porderid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	//Xuất Nguyen lieu + Phu lieu cho sản xuất theo đơn hàng
	@Query(value = "select a from StockOutD a inner join StockOut b on a.stockoutid_link = b.id"
			+ " where (b.stockouttypeid_link = 1 or b.stockouttypeid_link = 11) and b.pcontractid_link =:pcontractid_link"
			+ " and (:pcontract_poid_link is null or b.pcontract_poid_link = :pcontract_poid_link)"
			+ " and (:porderid_link is null or b.porderid_link = :porderid_link)"
			+ " and (:stockid_link is null or b.orgid_from_link = :stockid_link)"
			+ " and a.skuid_link = :skuid_link")
	public List<StockOutD> to_production_m_findBy_PContractAndSku(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("porderid_link")final Long porderid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	@Query(value = "select a from StockOutD a inner join StockOut b on a.stockoutid_link = b.id"
			+ " where (b.stockouttypeid_link = 1 or b.stockouttypeid_link = 11) and b.pcontractid_link =:pcontractid_link"
			+ " and (:pcontract_poid_link is null or b.pcontract_poid_link = :pcontract_poid_link)"
			+ " and (:porderid_link is null or b.porderid_link = :porderid_link)"
			+ " and (:stockid_link is null or b.orgid_from_link = :stockid_link)")
	public List<StockOutD> to_production_m_findBy_PContract(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("porderid_link")final Long porderid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select a from StockOutD a inner join StockOut b on a.stockoutid_link = b.id"
			+ " where b.stockouttypeid_link > 20 and b.pcontractid_link =:pcontractid_link"
			+ " and (:pcontract_poid_link is null or b.pcontract_poid_link = :pcontract_poid_link)"
			+ " and (:porderid_link is null or b.porderid_link = :porderid_link)"
			+ " and (:stockid_link is null or b.orgid_from_link = :stockid_link)"
			+ " and a.skuid_link = :skuid_link")
	public List<StockOutD> p_findBy_PContractAndSku(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("porderid_link")final Long porderid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	
	//21- Xuất thành phẩm cho khách hàng theo đơn hàng
	@Query(value = "select a from StockOutD a inner join StockOut b on a.stockoutid_link = b.id"
			+ " where b.stockouttypeid_link = 21 and b.pcontractid_link =:pcontractid_link"
			+ " and (:pcontract_poid_link is null or b.pcontract_poid_link = :pcontract_poid_link)"
			+ " and (:porderid_link is null or b.porderid_link = :porderid_link)"
			+ " and (:stockid_link is null or b.orgid_from_link = :stockid_link)"
			+ " and a.skuid_link = :skuid_link")
	public List<StockOutD> to_vendor_p_findBy_PContractAndSku(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("porderid_link")final Long porderid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "select a from StockOutD a inner join StockOut b on a.stockoutid_link = b.id"
			+ " where b.stockouttypeid_link = 21 and b.pcontractid_link =:pcontractid_link"
			+ " and (:pcontract_poid_link is null or b.pcontract_poid_link = :pcontract_poid_link)"
			+ " and (:porderid_link is null or b.porderid_link = :porderid_link)"
			+ " and (:stockid_link is null or b.orgid_from_link = :stockid_link)")
	public List<StockOutD> to_vendor_p_findBy_PContract(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("pcontract_poid_link")final Long pcontract_poid_link,
			@Param ("porderid_link")final Long porderid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
	
	@Query(value = "select distinct a.stockoutid_link from StockOutD a "
			+ "where a.skuid_link = :skuid_link "
			)
	public List<Long> getStockoutIdBySkuId(
			@Param ("skuid_link")final Long skuid_link);
	
	@Query(value = "select a from StockOutD a " 
			+ " inner join SKU b on a.skuid_link = b.id "
			+ " where a.stockoutid_link = :stockoutid_link "
			+ " and lower(b.code) like lower(concat('%',:maNpl,'%')) "
			)
	public List<StockOutD> getBy_MaNpl_StockoutId(
			@Param ("maNpl")final String maNpl,
			@Param ("stockoutid_link")final Long stockoutid_link
			);
	
	@Query(value = "select a from StockOutD a " 
			+ " inner join StockOut b on a.stockoutid_link = b.id "
			+ " where a.skuid_link = :skuid_link "
			+ " and b.stockoutorderid_link = :stockoutorderid_link "
			+ " and (b.status >= :status_from or :status_from is null) "
			+ " and (b.status <= :status_to or :status_to is null) "
			)
	public List<StockOutD> getBy_StockoutOrder_and_SkuId(
			@Param ("stockoutorderid_link")final Long stockoutorderid_link,
			@Param ("skuid_link")final Long skuid_link,
			@Param ("status_from")final Integer status_from,
			@Param ("status_to")final Integer status_to
			);
}
