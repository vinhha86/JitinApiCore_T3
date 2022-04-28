package vn.gpay.jitin.core.api.encode;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.tagencode.WareHouse_Encode;

public class encode_getlist_response extends ResponseBase {
	public List<WareHouse_Encode> data;
	public long totalCount;
}
