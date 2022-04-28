package vn.gpay.jitin.core.warehouse;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;
import vn.gpay.jitin.core.invcheck.InvcheckEpc;
import vn.gpay.jitin.core.invcheck.InvcheckSku;
import vn.gpay.jitin.core.stockout.StockOutD;

public interface IWarehouseService extends Operations<Warehouse>{

	List<Warehouse> findBySpaceepc(String spaceepc);
	List<Warehouse> findBySpaceepc(String spaceepc, Long stockid_link);
	List<Warehouse> findBySpaceepc_stock_buyercode(String spaceepc, String buyercode, Long stockid_link);
	List<Warehouse> findBySpaceepc_stock(String spaceepc, Long stockid_link);
	List<Warehouse> findBySpaceepcKXD(Long stockid_link);
	List<Warehouse> findBySpaceepcKXD_stock_buyercode(String buyercode, Long stockid_link);
	List<Warehouse> findBySpaceepcKXD_stock(Long stockid_link);
	List<Warehouse> findProductBySpaceepc(String spaceepc, Long stockid_link);
	List<Warehouse> findProductBySpaceepcKXD(Long stockid_link);
	List<Warehouse> findByLotNumber(String lotnumber);
	List<Warehouse> findMaterialByEPCAndStock(String epc, long stockid_link);
	List<Warehouse> findMaterialByEPCAndNotInStock(String epc, long stockid_link);
	boolean epcExistedInStock(String epc, long stockid_link);
	List<Warehouse> findCheckedEPC(String token);
	void deleteByEpc(String Epc,long stockid_link);
	List<InvcheckSku> invcheck_sku(long invcheckid_link,long orgid_link,Long bossid,Long orgfrom_code,Long productcode);
	List<InvcheckEpc> invcheck_epc(long invcheckid_link,long orgid_link,Long bossid,Long orgfrom_code,Long productcode);
	List<Warehouse> inv_getbyid(long stockid_link);
	void setEpcOnLogistic(String epc);
	boolean setEpcSoldout(String epc);
	List<Warehouse> findMaterialByEPC(String epc);
	boolean epcExistedInWarehouse(String epc);
	List<Warehouse> getby_org(List<Long> orgid_link, Long material_skuid_link, Long pcontractid_link, Long org_buyer_id_link, Long stockoutorderid_link);
	List<Warehouse> getby_cutplan(Long cutplanrowid_link);
	List<Warehouse> findByPContract(Long pcontractid_link, Long stockid_link, Long skuid_link);
	String getspaces_bysku(Long stockid_link, Long skuid_link);
	List<Warehouse> getByLotAndPackageId(Long skuid_link, String lotnumber, Integer packageid, Long stockid_link, Integer status);
	List<String> getSpaceEpcBySkucode_Pcontract_Org(Long skuid_link, String contractcode, Long orgid_link);
	List<String> getSpaceEpcBySkucode_Pcontract_Buyercode_Org(Long skuid_link, String contractcode, String buyercode, Long orgid_link);
	List<String> getSpaceEpcBySkucodeAndPcontract(Long skuid_link, String contractcode);
	List<String> getSpaceEpcBySkucode_Pcontract_ProductBuyerCode(Long skuid_link, String contractcode, String buyercode);
	List<String> getSpaceEpcBySkucode(Long skuid_link);
	List<Warehouse> getWarehouseBySpaceEpcNull(Long skuid_link, String contractcode, String buyercode, Long stockid_link);
	List<Warehouse> getWarehouseProductBySpaceEpcNull(Long skuid_link, Long stockid_link);
	List<Warehouse> getBySkuAndToCat(Long skuid_link, Long stockid_link);
	List<StockOutD> get_inStock_bySku(Long skuid_link, Long stockid_link);
	List<Warehouse> getByStock_Sku_isNotFreeze(Long skuid_link, Long stockid_link, Integer number);
	Long getByStock_Sku_isNotFreeze_number(Long skuid_link, Long stockid_link);
	List<Warehouse> findByPContract_Product(Long pcontractid_link, Long productid_link, Long stockid_link, Long skuid_link);
	List<Warehouse> findByPContract_Product_loan(Long pcontractProduct_id, Long stockid_link, Long skuid_link);
	List<Warehouse> findByPContract_Product_xuatDieuCHuyen(Long pcontractid_link, Long productid_link, Long stockid_link, Long skuid_link);
	List<Warehouse> getBy_sku_stockList(Long skuid_link, List<Long> khoVai_list, List<String> listSelectedEpc);
	List<String> findLotnumberBySku(Long skuid_link);
	Long getSumBy_Sku_Stock(Long skuid_link, Long stockid_link);
	List<Warehouse> getBySkuAndStock(Long skuid_link, Long stockid_link, Long skutypeid_link);
}
