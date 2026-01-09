/*
 * Copyright (C) <2023> <Snow>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ruoyi.common.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;

/**
 * <ul>
 * <li>Lambda : 列可以用 Lambda 注入，而不是使用 String</li>
 * <li>Chain  : 可以用 chain 的方式执行 query 、one 、page 、update 等</li>
 * </ul>
 *
 * @author Snow
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

	// -------------------------------------------------- Wrapper ------------------------------------------------------
	default QueryWrapper<T> queryWrapper() {
		return Wrappers.query();
	}

	default QueryWrapper<T> queryWrapper(T entity) {
		return Wrappers.query(entity);
	}

	// ·················································································································
	default UpdateWrapper<T> updateWrapper() {
		return Wrappers.update();
	}

	default UpdateWrapper<T> updateWrapper(T entity) {
		return Wrappers.update(entity);
	}

	// -------------------------------------------------- LambdaWrapper ------------------------------------------------
	default LambdaQueryWrapper<T> lambdaQueryWrapper() {
		return Wrappers.lambdaQuery();
	}

	default LambdaQueryWrapper<T> lambdaQueryWrapper(T entity) {
		return Wrappers.lambdaQuery(entity);
	}

	default LambdaQueryWrapper<T> lambdaQueryWrapper(Class<T> entityClass) {
		return Wrappers.lambdaQuery(entityClass);
	}

	// ·················································································································
	default LambdaUpdateWrapper<T> lambdaUpdateWrapper() {
		return Wrappers.lambdaUpdate();
	}

	default LambdaUpdateWrapper<T> lambdaUpdateWrapper(T entity) {
		return Wrappers.lambdaUpdate(entity);
	}

	default LambdaUpdateWrapper<T> lambdaUpdateWrapper(Class<T> entityClass) {
		return Wrappers.lambdaUpdate(entityClass);
	}

	// -------------------------------------------------- ChainWrapper -------------------------------------------------
	default QueryChainWrapper<T> chainQueryWrapper() {
		return ChainWrappers.queryChain(this);
	}

	default QueryChainWrapper<T> chainQueryWrapper(Class<T> entityClass) {
		return ChainWrappers.queryChain(entityClass);
	}

	// ·················································································································
	default UpdateChainWrapper<T> chainUpdateWrapper() {
		return ChainWrappers.updateChain(this);
	}

	default UpdateChainWrapper<T> chainUpdateWrapper(Class<T> entityClass) {
		return ChainWrappers.updateChain(entityClass);
	}

	// ------------------------------------------------- ChainLambdaWrapper --------------------------------------------
	default LambdaQueryChainWrapper<T> lambdaChainQueryWrapper() {
		return ChainWrappers.lambdaQueryChain(this);
	}

	default LambdaQueryChainWrapper<T> lambdaChainQueryWrapper(T entity) {
		return ChainWrappers.lambdaQueryChain(this, entity);
	}

	default LambdaQueryChainWrapper<T> lambdaChainQueryWrapper(Class<T> entityClass) {
		return ChainWrappers.lambdaQueryChain(this, entityClass);
	}

	// ·················································································································
	default LambdaUpdateChainWrapper<T> lambdaChainUpdateWrapper() {
		return ChainWrappers.lambdaUpdateChain(this);
	}

	default LambdaUpdateChainWrapper<T> lambdaChainUpdateWrapper(Class<T> entityClass) {
		return ChainWrappers.lambdaUpdateChain(entityClass);
	}

}
