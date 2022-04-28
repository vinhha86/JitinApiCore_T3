package vn.gpay.jitin.core.api.pcontract;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.pcontract.PContract;


public class PContract_getbypaging_response extends ResponseBase{
	public List<PContract> data;
	public long totalCount;
}
