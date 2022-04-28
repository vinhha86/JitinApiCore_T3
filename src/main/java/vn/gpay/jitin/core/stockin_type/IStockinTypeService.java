package vn.gpay.jitin.core.stockin_type;

import java.util.List;

import vn.gpay.jitin.core.stockin_type.StockinType;
import vn.gpay.jitin.core.base.Operations;

public interface IStockinTypeService extends Operations<StockinType> {
	public List<StockinType> findType(Long typeFrom, Long typeTo);
}
