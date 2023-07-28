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

package com.codeheadsystems.oop.mock.dagger;

import com.codeheadsystems.oop.OopMockConfiguration;
import com.codeheadsystems.oop.ResolverConfiguration;
import com.codeheadsystems.oop.mock.Hasher;
import com.codeheadsystems.oop.mock.converter.JsonConverter;
import com.codeheadsystems.oop.mock.manager.ResourceLookupManager;
import com.codeheadsystems.oop.mock.translator.Translator;
import com.google.common.collect.ImmutableMap;
import dagger.Binds;
import dagger.BindsOptionalOf;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import java.util.Map;
import java.util.Optional;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides all of the base pieces needed for the resolver set.
 *
 * @see com.codeheadsystems.oop.mock.resolver.ResolverFactory
 */
@Module(includes = {ResolverModule.ResolverConfigModule.class})
public interface ResolverModule {

  /**
   * The constant RESOLVER_MAP.
   */
  String RESOLVER_MAP = "resolver_map";
  /**
   * The constant RESOLVER_INTERNAL_MAP.
   */
  String RESOLVER_INTERNAL_MAP = "resolver internal map";
  /**
   * The constant DEFAULT_RESOLVER.
   */
  String DEFAULT_RESOLVER = "DEFAULT RESOLVER";
  /**
   * The constant RESOLVER_CLASSNAME.
   */
  String RESOLVER_CLASSNAME = "RESOLVER CLASSNAME";
  /**
   * The constant RESOLVER_ADDITIONAL_DEPS.
   */
  String RESOLVER_ADDITIONAL_DEPS = "Additional_Dependencies";

  /**
   * Binds oop mock configuration object.
   *
   * @param configuration the configuration
   * @return the object
   */
  @Named(RESOLVER_INTERNAL_MAP)
  @Binds
  @IntoMap
  @ClassKey(OopMockConfiguration.class)
  Object bindsOopMockConfiguration(OopMockConfiguration configuration);

  /**
   * Binds json converter object.
   *
   * @param converter the converter
   * @return the object
   */
  @Named(RESOLVER_INTERNAL_MAP)
  @Binds
  @IntoMap
  @ClassKey(JsonConverter.class)
  Object bindsJsonConverter(JsonConverter converter);

  /**
   * Binds resource lookup manager object.
   *
   * @param manager the manager
   * @return the object
   */
  @Named(RESOLVER_INTERNAL_MAP)
  @Binds
  @IntoMap
  @ClassKey(ResourceLookupManager.class)
  Object bindsResourceLookupManager(ResourceLookupManager manager);

  /**
   * Binds translator object.
   *
   * @param translator the translator
   * @return the object
   */
  @Named(RESOLVER_INTERNAL_MAP)
  @Binds
  @IntoMap
  @ClassKey(Translator.class)
  Object bindsTranslator(Translator translator);

  /**
   * Binds hasher object.
   *
   * @param hasher the hasher
   * @return the object
   */
  @Named(RESOLVER_INTERNAL_MAP)
  @Binds
  @IntoMap
  @ClassKey(Hasher.class)
  Object bindsHasher(Hasher hasher);

  /**
   * Default resolver string.
   *
   * @return the string
   */
  @BindsOptionalOf
  @Named(DEFAULT_RESOLVER)
  String defaultResolver();

  /**
   * The type Resolver config module.
   */
  @Module
  class ResolverConfigModule {

    private final Map<Class<?>, Object> additionalDependencies;

    /**
     * Instantiates a new Resolver config module.
     */
    public ResolverConfigModule() {
      this(ImmutableMap.of());
    }

    /**
     * Instantiates a new Resolver config module.
     *
     * @param additionalDependencies the additional dependencies
     */
    public ResolverConfigModule(final Map<Class<?>, Object> additionalDependencies) {
      this.additionalDependencies = additionalDependencies;
    }

    /**
     * Full dependencies map.
     *
     * @param addition      the addition
     * @param internal      the internal
     * @param configuration the configuration
     * @return the map
     */
    @Provides
    @Singleton
    @Named(RESOLVER_MAP)
    public Map<Class<?>, Object> fullDependencies(@Named(RESOLVER_ADDITIONAL_DEPS) final Map<Class<?>, Object> addition,
                                                  @Named(RESOLVER_INTERNAL_MAP) final Map<Class<?>, Object> internal,
                                                  final OopMockConfiguration configuration) {
      final ImmutableMap.Builder<Class<?>, Object> builder = ImmutableMap.builder();
      builder.putAll(internal);
      builder.putAll(addition);
      configuration.resolverConfiguration().ifPresent(rc -> builder.put(ResolverConfiguration.class, rc));
      return builder.build();
    }

    /**
     * Additional dependencies map.
     *
     * @return the map
     */
    @Provides
    @Singleton
    @Named(RESOLVER_ADDITIONAL_DEPS)
    public Map<Class<?>, Object> additionalDependencies() {
      return additionalDependencies;
    }

    /**
     * Resolver class name string.
     *
     * @param defaultResolver the default resolver
     * @param configuration   the configuration
     * @return the string
     */
    @Provides
    @Singleton
    @Named(RESOLVER_CLASSNAME)
    public String resolverClassName(@Named(DEFAULT_RESOLVER) final Optional<String> defaultResolver,
                                    final OopMockConfiguration configuration) {
      return configuration.resolverConfiguration()
          .map(ResolverConfiguration::resolverClass)
          .orElseGet(() -> defaultResolver
              .orElseThrow(() -> new IllegalArgumentException("No resolver found in configuration")));
    }
  }

}
