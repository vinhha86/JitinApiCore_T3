package vn.gpay.jitin.core.warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;
import vn.gpay.jitin.core.invcheck.InvcheckEpc;
import vn.gpay.jitin.core.invcheck.InvcheckSku;
import vn.gpay.jitin.core.stock.IStockspaceService;
import vn.gpay.jitin.core.stock.Stockspace;
import vn.gpay.jitin.core.stockout.StockOutD;
import vn.gpay.jitin.core.utils.EPCStockStatus;

@Service
public class WarehouseRepositoryImpl extends AbstractService<Warehouse> implements IWarehouseService{
	@Autowired WarehouseRepository repositoty;
	@Autowired IStockspaceService stockSpaceService;

	@Override
	protected JpaRepository<Warehouse, Long> getRepository() {
		return repositoty;
	}

	@Override
	public List<Warehouse> findBySpaceepc(String spaceepc) {
		return repositoty.findBySpaceepc(spaceepc);
	}
	
	@Override
	public List<Warehouse> findByPContract(Long pcontractid_link, Long stockid_link, Long skuid_link) {
		return repositoty.findByPContract(pcontractid_link, stockid_link, skuid_link);
	}
	
	@Override
	public List<Warehouse> findByLotNumber(String lotnumber) {
		return repositoty.findByLotNumber(lotnumber);
	}	

	@Override
	public List<Warehouse> findMaterialByEPCAndStock(String epc, long stockid_link) {
		// TODO Auto-generated method stub
		return repositoty.findMaterialByEPCAndStock(epc, stockid_link);
	}
	
	@Override
	public List<Warehouse> findMaterialByEPCAndNotInStock(String epc, long stockid_link) {
		// TODO Auto-generated method stub
		return repositoty.findMaterialByEPCAndNotInStock(epc, stockid_link);
	}
	
	@Override
	public List<Warehouse> findMaterialByEPC(String epc) {
		// TODO Auto-generated method stub
		return repositoty.findMaterialByEPC(epc);
	}

	@Override
	public boolean epcExistedInStock(String epc, long stockid_link) {
		// TODO Auto-generated method stub
		List<Warehouse> lstEpc = repositoty.findMaterialByEPCAndStock(epc, stockid_link);
		if (null != lstEpc && lstEpc.size() > 0)
			return true;
		else
			return false;
	}
	
	@Override
	public boolean epcExistedInWarehouse(String epc) {
		// TODO Auto-generated method stub
		List<Warehouse> lstEpc = repositoty.findMaterialByEPC(epc);
		if (null != lstEpc && lstEpc.size() > 0)
			return true;
		else
			return false;
	}
	
	@Override
	public List<Warehouse> findCheckedEPC(String token) {
		// TODO Auto-generated method stub
		return repositoty.findCheckedEPC(UUID.fromString(token));
	}

	@Override
	public List<InvcheckSku> invcheck_sku(long invcheckid_link,long orgrootid_link,Long bossid,Long orgfrom_code,Long productcode) {
		// TODO Auto-generated method stub
		List<InvcheckSku> listdata = new ArrayList<InvcheckSku>();
		try {
			List<Object[]> objectList = repositoty.invcheck_sku(orgfrom_code);
			for (Object[] row : objectList) {
				InvcheckSku entity = new InvcheckSku();
				//InvcheckSkuID id = new InvcheckSkuID();
				entity.setInvcheckid_link(invcheckid_link);
				entity.setOrgrootid_link(orgrootid_link);
				entity.setSkuid_link((Long) row[0]);
				entity.setYdsorigin((Float) row[1]);
		        entity.setUnitprice((Float)row[2]);
		        entity.setTotalamount((Float)row[3]);
		        entity.setUnitid_link((Integer)row[4]);
		       // entity.setInvchecksku_pk(id);;
				listdata.add(entity);
	        }
		}catch(Exception ex) {}
		return listdata;
	}

	@Override
	public List<InvcheckEpc> invcheck_epc(long invcheckid_link, long orgrootid_link, Long bossid, Long orgfrom_code,
			Long productcode) {
		// TODO Auto-generated method stub
		List<InvcheckEpc> listdata = new ArrayList<InvcheckEpc>();
		try {
			List<Object[]> objectList = repositoty.invcheck_epc(orgfrom_code);
			for (Object[] row : objectList) {
				//InvcheckEpcID id = new InvcheckEpcID();
				InvcheckEpc entity = new InvcheckEpc();
				entity.setInvcheckid_link(invcheckid_link);
				entity.setOrgrootid_link(orgrootid_link);
				entity.setEpc((String) row[0]);
		        //entity.setInvcheckepc_pk(id);
		        entity.setSkuid_link((Long)row[1]);
		        entity.setYdsorigin((Float)row[2]);
		        entity.setMet_origin((Float)row[3]);
		        entity.setUnitprice((Float) row[4]);
		        entity.setRssi(0);
				listdata.add(entity);
	        }
		}catch(Exception ex) {}
		return listdata;
	}

	@Override
	public void deleteByEpc(String epc,long orgid_link) {
		repositoty.deleteByEpc(epc, orgid_link);
	}

	@Override
	//EPC da xuat kho va dang troi noi o don vi van chuyen
	public void setEpcOnLogistic(String epc) {
		List<Warehouse> epc_ls = findMaterialByEPC(epc);
		if (epc_ls.size() > 0){
			Warehouse myepc = epc_ls.get(0);
			myepc.setStockid_link(EPCStockStatus.EPCSTOCK_ONLOGISTIC);
//			myepc.setSpaceepc_link(null);
			repositoty.save(myepc);
		}
	}
	@Override
	//EPC da ban cho khach hang
	public boolean setEpcSoldout(String epc) {
		try {
			List<Warehouse> epc_ls = findMaterialByEPC(epc);
			if (epc_ls.size() > 0){
				Warehouse myepc = epc_ls.get(0);
				myepc.setStockid_link(EPCStockStatus.EPCSTOCK_SOLDOUT);
				repositoty.save(myepc);
				return true;
			} else {
				return false;
			}
		} catch (Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	@Override
	public List<Warehouse> inv_getbyid(long stockid_link) {
		// TODO Auto-generated method stub
		return repositoty.inv_getby_stockinid_link(stockid_link);
	}

	@Override
	public List<Warehouse> getby_org(List<Long> orgid_link, Long material_skuid_link, Long pcontractid_link, Long org_buyer_id_link, Long stockoutorderid_link) {
		// TODO Auto-generated method stub
		return repositoty.getby_org(orgid_link, material_skuid_link, pcontractid_link, org_buyer_id_link, stockoutorderid_link);
	}

	@Override
	public List<Warehouse> getby_cutplan(Long cutplanrowid_link) {
		// TODO Auto-generated method stub
		return repositoty.getby_cutplan(cutplanrowid_link);
	}

	@Override
	public String getspaces_bysku(Long stockid_link, Long skuid_link) {
		String sSpaces = "";
		String sSpacesKXD = "";
		Long item_count_KXD = (long)0;
		Double item_total_KXD = (double)0;
		try {
			List<Object[]> lsSpaces = repositoty.getspaces_bystockandsku(skuid_link, stockid_link);
//			int i=0;
			for(Object[] theSpace: lsSpaces){
				//Lay thong tin space
				String space_epc = (String)theSpace[0];
				Long item_count = (Long)theSpace[1];
				Double item_total = (Double)theSpace[2];
				List<Stockspace> lsSpaceEpc = stockSpaceService.findStockspaceByEpc(stockid_link, space_epc);
				
				if (lsSpaceEpc.size()>0){
					Stockspace theSpaceEpc = lsSpaceEpc.get(0);
					sSpaces += "D-" + theSpaceEpc.getStockrow_code() + "|"
							+ "T-" + theSpaceEpc.getSpacename() + "|"
							+ "K-" + theSpaceEpc.getFloorid().toString() + "|" 
							+ "(" + item_count.toString() + ")" 
							+ "(" + item_total.toString() + ")";
					sSpaces += "; ";
				} else {
//					sSpaces += "KXD "
//							+ "(" + item_count.toString() + ")" 
//							+ "(" + item_total.toString() + ")";
					item_count_KXD +=item_count;
					item_total_KXD +=item_total;
				}
//				i++;
//				if (i<lsSpaces.size())sSpaces += "; ";
			}
			if(item_count_KXD > 0) {
				sSpacesKXD += "KXD "
						+ "(" + item_count_KXD.toString() + ")" 
						+ "(" + item_total_KXD.toString() + ")";
				sSpaces += sSpacesKXD;
			}
			return sSpaces == ""?"Không có thông tin":sSpaces;
		} catch(Exception ex){
			ex.printStackTrace();
			return "Lỗi tính toán vị trí khoang";
		}
	}

	@Override
	public List<Warehouse> getByLotAndPackageId(Long skuid_link, String lotnumber, Integer packageid, Long stockid_link, Integer status) {
		return repositoty.getByLotAndPackageId(skuid_link, lotnumber, packageid, stockid_link, status);
	}

	@Override
	public List<Warehouse> findBySpaceepc(String spaceepc, Long stockid_link) {
		return repositoty.findBySpaceepc(spaceepc, stockid_link);
	}

	@Override
	public List<Warehouse> findBySpaceepcKXD(Long stockid_link) {
		return repositoty.findBySpaceepcKXD(stockid_link);
	}

	@Override
	public List<String> getSpaceEpcBySkucodeAndPcontract(Long skuid_link, String contractcode) {
		return repositoty.getSpaceEpcBySkucodeAndPcontract(skuid_link, contractcode);
	}
	
	@Override
	public List<String> getSpaceEpcBySkucode(Long skuid_link) {
		return repositoty.getSpaceEpcBySkucode(skuid_link);
	}

	@Override
	public List<Warehouse> getWarehouseBySpaceEpcNull(Long skuid_link, String contractcode, String buyercode, Long stockid_link) {
		return repositoty.getWarehouseBySpaceEpcNull(skuid_link, contractcode, buyercode, stockid_link);
	}

	@Override
	public List<Warehouse> getWarehouseProductBySpaceEpcNull(Long skuid_link, Long stockid_link) {
		return repositoty.getWarehouseProductBySpaceEpcNull(skuid_link, stockid_link);
	}
	
	@Override
	public List<Warehouse> getBySkuAndToCat(Long skuid_link, Long stockid_link) {
		return repositoty.getBySkuAndToCat(skuid_link, stockid_link);
	}
	
	@Override
	public List<StockOutD> get_inStock_bySku(Long skuid, Long stockid) {
//		System.out.println(skuid);
//		System.out.println(stockid);
		List<Object[]> list = repositoty.get_inStock_bySku(skuid, stockid); 
		List<StockOutD> result = new ArrayList<StockOutD>();
		for(Object[] item: list){
			Long item_count = (Long)item[0];
			Long skuid_link = (Long)item[1];
			
//			System.out.println(item_count);
			
			StockOutD newStockOutD = new StockOutD();
			newStockOutD.setSkuid_link(skuid_link);
			newStockOutD.setTotalpackage(item_count.intValue());
			result.add(newStockOutD);
		}
		return result;
	}

	@Override
	public List<Warehouse> getByStock_Sku_isNotFreeze(Long skuid_link, Long stockid_link, Integer number) {
		return repositoty.getByStock_Sku_isNotFreeze(skuid_link, stockid_link, number);
	}

	@Override
	public Long getByStock_Sku_isNotFreeze_number(Long skuid_link, Long stockid_link) {
		return repositoty.getByStock_Sku_isNotFreeze_number(skuid_link, stockid_link);
	}

	@Override
	public List<Warehouse> findProductBySpaceepc(String spaceepc, Long stockid_link) {
		return repositoty.findProductBySpaceepc(spaceepc, stockid_link);
	}

	@Override
	public List<Warehouse> findProductBySpaceepcKXD(Long stockid_link) {
		return repositoty.findProductBySpaceepcKXD(stockid_link);
	}

	@Override
	public List<String> getSpaceEpcBySkucode_Pcontract_Org(Long skuid_link, String contractcode, Long orgid_link) {
		return repositoty.getSpaceEpcBySkucode_Pcontract_Org(skuid_link, contractcode, orgid_link);
	}

	@Override
	public List<String> getSpaceEpcBySkucode_Pcontract_ProductBuyerCode(Long skuid_link, String contractcode,
			String buyercode) {
		return repositoty.getSpaceEpcBySkucode_Pcontract_ProductBuyerCode(skuid_link, contractcode, buyercode);
	}

	@Override
	public List<Warehouse> findBySpaceepc_stock_buyercode(String spaceepc, String buyercode, Long stockid_link) {
		return repositoty.findBySpaceepc_stock_buyercode(spaceepc, buyercode, stockid_link);
	}

	@Override
	public List<Warehouse> findBySpaceepcKXD_stock_buyercode(String buyercode, Long stockid_link) {
		return repositoty.findBySpaceepcKXD_stock_buyercode(buyercode, stockid_link);
	}

	@Override
	public List<String> getSpaceEpcBySkucode_Pcontract_Buyercode_Org(Long skuid_link, String contractcode,
			String buyercode, Long orgid_link) {
		return repositoty.getSpaceEpcBySkucode_Pcontract_Buyercode_Org(skuid_link, contractcode, buyercode, orgid_link);
	}

	@Override
	public List<Warehouse> findBySpaceepc_stock(String spaceepc, Long stockid_link) {
		return repositoty.findBySpaceepc_stock(spaceepc, stockid_link);
	}

	@Override
	public List<Warehouse> findBySpaceepcKXD_stock(Long stockid_link) {
		return repositoty.findBySpaceepcKXD_stock(stockid_link);
	}

	@Override
	public List<Warehouse> findByPContract_Product(Long pcontractid_link, Long productid_link, Long stockid_link,
			Long skuid_link) {
		return repositoty.findByPContract_Product(pcontractid_link, productid_link, stockid_link, skuid_link);
	}

	@Override
	public List<Warehouse> findByPContract_Product_xuatDieuCHuyen(Long pcontractid_link, Long productid_link,
			Long stockid_link, Long skuid_link) {
		return repositoty.findByPContract_Product_xuatDieuCHuyen(pcontractid_link, productid_link, stockid_link, skuid_link);
	}

	@Override
	public List<Warehouse> findByPContract_Product_loan(Long pcontractProduct_id, Long stockid_link, Long skuid_link) {
		return repositoty.findByPContract_Product_loan(pcontractProduct_id, stockid_link, skuid_link);
	}

	@Override
	public List<Warehouse> getBy_sku_stockList(Long skuid_link, List<Long> khoVai_list, List<String> listSelectedEpc) {
		return repositoty.getBy_sku_stockList(skuid_link, khoVai_list, listSelectedEpc);
	}

	@Override
	public List<String> findLotnumberBySku(Long skuid_link) {
		return repositoty.findLotnumberBySku(skuid_link);
	}

	@Override
	public Long getSumBy_Sku_Stock(Long skuid_link, Long stockid_link) {
		return repositoty.getSumBy_Sku_Stock(skuid_link, stockid_link);
	}

	@Override
	public List<Warehouse> getBySkuAndStock(Long skuid_link, Long stockid_link, Long skutypeid_link) {
		return repositoty.getBySkuAndStock(skuid_link, stockid_link, skutypeid_link);
	}

}
