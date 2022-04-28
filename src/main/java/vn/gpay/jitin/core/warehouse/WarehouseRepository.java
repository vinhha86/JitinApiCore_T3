package vn.gpay.jitin.core.warehouse;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>,  JpaSpecificationExecutor<Warehouse>{

	@Query(value = "select c from Warehouse c where spaceepc_link =:spaceepc")
	public List<Warehouse> findBySpaceepc(@Param ("spaceepc")final String spaceepc);
	
	@Query(value = "select c from Warehouse c " 
			+ "inner join SKU b on c.skuid_link = b.id "
			+ "where c.spaceepc_link =:spaceepc " 
			+ "and c.stockid_link =:stockid_link "
			+ "and b.skutypeid_link = 20"
			)
	public List<Warehouse> findBySpaceepc(@Param ("spaceepc")final String spaceepc, @Param ("stockid_link")final Long stockid_link);
	
	@Query(value = "select distinct c from Warehouse c " 
			+ "inner join SKU b on c.skuid_link = b.id "
			+ "inner join PContract a on c.pcontractid_link = a.id "
			+ "inner join StockIn d on c.stockinid_link = d.id "
			+ "inner join StockinProduct e on e.stockinid_link = d.id "
			+ "inner join Product f on e.productid_link = f.id "
			+ "where c.spaceepc_link =:spaceepc " 
			+ "and c.stockid_link =:stockid_link "
			+ "and lower(f.buyercode) like lower(concat('%',:buyercode,'%')) "
			+ "and b.skutypeid_link = 20"
			)
	public List<Warehouse> findBySpaceepc_stock_buyercode(
			@Param ("spaceepc")final String spaceepc, 
			@Param ("buyercode")final String buyercode, 
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select distinct c from Warehouse c " 
			+ "inner join SKU b on c.skuid_link = b.id "
			+ "inner join PContract a on c.pcontractid_link = a.id "
			+ "inner join StockIn d on c.stockinid_link = d.id "
//			+ "inner join StockinProduct e on e.stockinid_link = d.id "
//			+ "inner join Product f on e.productid_link = f.id "
			+ "where c.spaceepc_link =:spaceepc " 
			+ "and c.stockid_link =:stockid_link "
//			+ "and lower(f.buyercode) like lower(concat('%',:buyercode,'%')) "
			+ "and b.skutypeid_link = 20"
			)
	public List<Warehouse> findBySpaceepc_stock(
			@Param ("spaceepc")final String spaceepc,
			@Param ("stockid_link")final Long stockid_link
			);
	
	
	@Query(value = "select c from Warehouse c " 
			+ "inner join SKU b on c.skuid_link = b.id "
			+ "where c.spaceepc_link is null " 
			+ "and c.stockid_link =:stockid_link "
			+ "and b.skutypeid_link = 20"
			)
	public List<Warehouse> findBySpaceepcKXD(@Param ("stockid_link")final Long stockid_link);
	
	@Query(value = "select distinct c from Warehouse c " 
			+ "inner join SKU b on c.skuid_link = b.id "
			+ "inner join PContract a on c.pcontractid_link = a.id "
			+ "inner join StockIn d on c.stockinid_link = d.id "
//			+ "inner join StockinProduct e on e.stockinid_link = d.id "
//			+ "inner join Product f on e.productid_link = f.id "
			+ "where c.spaceepc_link is null " 
			+ "and c.stockid_link =:stockid_link "
//			+ "and lower(f.buyercode) like lower(concat('%',:buyercode,'%')) "
			+ "and b.skutypeid_link = 20"
			)
	public List<Warehouse> findBySpaceepcKXD_stock(
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select distinct c from Warehouse c " 
			+ "inner join SKU b on c.skuid_link = b.id "
			+ "inner join PContract a on c.pcontractid_link = a.id "
			+ "inner join StockIn d on c.stockinid_link = d.id "
			+ "inner join StockinProduct e on e.stockinid_link = d.id "
			+ "inner join Product f on e.productid_link = f.id "
			+ "where c.spaceepc_link is null " 
			+ "and c.stockid_link =:stockid_link "
			+ "and lower(f.buyercode) like lower(concat('%',:buyercode,'%')) "
			+ "and b.skutypeid_link = 20"
			)
	public List<Warehouse> findBySpaceepcKXD_stock_buyercode(
			@Param ("buyercode")final String buyercode, 
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select c from Warehouse c " 
			+ "inner join SKU b on c.skuid_link = b.id "
			+ "where c.spaceepc_link =:spaceepc " 
			+ "and c.stockid_link =:stockid_link "
//			+ "and b.skutypeid_link = 10"
			)
	public List<Warehouse> findProductBySpaceepc(@Param ("spaceepc")final String spaceepc, @Param ("stockid_link")final Long stockid_link);
	
	@Query(value = "select c from Warehouse c " 
			+ "inner join SKU b on c.skuid_link = b.id "
			+ "where c.spaceepc_link is null " 
			+ "and c.stockid_link =:stockid_link "
//			+ "and b.skutypeid_link = 10"
			)
	public List<Warehouse> findProductBySpaceepcKXD(@Param ("stockid_link")final Long stockid_link);
	
	@Query(value = "select c from Warehouse c where"
			+ " (pcontractid_link =:pcontractid_link or :pcontractid_link is null)"
			+ " and (stockid_link =:stockid_link or :stockid_link is null)"
			+ " and (skuid_link =:skuid_link or :skuid_link is null)"
			+ " order by lotnumber, packageid"
			)
	public List<Warehouse> findByPContract(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "select c from Warehouse c "
			+ " inner join StockIn b on b.id = c.stockinid_link "
			+ " left join StockinProduct a on b.id = a.stockinid_link "
			+ " where (c.pcontractid_link =:pcontractid_link or :pcontractid_link is null)"
			+ " and (c.stockid_link =:stockid_link or :stockid_link is null)"
			+ " and (c.skuid_link =:skuid_link or :skuid_link is null)"
			+ " and (a.productid_link = :productid_link or a.productid_link is null) "
			+ " and c.p_skuid_link is null "
			+ " order by lotnumber, packageid"
			)
	public List<Warehouse> findByPContract_Product(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("productid_link")final Long productid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "select c from Warehouse c "
			+ " where (c.stockid_link =:stockid_link or :stockid_link is null)"
			+ " and (c.skuid_link =:skuid_link or :skuid_link is null)"
			+ " and c.p_skuid_link = :pcontractProduct_id "
			+ " order by lotnumber, packageid"
			)
	public List<Warehouse> findByPContract_Product_loan(
			@Param ("pcontractProduct_id")final Long pcontractProduct_id,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "select c from Warehouse c "
			+ " inner join StockIn b on b.id = c.stockinid_link "
			+ " left join StockinProduct a on b.id = a.stockinid_link "
			+ " where (c.pcontractid_link !=:pcontractid_link or :pcontractid_link is null)"
			+ " and (c.stockid_link =:stockid_link or :stockid_link is null)"
			+ " and (c.skuid_link =:skuid_link or :skuid_link is null)"
			+ " and (a.productid_link != :productid_link or a.productid_link is null) "
			+ " and c.p_skuid_link is null"
			+ " order by lotnumber, packageid"
			)
	public List<Warehouse> findByPContract_Product_xuatDieuCHuyen(
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("productid_link")final Long productid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skuid_link")final Long skuid_link
			);
	
	@Query(value = "select distinct c.lotnumber from Warehouse c " 
			+ "where c.skuid_link = :skuid_link "
			+ "and c.skuid_link is not null "
			+ "order by c.lotnumber "
			)
	public List<String> findLotnumberBySku(@Param ("skuid_link")final Long skuid_link);	
	
	@Query(value = "select c from Warehouse c where upper(lotnumber) =upper(:lotnumber)")
	public List<Warehouse> findByLotNumber(@Param ("lotnumber")final String lotnumber);	
	
	@Query(value = "select c from Warehouse c where epc =:epc and stockid_link =:stockid_link")
	public List<Warehouse> findMaterialByEPCAndStock(
			@Param ("epc")final String epc, 
			@Param ("stockid_link")final long stockid_link);
	
	@Query(value = "select c from Warehouse c where epc =:epc and stockid_link != :stockid_link")
	public List<Warehouse> findMaterialByEPCAndNotInStock(
			@Param ("epc")final String epc, 
			@Param ("stockid_link")final long stockid_link);
	
	@Query(value = "select c from Warehouse c where epc =:epc")
	public List<Warehouse> findMaterialByEPC(
			@Param ("epc")final String epc);
	
	//@Query(value = "select a from Warehouse a inner join EpcWarehouseCheck b on a.epc = b.epc   where b.token =:token")
	@Query(value = "select a from Warehouse a")
	public List<Warehouse> findCheckedEPC(@Param ("token")final UUID token);
	@Query(value="select a.skuid_link,SUM(a.yds),b.unitprice,SUM(a.yds)*b.unitprice,b.unitid_link " + 
			"from Warehouse a  " + 
			"inner join StockInD b on a.stockindid_link=b.id " + 
			"where a.stockid_link =:orgfrom_code "+
			"group by a.skuid_link,b.unitprice")
	public List<Object[]> invcheck_sku(@Param ("orgfrom_code")final Long orgfrom_code);
	//public List<Object[]> invcheck_sku(@Param ("bossid")final String bossid,@Param ("orgfrom_code")final String orgfrom_code,@Param ("productcode")final String productcode);
	
	
	@Query(value = "select c.epc, c.skuid_link,c.yds,c.met,c.unitprice,c.unitid_link from Warehouse c where c.stockid_link =:orgfrom_code")
	public List<Object[]> invcheck_epc(@Param ("orgfrom_code")final Long orgfrom_code);
	//public List<Object[]> invcheck_epc(@Param ("bossid")final String bossid,@Param ("orgfrom_code")final String orgfrom_code,@Param ("productcode")final String productcode);
	
	@Query(value = "select a from Warehouse a "
			+ "inner join StockInD b on a.stockindid_link = b.id "
			+ "where b.stockinid_link =:stockid_link")
	List<Warehouse> inv_getby_stockinid_link(@Param ("stockid_link")final long stockid_link) ;
	
	@Modifying
	@Query(value = "delete from Warehouse c where stockid_link =:stockid_link and epc=:epc")
 	public void deleteByEpc(@Param ("epc")final String epc,@Param ("stockid_link")final long stockid_link);
	
	@Query(value = "select a from Warehouse a "
//			+ "inner join StockInD b on a.stockindid_link = b.id "
			+ "inner join StockIn c on a.stockinid_link = c.id "
			+ "left join PContract d on a.pcontractid_link = d.id "
			+ "where (c.orgid_to_link in :orgid_link or :orgid_link is null) "
			+ "and (a.cutplanrowid_link is null or a.cutplanrowid_link = 0) "
			+ "and a.skuid_link = :material_skuid_link "
			+ "and (a.pcontractid_link = :pcontractid_link or :pcontractid_link is null) "
			+ "and (d.orgbuyerid_link = :org_buyer_id_link or :org_buyer_id_link is null) "
			+ "and a.epc not in (select epc from Stockout_order_pkl pkl where :stockout_orderid_link = pkl.stockoutorderid_link) ")
	List<Warehouse> getby_org(
			@Param ("orgid_link")final List<Long> orgid_link,
			@Param ("material_skuid_link")final Long material_skuid_link,
			@Param ("pcontractid_link")final Long pcontractid_link,
			@Param ("org_buyer_id_link")final Long org_buyer_id_link,
			@Param ("stockout_orderid_link")final Long stockout_orderid_link) ;
	
	@Query(value = "select a from Warehouse a "
			+ "where a.cutplanrowid_link = :cutplanrowid_link")
	List<Warehouse> getby_cutplan(
			@Param ("cutplanrowid_link")final Long cutplanrowid_link) ;
	
	
	@Query(value = "select a.spaceepc_link as spaceepc_link, count(a.id) as itemcount, sum(a.met) as itemtotal from Warehouse a "
			+ "where a.stockid_link >= 0"
			+ "and a.skuid_link = :skuid_link "
			+ "group by spaceepc_link")
	List<Object[]> getspaces_bysku(
			@Param ("skuid_link")final Long skuid_link) ;
	
	@Query(value = "select a.spaceepc_link as spaceepc_link, count(a.id) as itemcount, sum(a.met) as itemtotal from Warehouse a "
			+ "where a.stockid_link = :stockid_link "
			+ "and a.skuid_link = :skuid_link "
			+ "group by spaceepc_link")
	List<Object[]> getspaces_bystockandsku(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select c from Warehouse c"
			+ " where (c.skuid_link =:skuid_link or :skuid_link is null)"
			+ " and (c.lotnumber =:lotnumber or :lotnumber is null)"
			+ " and (c.packageid =:packageid or :packageid is null)"
			+ " and (c.stockid_link =:stockid_link or :stockid_link is null)"
			+ " and (c.status =:status or :status is null)"
//			+ " and c.status < 1"
			)
	public List<Warehouse> getByLotAndPackageId(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("lotnumber")final String lotnumber,
			@Param ("packageid")final Integer packageid,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("status")final Integer status
			);
	
	@Query(value = "select distinct c.spaceepc_link from Warehouse c "
			+ " inner join SKU b on c.skuid_link = b.id"
			+ " inner join PContract a on c.pcontractid_link = a.id "
			+ " inner join Stockspace d on d.spaceepc = c.spaceepc_link "
			+ " inner join Org e on e.id = d.orgid_link "
			+ " where c.spaceepc_link is not null "
			+ " and c.stockid_link > 0 "
			+ " and lower(a.contractcode) like lower(concat('%',:contractcode,'%')) "
			+ " and (c.skuid_link = :skuid_link or :skuid_link is null) "
//			+ " and (e.parentid_link = :orgid_link or :orgid_link is null) "
			+ " and (e.id = :orgid_link or :orgid_link is null) "
			)
	public List<String> getSpaceEpcBySkucode_Pcontract_Org(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("contractcode")final String contractcode,
			@Param ("orgid_link")final Long orgid_link
			);
	
	@Query(value = "select distinct c.spaceepc_link from Warehouse c "
			+ " inner join SKU b on c.skuid_link = b.id"
			+ " inner join PContract a on c.pcontractid_link = a.id "
			+ " inner join Stockspace d on d.spaceepc = c.spaceepc_link "
			+ " inner join Org e on e.id = d.orgid_link "

			+ " inner join StockIn f on c.stockinid_link = f.id "
			+ " inner join StockinProduct g on g.stockinid_link = f.id "
			+ " inner join Product h on g.productid_link = h.id "
			
			+ " where c.spaceepc_link is not null "
			+ " and c.stockid_link > 0 "
			+ " and lower(a.contractcode) like lower(concat('%',:contractcode,'%')) "
			+ " and lower(h.buyercode) like lower(concat('%',:buyercode,'%')) "
			+ " and (c.skuid_link = :skuid_link or :skuid_link is null) "
//			+ " and (e.parentid_link = :orgid_link or :orgid_link is null) "
			+ " and (e.id = :orgid_link or :orgid_link is null) "
			)
	public List<String> getSpaceEpcBySkucode_Pcontract_Buyercode_Org(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("contractcode")final String contractcode,
			@Param ("buyercode")final String buyercode,
			@Param ("orgid_link")final Long orgid_link
			);
	
	@Query(value = "select distinct c.spaceepc_link from Warehouse c "
			+ " inner join SKU b on c.skuid_link = b.id"
			+ " inner join PContract a on c.pcontractid_link = a.id "
			+ " where c.spaceepc_link is not null "
			+ " and c.stockid_link > 0 "
			+ " and lower(a.contractcode) like lower(concat('%',:contractcode,'%')) "
//			+ " and lower(c.skucode) like lower(concat('%',:skucode,'%')) "
			+ " and (c.skuid_link = :skuid_link or :skuid_link is null)  "
			)
	public List<String> getSpaceEpcBySkucodeAndPcontract(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("contractcode")final String contractcode
			);
	
//	@Query(value = "select distinct c.spaceepc_link from Warehouse c "
//			+ " inner join SKU b on c.skuid_link = b.id"
//			+ " inner join PContract a on c.pcontractid_link = a.id "
//			+ " inner join StockIn d on c.stockinid_link = d.id "
//			+ " inner join StockinProduct e on e.stockinid_link = d.id "
//			+ " inner join Product f on e.productid_link = f.id "
//			+ " where c.spaceepc_link is not null "
//			+ " and c.stockid_link > 0 "
//			+ " and lower(a.contractcode) like lower(concat('%',:contractcode,'%')) "
//			+ " and lower(f.buyercode) like lower(concat('%',:buyercode,'%')) "
////			+ " and lower(c.skucode) like lower(concat('%',:skucode,'%')) "
//			+ " and (c.skuid_link = :skuid_link or :skuid_link is null)  "
//			)
//	public List<String> getSpaceEpcBySkucode_Pcontract_ProductBuyerCode(
//			@Param ("skuid_link")final Long skuid_link,
//			@Param ("contractcode")final String contractcode,
//			@Param ("buyercode")final String buyercode
//			);
	
	@Query(value = "select distinct c.spaceepc_link from Warehouse c "
			+ " inner join SKU b on c.skuid_link = b.id"
			+ " inner join PContract a on c.pcontractid_link = a.id "
			+ " inner join StockIn d on c.stockinid_link = d.id "
			+ " left join StockinProduct e on e.stockinid_link = d.id "
			+ " left join Product f on e.productid_link = f.id "
			+ " where c.spaceepc_link is not null "
			+ " and c.stockid_link > 0 "
			+ " and lower(a.contractcode) like lower(concat('%',:contractcode,'%')) "
			+ " and (lower(f.buyercode) like lower(concat('%',:buyercode,'%')) or :buyercode = '' ) "
//			+ " and lower(c.skucode) like lower(concat('%',:skucode,'%')) "
			+ " and (c.skuid_link = :skuid_link or :skuid_link is null)  "
			)
	public List<String> getSpaceEpcBySkucode_Pcontract_ProductBuyerCode(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("contractcode")final String contractcode,
			@Param ("buyercode")final String buyercode
			);
	
	@Query(value = "select distinct c.spaceepc_link from Warehouse c "
			+ " inner join SKU b on c.skuid_link = b.id"
			+ " where c.spaceepc_link is not null "
			+ " and c.stockid_link > 0 "
			+ " and (c.skuid_link = :skuid_link or :skuid_link is null)  "
			)
	public List<String> getSpaceEpcBySkucode(
			@Param ("skuid_link")final Long skuid_link
			);
	
//	@Query(value = "select c from Warehouse c "
//			+ " inner join SKU b on c.skuid_link = b.id"
//			+ " inner join PContract a on c.pcontractid_link = a.id "
//			+ " inner join StockIn d on c.stockinid_link = d.id "
//			+ " inner join StockinProduct e on e.stockinid_link = d.id "
//			+ " inner join Product f on e.productid_link = f.id "
//			+ " where c.spaceepc_link is null "
//			+ " and lower(a.contractcode) like lower(concat('%',:contractcode,'%')) "
//			+ " and lower(f.buyercode) like lower(concat('%',:buyercode,'%')) "
//			+ " and (c.skuid_link = :skuid_link or :skuid_link is null)  "
//			+ " and c.stockid_link = :stockid_link"
//			+ " and b.skutypeid_link = 20"
//			)
//	public List<Warehouse> getWarehouseBySpaceEpcNull(
//			@Param ("skuid_link")final Long skuid_link,
//			@Param ("contractcode")final String contractcode,
//			@Param ("buyercode")final String buyercode,
//			@Param ("stockid_link")final Long stockid_link
//			);
	
	@Query(value = "select c from Warehouse c "
			+ " inner join SKU b on c.skuid_link = b.id"
			+ " inner join PContract a on c.pcontractid_link = a.id "
			+ " inner join StockIn d on c.stockinid_link = d.id "
			+ " left join StockinProduct e on e.stockinid_link = d.id "
			+ " left join Product f on e.productid_link = f.id "
			+ " where c.spaceepc_link is null "
			+ " and lower(a.contractcode) like lower(concat('%',:contractcode,'%')) "
			+ " and (lower(f.buyercode) like lower(concat('%',:buyercode,'%')) or :buyercode = '' ) "
			+ " and (c.skuid_link = :skuid_link or :skuid_link is null)  "
			+ " and c.stockid_link = :stockid_link"
			+ " and b.skutypeid_link = 20"
			)
	public List<Warehouse> getWarehouseBySpaceEpcNull(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("contractcode")final String contractcode,
			@Param ("buyercode")final String buyercode,
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select c from Warehouse c "
			+ " inner join SKU b on c.skuid_link = b.id"
			+ " where c.spaceepc_link is null "
			+ " and (c.skuid_link = :skuid_link or :skuid_link is null)  "
			+ " and c.stockid_link = :stockid_link"
			+ " and b.skutypeid_link = 10"
			)
	public List<Warehouse> getWarehouseProductBySpaceEpcNull(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select a from Warehouse a "
			+ "where (a.stockid_link = :stockid_link or :stockid_link is null) "
			+ "and a.skuid_link = :skuid_link "
			)
	List<Warehouse> getBySkuAndToCat(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select count(a.id) as itemcount, a.skuid_link as skuid_link from Warehouse a "
			+ "where a.skuid_link = :skuid_link "
			+ "and a.stockid_link = :stockid_link "
//			+ "and (a.is_freeze = false or a.is_freeze is null) "
			+ "group by skuid_link")
	List<Object[]> get_inStock_bySku(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
//	@Query(value = "select a from Warehouse a "
//			+ "where (a.stockid_link = :stockid_link or :stockid_link is null) "
//			+ "and a.skuid_link = :skuid_link "
//			+ "and (a.is_freeze = false or a.is_freeze is null) "
//			+ "order by a.id "
//			+ "limit :number "
//			)
//	List<Warehouse> getByStock_Sku_isNotFreeze(
//			@Param ("skuid_link")final Long skuid_link,
//			@Param ("stockid_link")final Long stockid_link,
//			@Param ("number")final Integer number
//			);
	
	@Query(nativeQuery = true, value = "select * from Warehouse a "
			+ "where (a.stockid_link = :stockid_link or :stockid_link is null) "
			+ "and a.skuid_link = :skuid_link "
			+ "and (a.is_freeze = false or a.is_freeze is null) "
			+ "order by a.id asc "
			+ "limit :number " // nativeQuery true vì JPQL ko có limit
			)
	List<Warehouse> getByStock_Sku_isNotFreeze(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("number")final Integer number
			);
	
	@Query(value = "select count(a.id) from Warehouse a "
			+ "where a.skuid_link = :skuid_link "
			+ "and a.stockid_link = :stockid_link "
			+ "and (a.is_freeze = false or a.is_freeze is null) "
			)
	Long getByStock_Sku_isNotFreeze_number(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
//	@Query(value = "select distinct c from Warehouse c "
//			+ " inner join SKU b on c.skuid_link = b.id "
//			+ " left join Stockout_order_pkl a on a.epc = c.epc "
//			+ " where c.skuid_link = :skuid_link "
//			+ " and c.stockid_link in :khoVai_list "
//			+ " and b.skutypeid_link = 20 "
//			+ " and a.epc is null "
//			+ " and c.epc not in :listSelectedEpc "
//			) 
//	public List<Warehouse> getBy_sku_stockList(
//			@Param ("skuid_link")final Long skuid_link,
//			@Param ("khoVai_list")final List<Long> khoVai_list,
//			@Param ("listSelectedEpc")final List<String> listSelectedEpc
//			);
	
	@Query(value = "select distinct c from Warehouse c "
			+ " inner join SKU b on c.skuid_link = b.id "
//			+ " left join Stockout_order_pkl a on a.epc = c.epc "
			+ " where c.skuid_link = :skuid_link "
			+ " and c.stockid_link in :khoVai_list "
			+ " and b.skutypeid_link = 20 "
//			+ " and a.epc is null "
			+ " and c.epc not in :listSelectedEpc "
			) 
	public List<Warehouse> getBy_sku_stockList(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("khoVai_list")final List<Long> khoVai_list,
			@Param ("listSelectedEpc")final List<String> listSelectedEpc
			);
	
	@Query(value = "select count(c.id) from Warehouse c "
			+ " where c.skuid_link = :skuid_link "
			+ " and c.stockid_link = :stockid_link "
			) 
	public Long getSumBy_Sku_Stock(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("stockid_link")final Long stockid_link
			);
	
	@Query(value = "select c from Warehouse c "
			+ " where (c.skuid_link = :skuid_link or :skuid_link is null ) "
			+ " and (c.stockid_link = :stockid_link or :stockid_link is null ) "
			+ " and (c.skutypeid_link = :skutypeid_link or :skutypeid_link is null ) "
			) 
	public List<Warehouse> getBySkuAndStock(
			@Param ("skuid_link")final Long skuid_link,
			@Param ("stockid_link")final Long stockid_link,
			@Param ("skutypeid_link")final Long skutypeid_link
			);
}
