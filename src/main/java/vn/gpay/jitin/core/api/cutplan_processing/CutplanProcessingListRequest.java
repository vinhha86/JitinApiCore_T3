package vn.gpay.jitin.core.api.cutplan_processing;

import java.util.Date;

import vn.gpay.jitin.core.base.RequestBase;

public class CutplanProcessingListRequest extends RequestBase{
	public Date processingdate_from;
	public Date processingdate_to;
	public Integer limit;
	public Integer page;
}
