package vn.gpay.jitin.core.api.invoice;

import java.util.List;

import vn.gpay.jitin.core.packinglist.PackingList;
import vn.gpay.jitin.core.base.ResponseBase;

public class invoice_getpkl_bylotnumber_response extends ResponseBase{
	public List<PackingList> data;
}
