package vn.gpay.jitin.core.api.org;

import java.util.List;

import vn.gpay.jitin.core.base.ResponseBase;
import vn.gpay.jitin.core.org.OrgTree;

public class Org_getTree_response extends ResponseBase {
	public List<OrgTree> children;
}
