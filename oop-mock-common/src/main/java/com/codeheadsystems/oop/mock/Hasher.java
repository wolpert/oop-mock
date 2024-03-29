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

package com.codeheadsystems.oop.mock;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The type Hasher.
 */
@Singleton
public class Hasher {

  /**
   * The constant NAMESPACE_DELIMINATOR.
   */
  public static final String NAMESPACE_DELIMINATOR = ":";
  /**
   * The constant DELIMINATOR.
   */
  public static final String DELIMINATOR = ".";
  /**
   * The System.
   */
  public final String system;

  /**
   * Used to get the current system.
   *
   * @param system the system
   */
  @Inject
  public Hasher(String system) {
    this.system = system;
  }

  /**
   * Hash string.
   *
   * @param args the args
   * @return the string
   */
  public String hash(final String... args) {
    return String.join(DELIMINATOR, args);
  }

  /**
   * Used to get the namespace for this instance.
   *
   * @param clazz for the namespace.
   * @return String string
   */
  public String namespace(final Class<?> clazz) {
    return String.join(NAMESPACE_DELIMINATOR, system, clazz.getCanonicalName());
  }

}
