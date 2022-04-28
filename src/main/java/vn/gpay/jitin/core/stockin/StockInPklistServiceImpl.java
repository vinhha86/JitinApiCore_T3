package vn.gpay.jitin.core.stockin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.jitin.core.base.AbstractService;

@Service
public class StockInPklistServiceImpl extends AbstractService<StockInPklist> implements IStockInPklistService{

	@Autowired StockInPklistRepository repository; 
	
	@Override
	protected JpaRepository<StockInPklist, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<StockInPklist> inv_getbyid(long stockinid_link) {
		// TODO Auto-generated method stub
		return repository.inv_getbyid(stockinid_link);
	}
	
	@Override
	public Date getLastTimePklistUpdate(long stockinid_link) {
		return repository.getLastTimePklistUpdate(stockinid_link);
	}

	@Override
	public List<StockInPklist> getStockinPklByStockinDIdAndGreaterThanStatus(Long stockindid_link, String lotnumber, Integer status) {
		// TODO Auto-generated method stub
		return repository.getStockinPklByStockinDIdAndGreaterThanStatus(stockindid_link, lotnumber, status);
	}

	@Override
	public List<StockInPklist> getStockinPklByStockinDIdAndEqualStatus(Long stockindid_link, String lotnumber, Integer status) {
		return repository.getStockinPklByStockinDIdAndEqualStatus(stockindid_link, lotnumber, status);
	}

	@Override
	public List<StockInPklist> getStockinPklByStockinDLotAndPackageId(Long stockindid_link, String lotnumber,
			Integer packageid) {
		return repository.getBy_Lot_Packageid(stockindid_link, lotnumber, packageid);
	}
	
	@Override
	public Boolean checkFabricRollExisted(Long stockindid_link, String lotnumber, Integer packageid){
		if (repository.getBy_Lot_Packageid(stockindid_link, lotnumber.toUpperCase(), packageid).size() > 0)
			return true;
		else
			return false;
	}
	
	@Override
	public Boolean checkFabricRollExisted_other(Long stockindid_link, String lotnumber, Integer packageid, Long id){
		if (repository.getBy_Lot_Packageid_other(stockindid_link, lotnumber.toUpperCase(), packageid, id).size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<StockInPklist> getBy_Lot_StockinD(Long stockindid_link, String lotnumber) {
		return repository.getBy_Lot_StockinD(stockindid_link, lotnumber);
	}

	@Override
	public List<StockInPklist> getBy_Lot_Packageid_and_notId(Long stockindid_link, String lotnumber, Integer packageid,
			Long id) {
		return repository.getBy_Lot_Packageid_and_notId(stockindid_link, lotnumber, packageid, id);
	}

	@Override
	public List<StockInPklist> getByStockinDId(Long stockindid_link) {
		// TODO Auto-generated method stub
		return repository.getByStockinDId(stockindid_link);
	}

	@Override
	public List<StockInPklist> getByStockinId(Long stockinid_link) {
		// TODO Auto-generated method stub
		return repository.getByStockinId(stockinid_link);
	}

	@Override
	public List<StockInPklist> getStockinPklByStockinDLotAndPackageIdNotCheck10(Long stockindid_link, String lotnumber,
			Integer packageid) {
		// TODO Auto-generated method stub
		return repository.getStockinPklByStockinDLotAndPackageIdNotCheck10(stockindid_link, lotnumber, packageid);
	}

	@Override
	public List<String> getLotnumberList_By_stockind(Long stockindid_link) {
		return repository.getLotnumberList_By_stockind(stockindid_link);
	}
}
