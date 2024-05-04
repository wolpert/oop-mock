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

package com.codeheadsystems.oop.dao.ddb;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.codeheadsystems.oop.dao.ddb.converter.DdbEntryConverter;
import com.codeheadsystems.oop.dao.ddb.model.DdbEntry;
import com.codeheadsystems.oop.mock.model.MockedData;
import com.codeheadsystems.test.datastore.DataStore;
import com.codeheadsystems.test.datastore.DynamoDbExtension;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * This is half unit test and have functional in that we use a real (local) ddb instance.
 * I'm just really tired of cases where people ignore how the database works until integ
 * tests. So forcing the issue here.
 *
 * <p>Don't worry, the DDB instance is fast. But you'll need to load the SQL lib into your
 * path for now to get it to work. See DynamoDBExtension for details. (Only needed for
 * intelij, not for gradle on the cmdline.)
 */
@ExtendWith({MockitoExtension.class, DynamoDbExtension.class})
class MockDataDdbDaoTest {

  /**
   * The constant NAMESPACE.
   */
  public static final String NAMESPACE = "namespace";
  /**
   * The constant LOOKUP.
   */
  public static final String LOOKUP = "lookup";
  /**
   * The constant DISCRIMINATOR.
   */
  public static final String DISCRIMINATOR = "discriminator";
  /**
   * The constant HASH.
   */
  public static final String HASH = "a";
  /**
   * The constant RANGE.
   */
  public static final String RANGE = "b";
  /**
   * The constant ENTRY_WITHOUT_DATA.
   */
  public static final DdbEntry ENTRY_WITHOUT_DATA = new DdbEntry(HASH, RANGE);
  /**
   * The constant MOCK_DATA.
   */
  public static final String MOCK_DATA = "c";
  /**
   * The constant ENTRY_WITH_DATA.
   */
  public static final DdbEntry ENTRY_WITH_DATA = new DdbEntry(HASH, RANGE, MOCK_DATA);
  private MockDataDdbDao dao;

  @DataStore private DynamoDBMapper mapper;
  @DataStore private AmazonDynamoDB amazonDynamoDb;
  @Mock private MockedData mockedData;
  @Mock private DdbEntryConverter converter;
  @Captor private ArgumentCaptor<DdbEntry> ddbEntryCaptor;

  /**
   * Sets .
   */
  @BeforeEach
  void setup() {
    dao = new MockDataDdbDao(mapper, converter);
    amazonDynamoDb.createTable(mapper.generateCreateTableRequest(DdbEntry.class)
        .withBillingMode(BillingMode.PAY_PER_REQUEST));
  }

  /**
   * Tear down.
   */
  @AfterEach
  void tearDown() {
    // force the table empty
    amazonDynamoDb.deleteTable(mapper.generateDeleteTableRequest(DdbEntry.class));
  }

  /**
   * Resolve doesnotexist.
   */
  @Test
  void resolve_doesnotexist() {
    when(converter.convert(NAMESPACE, LOOKUP, DISCRIMINATOR)).thenReturn(ENTRY_WITHOUT_DATA);
    assertThat(dao.resolve(NAMESPACE, LOOKUP, DISCRIMINATOR))
        .isNotNull()
        .isEmpty();
  }

  /**
   * Resolve exist.
   */
  @Test
  void resolve_exist() {
    when(converter.convert(NAMESPACE, LOOKUP, DISCRIMINATOR)).thenReturn(ENTRY_WITHOUT_DATA);
    when(converter.toMockedData(ddbEntryCaptor.capture())).thenReturn(Optional.of(mockedData));
    mapper.save(ENTRY_WITH_DATA);
    assertThat(dao.resolve(NAMESPACE, LOOKUP, DISCRIMINATOR))
        .isNotNull()
        .isNotEmpty()
        .contains(mockedData);
  }

  /**
   * Resolve exist butnomockeddata.
   */
  @Test
  void resolve_exist_butnomockeddata() {
    when(converter.convert(NAMESPACE, LOOKUP, DISCRIMINATOR)).thenReturn(ENTRY_WITHOUT_DATA);
    when(converter.toMockedData(ddbEntryCaptor.capture())).thenReturn(Optional.empty());
    mapper.save(ENTRY_WITH_DATA);
    assertThat(dao.resolve(NAMESPACE, LOOKUP, DISCRIMINATOR))
        .isNotNull()
        .isEmpty();
  }

  /**
   * Store.
   */
  @Test
  void store() {
    when(converter.convert(NAMESPACE, LOOKUP, DISCRIMINATOR, mockedData))
        .thenReturn(ENTRY_WITH_DATA);

    dao.store(NAMESPACE, LOOKUP, DISCRIMINATOR, mockedData);

    assertThat(mapper.load(ENTRY_WITHOUT_DATA))
        .isNotNull()
        .hasFieldOrPropertyWithValue("mockData", MOCK_DATA);
  }

  /**
   * Delete exists.
   */
  @Test
  void delete_exists() {
    mapper.save(ENTRY_WITH_DATA);
    assertThat(mapper.load(ENTRY_WITHOUT_DATA))
        .isNotNull()
        .hasFieldOrPropertyWithValue("mockData", MOCK_DATA); // verify we got this
    when(converter.convert(NAMESPACE, LOOKUP, DISCRIMINATOR)).thenReturn(ENTRY_WITHOUT_DATA);

    dao.delete(NAMESPACE, LOOKUP, DISCRIMINATOR);
    assertThat(dao.resolve(NAMESPACE, LOOKUP, DISCRIMINATOR))
        .isNotNull()
        .isEmpty();
  }

  /**
   * Delete doesnotexists.
   */
  @Test
  void delete_doesnotexists() {
    when(converter.convert(NAMESPACE, LOOKUP, DISCRIMINATOR)).thenReturn(ENTRY_WITHOUT_DATA);

    dao.delete(NAMESPACE, LOOKUP, DISCRIMINATOR);
    assertThat(dao.resolve(NAMESPACE, LOOKUP, DISCRIMINATOR))
        .isNotNull()
        .isEmpty();
    // really we are asserting there is no exception.
  }

}