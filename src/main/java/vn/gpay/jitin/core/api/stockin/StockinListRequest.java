package vn.gpay.jitin.core.api.stockin;

import java.util.Date;
import java.util.List;

import vn.gpay.jitin.core.base.RequestBase;

public class StockinListRequest extends RequestBase{

	public String stockcode;
	public Long orgid_to_link;
	public Long orgid_from_link;
	public Long pcontractid_link = null;
	public Date stockindate_from;
	public Date stockindate_to;
	public Long stockintypeid_link;
	public Long stockintypeid_link_from;
	public Long stockintypeid_link_to;
	public int limit;
	public int page;
	public List<Integer> status = null;
	public Long skuid_link;
	public String pcontract;
	public String product;
}
