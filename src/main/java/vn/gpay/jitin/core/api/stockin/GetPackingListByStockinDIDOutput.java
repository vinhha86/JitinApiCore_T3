package vn.gpay.jitin.core.api.stockin;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.packinglist.PackingList;

public class GetPackingListByStockinDIDOutput  extends ResponseBase{

	public List<PackingList> data;
}
