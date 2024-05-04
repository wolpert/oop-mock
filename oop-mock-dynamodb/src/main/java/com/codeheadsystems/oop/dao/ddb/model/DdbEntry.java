/*
 *    Copyright (c) 2022 Ned Wolpert <ned.wolpert@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.codeheadsystems.oop.dao.ddb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * The type Ddb entry.
 */
@DynamoDBTable(tableName = "oop_mock_data")
public class DdbEntry {

  private String hash;
  private String range;
  private String mockData;
  private Long ttl;

  /**
   * Instantiates a new Ddb entry.
   */
  public DdbEntry() {
  }

  /**
   * Instantiates a new Ddb entry.
   *
   * @param hash  the hash
   * @param range the range
   */
  public DdbEntry(final String hash, final String range) {
    this.hash = hash;
    this.range = range;
  }

  /**
   * Instantiates a new Ddb entry.
   *
   * @param hash     the hash
   * @param range    the range
   * @param mockData the mock data
   */
  public DdbEntry(final String hash, final String range, final String mockData) {
    this.hash = hash;
    this.range = range;
    this.mockData = mockData;
  }

  /**
   * Gets hash.
   *
   * @return the hash
   */
  @DynamoDBHashKey(attributeName = "hash")
  public String getHash() {
    return hash;
  }

  /**
   * Sets hash.
   *
   * @param hash the hash
   */
  public void setHash(final String hash) {
    this.hash = hash;
  }

  /**
   * Gets range.
   *
   * @return the range
   */
  @DynamoDBRangeKey(attributeName = "range")
  public String getRange() {
    return range;
  }

  /**
   * Sets range.
   *
   * @param range the range
   */
  public void setRange(final String range) {
    this.range = range;
  }

  /**
   * Gets mock data.
   *
   * @return the mock data
   */
  @DynamoDBAttribute(attributeName = "mock_data")
  public String getMockData() {
    return mockData;
  }

  /**
   * Sets mock data.
   *
   * @param mockData the mock data
   */
  public void setMockData(final String mockData) {
    this.mockData = mockData;
  }

  /**
   * Gets ttl.
   *
   * @return the ttl
   */
  @DynamoDBAttribute(attributeName = "ttl")
  public Long getTtl() {
    return ttl;
  }

  /**
   * Sets ttl.
   *
   * @param ttl the ttl
   */
  public void setTtl(final Long ttl) {
    this.ttl = ttl;
  }

}
