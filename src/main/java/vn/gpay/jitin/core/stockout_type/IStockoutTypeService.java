package vn.gpay.jitin.core.stockout_type;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;

public interface IStockoutTypeService extends Operations<StockoutType> {

	List<StockoutType> findStockoutTypeByIdRange(Long idFrom, Long idTo);
}
