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

package com.codeheadsystems.oop.mock.resolver;

import com.codeheadsystems.oop.mock.model.MockedData;
import java.util.Optional;

/**
 * Provides the way we want to lookup the data in the data store for the discriminator.
 * This should be the full discriminator including namespace. These, obviously, need to
 * be unique.
 */
public interface MockDataResolver {

  /**
   * Resolve optional.
   *
   * @param namespace     the namespace
   * @param lookup        the lookup
   * @param discriminator the discriminator
   * @return the optional
   */
  Optional<MockedData> resolve(String namespace, String lookup, String discriminator);

}
