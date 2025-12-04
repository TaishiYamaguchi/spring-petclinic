/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Ownerドメインオブジェクトのリポジトリインターフェース。
 * すべてのメソッド名はSpring Dataの命名規則に準拠しているため、
 * このインターフェースはSpring Dataで簡単に拡張できます。
 * 参照: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Wick Dynex
 */
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

	/**
	 * 姓でオーナーを検索します。
	 * 指定された文字列で<i>始まる</i>姓を持つすべてのオーナーを返します。
	 * 
	 * @param lastName 検索する姓（前方一致）
	 * @param pageable ページング情報
	 * @return 一致するオーナーのページ（見つからない場合は空のページ）
	 */
	Page<Owner> findByLastNameStartingWith(String lastName, Pageable pageable);

	/**
	 * IDでオーナーを検索します。
	 * <p>
	 * このメソッドは、見つかった場合はOwnerを含むOptionalを返します。
	 * 指定されたIDのOwnerが見つからない場合は、空のOptionalを返します。
	 * </p>
	 * 
	 * @param id 検索するID
	 * @return Ownerを含むOptional、または空のOptional
	 * @throws IllegalArgumentException IDがnullの場合
	 */
	Optional<Owner> findById(Integer id);

}
