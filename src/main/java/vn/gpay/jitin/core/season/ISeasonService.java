package vn.gpay.jitin.core.season;

import java.util.List;

import vn.gpay.jitin.core.base.Operations;


public interface ISeasonService extends Operations<Season> {
	public List<Season> getall_byorgrootid(long orgrootid_link);
}
