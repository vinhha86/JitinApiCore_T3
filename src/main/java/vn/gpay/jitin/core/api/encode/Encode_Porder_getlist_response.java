package vn.gpay.jitin.core.api.encode;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.encode.Encode;

public class Encode_Porder_getlist_response extends ResponseBase {
	public List<Encode> data;
	public long totalCount;
}
