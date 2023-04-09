package com.pagonxt.onetradefinance.work.users.service;


import com.flowable.core.idm.api.PlatformGroup;
import com.flowable.core.idm.api.PlatformIdentityService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.work.common.GroupRoleCommonConstants.*;

/**
 * Service class to recalculate Flowable groups
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.core.idm.api.PlatformIdentityService
 * @since jdk-11.0.13
 */
@Service
public class RecalculateFlowableGroupsService {

    public static final String RESOLUTION_GROUP_START_WITH = "grpRes";
    public static final String SEPARATOR = "_";

    public static final String PAYLOAD_GROUPS_GROUP = "group";
    public static final String PAYLOAD_GROUPS_ROLE = "role";

    //class attribute
    private final PlatformIdentityService platformIdentityService;

    /**
     * constructor method
     * @param platformIdentityService : a PlatformIdentityService object
     */
    public RecalculateFlowableGroupsService(PlatformIdentityService platformIdentityService) {
        this.platformIdentityService = platformIdentityService;
    }

    /**
     * Method to recalculate groups
     * @param userId    : a string object with the user id
     * @param groups    : a list of groups
     * @param countries : a list of countries
     */
    public void recalculateGroups(String userId, List<Map<String, Object>> groups, List<String> countries) {
        groups = removeDuplicatesFromList(groups);

        List<String> resolutionGroups = calculateFlowableResolutionGroupsNames(groups, countries);

        deletePreviousResolutionGroups(userId, resolutionGroups);

        for (String resolutionGroup : resolutionGroups) {
            createFlowableGroupIfNotExists(resolutionGroup);

            if (userIsNotAlreadyMember(userId, resolutionGroup)) {
                platformIdentityService.createMembership(userId, resolutionGroup);
            }
        }
    }

    /**
     * Method to remove duplicates from a list
     * @param groups    : a list of groups
     * @return a modified list of groups
     */
    private List<Map<String, Object>> removeDuplicatesFromList(List<Map<String, Object>> groups) {
        if (groups == null || groups.isEmpty()) {
            return Collections.emptyList();
        }

        return groups.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Method to check if a user is not already member of a group
     * @param userId    : a string object with the user id
     * @param groupId   : a string object with the group id
     * @return a modified list of groups
     */
    private boolean userIsNotAlreadyMember(String userId, String groupId) {
        return platformIdentityService.createGroupQuery().
                groupMember(userId).list()
                .stream().noneMatch(group -> groupId.equals(group.getId()));
    }

    /**
     * Method to create a Flowable group if not exists
     * @param resolutionGroup : a string object with the resolution group
     */
    private void createFlowableGroupIfNotExists(String resolutionGroup) {
        PlatformGroup platformGroup = platformIdentityService
                .createPlatformGroupQuery().groupId(resolutionGroup).singleResult();

        if (platformGroup == null) {
            platformIdentityService.createNewGroupBuilder(resolutionGroup)
                    .name(resolutionGroup)
                    .save();
        }
    }

    /**
     * Method to delete previous resolution groups
     * @param userId            : a string object with the user id
     * @param resolutionGroups  : a list with resolution groups
     */
    private void deletePreviousResolutionGroups(String userId, List<String> resolutionGroups) {
        List<PlatformGroup> userGroups = platformIdentityService.createPlatformGroupQuery().groupMember(userId).list();
        List<String> grpResUserGroups =
                userGroups.stream().filter(group -> group.getId().startsWith(RESOLUTION_GROUP_START_WITH))
                        .map(PlatformGroup::getKey).collect(Collectors.toList());

        grpResUserGroups.removeAll(resolutionGroups);
        for (String grpResUserGroup : grpResUserGroups) {
            platformIdentityService.deleteMembership(userId, grpResUserGroup);
        }
    }

    /**
     * Method to calculate Flowable resolution groups names
     * @param selectedCountries         : a list with selected
     * @param selectedResolutionGroups  : a list with selected resolution groups
     * @return a list of resolution groups names
     */
    private List<String> calculateFlowableResolutionGroupsNames(List<Map<String, Object>> selectedResolutionGroups,
                                                                List<String> selectedCountries) {
        List<String> flowableGroups = new ArrayList<>();

        if (selectedCountries == null || selectedCountries.isEmpty()
                || selectedResolutionGroups == null || selectedResolutionGroups.isEmpty()) {

            return flowableGroups;
        }

        for (Map<String, Object> selectedResolutionGroup : selectedResolutionGroups) {
            String userGroup = (String) selectedResolutionGroup.get(PAYLOAD_GROUPS_GROUP);

           String userRole = (String) selectedResolutionGroup.get(PAYLOAD_GROUPS_ROLE);
            userRole = capitalizeFirstLetter(userRole);

            for (String selectedCountry : selectedCountries) {
                String baseFlowableGroupWithoutRole = RESOLUTION_GROUP_START_WITH
                        .concat(SEPARATOR).concat(selectedCountry)
                        .concat(SEPARATOR).concat(userGroup).concat(SEPARATOR);

                String resolutionGroup = baseFlowableGroupWithoutRole.concat(userRole);
                flowableGroups.add(resolutionGroup);

                if (Objects.equals(userRole, ROLE_ADMINISTRATOR)) {
                    resolutionGroup = baseFlowableGroupWithoutRole.concat(ROLE_SUPERVISOR);
                    flowableGroups.add(resolutionGroup);
                    resolutionGroup = baseFlowableGroupWithoutRole.concat(ROLE_USER);
                    flowableGroups.add(resolutionGroup);
                }
                if (Objects.equals(userRole, ROLE_SUPERVISOR)) {
                    resolutionGroup = baseFlowableGroupWithoutRole.concat(ROLE_USER);
                    flowableGroups.add(resolutionGroup);
                }
            }
        }

        return flowableGroups;
    }

    /**
     * Method to capitalize the first letter of a string
     * @param myString  : a string object
     * @return a string object
     */
    private String capitalizeFirstLetter(String myString) {

        return myString.substring(0, 1).toUpperCase() + myString.substring(1);
    }

}
