package vn.gpay.jitin.core.api.invcheck;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.invcheck.InvcheckEpc;

public class EpcBySkuResponse extends ResponseBase{
	public List<InvcheckEpc> data;
}
