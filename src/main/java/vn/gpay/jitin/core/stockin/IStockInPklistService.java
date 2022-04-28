package vn.gpay.jitin.core.stockin;

import java.util.Date;
import java.util.List;

import vn.gpay.jitin.core.base.Operations;



public interface IStockInPklistService extends Operations<StockInPklist>{

	public List<StockInPklist> inv_getbyid(long stockoutid_link);
	
	List<StockInPklist> getStockinPklByStockinDIdAndGreaterThanStatus(Long stockindid_link, String lotnumber, Integer status);
	
	List<StockInPklist> getStockinPklByStockinDIdAndEqualStatus(Long stockindid_link, String lotnumber, Integer status);

	Boolean checkFabricRollExisted(Long stockindid_link, String lotnumber, Integer packageid);
	
	Boolean checkFabricRollExisted_other(Long stockindid_link, String lotnumber, Integer packageid, Long id);
	
	List<StockInPklist> getStockinPklByStockinDLotAndPackageId(Long stockindid_link, String lotnumber, Integer packageid);
	
	List<StockInPklist> getStockinPklByStockinDLotAndPackageIdNotCheck10(Long stockindid_link, String lotnumber, Integer packageid);
	
	List<StockInPklist> getBy_Lot_Packageid_and_notId(Long stockindid_link, String lotnumber, Integer packageid, Long id);
	
	List<StockInPklist> getBy_Lot_StockinD(Long stockindid_link, String lotnumber);
	
	List<StockInPklist> getByStockinDId(Long stockindid_link);
	
	List<StockInPklist> getByStockinId(Long stockinid_link);

	Date getLastTimePklistUpdate(long stockinid_link);
	
	List<String> getLotnumberList_By_stockind(Long stockindid_link);
}
