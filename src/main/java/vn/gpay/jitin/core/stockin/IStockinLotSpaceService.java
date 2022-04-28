package vn.gpay.jitin.core.stockin;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IStockinLotSpaceService extends Operations<StockinLotSpace>{
	List<StockinLotSpace> getByLotAndSpace(Long stockinlotid_link, String spaceepcid_link);
	List<StockinLotSpace> getByLot(Long stockinlotid_link);
}
