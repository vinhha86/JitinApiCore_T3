package vn.gpay.jitin.core.stockin;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IStockinLotService extends Operations<StockinLot>{
	List<StockinLot> getByStockinId(Long stockinid_link);
	
	List<StockinLot> getByStockinDId(Long stockindid_link);
	
	List<StockinLot> getByStockinD_LOT(Long stockindid_link, String lot_number);

	void recal_Totalcheck(Long stockindid_link, String lot_number);
	
	
}
