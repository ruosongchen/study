package com.zx.code.dao;

import java.util.List;
import java.util.Map;

import com.zx.code.model.ColumnEntity;
import com.zx.code.model.TableInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcColumnInfo {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<ColumnEntity> findList(String sql, Map<String, Object> params) {
		// 查询第page页数据
		List<ColumnEntity> list = namedParameterJdbcTemplate.query(sql, params,
				new BeanPropertyRowMapper<ColumnEntity>(ColumnEntity.class));
		return list;
	}

	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public String getTableComment(String sql, Map<String, Object> params) {
		// 查询第page页数据
		// 查询第page页数据
		List<TableInfoEntity> list = namedParameterJdbcTemplate.query(sql, params,
				new BeanPropertyRowMapper<TableInfoEntity>(TableInfoEntity.class));
		if (list != null && list.size() > 0) {
			return list.get(0).getComments();
		}
		return null;
	}

}
