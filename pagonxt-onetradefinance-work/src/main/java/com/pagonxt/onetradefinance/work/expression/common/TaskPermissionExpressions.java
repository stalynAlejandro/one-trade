package com.pagonxt.onetradefinance.work.expression.common;

import com.flowable.core.idm.api.PlatformGroup;
import com.pagonxt.onetradefinance.work.utils.CaseUtils;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.identitylink.api.IdentityLinkType;
import org.flowable.identitylink.service.impl.persistence.entity.IdentityLinkEntity;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * class for task permissions expressions
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.engine.TaskService
 * @see  com.pagonxt.onetradefinance.work.utils.CaseUtils
 * @see org.flowable.idm.api.IdmIdentityService
 * @see org.flowable.cmmn.api.CmmnRuntimeService
 * @since jdk-11.0.13
 */
@Service
public class TaskPermissionExpressions {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(TaskPermissionExpressions.class);

    private static final String TASK_VARIABLE_CANDIDATE_GROUP = "candidateGroup";

    //Class attributes
    private final TaskService taskService;
    private final CmmnRuntimeService cmmnRuntimeService;
    private final IdmIdentityService idmIdentityService;
    private final CaseUtils caseUtils;

    /**
     * constructor method
     * @param taskService       : a TaskService object
     * @param cmmnRuntimeService: a CmmnRuntimeService object
     * @param idmIdentityService: a IdmIdentityService object
     * @param caseUtils         : a CaseUtils object
     */
    public TaskPermissionExpressions(TaskService taskService,
                                     CmmnRuntimeService cmmnRuntimeService,
                                     IdmIdentityService idmIdentityService,
                                     CaseUtils caseUtils) {
        this.taskService = taskService;
        this.cmmnRuntimeService = cmmnRuntimeService;
        this.idmIdentityService = idmIdentityService;
        this.caseUtils = caseUtils;
    }

    /**
     * Method to resolve permissions
     * @param execution : a DelegateExecution object
     * @param task      : a TaskEntity object
     * @see org.flowable.engine.delegate.DelegateExecution
     * @see org.flowable.task.service.impl.persistence.entity
     */
    public void resolvePermissions(DelegateExecution execution, TaskEntity task) {
        LOG.debug("resolveCandidateGroups(task: {})", task.getId());

        List<String> candidateGroupsIdentifiers = getTaskCandidateGroupsIdentifiers(task);
        if (!candidateGroupsIdentifiers.isEmpty()) {
            //Recover needed root case instance variables
            String caseInstanceId = caseUtils.getCaseIdFromDescendantExecution(execution);
            String pgnNxtResolutionGroupPrefix;
            try {
                String product = cmmnRuntimeService.getVariable(caseInstanceId, PRODUCT).toString();
                String event = cmmnRuntimeService.getVariable(caseInstanceId, EVENT).toString();
                String country = cmmnRuntimeService.getVariable(caseInstanceId, COUNTRY).toString();
                pgnNxtResolutionGroupPrefix = String.format("grpRes_%s_%s_%s", country, product, event);
            } catch (Exception e) {
                LOG.error("Error recovering some mandatory case variable", e);
                throw new NoSuchElementException(String.format("Error recovering some mandatory" +
                        " case variable:  %s, %s, %s", PRODUCT, EVENT, COUNTRY));
            }
            // Replace identifier por real Flowable group
            for (String identifier : candidateGroupsIdentifiers) {
                //Change candidate group only if configured groupId is not a real flowable group
                if (!groupExists(identifier)) {
                    taskService.deleteGroupIdentityLink(task.getId(), identifier, IdentityLinkType.CANDIDATE);
                    taskService.addGroupIdentityLink(task.getId(), String
                            .format("%s_%s", pgnNxtResolutionGroupPrefix, identifier), IdentityLinkType.CANDIDATE);
                    taskService.setVariable(task.getId(), TASK_VARIABLE_CANDIDATE_GROUP,  String
                            .format("%s_%s", pgnNxtResolutionGroupPrefix, identifier));
                    //Needed for obtain candidate group from task in dashboard
                }
            }
            LOG.debug("Candidate group identifiers replaced from task");
        }
    }

    /**
     * Method to get the identifiers of the candidate groups
     * @param task :  a TaskEntity object
     * @return a list with candidate groups
     */
    private List<String> getTaskCandidateGroupsIdentifiers(TaskEntity task) {
        List<String> candidateGroupsIdentifiers = task.getIdentityLinks().stream()
                .filter(identity -> IdentityLinkType.CANDIDATE.
                        equals(identity.getType()) && identity.getGroupId() != null)
                .map(IdentityLinkEntity::getGroupId)
                .distinct()
                .collect(Collectors.toList());
        LOG.debug("Candidate group identifiers extracted: {}", candidateGroupsIdentifiers);

        return candidateGroupsIdentifiers;
    }

    public boolean groupExists(String groupId) {
        PlatformGroup group = (PlatformGroup)idmIdentityService.createGroupQuery().groupId(groupId).singleResult();
        return group != null;
    }
}
