package vn.gpay.jitin.core.api.invoice;

import java.util.List;

import vn.gpay.jitin.core.packinglist.LotNumber;
import vn.gpay.jitin.core.base.ResponseBase;

public class invoice_getlotnumber_response extends ResponseBase{
	public List<LotNumber> data;
}
