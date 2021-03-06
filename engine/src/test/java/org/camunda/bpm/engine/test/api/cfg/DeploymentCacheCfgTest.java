/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.camunda.bpm.engine.test.api.cfg;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.persistence.deploy.DeploymentCache;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.util.ProcessEngineBootstrapRule;
import org.camunda.bpm.engine.test.util.ProvidedProcessEngineRule;
import org.camunda.commons.utils.cache.Cache;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Johannes Heinemann
 */
public class DeploymentCacheCfgTest {

  protected ProcessEngineBootstrapRule cacheFactoryBootstrapRule = new ProcessEngineBootstrapRule() {
    public ProcessEngineConfiguration configureEngine(ProcessEngineConfigurationImpl configuration) {
      // apply configuration options here
      configuration.setCacheCapacity(2);
      configuration.setCacheFactory(new MyCacheFactory());
      return configuration;
    }
  };

  protected ProvidedProcessEngineRule cacheFactoryEngineRule = new ProvidedProcessEngineRule(cacheFactoryBootstrapRule);

  @Rule
  public RuleChain ruleChain = RuleChain.outerRule(cacheFactoryBootstrapRule).around(cacheFactoryEngineRule);
  RepositoryService repositoryService;
  ProcessEngineConfigurationImpl processEngineConfiguration;

  @Before
  public void initialize() {
    repositoryService = cacheFactoryEngineRule.getRepositoryService();
    processEngineConfiguration = cacheFactoryEngineRule.getProcessEngineConfiguration();
  }

  @Test
  @Deployment(resources =
      {"org/camunda/bpm/engine/test/api/cfg/DeploymentCacheCfgTest.testDefaultCacheRemovesLRUElementWhenMaxSizeIsExceeded.bpmn20.xml"})
  public void testPlugInOwnCacheImplementation() {

    // given
    DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();

    // when
    Cache cache = deploymentCache.getProcessDefinitionCache();

    // then
    assertThat(cache, instanceOf(MyCacheImplementation.class));
  }

  @Test
  @Deployment(resources =
      {"org/camunda/bpm/engine/test/api/cfg/DeploymentCacheCfgTest.testDefaultCacheRemovesLRUElementWhenMaxSizeIsExceeded.bpmn20.xml"})
  public void testDefaultCacheRemovesLRUElementWhenMaxSizeIsExceeded() {
    // The engine rule sets the maximum number of elements of the to 2.
    // Accordingly, one process should not be contained in the cache anymore at the end.

    // given
    String processDefinitionIdOne = repositoryService.createProcessDefinitionQuery()
        .processDefinitionKey("one")
        .singleResult()
        .getId();
    String processDefinitionIdTwo = repositoryService.createProcessDefinitionQuery()
        .processDefinitionKey("two")
        .singleResult()
        .getId();
    String processDefinitionIdThree = repositoryService.createProcessDefinitionQuery()
        .processDefinitionKey("three")
        .singleResult()
        .getId();

    // when
    DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();

    // then
    int numberOfProcessesInCache = 0;
    numberOfProcessesInCache +=
        deploymentCache.getProcessDefinitionCache().get(processDefinitionIdOne) == null ? 0 : 1;
    numberOfProcessesInCache +=
        deploymentCache.getProcessDefinitionCache().get(processDefinitionIdTwo) == null ? 0 : 1;
    numberOfProcessesInCache +=
        deploymentCache.getProcessDefinitionCache().get(processDefinitionIdThree) == null ? 0 : 1;

    assertEquals(2, numberOfProcessesInCache);
  }
}