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
package org.camunda.bpm.container.impl.jboss.extension.handler;

import org.camunda.bpm.container.impl.jboss.service.ServiceNames;
import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceName;

/**
 * @author Daniel Meyer
 * @author Christian Lipphardt
 *
 */
public class JobExecutorRemove extends AbstractRemoveStepHandler {

  public static JobExecutorRemove INSTANCE = new JobExecutorRemove();

  protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
    ServiceName name = ServiceNames.forMscExecutorService();
    context.removeService(name);
  }

}
