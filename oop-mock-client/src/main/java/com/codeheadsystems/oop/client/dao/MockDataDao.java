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

package com.codeheadsystems.oop.client.dao;

import com.codeheadsystems.oop.mock.model.MockedData;
import com.codeheadsystems.oop.mock.resolver.MockDataResolver;

/**
 * The interface Mock data dao.
 */
public interface MockDataDao extends MockDataResolver {

  /**
   * Store.
   *
   * @param namespace     the namespace
   * @param lookup        the lookup
   * @param discriminator the discriminator
   * @param data          the data
   */
  void store(String namespace, String lookup, String discriminator, MockedData data);

  /**
   * Delete.
   *
   * @param namespace     the namespace
   * @param lookup        the lookup
   * @param discriminator the discriminator
   */
  void delete(String namespace, String lookup, String discriminator);

}
