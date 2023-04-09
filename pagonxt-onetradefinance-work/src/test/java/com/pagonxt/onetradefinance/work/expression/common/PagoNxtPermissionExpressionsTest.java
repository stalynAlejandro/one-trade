package com.pagonxt.onetradefinance.work.expression.common;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

@UnitTest
class PagoNxtPermissionExpressionsTest {

    @Mock
    CmmnRuntimeService cmmnRuntimeService;

    @InjectMocks
    PagoNxtPermissionExpressions pagoNxtPermissionExpressions;


    @Test
    void getFlwGroupNameFromPgnNxtResolutionGroup_blankPgnNxtResolutionGroup() {
        //Given
        String pgnNxtResolutionGroup = " ";
        //When
        String result = pagoNxtPermissionExpressions.getFlwGroupNameFromPgnNxtResolutionGroup("aCaseInstanceId",pgnNxtResolutionGroup);
        //Then
        assertNull(result);

    }
    @Test
    void getFlwGroupNameFromPgnNxtResolutionGroup_notBlankPgnNxtResolutionGroup() {
        //Given
        String pgnNxtResolutionGroup = "BOI_01_User";
        String caseId = "aCaseInstanceId";
        doReturn("CLE").when(cmmnRuntimeService).getVariable(caseId, PRODUCT);
        doReturn("REQUEST").when(cmmnRuntimeService).getVariable(caseId, EVENT);
        doReturn("ES").when(cmmnRuntimeService).getVariable(caseId, COUNTRY);
        //When
        String result = pagoNxtPermissionExpressions.getFlwGroupNameFromPgnNxtResolutionGroup("aCaseInstanceId",pgnNxtResolutionGroup);
        //Then
        assertEquals("grpRes_ES_CLE_REQUEST_BOI_01_User",result);

    }
}
