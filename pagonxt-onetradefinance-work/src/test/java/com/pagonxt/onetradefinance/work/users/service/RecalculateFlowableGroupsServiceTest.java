package com.pagonxt.onetradefinance.work.users.service;

import com.flowable.core.idm.api.PlatformGroup;
import com.flowable.core.idm.api.PlatformGroupBuilder;
import com.flowable.core.idm.api.PlatformGroupQuery;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.idm.api.GroupQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@UnitTest
class RecalculateFlowableGroupsServiceTest {

    @Mock
    PlatformIdentityService platformIdentityService;

    @InjectMocks
    RecalculateFlowableGroupsService recalculateFlowableGroupsService;

    @Test
    void recalculateGroups_groupIsEmpty_countriesNotEmpty_userHasAssignedGroups_deletesUserMembership() {
        // Given
        String groupOne = "grpRes_Test_01";
        String userTest = "test.user";
        List<Map<String, Object>> groups = new ArrayList<>();
        List<String> countries = new ArrayList<>();
        countries.add("ESP");

        PlatformGroupQuery platformGroupQuery = mock(PlatformGroupQuery.class);
        PlatformGroup platformGroup = mock(PlatformGroup.class);
        doReturn(groupOne).when(platformGroup).getId();
        doReturn(groupOne).when(platformGroup).getKey();
        List<PlatformGroup> userGroups = List.of(platformGroup);

        doReturn(platformGroupQuery).when(platformIdentityService).createPlatformGroupQuery();
        doReturn(platformGroupQuery).when(platformGroupQuery).groupMember(userTest);
        doReturn(userGroups).when(platformGroupQuery).list();

        // When
        recalculateFlowableGroupsService.recalculateGroups(userTest, groups, countries);

        // Then
        verify(platformIdentityService).deleteMembership(userTest, groupOne);
    }

    @Test
    void recalculateGroups_groupIsNotEmpty_countriesNotEmpty_userHasNotAssignedCountriesAndRole_flowableGroupExists_userNotMember_createsUserMembership() {
        // Given
        String userTest = "test.user";
        List<Map<String, Object>> groups = new ArrayList<>();
        Map<String, Object> group = new HashMap<>();
        group.put("group", "Test_01");
        group.put("role", "user");
        groups.add(group);
        List<String> countries = new ArrayList<>();
        countries.add("ESP");

        PlatformGroupQuery platformGroupQuery = mock(PlatformGroupQuery.class);
        List<PlatformGroup> userGroups = new ArrayList<>();
        doReturn(platformGroupQuery).when(platformGroupQuery).groupMember(userTest);
        doReturn(userGroups).when(platformGroupQuery).list();

        String userRole = capitalizeFirstLetter((String) group.get("role"));
        String groupId = "grpRes".concat("_").concat("ESP").concat("_").concat("Test_01").concat("_").concat(userRole);

        PlatformGroup platformGroup = mock(PlatformGroup.class);
        doReturn(platformGroupQuery).when(platformIdentityService).createPlatformGroupQuery();
        doReturn(platformGroupQuery).when(platformGroupQuery).groupId(groupId);
        doReturn(platformGroup).when(platformGroupQuery).singleResult();

        GroupQuery groupQuery = mock(GroupQuery.class);
        doReturn(groupQuery).when(platformIdentityService).createGroupQuery();
        doReturn(groupQuery).when(groupQuery).groupMember(userTest);

        // When
        recalculateFlowableGroupsService.recalculateGroups(userTest, groups, countries);

        // Then
        verify(platformIdentityService).createMembership(userTest, groupId);
    }

    @Test
    void recalculateGroups_groupIsNotEmpty_countriesNotEmpty_userHasNotAssignedCountriesAndRole_flowableGroupDoesntExists_userIsNotMember_createsUserMembership() {
        // Given
        String userTest = "test.user";
        List<Map<String, Object>> groups = new ArrayList<>();
        Map<String, Object> group = new HashMap<>();
        group.put("group", "Test_01");
        group.put("role", "user");
        groups.add(group);
        List<String> countries = new ArrayList<>();
        countries.add("ESP");

        PlatformGroupQuery platformGroupQuery = mock(PlatformGroupQuery.class);
        List<PlatformGroup> userGroups = new ArrayList<>();
        doReturn(platformGroupQuery).when(platformGroupQuery).groupMember(userTest);
        doReturn(userGroups).when(platformGroupQuery).list();

        String userRole = capitalizeFirstLetter((String) group.get("role"));
        String groupId = "grpRes".concat("_").concat("ESP").concat("_").concat("Test_01").concat("_").concat(userRole);

        doReturn(platformGroupQuery).when(platformIdentityService).createPlatformGroupQuery();
        doReturn(platformGroupQuery).when(platformGroupQuery).groupId(groupId);
        doReturn(null).when(platformGroupQuery).singleResult();

        PlatformGroupBuilder platformGroupBuilder = mock(PlatformGroupBuilder.class);
        PlatformGroup platformGroup = mock(PlatformGroup.class);
        doReturn(platformGroupBuilder).when(platformIdentityService).createNewGroupBuilder(groupId);
        doReturn(platformGroupBuilder).when(platformGroupBuilder).name(groupId);
        doReturn(platformGroup).when(platformGroupBuilder).save();

        GroupQuery groupQuery = mock(GroupQuery.class);
        doReturn(groupQuery).when(platformIdentityService).createGroupQuery();
        doReturn(groupQuery).when(groupQuery).groupMember(userTest);

        // When
        recalculateFlowableGroupsService.recalculateGroups(userTest, groups, countries);

        // Then
        verify(platformIdentityService).createMembership(userTest, groupId);
    }

    private String capitalizeFirstLetter(String myString) {

        return myString.substring(0, 1).toUpperCase() + myString.substring(1);
    }
}
