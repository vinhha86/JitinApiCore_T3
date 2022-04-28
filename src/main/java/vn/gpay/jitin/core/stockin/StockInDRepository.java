package vn.gpay.jitin.core.stockin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StockInDRepository extends JpaRepository<StockInD, Long>{
	@Query(value = "select a from StockInD a inner join StockIn b on a.stockinid_link = b.id"
			+ " where b.material_invoiceid_link =:material_invoiceid_link"
			+ " and a.skuid_link = :skuid_link")
	public List<StockInD> findBy_InvoiceAndSku(
			@Param ("material_invoiceid_link")final Long material_invoiceid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "select a from StockInD a inner join StockIn b on a.stockinid_link = b.id"
			+ " where b.pcontractid_link =:pcontractid_link"
			+ " and (:stockid_link is null or b.orgid_to_link = :stockid_link)"
			+ " and a.skuid_link = :skuid_link")
	public List<StockInD> findBy_PContractAndSku(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	//Nhập mua mới NPL từ nhà cung cấp hoặc vendor
	@Query(value = "select a from StockInD a inner join StockIn b on a.stockinid_link = b.id"
			+ " where (b.stockintypeid_link = 1 or b.stockintypeid_link = 11) and b.pcontractid_link =:pcontractid_link"
			+ " and (:stockid_link is null or b.orgid_to_link = :stockid_link)"
			+ " and a.skuid_link = :skuid_link")
	public List<StockInD> from_vendor_findBy_PContractAndSku(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "select a from StockInD a inner join StockIn b on a.stockinid_link = b.id"
			+ " where (b.stockintypeid_link = 1 or b.stockintypeid_link = 11) and b.pcontractid_link =:pcontractid_link"
			+ " and (:stockid_link is null or b.orgid_to_link = :stockid_link)")
	public List<StockInD> from_vendor_findBy_PContract(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
	//Nhập kho thanh pham tu hoan thien
	//21 - Nhap tu san xuat
	//22 - Nhap dieu chuyen
	@Query(value = "select a from StockInD a inner join StockIn b on a.stockinid_link = b.id"
			+ " where (b.stockintypeid_link = 21 or b.stockintypeid_link = 22) and b.pcontractid_link =:pcontractid_link"
			+ " and (:stockid_link is null or b.orgid_to_link = :stockid_link)"
			+ " and a.skuid_link = :skuid_link")
	public List<StockInD> pfrom_packing_findBy_PContractAndSku(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "select a from StockInD a inner join StockIn b on a.stockinid_link = b.id"
			+ " where (b.stockintypeid_link = 21 or b.stockintypeid_link = 22) and b.pcontractid_link =:pcontractid_link"
			+ " and (:stockid_link is null or b.orgid_to_link = :stockid_link)")
	public List<StockInD> pfrom_packing_findBy_PContract(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select a from StockInD a "
			+ " where a.stockinid_link =:stockinid_link")
	public List<StockInD> getByStockinId(
			@Param ("stockinid_link")final Long stockinid_link
			);
	
	@Query(value = "select a from StockInD a "
			+ " where a.stockinid_link =:stockinid_link"
			+ " and (a.id =:stockindid_link or :stockindid_link is null) "
			)
	public List<StockInD> getbyStockinID_StockinDID(
			@Param ("stockinid_link")final Long stockinid_link,
			@Param ("stockindid_link")final Long stockindid_link
			);
	
	@Query(value = "select distinct a.stockinid_link from StockInD a "
			+ "where a.skuid_link = :skuid_link "
			)
	public List<Long> getStockinIdBySkuId(
			@Param ("skuid_link")final Long skuid_link);
}
