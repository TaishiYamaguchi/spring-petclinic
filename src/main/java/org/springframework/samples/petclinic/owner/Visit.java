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

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * ペットの診察記録を表すドメインオブジェクト。 診察日と診察内容の説明を保持します。
 *
 * @author Ken Krebs
 * @author Dave Syer
 */
@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {

	@Column(name = "visit_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	@NotBlank
	private String description;

	/**
	 * 現在の日付で診察記録の新しいインスタンスを作成します。
	 */
	public Visit() {
		this.date = LocalDate.now();
	}

	/**
	 * 診察日を取得します。
	 * @return 診察日
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * 診察日を設定します。
	 * @param date 診察日
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * 診察内容の説明を取得します。
	 * @return 診察内容の説明
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * 診察内容の説明を設定します。
	 * @param description 診察内容の説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
